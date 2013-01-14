package de.eonas.website.rss.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Entity @IdClass(RssSettingsId.class)
public class RssSettings implements Serializable {

    @Id
    String username;

    @Id
    String portletId;

    String url;
    private String httpUsername;
    private String httpPassword;
    private boolean basicAuth;

    public String getPortletId() {
        return portletId;
    }

    public void setPortletId(String portletId) {
        this.portletId = portletId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHttpUsername(String httpusername) {
        this.httpUsername = httpusername;
    }

    public String getHttpUsername() {
        return httpUsername;
    }

    public void setHttpPassword(String httppassword) {
        this.httpPassword = httppassword;
    }

    public String getHttpPassword() {
        return httpPassword;
    }

    public void setBasicAuth(boolean basicAuth) {
        this.basicAuth = basicAuth;
    }

    public boolean isBasicAuth() {
        return basicAuth;
    }
}
