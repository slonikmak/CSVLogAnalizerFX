package com.oceanos.csvloganalizer;

import com.oceanos.csvloganalizer.csv.CsvReader;
import javafx.beans.property.*;

/**
 * @autor slonikmak on 01.08.2019.
 */
public class CellData {
    int position;
    StringProperty name = new SimpleStringProperty();
    ObjectProperty<CsvReader.ColumnType> type = new SimpleObjectProperty<>();
    BooleanProperty enable = new SimpleBooleanProperty();

    public CellData() {
    }

    public CellData(String name, CsvReader.ColumnType type, boolean enable, int position) {
        this.name.setValue(name);
        this.type.setValue(type);
        this.enable.setValue(enable);

        this.position = position;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public CsvReader.ColumnType getColumnType() {
        return type.get();
    }

    public ObjectProperty<CsvReader.ColumnType> typeProperty() {
        return type;
    }

    public void setType(CsvReader.ColumnType type) {
        this.type.set(type);
    }

    public boolean isEnable() {
        return enable.get();
    }

    public BooleanProperty enableProperty() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable.set(enable);
    }

    public int getPosition() {
        return position;
    }
}
