package sample.Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ResurchifyTableFx {
    SimpleDoubleProperty impactFactor;
    SimpleIntegerProperty citations;
    SimpleIntegerProperty year;

    public ResurchifyTableFx(Double impactFactor, Integer citations, Integer year) {


        this.impactFactor = new SimpleDoubleProperty(impactFactor);
        this.citations = new SimpleIntegerProperty(citations);
        this.year = new SimpleIntegerProperty(year);
    }

    public double getImpactFactor() {
        return impactFactor.get();
    }

    public SimpleDoubleProperty impactFactorProperty() {
        return impactFactor;
    }

    public void setImpactFactor(double impactFactor) {
        this.impactFactor.set(impactFactor);
    }

    public int getCitations() {
        return citations.get();
    }

    public SimpleIntegerProperty citationsProperty() {
        return citations;
    }

    public void setCitations(int citations) {
        this.citations.set(citations);
    }

    public int getYear() {
        return year.get();
    }

    public SimpleIntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    @Override
    public String toString() {
        return year+ "," + impactFactor + "," + citations;

    }
}
