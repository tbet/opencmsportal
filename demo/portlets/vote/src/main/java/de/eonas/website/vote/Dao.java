package de.eonas.website.vote;

import de.eonas.website.vote.model.Answer;
import de.eonas.website.vote.model.Option;
import de.eonas.website.vote.model.PortletSettings;
import de.eonas.website.vote.model.Question;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Component
public class Dao {
    @PersistenceContext(unitName = "Settings")
    EntityManager em;

    @Nullable
    public PortletSettings fetchSettings(String portletId) {
        TypedQuery<PortletSettings> query = em.createQuery("select a from PortletSettings a where a.portletId = :portletId", PortletSettings.class);
        query.setParameter("portletId", portletId);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public void saveSettings(PortletSettings s) {
        em.merge(s);
    }

    public List<Question> getAllQuestions() {
        TypedQuery<Question> query = em.createQuery("select a from Question a", Question.class);
        return query.getResultList();
    }

    @NotNull
    public PortletSettings getDefaultValues(String windowId) {
        PortletSettings s = new PortletSettings();
        s.setPortletId(windowId);
        return s;
    }

    public Answer getSelectedAnswer(String username, Question question) {
        TypedQuery<Answer> query = em.createQuery("select a from Answer a where a.question = :question and a.username = :username", Answer.class);
        query.setParameter("question", question);
        query.setParameter("username", username);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return getDefaultAnswer(username, question);
        }
    }

    private Answer getDefaultAnswer(String username, Question question) {
        Answer a = new Answer();
        a.setUsername(username);
        a.setQuestion(question);
        return a;
    }

    public void saveAnswer(Answer selectedAnswer, long selectOptionId) {
        Option o = em.find(Option.class, selectOptionId);
        selectedAnswer.setOption(o);
        em.merge(selectedAnswer);
    }

    public List<Object[]> getResult(Question question) {
        Query query = em.createQuery("select count(*), a.option.id from Answer a where a.question = :question group by a.option.id");
        query.setParameter("question", question);
        List<Object[]> result = query.getResultList();
        for (Object[] objects : result) {
            objects[1] = em.find(Option.class, objects[1]);
        }
        return result;
    }
}
