package de.eonas.website.rss;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.sun.syndication.io.impl.Base64;
import de.eonas.website.rss.model.RssSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.RenderRequest;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@ManagedBean
@SessionScoped
public class RssController implements Serializable {

    private WebApplicationContext ctx;
    private Dao dao;
    private RssSettings settings;
    private String username;
    private boolean defaultValues;
    private SyndFeed feed;
    private boolean feedLoaded;
    private String windowId;
    private static Logger logger = LoggerFactory.getLogger(RssController.class);

    public RssController() {
        ctx = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
        dao = ctx.getBean(Dao.class);

        final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        username = externalContext.getRemoteUser();
        RenderRequest renderRequest = (RenderRequest) externalContext.getRequest();
        windowId = renderRequest.getWindowID();
        settings = dao.fetchSettings(username, windowId);
        defaultValues = false;

        if (settings == null) {
            settings = dao.getDefaultValues(username, windowId);
            defaultValues = true;
        }

        loadFeed();
    }

    public void loadFeed() {
        if (defaultValues == true) return;

        try {
            loadFeedInternal();
        } catch (Exception e) {
            msg(FacesMessage.SEVERITY_FATAL, "Unable to load feed", e.toString());
            logger.warn("unable to load feed", e);
        }
    }

    private void loadFeedInternal() throws IOException, FeedException {
        XmlReader reader = null;
        try {
            URL url = new URL(settings.getUrl());
            URLConnection urlConnection = url.openConnection();
            if (settings.isBasicAuth()) {
                String basicAuth = Base64.encode(settings.getHttpUsername() + ":" + settings.getHttpPassword());
                urlConnection.setRequestProperty("Authorization", "Basic " + basicAuth);
                Object content = urlConnection.getContent();
                logger.warn(content.toString());
            }
            reader = new XmlReader(urlConnection);
            feed = new SyndFeedInput().build(reader);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    logger.warn("unable to close reader", ex);
                }
            }
        }
    }

    public boolean isDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(boolean defaultValues) {
        this.defaultValues = defaultValues;
    }

    public RssSettings getSettings() {
        return settings;
    }

    public void setSettings(RssSettings settings) {
        this.settings = settings;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void saveSettings() {
        try {
            loadFeedInternal();
            dao.saveSettings(settings);
            msg(FacesMessage.SEVERITY_INFO, "Settings saved.", "Connection validated.");
        } catch (FeedException e) {
            msg(FacesMessage.SEVERITY_ERROR, "Feed invalid", e.toString());
        } catch (IOException e) {
            String text = e.toString();
            if ( text.contains("Server returned HTTP response code: 401" ) ) {
                settings.setBasicAuth(true);
                msg(FacesMessage.SEVERITY_ERROR, "Authentication required.", e.toString());
            } else {
                msg(FacesMessage.SEVERITY_ERROR, "Unable to access URL.", e.toString());
            }
        }
    }

    private void msg(FacesMessage.Severity severityInfo, String summary, String detail) {
        FacesMessage msg = new FacesMessage(severityInfo, summary, detail);
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, msg);
    }

    public SyndFeed getFeed() {
        return feed;
    }

    public void setFeed(SyndFeed feed) {
        this.feed = feed;
    }


    public boolean isFeedLoaded() {
        return feedLoaded;
    }

    public void setFeedLoaded(boolean feedLoaded) {
        this.feedLoaded = feedLoaded;
    }

}
