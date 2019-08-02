package com.oceanos.csvloganalizer;

import com.oceanos.csvloganalizer.csv.CsvDataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * @author slonikmak on 02.08.2019.
 */

public class DataSetRepository implements Repository {

    private ObservableList<CsvDataSet> dataSets = FXCollections.observableArrayList();

    @Override
    public ObservableList<CsvDataSet> getDataSet() {
        return dataSets;
    }

    @Override
    public void addDataSets(List<CsvDataSet> dataSets){
        this.dataSets.addAll(dataSets);
    }
}
