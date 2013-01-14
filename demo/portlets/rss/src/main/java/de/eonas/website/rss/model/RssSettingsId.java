package de.eonas.website.rss.model;

import java.io.Serializable;

public class RssSettingsId implements Serializable {
    String username;
    String portletId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RssSettingsId)) return false;

        RssSettingsId that = (RssSettingsId) o;

        if (portletId != null ? !portletId.equals(that.portletId) : that.portletId != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (portletId != null ? portletId.hashCode() : 0);
        return result;
    }
}
