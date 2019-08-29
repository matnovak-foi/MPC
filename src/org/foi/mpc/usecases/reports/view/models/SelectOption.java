package org.foi.mpc.usecases.reports.view.models;

import java.util.Objects;

public class SelectOption {
    private String name;
    private String value;

    public SelectOption(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SelectOption)) return false;
        SelectOption that = (SelectOption) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "SelectOption{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
