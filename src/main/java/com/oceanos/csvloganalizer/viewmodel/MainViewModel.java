package com.oceanos.csvloganalizer.viewmodel;

import com.google.inject.Inject;
import com.oceanos.csvloganalizer.Repository;
import com.oceanos.csvloganalizer.csv.CsvDataSet;
import de.saxsys.mvvmfx.ViewModel;
import javafx.collections.ObservableList;

/**
 * @autor slonikmak on 02.08.2019.
 */
public class MainViewModel implements ViewModel {
    @Inject
    Repository repository;

    public ObservableList<CsvDataSet> getDataSets(){
        return repository.getDataSet();
    }

}
