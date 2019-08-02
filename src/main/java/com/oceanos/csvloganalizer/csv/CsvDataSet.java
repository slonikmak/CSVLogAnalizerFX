package com.oceanos.csvloganalizer.csv;

import java.util.List;

/**
 * @autor slonikmak on 02.08.2019.
 */
public class CsvDataSet<T> {
    private Class<T> type;
    private String name;
    private List<T> values;

    public CsvDataSet(){

    }

    public CsvDataSet(Class<T> type, String name, List<T> values) {
        this.type = type;
        this.name = name;
        this.values = values;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }
}
