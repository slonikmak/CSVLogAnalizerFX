package com.oceanos.csvloganalizer.view;

import com.google.inject.Inject;
import com.oceanos.csvloganalizer.viewmodel.CsvLoaderViewModel;
import com.oceanos.csvloganalizer.viewmodel.MainViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor slonikmak on 02.08.2019.
 */
public class MainView implements FxmlView<MainViewModel> {

    @InjectViewModel
    MainViewModel viewModel;

    @Inject
    private Stage primaryStage;

    @FXML
    private AnchorPane chartPane;

    @FXML
    private AnchorPane tablePane;

    @FXML
    void openCsv(ActionEvent event) {
        final Stage dialogStage = new Stage(StageStyle.UTILITY);
        dialogStage.initOwner(primaryStage);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        final ViewTuple<CsvLoaderView, CsvLoaderViewModel> tuple = FluentViewLoader.fxmlView(CsvLoaderView.class).load();
        tuple.getCodeBehind().setStage(dialogStage);
        final Parent view = tuple.getView();
        Scene scene = new Scene(view);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    @FXML
    @SuppressWarnings(value = "unchecked")
    void showPlot(ActionEvent e){
        List<Double> values = viewModel.getDataSets().get(0).getValues();
        NumberAxis xAxis = new NumberAxis(0,values.size(), 1);
        xAxis.setLabel("Numbers");
        NumberAxis yAxis = new NumberAxis(0, values.stream().max(Double::compareTo).get(), 0.05);
        yAxis.setLabel("No.of schools");
        LineChart linechart = new LineChart(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName(viewModel.getDataSets().get(0).getName());
        for (int i = 0; i < values.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, values.get(i)));
        }
        linechart.getData().add(series);

        linechart.setCreateSymbols(false); //this part is the one that consumes more time
        linechart.setAnimated(false);

        chartPane.getChildren().clear();
        chartPane.getChildren().add(linechart);
        AnchorPane.setBottomAnchor(linechart, 0.);
        AnchorPane.setTopAnchor(linechart, 0.);
        AnchorPane.setLeftAnchor(linechart, 0.);
        AnchorPane.setRightAnchor(linechart, 0.);
    }

    public void initialize(){
        viewModel.getDataSets().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                //createTable();
            }
        });
    }

    private void createTable(){
        int rowCount = viewModel.getDataSets().get(0).getValues().size();
        int columnCount = viewModel.getDataSets().size();
        GridBase grid = new GridBase(rowCount, columnCount);
        grid.getColumnHeaders().clear();
        grid.getColumnHeaders().addAll(viewModel.getDataSets().stream().map(csvDataSet -> csvDataSet.getName()).collect(Collectors.toCollection(ArrayList::new)));
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for (int row = 0; row < grid.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            for (int column = 0; column < grid.getColumnCount(); ++column) {
                list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,viewModel.getDataSets().get(column).getValues().get(row).toString()));
            }
            rows.add(list);
        }
        grid.setRows(rows);

        SpreadsheetView spv = new SpreadsheetView(grid);

        AnchorPane.setBottomAnchor(spv, 0.);
        AnchorPane.setTopAnchor(spv, 0.);
        AnchorPane.setLeftAnchor(spv, 0.);
        AnchorPane.setRightAnchor(spv, 0.);

        tablePane.getChildren().clear();
        tablePane.getChildren().add(spv);
    }


}
