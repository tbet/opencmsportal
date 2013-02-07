package de.eonas.opencms.install;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import org.opencms.main.CmsShell;
import org.opencms.module.CmsModule;
import org.opencms.setup.CmsSetupDb;
import org.opencms.setup.comptest.CmsSetupTestResult;
import org.opencms.util.CmsStringUtil;

public class Installer {
	private static final String COMPONENT = "component.";
	private static final String SERVER_MODULES = "server.modules";
	private static final String SERVER_WORKPLACE = "server.workplace";
	private static final String DRIVER_TABLESPACEINDEX = "driver.tablespaceindex";
	private static final String DRIVER_TABLESPACETEMP = "driver.tablespacetemp";
	private static final String DRIVER_TABLESPACEDEFAULT = "driver.tablespacedefault";
	private static final String DRIVER_PASSWORD = "driver.password";
	private static final String DRIVER_USERNAME = "driver.username";
	private static final String DRIVER_JDBCURI = "driver.jdbcuri";
	private static final String DRIVER_DATABASE = "driver.database";
	private static final String DRIVER_PROVIDER = "driver.provider";

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err
					.println("Usage: de.eonas.opencms.Installer <opencms-basedir> <propertyfile>");
			System.exit(1);
		}

		String opencmsbasedir = args[0];
		String propertyfile = args[1];

		Properties p = new Properties();
		p.load(new FileInputStream(propertyfile));

		org.opencms.setup.CmsSetupBean Bean = new org.opencms.setup.CmsSetupBean();
		PageContext fakePageContext = new FakePageContext(opencmsbasedir);
		Bean.init(fakePageContext);

		Bean.setEthernetAddress(p.getProperty("server.ethernet")); // ethernet
		Bean.setServerName(p.getProperty("server.name")); // server name

		Map<String, CmsModule> allmodules = Bean.getAvailableModules();

		String selectedModules = resolveSelectedModules(opencmsbasedir);
		System.out.println("Installing " + selectedModules);

		String modules = resolveModules(allmodules, selectedModules);

		Bean.setInstallModules(modules); // server modules
		Bean.setWorkplaceSite(p.getProperty(SERVER_WORKPLACE)); // server
																// workplace uri

		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put("submit", "true");
		parameter.put("createTables", "true");
		parameter.put("db", p.getProperty(DRIVER_DATABASE));
		parameter.put("dbName", p.getProperty(DRIVER_DATABASE));
		parameter.put("templateDb", p.getProperty(DRIVER_DATABASE));
		parameter.put("dbCreateConStr", p.getProperty(DRIVER_JDBCURI));
		parameter.put("dbWorkUser", p.getProperty(DRIVER_USERNAME));
		parameter.put("dbWorkPwd", p.getProperty(DRIVER_PASSWORD));
		parameter.put("dbDefaultTablespace",
				p.getProperty(DRIVER_TABLESPACEDEFAULT));
		parameter.put("dbTemporaryTablespace",
				p.getProperty(DRIVER_TABLESPACETEMP));
		parameter.put("dbIndexTablespace",
				p.getProperty(DRIVER_TABLESPACEINDEX));

		HttpServletRequest request = new FakeHttpRequest(parameter);
		Bean.setDatabase(p.getProperty(DRIVER_PROVIDER));
		Bean.setDbParamaters(request, p.getProperty(DRIVER_PROVIDER));
		// Bean.setDbProperty(p.getProperty(DRIVER_PROVIDER) + ".constr.newDb",
		// p.getProperty(DRIVER_JDBCURI));

		org.opencms.setup.comptest.CmsSetupTests setupTests = new org.opencms.setup.comptest.CmsSetupTests();
		setupTests.runTests(Bean);
		List<CmsSetupTestResult> testResults = setupTests.getTestResults();
		for (int i = 0; i < testResults.size(); i++) {
			CmsSetupTestResult testResult = (org.opencms.setup.comptest.CmsSetupTestResult) testResults
					.get(i);
			if (!testResult.isGreen()) {
				System.err.println("Failed test " + testResult.getName() + ": "
						+ testResult.getInfo() + "/" + testResult.getResult());
			}
		}
		if (!setupTests.isGreen()) {
			throw new Exception("Setup tests failed.");
		}

		CmsSetupDb db = checkDb(opencmsbasedir, Bean);

		/*
		 * db.clearErrors(); db.dropDatabase(Bean.getDatabase(),
		 * Bean.getReplacer()); if ( db.noErrors() == false ) throw new
		 * Exception ( "dropDatabase failed: " + db.getErrors().toString());
		 */

		db.clearErrors();
		db.createTables(Bean.getDatabase(), Bean.getReplacer());
		if (db.noErrors() == false)
			throw new Exception("createTables failed."
					+ db.getErrors().toString());

		List<String> beanmodules = Bean.getModulesToInstall();

		for (Object o : beanmodules) {
			String k = (String) o;
			System.out.println(k);
		}

		Bean.prepareStep8();
		Bean.prepareStep10();



		CmsShell shell = new CmsShell(Bean.getWebAppRfsPath() + "WEB-INF"
				+ File.separator, Bean.getServletMapping(),
				Bean.getDefaultWebApplication(), "${user}@${project}>", Bean);

		shell.start(new FileInputStream(new File(Bean.getWebAppRfsPath()
				+ CmsSetupDb.SETUP_DATA_FOLDER + "cmssetup.txt")));

        System.exit(0);
	}

	private static String resolveSelectedModules(String opencmsbasedir)
			throws FileNotFoundException, IOException {
		Properties p = new Properties();

		p.load(new FileInputStream(opencmsbasedir + File.separator + "setup"
				+ File.separator + "components.properties"));

		StringBuffer buffer = new StringBuffer();
		String compos = p.getProperty("components");

		if ( compos == null ) {
			throw new IllegalArgumentException("components property not found");
		}
		
		for (String comp : compos.split(",")) {
			String sEnabled = p.getProperty(COMPONENT + comp + ".checked");
			if (Boolean.parseBoolean(sEnabled)) {
				if (buffer.length() > 0) {
					buffer.append("|");
				}
				buffer.append(p.getProperty(COMPONENT + comp + ".modules"));
			}
		}
		return buffer.toString();
	}

	private static String resolveModules(Map<String, CmsModule> allmodules,
			String property) {
		List<String> modulematcher = CmsStringUtil.splitAsList(property, "|",
				true);
		StringBuffer returnlist = new StringBuffer();
		boolean first = true;

		for (Object o : modulematcher) {
			String pattern = (String) o;
			Pattern p = Pattern.compile(pattern);
			TreeSet<String> sortedSet = new TreeSet<String>(allmodules.keySet());
			for (Object omodule : sortedSet) {
				String module = (String) omodule;
				Matcher m = p.matcher(module);
				if (m.matches()) {
					if (first == false)
						returnlist.append("|");
					else
						first = false;
					returnlist.append(module);
				}
			}
		}
		return returnlist.toString();
	}

	private static CmsSetupDb checkDb(String opencmspath,
			org.opencms.setup.CmsSetupBean Bean) throws Exception {
		CmsSetupDb db = null;
		db = new CmsSetupDb(opencmspath);
		db.setConnection(Bean.getDbDriver(), Bean.getDbWorkConStr(),
				Bean.getDbConStrParams(), Bean.getDbWorkUser(),
				Bean.getDbWorkPwd());
		db.closeConnection();
		List<String> conErrors = new ArrayList<String>(db.getErrors());
		boolean enableContinue = conErrors.isEmpty();
		if (enableContinue == false) {
			throw new Exception("Errors during connect: "
					+ conErrors.toString());
		}
		if (db.noErrors() == false) {
			throw new Exception("Error in db:" + db.getErrors().toString());
		}

		String chkVars = db.checkVariables(Bean.getDatabase());

		if (enableContinue && db.noErrors() && chkVars == null
				&& Bean.validateJdbc()) {
		} else {
			throw new Exception("Database check failed.");
		}
		db.setConnection(Bean.getDbDriver(), Bean.getDbWorkConStr(),
				Bean.getDbConStrParams(), Bean.getDbWorkUser(),
				Bean.getDbWorkPwd());
		return db;
	}
}
