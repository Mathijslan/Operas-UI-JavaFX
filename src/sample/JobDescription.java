package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class JobDescription {

    int id;
    StringProperty professionTxt;
    StringProperty secteurTxt;
    StringProperty codePcs;
    StringProperty codeNaf;
    int confidence;
    private ComboBox suggestedCodes;

    public JobDescription(int subjectId, String profession, String secteur, String pcs, String naf, int codeConfidence, ObservableList codes) {
        id = subjectId;
        this.confidence = codeConfidence;
        this.professionTxt = new SimpleStringProperty(profession);
        this.secteurTxt = new SimpleStringProperty(secteur);
        this.codePcs = new SimpleStringProperty(pcs);
        this.codeNaf = new SimpleStringProperty(naf);
        this.suggestedCodes = new ComboBox(codes);
    }

    public void setSubjectId(int id) {
        this.id = id;
    }

    public void setProfessionTxt(String txt) {
        this.professionTxt.set(txt) ;
    }

    public void setSecteurTxt (String txt) {
        this.secteurTxt.set(txt);
    }

    public void setCodePcs(String pcs) {
        this.codePcs.set(pcs);
    }

    public void setCodeNaf(String naf) {
        this.codeNaf.set(naf);
    }

    public void setConfidence(int confidence) { this.confidence = confidence; }

    public void setSuggestedCodes(ComboBox suggestedCodes) {this.suggestedCodes = suggestedCodes;}

    public String getProfessionTxt() {
        return professionTxt.get();
    }

    public int getId() { return id; }

    public int getConfidence() {return confidence;}

    public String getSecteurTxt () {
        return secteurTxt.get();
    }

    public String getCodePcs() {
        return codePcs.get();
    }

    public String getCodeNaf() { return codeNaf.get(); }

    public ComboBox getSuggestedCodes() { return suggestedCodes; }

    public StringProperty professionProperty() { return professionTxt; }

    public StringProperty secteurProperty() { return secteurTxt; }

    public StringProperty codePcsProperty() { return codePcs; }

    public StringProperty codeNafProperty() { return codeNaf; }

}
