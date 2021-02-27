package sample.Model;

import java.util.Set;

public class FilterData {

    private Integer minPointsVal;
    private Integer maxPointsVal;
    private Set<Category> chosenCats;
    private int searchOption;


    public FilterData() {
    }

    public Integer getMinPointsVal() {
        return minPointsVal;
    }

    public void setMinPointsVal(Integer minPointsVal) {
        this.minPointsVal = minPointsVal;
    }

    public Integer getMaxPointsVal() {
        return maxPointsVal;
    }

    public void setMaxPointsVal(Integer maxPointsVal) {
        this.maxPointsVal = maxPointsVal;
    }

    public Set<Category> getChosenCats() {
        return chosenCats;
    }

    public void setChosenCats(Set<Category> chosenCats) {
        this.chosenCats = chosenCats;
    }

    public int getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(int searchOption) {
        this.searchOption = searchOption;
    }

    @Override
    public String toString() {
        return "FilterData{" +
                "minPointsVal=" + minPointsVal +
                ", maxPointsVal=" + maxPointsVal +
                ", chosenCats=" + chosenCats +
                ", searchOption=" + searchOption +
                '}';
    }
}
