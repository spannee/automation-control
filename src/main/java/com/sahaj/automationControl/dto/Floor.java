package com.sahaj.automationControl.dto;

import java.util.List;
import java.util.Objects;

/**
 * Represents the characteristics of a building floor
 */
public class Floor {

    private int number;

    private List<Corridor> mainCorridor;

    private List<Corridor> subCorridor;

    public Floor(int number, List<Corridor> mainCorridor, List<Corridor> subCorridor) {
        this.number = number;
        this.mainCorridor = mainCorridor;
        this.subCorridor = subCorridor;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Corridor> getMainCorridor() {
        return mainCorridor;
    }

    public void setMainCorridor(List<Corridor> mainCorridor) {
        this.mainCorridor = mainCorridor;
    }

    public List<Corridor> getSubCorridor() {
        return subCorridor;
    }

    public void setSubCorridor(List<Corridor> subCorridor) {
        this.subCorridor = subCorridor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Floor)) return false;
        Floor floor = (Floor) o;
        return getNumber() == floor.getNumber() &&
                Objects.equals(getMainCorridor(), floor.getMainCorridor()) &&
                Objects.equals(getSubCorridor(), floor.getSubCorridor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber(), getMainCorridor(), getSubCorridor());
    }

    @Override
    public String toString() {
        return "Floor " + number + "\n" +
                mainCorridor.toString().substring(1, mainCorridor.toString().length()-1) + "\n" +
                subCorridor.toString().substring(1, subCorridor.toString().length()-1)
                + "\n" + "\n";
    }

}
