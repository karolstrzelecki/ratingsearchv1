package sample.Controllers;

import com.jfoenix.controls.JFXListView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


import java.net.URL;
import java.util.*;

import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import org.controlsfx.control.RangeSlider;
import sample.Model.Category;
import sample.Model.FilterData;

public class MainController implements Initializable {

    @FXML
    private ListView categoryList;


    @FXML
    private BorderPane resultTable;

    @FXML
    private ChoiceBox choiceBox;

    @FXML
    private Slider min;

    @FXML
    private Slider max;

    @FXML
    private Label minLabel;

    @FXML
    private Label maxLabel;

    @FXML
    private Button searchButton;

    @FXML
    private Label error;

    @FXML
    PaginationController paginationController;


    private Integer minPointsVal;
    private Integer maxPointsVal;
    private Set<Category> chosenCats;
    private int searchOption;


//



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paginationController.injectMainController(this);


//        Center



//        Side Menu

//       Category List
        chosenCats = new HashSet<>();
        List<Category> categories = Arrays.asList(Category.values());
        categories.stream().forEach(e -> {
            categoryList.getItems().add(e.name);
        });


        categoryList.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) ->{
//                            System.out.println("Check box for " + item + " changed from " + wasSelected + " to " + isNowSelected);
                            Category cat = Category.get(item);
                            if(isNowSelected == true){
                                chosenCats.add(cat);
                            }else {
                                chosenCats.remove(cat);
                            }
                        }


                );
                return observable;
            }
        }));

//        Choice Box

        choiceBox.getItems().add("Poszerz");
        choiceBox.getItems().add("Zawęź");
        choiceBox.setValue("Zawęź");
        searchOption = 1;


        choiceBox.setOnAction((event)-> {
            int selectedIndex = choiceBox.getSelectionModel().getSelectedIndex();
            Object selectedItem = choiceBox.getSelectionModel().getSelectedItem();

            searchOption=selectedIndex;


        });

//        Sliders

        min.setMin(0);
        min.setMax(200);
        min.setValue(0);
        minPointsVal = 0;
        minLabel.setText(String.valueOf(minPointsVal));


        min.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                minPointsVal = new_val.intValue();
                minLabel.setText(String.valueOf(minPointsVal));
                if(minPointsVal>maxPointsVal){
                    error.setText("Błędny zakres");
                }

            }
        });



        max.setMin(0);
        max.setMax(200);
        max.setValue(200);
        maxPointsVal = 200;
        maxLabel.setText(String.valueOf(maxPointsVal));


        max.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                maxPointsVal = newValue.intValue();
                maxLabel.setText(String.valueOf(maxPointsVal));
                if(minPointsVal>maxPointsVal){
                    error.setText("Błędny zakres");
                }

            }
        });

//        FilterButton

searchButton.setOnAction((event -> {
    if(minPointsVal>maxPointsVal){
        error.setText("Błędny zakres");
//        Alert window
    }else{

   paginationController.printFilterData();


    }

}));



    }



    public FilterData getFilterData(){
        FilterData filterData = new FilterData();
        filterData.setChosenCats(chosenCats);
        filterData.setMaxPointsVal(maxPointsVal);
        filterData.setMinPointsVal(minPointsVal);
        filterData.setSearchOption(searchOption);
        return filterData;
    }




}
