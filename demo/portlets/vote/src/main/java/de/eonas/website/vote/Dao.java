package de.eonas.website.vote;

import de.eonas.website.vote.model.Answer;
import de.eonas.website.vote.model.Option;
import de.eonas.website.vote.model.PortletSettings;
import de.eonas.website.vote.model.Question;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class Dao {
    @PersistenceContext(unitName = "Settings")
    EntityManager em;

    public void insertDemoDataTransacted () {
        Question q1 = new Question();
        q1.setText("What's up for todays lunch?");
        q1.setOptionText("Food Style");
        String[] options = { "Italian", "Burger", "Pizza", "Anatolian", "Steak" };
        populate(q1, options);
        q1 = em.merge(q1);


        Question q2 = new Question();
        q2.setText("What's your favorite air condition temperature?");
        q2.setOptionText("Temperature");
        String[] options2 = { "19", "20", "21", "22" };
        populate(q2, options2);
        q2 = em.merge(q2);

        PortletSettings s = new PortletSettings();
        s.setPortletId("vote.vote!1");
        s.setQuestion(q1);
        s = em.merge(s);
    }

    private void populate(Question q1, String[] options) {
        List<Option> optionList = new ArrayList<Option>();
        for (String option : options) {
            Option optionObj = new Option();
            optionObj.setText(option);
            optionObj.setQuestion(q1);
            optionList.add(optionObj);
        }
        q1.setOptionList(optionList);
    }

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
