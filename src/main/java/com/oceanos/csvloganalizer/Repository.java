package com.oceanos.csvloganalizer;

import com.oceanos.csvloganalizer.csv.CsvDataSet;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * @autor slonikmak on 02.08.2019.
 */
public interface Repository {
    ObservableList<CsvDataSet> getDataSet();
    public void addDataSets(List<CsvDataSet> dataSets);
}
