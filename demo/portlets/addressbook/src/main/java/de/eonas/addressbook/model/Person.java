package de.eonas.addressbook.model;

import de.eonas.addressbook.genericmodel.LdapSelectableData;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

@SuppressWarnings("UnusedDeclaration")
@Entity
public class Person implements LdapSelectableData, Cloneable, Serializable {
    @Id
    //@GeneratedValue
    String dn;
    @Transient
    String[] objectClass;
    String structuralObjectClass;
    String entryUUID;
    String creatorsName;
    String createTimestamp;
    String uid;
    String cn;
    String givenName;
    String sn;
    String labeledURI;
    String o;
    String ou;
    String street;
    String l;
    String postalCode;
    String telephoneNumber;
    String facsimileTelephoneNumber;
    String mobile;
    String mail;
    String displayName;
    String title;
    String entryCSN;
    String modifiersName;
    String modifyTimestamp;
    String c;
    byte[] jpegPhoto;

    @Nullable
    static String cleanAndTrim(@Nullable String in) {
        if (in == null) return in;
        in = in.trim();
        if (in.length() == 0) return null;
        return in;
    }

    private void rebuildCn() {
        if (sn == null) {
            setCn(givenName);
            return;
        }
        if (givenName == null) {
            setCn(sn);
            return;
        }
        setCn(givenName + " " + sn);
    }

    private void rebuildDisplayName() {
        if (o == null) {
            setDisplayName(cn);
            return;
        }
        if (cn == null) {
            setDisplayName(o);
            return;
        }
        setDisplayName(o + ": " + cn);
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        cn = cleanAndTrim(cn);
        this.cn = cn;
        rebuildDisplayName();
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getCreatorsName() {
        return creatorsName;
    }

    public void setCreatorsName(String creatorsName) {
        this.creatorsName = creatorsName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        displayName = cleanAndTrim(displayName);
        this.displayName = displayName;
    }

    @Override
    public void setDn(String dn) {
        this.dn = dn;
    }

    @Override
    public String getDn() {
        return dn;
    }

    public String getEntryCSN() {
        return entryCSN;
    }

    public void setEntryCSN(String entryCSN) {
        this.entryCSN = entryCSN;
    }

    public String getEntryUUID() {
        return entryUUID;
    }

    public void setEntryUUID(String entryUUID) {
        this.entryUUID = entryUUID;
    }

    public String getFacsimileTelephoneNumber() {
        return facsimileTelephoneNumber;
    }

    public void setFacsimileTelephoneNumber(String facsimileTelephoneNumber) {
        this.facsimileTelephoneNumber = facsimileTelephoneNumber;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        givenName = cleanAndTrim(givenName);
        this.givenName = givenName;
        rebuildCn();
    }


    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getLabeledURI() {
        return labeledURI;
    }

    public void setLabeledURI(String labeledURI) {
        labeledURI = cleanAndTrim(labeledURI);
        if (labeledURI != null) {
            try {
                new URL(labeledURI);
                this.labeledURI = labeledURI;
            } catch (MalformedURLException ex) {
                this.labeledURI = "http://" + labeledURI;
            }
        }
        this.labeledURI = labeledURI;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getModifiersName() {
        return modifiersName;
    }

    public void setModifiersName(String modifiersName) {
        this.modifiersName = modifiersName;
    }

    public String getModifyTimestamp() {
        return modifyTimestamp;
    }

    public void setModifyTimestamp(String modifyTimestamp) {
        this.modifyTimestamp = modifyTimestamp;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public String[] getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String[] objectClass) {
        this.objectClass = objectClass;
    }

    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        sn = cleanAndTrim(sn);
        this.sn = sn;
        rebuildCn();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStructuralObjectClass() {
        return structuralObjectClass;
    }

    public void setStructuralObjectClass(String structuralObjectClass) {
        this.structuralObjectClass = structuralObjectClass;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getC() {
        return c;
    }

    public byte[] getJpegPhoto() {
        return jpegPhoto;
    }

    public void setJpegPhoto(@Nullable byte[] jpegPhoto) {
        this.jpegPhoto = jpegPhoto;
    }

    @Override
    public Person clone() throws CloneNotSupportedException {
        return (Person) super.clone();
    }
}
