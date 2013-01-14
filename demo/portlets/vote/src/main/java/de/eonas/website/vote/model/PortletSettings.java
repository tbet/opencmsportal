package de.eonas.website.vote.model;

import org.aspectj.weaver.patterns.TypePatternQuestions;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class PortletSettings implements Serializable {

    @Id
    String portletId;

    @ManyToOne(fetch = FetchType.EAGER)
    Question question;

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
