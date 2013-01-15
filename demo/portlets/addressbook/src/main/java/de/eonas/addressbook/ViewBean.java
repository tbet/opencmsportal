package de.eonas.addressbook;

import de.eonas.addressbook.genericmodel.LazyHibernateDataModel;
import de.eonas.addressbook.genericmodel.LazyLdapDataModel;
import de.eonas.addressbook.model.LazyDataModel;
import de.eonas.addressbook.model.Person;
import org.jetbrains.annotations.NotNull;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.naming.NamingException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@SessionScoped
public class ViewBean implements Serializable {
    private static final String IMAGES_NOPICTURE_PNG = "/images/nopicture.png";
    private final Logger LOG = LoggerFactory.getLogger(ViewBean.class);

    private List<Person> personList;
    private Person selectedPerson;
    private Person editPerson;
    private boolean editMode;
    private List filteredValue;

    public ViewBean() throws NamingException {
        refresh();
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public Person getEditPerson() {
        return editPerson;
    }

    public void setEditPerson(Person editPerson) {
        this.editPerson = editPerson;
    }


    public void deletePic () {
        LOG.info("Delete pic");
        editPerson.setJpegPhoto(null);
    }

    public void deleteEntry () {
        // TODO
        LOG.info("Delete entry");
    }

    public void createEntry () throws CloneNotSupportedException {
        LOG.info("Create entry");
        editPerson = new Person();
        editMode = true;
    }

    public void refresh() {
        LazyDataModel<Person> lazyModel = getModel();
        this.personList = lazyModel.load();
        this.editMode = false;
        this.editPerson = new Person();
    }

    private LazyDataModel<Person> getModel() {
        //noinspection ConstantIfStatement
        if ( false ) {
            return new LazyLdapDataModel<Person>(Person.class, "inetOrgPerson");
        } else {
            WebApplicationContext springContext = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
            @SuppressWarnings("unchecked") Dao<Person> dao = springContext.getBean(Dao.class); // attention: type erasure
            dao.setClazz(Person.class); // take care!!
            return new LazyHibernateDataModel<Person>(Person.class, dao);
        }
    }

    public void savePerson() {
        LOG.info("Saving " + editPerson.getCn());

        LazyDataModel<Person> lazyModel = getModel();
        lazyModel.save(editPerson);
        selectedPerson = editPerson;

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, editPerson.getCn(), "saved.");
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, msg);

        refresh();
        switchToView();

    }

    public void handleFileUpload(@NotNull FileUploadEvent event) {
        UploadedFile file = event.getFile();

        FacesMessage msg;
        try {
            BufferedImage image = ImageIO.read(file.getInputstream());
            int width = image.getWidth();
            int height = image.getHeight();
            if ( width > 128 ) {
                double factor = (double)width / 128.0;
                double dHeight = (double)height / factor;
                height = (int) dHeight;
                width = 128;
            }
            LOG.info("Resize image to " + width + "/" + height);
            image = createResizedCopy(image, width, height);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", byteArrayOutputStream);
            editPerson.setJpegPhoto(byteArrayOutputStream.toByteArray());
            msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        } catch (IOException e) {
            msg = new FacesMessage("Upload failed", e.toString());
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    @NotNull
    BufferedImage createResizedCopy(Image originalImage,
                                    int scaledWidth, int scaledHeight) {
        int imageType = BufferedImage.TYPE_INT_RGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }


    public void switchToEdit() throws CloneNotSupportedException {
        LOG.info("Switched to edit mode");
        editMode = true;
        editPerson = selectedPerson.clone();
    }

    public void switchToView() {
        LOG.info("Switched to view mode");
        editMode = false;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    @NotNull
    public Object getJpegPhotoStream() {
        if (selectedPerson == null) return IMAGES_NOPICTURE_PNG;
        final byte[] jpegPhoto = selectedPerson.getJpegPhoto();
        if (jpegPhoto == null) return IMAGES_NOPICTURE_PNG;
        return new DefaultStreamedContent(new ByteArrayInputStream(jpegPhoto), "image/jpeg", "logo.jpg");
    }

    @NotNull
    public Object getJpegPhotoStreamEdit() {
        if (editPerson == null) return IMAGES_NOPICTURE_PNG;
        final byte[] jpegPhoto = editPerson.getJpegPhoto();
        if (jpegPhoto == null) return IMAGES_NOPICTURE_PNG;
        return new DefaultStreamedContent(new ByteArrayInputStream(jpegPhoto), "image/jpeg", "logo.jpg");
    }

    public List getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List filteredValue) {
        this.filteredValue = filteredValue;
    }


}
