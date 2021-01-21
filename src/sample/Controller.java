package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;


import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Controller implements Initializable {

    public DbModel dbModel = new DbModel();
    public ArrayList<JobDescription> jobDescriptions;
    public Label pcsLabel;
    public Label nafLabel;
    public TextArea pcsTextArea;
    public TextArea nafTextArea;
    public ListView previousCodes;
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
        previousCodes.getItems().add(currentJd.id);
        currentJd = dbModel.getNextDescription();
        pcsLabel.setText(currentJd.getProfessionTxt());
        nafLabel.setText(currentJd.getSecteurTxt());
        pcsTextArea.setText("");
        nafTextArea.setText("");
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

    public void saveAndNext() throws SQLException {
        if (checkPcs() && checkNaf()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save " + pcsTextArea.getText() + " and " + nafTextArea.getText() + " and proceed? ", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                nextDescription();
            }
        } else if (checkNaf() && !checkPcs()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The PCS code does not follow the guidelines. \nPlease check and correct.", ButtonType.CLOSE);
            alert.showAndWait();

        } else if (!checkNaf() && checkPcs()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The NAF code does not follow the guidelines. \nPlease check and correct.", ButtonType.CLOSE);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Both codes do not follow the guidelines. \nPlease check and correct.", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    public boolean checkPcs() {
        String patternPcs = "([1-9]|#){3}([A-Z]|#)";
        Pattern Pcspattern = Pattern.compile(patternPcs);
        Matcher Pcsmatcher = Pcspattern.matcher(pcsTextArea.getText());
        return Pcsmatcher.matches();
    }

    public boolean checkNaf() {
        String patternNaf = "([1-9]|#){4}([A-Z]|#)";
        Pattern Nafpattern = Pattern.compile(patternNaf);
        Matcher Nafmatcher = Nafpattern.matcher(nafTextArea.getText());
        return Nafmatcher.matches();
    }

    public void nafColor() {
        if (checkNaf()) {
            nafTextArea.setStyle("-fx-text-fill: black");
        } else {
            nafTextArea.setStyle("-fx-text-fill: red");
        }
    }

    public void pcsColor() {
        if (checkPcs()) {
            pcsTextArea.setStyle("-fx-text-fill: black");
        } else {
            pcsTextArea.setStyle("-fx-text-fill: red");
        }

    }

    public void selectPreviousCode() throws SQLException {
        int previousId = (int) previousCodes.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to load " + previousId + " ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            currentJd = dbModel.getPreviousDescription(previousId);
            pcsLabel.setText(currentJd.getProfessionTxt());
            nafLabel.setText(currentJd.getSecteurTxt());
            pcsTextArea.setText(currentJd.getCodePcs());
            nafTextArea.setText(currentJd.getCodeNaf());
        }
    }

}
