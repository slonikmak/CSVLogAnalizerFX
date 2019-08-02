package com.oceanos.csvloganalizer.csv;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor slonikmak on 31.07.2019.
 */
public class CsvReader {
    private ICsvListReader listReader;
    private CellProcessor[] processors;

    public void read(String path) throws FileNotFoundException {
       listReader =  new CsvListReader(new FileReader(path), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
    }

    public List<String> getHeader() throws IOException {
        return Arrays.asList(listReader.getHeader(true));
    }

    public void setProcessors(CellProcessor... processors){
        this.processors = processors;
    }

    public List<List<Object>> getRecords() throws IOException {
        List<List<Object>> result = new ArrayList<>();
        List<Object> record;
        final CellProcessor[] processors = getProcessors();
        while( (record = listReader.read(processors)) != null ) {
                result.add(record);
        }
        return result;
    }

    public <T> List<T> getColumn(List<List<Object>> records, int column, Class<T> type){
        return records.stream().map(r->{
            return type.cast(r.get(column));
        }).collect(Collectors.toList());
    }

    private CellProcessor[] getProcessors(){
        return processors;
    }

    public void close() throws IOException {
        if (listReader != null){
            listReader.close();
        }
    }

    public static enum ColumnType{
        STRING(String.class), DOUBLE(Double.class);

        Class type;
        ColumnType(Class type){
            this.type = type;
        }
        public Class getType(){
            return type;
        }
    }

}
