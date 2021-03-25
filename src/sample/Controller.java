package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;


import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/* */
public class Controller implements Initializable {

    public DbModel dbModel = new DbModel();
    public TextField filterField;
    public TableView<JobDescription> descriptionTable;
    public TableColumn<JobDescription, String> activitiesColumn;
    public TableColumn<JobDescription, String> suggestedCodesPcs;
    public TableColumn<JobDescription, String> suggestedCodesNaf;
    public TableColumn<JobDescription, String> sectorColumn;
    public TableColumn<JobDescription, String> pcsManualColumn;
    public TableColumn<JobDescription, String> nafManualColumn;
    public ObservableList<JobDescription> jobDescriptions = FXCollections.observableArrayList();

    //Initialize program, here the databasemodel(class) is invoked and a connection is made.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (dbModel.isDbConnected()) {
            try {
                dbModel.getList(jobDescriptions);
                FilteredList<JobDescription> filteredDescriptions = new FilteredList<>(jobDescriptions, s -> true);
                descriptionTable.setEditable(true);
                filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredDescriptions.setPredicate(jobDescription -> {
                        // If filter text is empty, display all descriptions.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        // Compare whether the confidence of the first code is higher than the threshold
                        int threshold = Integer.parseInt(newValue);
                        if (jobDescription.getConfidencePcs() > threshold && jobDescription.getConfidenceNaf() > threshold) {
                            return false;
                        } else {
                            return true;
                        }
                    });
                });

                SortedList<JobDescription> sortedDescriptions = new SortedList<>(filteredDescriptions);
                // 4. Bind the SortedList comparator to the TableView comparator.
                sortedDescriptions.comparatorProperty().bind(descriptionTable.comparatorProperty());
                // 5. Add sorted (and filtered) data to the table.
                descriptionTable.setItems(sortedDescriptions);
                activitiesColumn.setCellValueFactory(cellData -> cellData.getValue().secteurProperty());
                sectorColumn.setCellValueFactory(cellData -> cellData.getValue().professionProperty());
                pcsManualColumn.setCellValueFactory(cellData -> cellData.getValue().manualPcsProperty());
                pcsManualColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                pcsManualColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<JobDescription, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<JobDescription, String> jobDescriptionStringCellEditEvent) {
                        if (jobDescriptionStringCellEditEvent.getNewValue().equals("")) {
                            JobDescription jobDescription = jobDescriptionStringCellEditEvent.getRowValue();
                            jobDescription.setManualPcsCode(jobDescriptionStringCellEditEvent.getNewValue());
                            if (jobDescription.isSetToManual()) {
                                jobDescription.setPcsToSuggest();
                                jobDescription.getSuggestedCodesPcs().getSelectionModel().selectFirst();
                            }
                            descriptionTable.refresh();
                        } else {
                            JobDescription jobDescription = jobDescriptionStringCellEditEvent.getRowValue();
                            jobDescription.setManualPcsCode(jobDescriptionStringCellEditEvent.getNewValue());
                            jobDescription.setPcsToManual();
                            jobDescription.getSuggestedCodesPcs().getSelectionModel().selectFirst();
                            descriptionTable.refresh();
                        }
                    }
                });
                nafManualColumn.setCellValueFactory(cellData -> cellData.getValue().manualNafProperty());
                nafManualColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                nafManualColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<JobDescription, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<JobDescription, String> jobDescriptionStringCellEditEvent) {
                        if (jobDescriptionStringCellEditEvent.getNewValue().equals("")) {
                            JobDescription jobDescription = jobDescriptionStringCellEditEvent.getRowValue();
                            jobDescription.setManualNafCode(jobDescriptionStringCellEditEvent.getNewValue());
                            if (jobDescription.isSetToManual()){
                                jobDescription.setNafToSuggest();
                                jobDescription.getSuggestedCodesNaf().getSelectionModel().selectFirst();
                            }
                            descriptionTable.refresh();
                        } else {
                            JobDescription jobDescription = jobDescriptionStringCellEditEvent.getRowValue();
                            jobDescription.setManualNafCode(jobDescriptionStringCellEditEvent.getNewValue());
                            jobDescription.setNafToManual();
                            jobDescription.getSuggestedCodesNaf().getSelectionModel().selectFirst();
                            descriptionTable.refresh();
                        }
                    }
                });
                suggestedCodesPcs.setCellValueFactory(new PropertyValueFactory<>("suggestedCodesPcs"));
                suggestedCodesNaf.setCellValueFactory(new PropertyValueFactory<>("suggestedCodesNaf"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            System.out.println("Not connected");
        }
    }

    public ObservableList<JobDescription> getDescriptions() {
        return jobDescriptions;
    }

    public void saveDescription() throws SQLException {
        filterField.setText("100");
        JobDescription description;
        List<List<String>> arrList = new ArrayList<>();
        for (int i = 0; i < descriptionTable.getItems().size(); i++) {
            description = descriptionTable.getItems().get(i);
            if (description.getManualNafCode().equals("") && description.getManualPcsCode().equals("")) {
                arrList.add(new ArrayList<>());
                arrList.get(i).add(Integer.toString(description.getId()));
                arrList.get(i).add((String) description.getSuggestedCodesPcs().getValue());
                arrList.get(i).add((String) description.getSuggestedCodesNaf().getValue());
            } else if (!description.getManualNafCode().equals("") && !description.getManualPcsCode().equals("")) {
                arrList.add(new ArrayList<>());
                arrList.get(i).add(Integer.toString(description.getId()));
                arrList.get(i).add(description.getManualPcsCode());
                arrList.get(i).add(description.getManualNafCode());
            } else if (!description.getManualNafCode().equals("") && description.getManualPcsCode().equals("")) {
                arrList.add(new ArrayList<>());
                arrList.get(i).add(Integer.toString(description.getId()));
                arrList.get(i).add((String) description.getSuggestedCodesPcs().getValue());
                arrList.get(i).add(description.getManualNafCode());
            } else if (description.getManualNafCode().equals("") && !description.getManualPcsCode().equals("")) {
                arrList.add(new ArrayList<>());
                arrList.get(i).add(Integer.toString(description.getId()));
                arrList.get(i).add(description.getManualPcsCode());
                arrList.get(i).add((String) description.getSuggestedCodesNaf().getValue());
            }
        }
        dbModel.saveCode(arrList);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Codes saved");
        alert.setHeaderText(null);
        alert.setContentText("The job descriptions with the corresponding codes are saved.");

        alert.showAndWait();
    }

    public void openThresholdMenu() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Set confidence threshold");
        dialog.setHeaderText("Set confidence threshold (%)");
        dialog.showAndWait().ifPresent(string -> filterField.setText(string));
    }
}

    /*public void saveAndNext() throws SQLException {
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
    }*/

    /*public boolean checkPcs() {
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

    }*/

