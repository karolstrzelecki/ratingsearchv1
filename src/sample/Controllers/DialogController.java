package sample.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Model.PreparedData;
import sample.Model.ResurchifyTable;
import sample.Model.ResurchifyTableFx;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DialogController implements Initializable {

    @FXML
    private TableView tableView2;
    @FXML
    private TableColumn<ResurchifyTableFx, SimpleDoubleProperty> impactFactor;
    @FXML
    private TableColumn<ResurchifyTableFx, SimpleIntegerProperty> citations;
    @FXML
    private TableColumn<ResurchifyTableFx, SimpleIntegerProperty> year;

    ObservableList<ResurchifyTableFx> data = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        year.setCellValueFactory(new PropertyValueFactory<ResurchifyTableFx, SimpleIntegerProperty>("year"));
        impactFactor.setCellValueFactory(new PropertyValueFactory<ResurchifyTableFx, SimpleDoubleProperty>("impactFactor"));
        citations.setCellValueFactory(new PropertyValueFactory<ResurchifyTableFx, SimpleIntegerProperty>("citations"));
        tableView2.getItems().setAll(data);
    }



    public void setResurchifyList(List<ResurchifyTable> dataList){
        dataList.stream().forEach(x->{
            ResurchifyTableFx tmp = new ResurchifyTableFx(x.getImpactFactor(), x.getCitations(), x.getYear());
            data.add(tmp);
            tableView2.getItems().setAll(data);
        });
    }



    @FXML
    public void btnCloseStage(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
