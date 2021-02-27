package sample.Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

import java.util.List;

public class CsvPreparedData {

    private String issn;
    private Integer hindexsci;
    private List<ResurchifyTable> resurchifyTable;
    private Double impactFactor;
    private Integer citations;

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public Integer getHindexsci() {
        return hindexsci;
    }

    public void setHindexsci(Integer hindexsci) {
        this.hindexsci = hindexsci;
    }

    public List<ResurchifyTable> getResurchifyTable() {
        return resurchifyTable;
    }

    public void setResurchifyTable(List<ResurchifyTable> resurchifyTable) {
        this.resurchifyTable = resurchifyTable;
    }

    public Double getImpactFactor() {
        return impactFactor;
    }

    public void setImpactFactor(Double impactFactor) {
        this.impactFactor = impactFactor;
    }

    public Integer getCitations() {
        return citations;
    }

    public void setCitations(Integer citations) {
        this.citations = citations;
    }

    @Override
    public String toString() {
        return "CsvPreparedData{" +
                "issn='" + issn + '\'' +
                ", hindexsci=" + hindexsci +
                ", resurchifyTable=" + resurchifyTable +
                ", impactFactor=" + impactFactor +
                ", citations=" + citations +
                '}';
    }
}
