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

        if (!question.equals(answerId.question)) return false;
        if (!username.equals(answerId.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + question.hashCode();
        return result;
    }
}
