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
            JobDescription jobDescription = new JobDescription(9999, "profession", "secteur", "pcs", "naf", 0, FXCollections.observableArrayList("codes"));
            jobDescription.setSubjectId(allDescriptions.getInt("subject_id"));
            jobDescription.setProfessionTxt(allDescriptions.getString("profession_txt"));
            jobDescription.setSecteurTxt(allDescriptions.getString("secteur_txt"));
            jobDescription.setCodePcs(allDescriptions.getString("code_pcs"));
            jobDescription.setCodeNaf(allDescriptions.getString("code_naf"));
            jobDescription.setConfidence(allDescriptions.getInt("code_1_conf"));
            ComboBox codes = new ComboBox(FXCollections.observableArrayList(allDescriptions.getString("suggested_code_1") + " | "
                    + allDescriptions.getString("code_1_conf") + "%", allDescriptions.getString("suggested_code_2")  + " | "
                    + allDescriptions.getString("code_2_conf") + "%", allDescriptions.getString("suggested_code_3")  + " | "
                    + allDescriptions.getString("code_3_conf") + "%"));
            codes.getSelectionModel().selectFirst();
            jobDescription.setSuggestedCodes(codes);
            descriptions.add(jobDescription);
        }
        return descriptions;
    }

    public void saveCode(List<List<String>> descriptions) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE descriptions SET code_pcs = ? WHERE subject_id = ?");
        for (int i=0; i < descriptions.size(); i++){
                String id = descriptions.get(i).get(0);
                String code = descriptions.get(i).get(1);
                String[] parts = code.split(" | ");
                int intCode = Integer.parseInt(id);
                ps.setInt(2, intCode);
                ps.setString(1, parts[0]);
                ps.executeUpdate();
            }
        }
}
