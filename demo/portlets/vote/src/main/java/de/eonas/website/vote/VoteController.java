package de.eonas.website.vote;

import de.eonas.website.vote.model.Answer;
import de.eonas.website.vote.model.Option;
import de.eonas.website.vote.model.PortletSettings;
import de.eonas.website.vote.model.Question;
import org.primefaces.model.chart.PieChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@SessionScoped
public class VoteController implements Serializable {

    private Dao dao;
    private PortletSettings settings;
    private boolean defaultValues;

    @SuppressWarnings("UnusedDeclaration")
    private static Logger logger = LoggerFactory.getLogger(VoteController.class);

    private Answer selectedAnswer;
    private List<Question> allQuestions;
    private long selectOptionId;

    private PieChartModel pieModel;

    public VoteController() {
        WebApplicationContext ctx = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
        dao = ctx.getBean(Dao.class);

        init();
    }

    private void init() {
        final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String username = externalContext.getRemoteUser();

        PortletRequest renderRequest = (PortletRequest) externalContext.getRequest();
        String windowId = renderRequest.getWindowID();

        settings = dao.fetchSettings(windowId);
        defaultValues = false;

        if (settings == null) {
            settings = dao.getDefaultValues(windowId);
            defaultValues = true;
        } else {
            selectedAnswer = dao.getSelectedAnswer(username, settings.getQuestion());

        }

        allQuestions = dao.getAllQuestions();
    }

    public List<Question> getAllQuestions() {
        return allQuestions;
    }

    public boolean isDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(boolean defaultValues) {
        this.defaultValues = defaultValues;
    }

    public PortletSettings getSettings() {
        return settings;
    }

    public void setSettings(PortletSettings settings) {
        this.settings = settings;
    }

    public String saveAnswer() {
        if ( selectedAnswer != null ) {
            dao.saveAnswer(selectedAnswer, selectOptionId);
            msg(FacesMessage.SEVERITY_INFO, "Answer saved.", "Answer saved.");

            pieModel = new PieChartModel();
            List<Object[]> result = dao.getResult(settings.getQuestion());
            for (Object[] objects : result) {
                Long count = (Long) objects[0];
                Option option = (Option) objects[1];
                pieModel.set(option.getText(), count);
            }

            return "chosen";
        }
        return null;
    }

    public void saveSettings() {
        dao.saveSettings(settings);
        init();
        msg(FacesMessage.SEVERITY_INFO, "Settings saved.", "Settings saved.");
    }

    private void msg(FacesMessage.Severity severityInfo, String summary, String detail) {
        FacesMessage msg = new FacesMessage(severityInfo, summary, detail);
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, msg);
    }

    public void setSelectedAnswer(Answer selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public Answer getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectOptionId(long selectOptionId) {
        this.selectOptionId = selectOptionId;
    }

    public long getSelectOptionId() {
        return selectOptionId;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }


}
