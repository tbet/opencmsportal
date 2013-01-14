package de.eonas.website.vote.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Question {
    @Id @GeneratedValue
    long id;

    String text;

    String optionText;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    List<Option> optionList;

    @OneToMany(mappedBy = "question")
    List<Answer> answerList;

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
}
