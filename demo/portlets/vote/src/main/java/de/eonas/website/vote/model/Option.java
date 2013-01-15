package de.eonas.website.vote.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Option {
    @Id @GeneratedValue
    long id;

    @ManyToOne(cascade = CascadeType.ALL)
    Question question;

    String text;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL)
    private Collection<Answer> answer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Collection<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(Collection<Answer> answer) {
        this.answer = answer;
    }
}
