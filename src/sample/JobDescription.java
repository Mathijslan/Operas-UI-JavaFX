package sample;

public class JobDescription {

    int id;
    String professionTxt;
    String secteurTxt;
    String codePcs;
    String codeNaf;

    public JobDescription(int subjectId, String profession, String secteur, String pcs, String naf) {
        id = subjectId;
        professionTxt = profession;
        secteurTxt = secteur;
        codePcs = pcs;
        codeNaf = naf;
    }

    public void setSubjectId(int id) {
        this.id = id;
    }

    public void setProfessionTxt(String txt) {
        this.professionTxt = txt;
    }

    public void setSecteurTxt (String txt) {
        this.secteurTxt = txt;
    }

    public void setCodePcs(String pcs) {
        this.codePcs = pcs;
    }

    public void setCodeNaf(String naf) {
        this.codeNaf = naf;
    }

    public String getProfessionTxt() {
        return this.professionTxt;
    }

    public String getSecteurTxt () {
        return this.secteurTxt;
    }

    public String getCodePcs() {
        return this.codePcs;
    }

    public String getCodeNaf() { return this.codeNaf; }
}
