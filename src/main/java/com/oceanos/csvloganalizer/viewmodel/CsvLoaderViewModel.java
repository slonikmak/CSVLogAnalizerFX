package com.oceanos.csvloganalizer.viewmodel;

import com.google.inject.Inject;
import com.oceanos.csvloganalizer.CellData;
import com.oceanos.csvloganalizer.Repository;
import com.oceanos.csvloganalizer.csv.CsvDataSet;
import com.oceanos.csvloganalizer.csv.CsvReader;
import de.saxsys.mvvmfx.ViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor slonikmak on 02.08.2019.
 */
public class CsvLoaderViewModel implements ViewModel {

    @Inject
    Repository repository;

    private CsvReader csvReader = new CsvReader();

    private ObservableList<CellData> cellDataObservableList = FXCollections.observableArrayList();

    private ObservableList<String> delimiters = FXCollections.observableArrayList(Arrays.asList(";",",","."));

    public CsvReader getCsvReader() {
        return csvReader;
    }

    public ObservableList<CellData> getCellDataObservableList() {
        return cellDataObservableList;
    }

    public ObservableList<String> getDelimiters() {
        return delimiters;
    }

    public List<CsvDataSet> getEnabledColumns(){
        List<CellData> enabled = cellDataObservableList.stream().filter(CellData::isEnable).collect(Collectors.toCollection(ArrayList::new));

        CellProcessor[] processors = cellDataObservableList.stream().map(cellData->{
            if (cellData.getColumnType().equals(CsvReader.ColumnType.STRING)){
                return new NotNull();
            } else return new ParseDouble();
        }).toArray(CellProcessor[]::new);

        csvReader.setProcessors(processors);

        List<CsvDataSet> csvDataSets = new ArrayList<>();
        try {
            List<List<Object>> records = csvReader.getRecords();
            enabled.forEach(cellData -> {
                CsvDataSet csvDataSet = new CsvDataSet();
                csvDataSet.setName(cellData.getName());
                csvDataSet.setType(cellData.getColumnType().getClass());
                csvDataSet.setValues(csvReader.getColumn(records, cellData.getPosition(), cellData.getColumnType().getType()));
                csvDataSets.add(csvDataSet);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvDataSets;
    }

    public void fillCells(List<String> header) {
        for (int i = 0; i < header.size(); i++) {
            cellDataObservableList.add(new CellData(header.get(i), CsvReader.ColumnType.STRING, true, i));
        }
    }

    public void addDataSets() {
        repository.addDataSets(getEnabledColumns());
    }
}
