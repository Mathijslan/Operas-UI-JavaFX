package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class JobDescription {

    int id;
    StringProperty professionTxt;
    StringProperty secteurTxt;
    StringProperty codePcs;
    StringProperty codeNaf;
    StringProperty manualPcsCode;
    StringProperty manualNafCode;
    public boolean setToManual;
    int confidencePcs;
    int confidenceNaf;
    private ComboBox suggestedCodesPcs;
    private ComboBox suggestedCodesNaf;
    private ComboBox manual;
    private ComboBox originalCodesPcs;
    private ComboBox originalCodesNaf;


    public JobDescription(int subjectId, String profession, String secteur, String pcs, String naf, int codeConfidencePcs, String manualPcs, String manualNaf, ObservableList codesPcs, int codeConfidenceNaf, ObservableList codesNaf, boolean setToManual) {
        id = subjectId;
        this.confidencePcs = codeConfidencePcs;
        this.confidenceNaf = codeConfidenceNaf;
        this.professionTxt = new SimpleStringProperty(profession);
        this.secteurTxt = new SimpleStringProperty(secteur);
        this.codePcs = new SimpleStringProperty(pcs);
        this.codeNaf = new SimpleStringProperty(naf);
        this.manualPcsCode = new SimpleStringProperty(manualPcs);
        this.manualNafCode = new SimpleStringProperty(manualNaf);
        this.suggestedCodesPcs = new ComboBox(codesPcs);
        this.setToManual = setToManual;
        this.suggestedCodesNaf = new ComboBox(codesNaf);
        this.originalCodesPcs = new ComboBox(codesPcs);
        this.originalCodesNaf = new ComboBox(codesNaf);
    }

    public boolean isSetToManual () {
        return this.setToManual;
    }

    public void setPcsToManual() {
        originalCodesPcs = suggestedCodesPcs;
        this.setToManual = true;
        manual = new ComboBox(FXCollections.observableArrayList("Manual"));
        setSuggestedCodesPcs(manual);
    }

    public void setPcsToSuggest() {
        setSuggestedCodesPcs(originalCodesPcs);
        this.setToManual = false;
    }

    public void setNafToManual() {
        originalCodesNaf = suggestedCodesNaf;
        manual = new ComboBox(FXCollections.observableArrayList("Manual"));
        this.setToManual = true;
        setSuggestedCodesNaf(manual);
    }

    public void setNafToSuggest() {
        setSuggestedCodesNaf(originalCodesNaf);
        this.setToManual = false;
    }

    public void setSubjectId(int id) {
        this.id = id;
    }

    public void setProfessionTxt(String txt) {
        this.professionTxt.set(txt) ;
    }

    public void setManualPcsCode (String txt) { this.manualPcsCode.set(txt); }

    public void setSecteurTxt (String txt) {
        this.secteurTxt.set(txt);
    }

    public void setManualNafCode (String txt) {
        this.manualNafCode.set(txt);
    }

    public void setCodePcs(String pcs) {
        this.codePcs.set(pcs);
    }

    public void setCodeNaf(String naf) {
        this.codeNaf.set(naf);
    }

    public void setConfidencePcs(int confidence) { this.confidencePcs = confidence; }

    public void setConfidenceNaf(int confidence) { this.confidenceNaf = confidence; }

    public void setSuggestedCodesPcs(ComboBox suggestedCodes) {this.suggestedCodesPcs = suggestedCodes;}

    public void setSuggestedCodesNaf(ComboBox suggestedCodes) {this.suggestedCodesNaf = suggestedCodes;}

    public String getProfessionTxt() {
        return professionTxt.get();
    }

    public int getId() { return id; }

    public int getConfidencePcs() {return confidencePcs;}

    public int getConfidenceNaf() {return confidenceNaf;}

    public String getManualPcsCode () {
        return manualPcsCode.get();
    }

    public String getManualNafCode () {
        return manualNafCode.get();
    }

    public String getSecteurTxt () {
        return secteurTxt.get();
    }

    public String getCodePcs() {
        return codePcs.get();
    }

    public String getCodeNaf() { return codeNaf.get(); }

    public ComboBox getSuggestedCodesPcs() { return suggestedCodesPcs; }

    public ComboBox getSuggestedCodesNaf() { return suggestedCodesNaf; }

    public StringProperty professionProperty() { return professionTxt; }

    public StringProperty secteurProperty() { return secteurTxt; }

    public StringProperty codePcsProperty() { return codePcs; }

    public StringProperty codeNafProperty() { return codeNaf; }

    public StringProperty manualNafProperty() { return manualNafCode; }

    public StringProperty manualPcsProperty() { return manualPcsCode; }


}
