package main;

import chapin.ChapinMetrics;
import chapin.enums.GroupType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import spen.SpenMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Item> tableSpen;

    @FXML
    private TableColumn<Item, String> spenVar;

    @FXML
    private TableColumn<Item, String> spens;

    @FXML
    private TableView<Item> tableChapinP;

    @FXML
    private TableColumn<Item, String> chapinPVar;

    @FXML
    private TableColumn<Item, String> chapinPAmount;

    @FXML
    private TableView<Item> tableChapinM;

    @FXML
    private TableColumn<Item, String> chapinMVar;

    @FXML
    private TableColumn<Item, String> ChapinMAmount;

    @FXML
    private TableView<Item> tableChapinC;

    @FXML
    private TableColumn<Item, String> chapinCVar;

    @FXML
    private TableColumn<Item, String> chapinCAmount;

    @FXML
    private TableView<Item> tableChapinT;

    @FXML
    private TableColumn<Item, String> chapinTVar;

    @FXML
    private TableColumn<Item, String> chapinTAmount;

    @FXML
    private TableView<Item> tableChapinIOP;

    @FXML
    private TableColumn<Item, String> chapinIOPVar;

    @FXML
    private TableColumn<Item, String> chapinIOPAmount;

    @FXML
    private TableView<Item> tableChapinIOM;

    @FXML
    private TableColumn<Item, String> chapinIOMVar;

    @FXML
    private TableColumn<Item, String> ChapinIOMAmount;

    @FXML
    private TableView<Item> tableChapinIOC;

    @FXML
    private TableColumn<Item, String> chapinIOCVar;

    @FXML
    private TableColumn<Item, String> chapinIOCAmount;

    @FXML
    private TableView<Item> tableChapinIOT;

    @FXML
    private TableColumn<Item, String> chapinIOTVar;

    @FXML
    private TableColumn<Item, String> chapinIOTAmount;


    private static ObservableList<Item> dataSpenVars = FXCollections.observableArrayList();

    private static ObservableList<Item> dataChapinIOPVars = FXCollections.observableArrayList();
    private static ObservableList<Item> dataChapinIOMVars = FXCollections.observableArrayList();
    private static ObservableList<Item> dataChapinIOCVars = FXCollections.observableArrayList();
    private static ObservableList<Item> dataChapinIOTVars = FXCollections.observableArrayList();

    private static ObservableList<Item> dataChapinPVars = FXCollections.observableArrayList();
    private static ObservableList<Item> dataChapinMVars = FXCollections.observableArrayList();
    private static ObservableList<Item> dataChapinCVars = FXCollections.observableArrayList();
    private static ObservableList<Item> dataChapinTVars = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spenVar.setCellValueFactory(new PropertyValueFactory<>("item"));
        spens.setCellValueFactory(new PropertyValueFactory<>("amount"));
        chapinPVar.setCellValueFactory(new PropertyValueFactory<>("item"));
        chapinPAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        chapinMVar.setCellValueFactory(new PropertyValueFactory<>("item"));
        ChapinMAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        chapinCVar.setCellValueFactory(new PropertyValueFactory<>("Item"));
        chapinCAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        chapinTVar.setCellValueFactory(new PropertyValueFactory<>("item"));
        chapinTAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        chapinIOPVar.setCellValueFactory(new PropertyValueFactory<>("item"));
        chapinIOPAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        chapinIOMVar.setCellValueFactory(new PropertyValueFactory<>("item"));
        ChapinIOMAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        chapinIOCVar.setCellValueFactory(new PropertyValueFactory<>("item"));
        chapinIOCAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        chapinIOTVar.setCellValueFactory(new PropertyValueFactory<>("item"));
        chapinIOTAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    @FXML
    private void exitAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void aboutDevelopers(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About developers");
        alert.setContentText("This program was developed by Bakyt Madi and Maiski Vlad, group 951007");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void aboutProgram(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About program");
        alert.setContentText("This program shows Spen and Chapin metrics for a program written in Kotlin");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    void clearData() {
        dataChapinPVars.clear();
        dataChapinMVars.clear();
        dataChapinCVars.clear();
        dataChapinTVars.clear();

        dataChapinIOPVars.clear();
        dataChapinIOMVars.clear();
        dataChapinIOCVars.clear();
        dataChapinIOTVars.clear();

        dataSpenVars.clear();
    }

    @FXML
    private void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showOpenDialog(Main.getStage());
        if (file != null && file.getAbsolutePath().matches("^.+.kt$")) {
            StringBuilder sb = new StringBuilder("");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String s = br.readLine();
                while(s != null) {
                    sb.append(s).append("\n");
                    s = br.readLine();
                }
                clearData();
            } catch (Exception e) {
                System.err.println("File error");
            }

            ChapinMetrics chapinMetrics = new ChapinMetrics(sb.toString());
            SpenMetrics spenMetrics = new SpenMetrics(sb.toString());

            fillData(spenMetrics.getSpens(), chapinMetrics.getChapinTypes(), chapinMetrics.getIOChapinTypes());
            fillTables();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error file");
            alert.setHeaderText(null);
            alert.setContentText((file == null ? "You haven't selected a file."
                    : "Selected file is not a Kotlin program.") + " Please, try again.");
            alert.showAndWait();
        }
    }



    private static void fillData(Map<String, Integer> spens, Map<GroupType, List<String>> chapin, Map<GroupType, List<String>> chapinIO) {

        spens.forEach((name, amount) -> {
            dataSpenVars.add(new Item(name, amount.toString()));
        });

        chapin.forEach((group, vars) -> {
            vars.forEach((name) -> {
                switch (group){
                    case C:
                        dataChapinCVars.add(new Item(name, ""));
                        break;
                    case M:
                        dataChapinMVars.add(new Item(name, ""));
                        break;
                    case P:
                        dataChapinPVars.add(new Item(name, ""));
                        break;
                    case T:
                        dataChapinTVars.add(new Item(name, ""));
                        break;
                }
            });
        });

        chapinIO.forEach((group, vars) -> {
            vars.forEach((name) -> {
                switch (group){
                    case C:
                        dataChapinIOCVars.add(new Item(name, ""));
                        break;
                    case M:
                        dataChapinIOMVars.add(new Item(name, ""));
                        break;
                    case P:
                        dataChapinIOPVars.add(new Item(name, ""));
                        break;
                    case T:
                        dataChapinIOTVars.add(new Item(name, ""));
                        break;
                }
            });
        });
        dataChapinIOPVars.add(new Item("", Integer.toString(dataChapinIOTVars.size())));
        dataChapinIOMVars.add(new Item("", Integer.toString(dataChapinIOMVars.size())));
        dataChapinIOCVars.add(new Item("", Integer.toString(dataChapinIOCVars.size())));
        dataChapinIOTVars.add(new Item("", Integer.toString(dataChapinIOTVars.size())));
        dataChapinPVars.add(new Item("", Integer.toString(dataChapinPVars.size())));
        dataChapinMVars.add(new Item("", Integer.toString(dataChapinMVars.size())));
        dataChapinCVars.add(new Item("", Integer.toString(dataChapinCVars.size())));
        dataChapinTVars.add(new Item("", Integer.toString(dataChapinTVars.size())));
    }

    public void fillTables() {
        tableSpen.setItems(dataSpenVars);
        tableChapinP.setItems(dataChapinPVars);
        tableChapinM.setItems(dataChapinMVars);
        tableChapinC.setItems(dataChapinCVars);
        tableChapinT.setItems(dataChapinTVars);
        tableChapinIOP.setItems(dataChapinIOPVars);
        tableChapinIOM.setItems(dataChapinIOMVars);
        tableChapinIOC.setItems(dataChapinIOCVars);
        tableChapinIOT.setItems(dataChapinIOTVars);
    }
}