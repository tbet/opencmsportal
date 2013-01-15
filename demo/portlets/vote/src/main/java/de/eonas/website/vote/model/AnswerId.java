package de.eonas.website.vote.model;

import java.io.Serializable;

public class AnswerId implements Serializable {
    String username;
    Question question;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnswerId)) return false;

        AnswerId answerId = (AnswerId) o;

        return question.equals(answerId.question) && username.equals(answerId.username);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + question.hashCode();
        return result;
    }
}
