package de.eonas.website.rss;

import de.eonas.website.rss.model.RssSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Component
public class Dao {
    @PersistenceContext(unitName = "Settings")
    EntityManager em;


    public void insertDemoDataTransacted () {
        RssSettings s = new RssSettings();
        s.setPortletId("rss.rss!1");
        s.setUrl("http://www.heise.de/newsticker/heise-atom.xml");
        s.setUsername("Guest");
        s.setBasicAuth(false);
        em.merge(s);

        s.setUsername("Admin");
        em.merge(s);
    }


    @Nullable
    public RssSettings fetchSettings ( String username, String portletId ) {
        TypedQuery<RssSettings> query = em.createQuery("select a from RssSettings a where a.username = :name and a.portletId = :portletId", RssSettings.class);
        query.setParameter("name", username);
        query.setParameter("portletId", portletId);
        try {
           return query.getSingleResult();
        } catch ( NoResultException ex) {
           return null;
        }
    }

    public void saveSettings ( RssSettings s ) {
        em.merge(s);
    }

    @NotNull
    public RssSettings getDefaultValues(String username, String windowId) {
        RssSettings s = new RssSettings();
        s.setUsername(username);
        s.setBasicAuth(false);
        s.setPortletId(windowId);
        return s;
    }
}
