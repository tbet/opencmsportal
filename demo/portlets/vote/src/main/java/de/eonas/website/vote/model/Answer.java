package de.eonas.website.vote.model;

import javax.persistence.*;

@Entity
@IdClass(AnswerId.class)
public class Answer {
    @Id
    String username;

    @Id
    @ManyToOne
    Question question;

    @ManyToOne
    Option option;

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
