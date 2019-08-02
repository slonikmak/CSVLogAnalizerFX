package com.oceanos.csvloganalizer.view;

import com.google.inject.Inject;
import com.oceanos.csvloganalizer.CellData;
import com.oceanos.csvloganalizer.Repository;
import com.oceanos.csvloganalizer.csv.CsvReader;
import com.oceanos.csvloganalizer.viewmodel.CsvLoaderViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * @autor slonikmak on 01.08.2019.
 */
public class CsvLoaderView implements FxmlView<CsvLoaderViewModel> {

    Stage stage;

    @InjectViewModel
    CsvLoaderViewModel viewModel;

    @FXML
    private Label pathLabel;

    @FXML
    private TextArea preview;

    @FXML
    private ChoiceBox<String> delimiterBox;

    @FXML
    private AnchorPane tablePane;

    @FXML
    void cancel(ActionEvent event) {
        close();
    }

    @FXML
    void load(ActionEvent event) {
        viewModel.addDataSets();
        close();
    }

    @FXML
    void openFile(ActionEvent event) {
        Preferences prefs = Preferences.userNodeForPackage(CsvLoaderView.class);
        String last = prefs.get("lastFolder", "null");

        FileChooser fileChooser = new FileChooser();
        if (!last.equals("null")){
            Path path = Paths.get(last).getParent();
            fileChooser.setInitialDirectory(path.toFile());
        }
        File file = fileChooser.showOpenDialog(preview.getScene().getWindow());
        if (file!=null){
            prefs.put("lastFolder", file.getPath());
            pathLabel.setText(file.getPath());
            try {
                viewModel.getCsvReader().read(file.getPath());
                List<String> header = viewModel.getCsvReader().getHeader();
                preview.setText(header.stream().reduce("",(a,s)->a+" "+s));
                viewModel.fillCells(header);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void close(){
        if (stage!=null){
            stage.close();
        }
    }


    public void initialize(){
        delimiterBox.setItems(viewModel.getDelimiters());
        delimiterBox.getSelectionModel().selectedItemProperty().addListener((o,oV,nV) ->{
            initTable();
        });
    }

    private void initTable(){

        TableView<CellData> tableView = new TableView<>();

        tableView.setEditable(true);

        TableColumn<CellData, String> nameColumn = new TableColumn<>("Имя");
        TableColumn<CellData, Boolean> enabledColumn = new TableColumn<>("Добавить");
        TableColumn<CellData, CsvReader.ColumnType> typeColumn = new TableColumn<>("Тип");


        enabledColumn.setCellValueFactory(new PropertyValueFactory<>("enable"));

        /////Name
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.<CellData> forTableColumn());
        nameColumn.setMinWidth(200);
        nameColumn.setOnEditCommit((TableColumn.CellEditEvent<CellData, String> event) -> {
            TablePosition<CellData, String> pos = event.getTablePosition();
            String newName = event.getNewValue();
            int row = pos.getRow();
            CellData cellData = event.getTableView().getItems().get(row);
            cellData.setName(newName);
        });

        //Type
        ObservableList<CsvReader.ColumnType> types = FXCollections.observableArrayList(CsvReader.ColumnType.values());
        typeColumn.setCellValueFactory(param -> {
            CellData cellData = param.getValue();
            CsvReader.ColumnType type = cellData.getColumnType();
            return new SimpleObjectProperty<CsvReader.ColumnType>(type);
        });
        typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(types));
        typeColumn.setOnEditCommit(event -> {
            TablePosition<CellData, CsvReader.ColumnType> pos = event.getTablePosition();
            CsvReader.ColumnType type = event.getNewValue();
            int row = pos.getRow();
            CellData cellData = event.getTableView().getItems().get(row);

            cellData.setType(type);
        });
        typeColumn.setMinWidth(120);

        // ==== ENABLE? (CHECK BOX) ===
        enabledColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CellData, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<CellData, Boolean> param) {
                CellData cellData = param.getValue();
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(cellData.isEnable());

                // Note: singleCol.setOnEditCommit(): Not work for
                // CheckBoxTableCell.

                // When "Single?" column change.
                booleanProp.addListener(new ChangeListener<Boolean>() {

                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                        Boolean newValue) {
                        cellData.setEnable(newValue);
                    }
                });
                return booleanProp;
            }
        });

        enabledColumn.setCellFactory(new Callback<TableColumn<CellData, Boolean>,
                TableCell<CellData, Boolean>>() {
            @Override
            public TableCell<CellData, Boolean> call(TableColumn<CellData, Boolean> p) {
                CheckBoxTableCell<CellData, Boolean> cell = new CheckBoxTableCell<CellData, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        tableView.setItems(viewModel.getCellDataObservableList());

        tableView.getColumns().addAll(nameColumn, enabledColumn, typeColumn);

        tablePane.getChildren().add(tableView);
        tableView.setPrefWidth(500);
        tableView.setPrefHeight(150);
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }


}

