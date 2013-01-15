package de.eonas.website.vote.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class PortletSettings implements Serializable {

    @Id
    String portletId;

    @ManyToOne(fetch = FetchType.EAGER)
    Question question;

    @SuppressWarnings("UnusedDeclaration")
    public String getPortletId() {
        return portletId;
    }

    public void setPortletId(String portletId) {
        this.portletId = portletId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
