package sample;

import java.sql.*;
import java.util.ArrayList;
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

    public ArrayList<JobDescription> getDescriptions() {
        ArrayList<JobDescription> jobDescriptions = new ArrayList<>();
        try (
                Statement stmnt = connection.createStatement();
                ResultSet descriptions = stmnt.executeQuery("select * from descriptions");
        ) {
            while (descriptions.next()) {
                int id = descriptions.getInt("subject_id");
                String profession = descriptions.getString("profession_txt");
                String secteur = descriptions.getString("secteur_txt");
                String pcs = descriptions.getString("code_pcs");
                String naf = descriptions.getString("code_naf");
                JobDescription jobDescription = new JobDescription(id, profession, secteur, pcs, naf);
                jobDescriptions.add(jobDescription);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return jobDescriptions;
    }

    public void getRS() throws SQLException {
        Statement stmnt = connection.createStatement();
        allDescriptions = stmnt.executeQuery("select * from descriptions");

    }

    public JobDescription getNextDescription() throws SQLException {
        JobDescription jobDescription = new JobDescription(9999, "profession", "secteur", "pcs", "naf");
        if (allDescriptions.next()) {
            jobDescription.setSubjectId(allDescriptions.getInt("subject_id"));
            jobDescription.setProfessionTxt(allDescriptions.getString("profession_txt"));
            jobDescription.setSecteurTxt(allDescriptions.getString("secteur_txt"));
            jobDescription.setCodePcs(allDescriptions.getString("code_pcs"));
            jobDescription.setCodeNaf(allDescriptions.getString("code_naf"));
        }
        return jobDescription;
    }

    public JobDescription getPreviousDescription() throws SQLException {
        JobDescription jobDescription = new JobDescription(9999, "profession", "secteur", "pcs", "naf");
        if (allDescriptions.previous()) {
            jobDescription.setSubjectId(allDescriptions.getInt("subject_id"));
            jobDescription.setProfessionTxt(allDescriptions.getString("profession_txt"));
            jobDescription.setSecteurTxt(allDescriptions.getString("secteur_txt"));
            jobDescription.setCodePcs(allDescriptions.getString("code_pcs"));
            jobDescription.setCodeNaf(allDescriptions.getString("code_naf"));
        }
        return jobDescription;
    }

    public void saveDescription(JobDescription jobDescription) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE descriptions SET code_pcs = ?, code_naf = ? WHERE subject_id = ?");
        ps.setString(1, jobDescription.codePcs);
        ps.setString(2, jobDescription.codeNaf);
        ps.setInt(3, jobDescription.id);
        ps.executeUpdate();
    }
}
