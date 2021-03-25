package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.sql.*;
import java.util.List;

public class DbModel {
    Connection connection;
    ResultSet allDescriptions;
    

    public DbModel() {
        connection = DbConnection.connector();
        if (connection == null) System.out.println("Geen verbinding");
    }

    public boolean isDbConnected() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<JobDescription> getList(ObservableList<JobDescription> descriptions) throws SQLException {
        Statement stmnt = connection.createStatement();
        allDescriptions = stmnt.executeQuery("select * from descriptions");
        while (allDescriptions.next()) {
            JobDescription jobDescription = new JobDescription(9999, "profession", "secteur", "pcs", "naf", 0,"","", FXCollections.observableArrayList("codesPcs"), 0, FXCollections.observableArrayList("codesNaf"), false);
            jobDescription.setSubjectId(allDescriptions.getInt("subject_id"));
            jobDescription.setProfessionTxt(allDescriptions.getString("profession_txt"));
            jobDescription.setSecteurTxt(allDescriptions.getString("secteur_txt"));
            jobDescription.setConfidencePcs(allDescriptions.getInt("code_1_conf_pcs"));
            jobDescription.setConfidenceNaf(allDescriptions.getInt("code_1_conf_naf"));
            ComboBox codesPcs = new ComboBox(FXCollections.observableArrayList(allDescriptions.getString("suggested_code_1_pcs") + " | "
                    + allDescriptions.getString("code_1_conf_pcs") + "%", allDescriptions.getString("suggested_code_2_pcs")  + " | "
                    + allDescriptions.getString("code_2_conf_pcs") + "%", allDescriptions.getString("suggested_code_3_pcs")  + " | "
                    + allDescriptions.getString("code_3_conf_pcs") + "%"));
            ComboBox codesNaf = new ComboBox(FXCollections.observableArrayList(allDescriptions.getString("suggested_code_1_naf") + " | "
                    + allDescriptions.getString("code_1_conf_naf") + "%", allDescriptions.getString("suggested_code_2_naf")  + " | "
                    + allDescriptions.getString("code_2_conf_naf") + "%", allDescriptions.getString("suggested_code_3_naf")  + " | "
                    + allDescriptions.getString("code_3_conf_naf") + "%"));
            codesPcs.getSelectionModel().selectFirst();
            codesNaf.getSelectionModel().selectFirst();
            jobDescription.setSuggestedCodesPcs(codesPcs);
            jobDescription.setSuggestedCodesNaf(codesNaf);
            descriptions.add(jobDescription);
        }
        return descriptions;
    }

    public void saveCode(List<List<String>> descriptions) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE descriptions SET code_pcs = ?,code_naf = ? WHERE subject_id = ?");
        for (int i=0; i < descriptions.size(); i++){
                String id = descriptions.get(i).get(0);

                String codePcs = descriptions.get(i).get(1);
                String codeNaf = descriptions.get(i).get(2);

                String[] pcsParts = codePcs.split(" | ");
                String[] nafParts = codeNaf.split(" | ");

                int intCode = Integer.parseInt(id);
                ps.setInt(3, intCode);
                ps.setString(1, pcsParts[0]);
                ps.setString(2, nafParts[0]);
                ps.executeUpdate();
            }
        }
}
