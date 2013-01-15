package de.eonas.website.vote.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Question {
    @Id @GeneratedValue
    long id;

    String text;

    String optionText;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Collection<Option> optionList;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    Collection<Answer> answerList;

    public Collection<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(Collection<Option> optionList) {
        this.optionList = optionList;
    }

    @SuppressWarnings("UnusedDeclaration")
    public long getId() {
        return id;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Collection<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(Collection<Answer> answerList) {
        this.answerList = answerList;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
}
