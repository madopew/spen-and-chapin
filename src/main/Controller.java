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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<SpenItem> tableSpen;

    @FXML
    private TableColumn<SpenItem, String> spenVar;

    @FXML
    private TableColumn<SpenItem, String> spens;

    @FXML
    private TableView<Item> tableChapinFull;

    @FXML
    private TableColumn<Item, String> chapinTable;

    @FXML
    private TableColumn<Item, String> chapinPVar;

    @FXML
    private TableColumn<Item, String> chapinMVar;

    @FXML
    private TableColumn<Item, String> chapinCVar;

    @FXML
    private TableColumn<Item, String> chapinTVar;

    @FXML
    private TableColumn<Item, String> chapinIOTable;

    @FXML
    private TableColumn<Item, String> chapinIOMVar;

    @FXML
    private TableColumn<Item, String> chapinIOCVar;

    @FXML
    private TableColumn<Item, String> chapinIOTVar;

    @FXML
    private Text chapinP;

    @FXML
    private Text chapinM;

    @FXML
    private Text chapinC;

    @FXML
    private Text chapinT;

    @FXML
    private Text chapinIOP;

    @FXML
    private Text chapinIOM;

    @FXML
    private Text chapinIOC;

    @FXML
    private Text spensSum;

    @FXML
    private Text chapinQ;

    @FXML
    private Text chapinIOQ;

    @FXML
    private TableColumn<Item, String> chapinIOPVar;

    @FXML
    private Text chapinIOT;

    final String spensSumText = "Sum = ";
    final String pText = "p = ";
    final String mText = "m = ";
    final String cText = "c = ";
    final String tText = "t = ";

    private static ObservableList<SpenItem> dataSpenVars = FXCollections.observableArrayList();

    private static ObservableList<Item> dataChapinTable = FXCollections.observableArrayList();

    private static ObservableList<String> dataChapinIOPVars = FXCollections.observableArrayList();
    private static ObservableList<String> dataChapinIOMVars = FXCollections.observableArrayList();
    private static ObservableList<String> dataChapinIOCVars = FXCollections.observableArrayList();
    private static ObservableList<String> dataChapinIOTVars = FXCollections.observableArrayList();

    private static ObservableList<String> dataChapinPVars = FXCollections.observableArrayList();
    private static ObservableList<String> dataChapinMVars = FXCollections.observableArrayList();
    private static ObservableList<String> dataChapinCVars = FXCollections.observableArrayList();
    private static ObservableList<String> dataChapinTVars = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spenVar.setCellValueFactory(new PropertyValueFactory<SpenItem, String>("spenName"));
        spens.setCellValueFactory(new PropertyValueFactory<SpenItem, String>("spen"));

        chapinPVar.setCellValueFactory(new PropertyValueFactory<Item, String>("p"));
        chapinMVar.setCellValueFactory(new PropertyValueFactory<Item, String>("m"));
        chapinCVar.setCellValueFactory(new PropertyValueFactory<Item, String>("c"));
        chapinTVar.setCellValueFactory(new PropertyValueFactory<Item, String>("t"));
        chapinIOPVar.setCellValueFactory(new PropertyValueFactory<Item, String>("IOp"));
        chapinIOMVar.setCellValueFactory(new PropertyValueFactory<Item, String>("IOm"));
        chapinIOCVar.setCellValueFactory(new PropertyValueFactory<Item, String>("IOc"));
        chapinIOTVar.setCellValueFactory(new PropertyValueFactory<Item, String>("IOt"));
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

        dataChapinTable.clear();

        dataSpenVars.clear();

        spenSum = 0;
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
                while (s != null) {
                    sb.append(s).append("\n");
                    s = br.readLine();
                }
                clearData();
            } catch (Exception e) {
                System.err.println("File error");
            }

            ChapinMetrics chapinMetrics = new ChapinMetrics(sb.toString());
            SpenMetrics spenMetrics = new SpenMetrics(sb.toString());
            setDefaultAmounts();
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

    void setDefaultAmounts() {

        chapinP.setText(pText);
        chapinM.setText(mText);
        chapinC.setText(cText);
        chapinT.setText(tText);

        chapinIOP.setText(pText);
        chapinIOM.setText(mText);
        chapinIOC.setText(cText);
        chapinIOT.setText(tText);
    }

    static int spenSum = 0;
    private void fillData(Map<String, Integer> spens, Map<GroupType, List<String>> chapin, Map<GroupType, List<String>> chapinIO) {

        spens.forEach((name, amount) -> {
            dataSpenVars.add(new SpenItem(name, amount.toString()));
            spenSum += amount;
        });


        chapin.forEach((group, vars) -> {
            vars.forEach((name) -> {
                switch (group) {
                    case C:
                        dataChapinCVars.add(name);
                        break;
                    case M:
                        dataChapinMVars.add(name);
                        break;
                    case P:
                        dataChapinPVars.add(name);
                        break;
                    case T:
                        dataChapinTVars.add(name);
                        break;
                }
            });
        });

        chapinIO.forEach((group, vars) -> {
            vars.forEach((name) -> {
                switch (group) {
                    case C:
                        dataChapinIOCVars.add(name);
                        break;
                    case M:
                        dataChapinIOMVars.add(name);
                        break;
                    case P:
                        dataChapinIOPVars.add(name);
                        break;
                    case T:
                        dataChapinIOTVars.add(name);
                        break;
                }
            });
        });

        int max = findMax();
        int index = 0;

        while (index < max) {
            String pV;
            String mV;
            String cV;
            String tV;
            String IOpV;
            String IOmV;
            String IOcV;
            String IOtV;

            if (index < dataChapinCVars.size())
                cV = dataChapinCVars.get(index);
            else
                cV = "";

            if (index < dataChapinPVars.size())
                pV = dataChapinPVars.get(index);
            else
                pV = "";

            if (index < dataChapinMVars.size())
                mV = dataChapinMVars.get(index);
            else
                mV = "";

            if (index < dataChapinTVars.size())
                tV = dataChapinTVars.get(index);
            else
                tV = "";

            if (index < dataChapinIOCVars.size())
                IOcV = dataChapinIOCVars.get(index);
            else
                IOcV = "";

            if (index < dataChapinIOPVars.size())
                IOpV = dataChapinIOPVars.get(index);
            else
                IOpV = "";

            if (index < dataChapinIOMVars.size())
                IOmV = dataChapinIOMVars.get(index);
            else
                IOmV = "";

            if (index < dataChapinIOTVars.size())
                IOtV = dataChapinIOTVars.get(index);
            else
                IOtV = "";

            dataChapinTable.add(new Item(pV ,mV, cV, tV, IOpV, IOmV, IOcV, IOtV));

            index++;
        }

        chapinQ.setText(makeQText(dataChapinPVars.size(), dataChapinMVars.size(), dataChapinCVars.size(), dataChapinTVars.size()));
        chapinIOQ.setText(makeQText(dataChapinIOPVars.size(), dataChapinIOMVars.size(), dataChapinIOCVars.size(), dataChapinIOTVars.size()));

        spensSum.setText(spensSumText + spenSum);

        chapinP.setText(pText + dataChapinPVars.size());
        chapinM.setText(mText + dataChapinMVars.size());
        chapinC.setText(cText + dataChapinCVars.size());
        chapinT.setText(tText + dataChapinTVars.size());
        chapinIOP.setText(pText + dataChapinIOPVars.size());
        chapinIOM.setText(mText + dataChapinIOMVars.size());
        chapinIOC.setText(cText + dataChapinIOCVars.size());
        chapinIOT.setText(tText + dataChapinIOTVars.size());
    }

    public String makeQText(int p, int m, int c, int t){
        double res = p + 2*m + 3*c + 0.5*t;
        return "Q = 1*" + p + " + 2*" + m + " + 3*" + c + " + 0,5*" + t  + " = " + res;
    }

    public int findMax() {
        return Math.max(dataChapinPVars.size(), Math.max(dataChapinMVars.size(),
                Math.max(dataChapinCVars.size(), Math.max(dataChapinTVars.size(),
                        Math.max(dataChapinIOPVars.size(), Math.max(dataChapinIOMVars.size(),
                                Math.max(dataChapinIOCVars.size(), dataChapinIOTVars.size())))))));
    }

    public void fillTables() {
        tableSpen.setItems(dataSpenVars);
        tableChapinFull.setItems(dataChapinTable);
    }
}