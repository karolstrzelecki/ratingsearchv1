package sample.Model;

public class ResurchifyTable {
    Integer year;
    Double impactFactor;
    Integer citations;


    public ResurchifyTable() {
    }

    public ResurchifyTable(Integer year, Double impactFactor, Integer citations) {
        this.year = year;
        this.impactFactor = impactFactor;
        this.citations = citations;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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
        return  year +","+ impactFactor +","+citations;

    }
}
