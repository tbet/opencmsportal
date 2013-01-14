package de.eonas.addressbook.genericmodel;

import de.eonas.addressbook.model.I_LazyLdapDataModel;
import org.apache.commons.beanutils.BeanUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.SizeLimitExceededException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class LazyLdapDataModel<T extends LdapSelectableData> extends LazyDataModel<T> implements I_LazyLdapDataModel<T> {

    private static final String LDAP_CONTEXT = "ldap://ldap.office.eonas.de:389/ou=people,dc=eonas,dc=de";
    private static final String LDAP_DN = "uid=egroupware,ou=DSA,dc=eonas,dc=de";
    private static final String LDAP_PW = "";

    @NotNull
    private final Hashtable<String, String> env;
    private final Class<T> clazz;
    private final String objectClass;

    private static final Logger LOG = LoggerFactory.getLogger(LazyLdapDataModel.class);

    public LazyLdapDataModel(Class<T> clazz, @SuppressWarnings("SameParameterValue") String objectClass) {
        this.env = contructJndiEnv();
        this.clazz = clazz;
        this.objectClass = objectClass;
    }

    @NotNull
    private InitialLdapContext getConnection() throws NamingException {
        return new InitialLdapContext(env, null);
    }

    @NotNull
    private Hashtable<String, String> contructJndiEnv() {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, LDAP_CONTEXT);

        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, LDAP_DN);
        env.put(Context.SECURITY_CREDENTIALS, LDAP_PW);
        env.put("com.sun.jndi.ldap.connect.pool", "true");
        return env;
    }

    @Override
    @NotNull
    public List<T> load() {
        return load(0, Integer.MAX_VALUE, null, null, null);
    }

    @NotNull
    @Override
    /**
     * Diese Funktion ist der Kern des Lazy Loadings. Bitte nicht verwenden, Sortieren geht nicht, weil OpenLdap die
     * extension nicht unterstützt. Pagination geht aus dem gleichen Grund nur schlecht.
     */
    public List<T> load(int first, int pageSize, @Nullable String sortField, @Nullable SortOrder sortOrder, @Nullable Map<String, String> filters) {

        try {
            List<T> list = new ArrayList<T>();

            SearchControls searchControls = new SearchControls();
            //searchControls.setCountLimit(first + pageSize + 10);
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            Set<String> exactMatch = new HashSet<String>();

            if (filters == null) {
                filters = new HashMap<String, String>();
            }

            filters.put("objectclass", objectClass);
            exactMatch.add("objectclass");

            StringBuilder ldapFilterString = new StringBuilder();
            List<String> objects = new ArrayList<String>();
            constructFilter(filters, ldapFilterString, objects, exactMatch);

            int numberOfEntries = 0;

            InitialLdapContext ctx = getConnection();

            try {
                /* NOT SUPPORTED ON OPENLDAP openldap-servers-2.3.43-25.el5_8.1
                final SortControl sortControl = new SortControl(sortField, Control.CRITICAL);
                final PagedResultsControl pageControl = new PagedResultsControl(pageSize, Control.CRITICAL);
                ctx.setRequestControls(new Control[]{
                        sortControl});
                */

                NamingEnumeration<SearchResult> enu = ctx.search("", ldapFilterString.toString(), objects.toArray(), searchControls);
                try {
                    for (; enu.hasMore(); ) {
                        SearchResult sr = enu.next();
                        if (numberOfEntries >= first && numberOfEntries < first + pageSize) {
                            T p = map(sr);
                            list.add(p);
                        }

                        numberOfEntries++;
                    }
                } catch (SizeLimitExceededException ex) {
                    numberOfEntries++;
                    LOG.debug(ex.toString(), ex);
                }
            } finally {
                closeCtx(ctx);
            }

            setRowCount(numberOfEntries);

            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getRowKey(@NotNull T object) {
        return object.getDn();
    }

    @Nullable
    @Override
    public T getRowData(String rowKey) {

        SearchControls searchControls = new SearchControls();
        searchControls.setCountLimit(1);
        searchControls.setTimeLimit(2000);
        searchControls.setSearchScope(SearchControls.OBJECT_SCOPE);

        NamingEnumeration<SearchResult> enu;
        try {
            InitialLdapContext ctx = getConnection();

            try {
                enu = ctx.search(rowKey, "objectclass=*", searchControls);
                if (!enu.hasMore()) {
                    return null;
                }
                SearchResult sr = enu.next();
                return map(sr);

            } finally {
                closeCtx(ctx);
            }
        } catch (Exception e) {
            LOG.error("Unable to fetch connection", e);
            throw new RuntimeException(e);
        }
    }

    private void closeCtx(@NotNull InitialLdapContext ctx) {
        try {
            ctx.close();
        } catch (NamingException e) {
            LOG.warn("Unable to close connection", e);
        }
    }

    private void constructFilter(@NotNull Map<String, String> filters, @NotNull StringBuilder ldapFilterString, @NotNull List<String> objects, @NotNull Set<String> exactMatch) {
        int pos = 0;
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            String key = filter.getKey();
            ldapFilterString.append(String.format("(%s={%d}%s)", key, pos, exactMatch.contains(key) ? "" : "*"));
            objects.add(filter.getValue());

            if (pos == 1) {
                ldapFilterString.insert(0, "(&");
            }

            pos++;
        }
        if (pos > 1) {
            ldapFilterString.append(")");
        }
    }

    T map(@NotNull SearchResult sr) throws NamingException, InvocationTargetException, IllegalAccessException, InstantiationException {
        T p = clazz.newInstance();
        p.setDn(sr.getName());
        Attributes attributes = sr.getAttributes();
        NamingEnumeration<? extends Attribute> enu;
        for (enu = attributes.getAll(); enu.hasMore(); ) {
            Attribute attribute = enu.next();
            map(p, attribute);
        }

        return p;
    }

    void map(T p, @NotNull Attribute attribute) throws InvocationTargetException, IllegalAccessException, NamingException {
        String name = attribute.getID();
        Object value = fetch(attribute);
        BeanUtils.setProperty(p, name, value);
    }

    @Nullable
    private Object fetch(@NotNull Attribute attribute) throws NamingException {
        if (attribute.size() == 0) return null;
        if (attribute.size() == 1) {
            return attribute.get(0);
        } else {
            List<Object> list = new ArrayList<Object>();
            for (int i = 0; i < attribute.size(); i++) {
                list.add(attribute.get(i));
            }
            return list.toArray(new Object[attribute.size()]);
        }
    }

    @Override
    public void save(T object) {
        // TODO: save object
        LOG.warn("Saving to LDAP not implemented.");
        String dn = object.getDn();
        if ( dn == null ) {
            // create
        } else {
            // update and save
        }
    }
}
