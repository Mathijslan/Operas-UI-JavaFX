package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public DbModel dbModel = new DbModel();
    public ArrayList<JobDescription> jobDescriptions;
    public Label pcsLabel;
    public Label nafLabel;
    public TextArea pcsTextArea;
    public TextArea nafTextArea;
    public JobDescription currentJd;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (dbModel.isDbConnected()) {
            try {
                dbModel.getRS();
                currentJd = dbModel.getNextDescription();
                pcsLabel.setText(currentJd.getProfessionTxt());
                nafLabel.setText(currentJd.getSecteurTxt());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            pcsLabel.setText("Not Connected");
        }
    }

    public void nextDescription() throws SQLException {
        saveDescription();
        currentJd = dbModel.getNextDescription();
        pcsLabel.setText(currentJd.getProfessionTxt());
        nafLabel.setText(currentJd.getSecteurTxt());
    }

    public void previousDescription() throws SQLException {
        currentJd = dbModel.getPreviousDescription();
        pcsLabel.setText(currentJd.getProfessionTxt());
        nafLabel.setText(currentJd.getSecteurTxt());
    }

    public void saveDescription() throws SQLException {
        currentJd.setCodePcs(pcsTextArea.getText());
        currentJd.setCodeNaf(nafTextArea.getText());
        dbModel.saveDescription(currentJd);
    }

}
