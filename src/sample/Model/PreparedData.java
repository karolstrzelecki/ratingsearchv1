package sample.Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;
import sample.Controllers.DialogController;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PreparedData {
    SimpleStringProperty title;
    SimpleStringProperty issn;
    SimpleStringProperty hindexsci;
    List<ResurchifyTable>resurchifyTable;
    SimpleDoubleProperty impactFactor;
    SimpleIntegerProperty citations;
    Button dialog;


    public PreparedData(String title, String  issn, String hindexsci) {
        this.title = new SimpleStringProperty(title);
        this.issn = new SimpleStringProperty(issn);
        this.hindexsci = new SimpleStringProperty(hindexsci);

    }

    public PreparedData(String title, String  issn, String hindexsci, Button dialog) {
        this.title = new SimpleStringProperty(title);
        this.issn = new SimpleStringProperty(issn);
        this.hindexsci = new SimpleStringProperty(hindexsci);
        this.dialog = dialog;
        dialog.setOnAction(e->{

            String sceneFile = "/sample/GUI/tabledialog.fxml";
            URL url  = getClass().getResource( sceneFile );
            FXMLLoader fxmlLoader = new FXMLLoader( url);

            try {
                Parent parent = fxmlLoader.load();

                Scene scene = new Scene(parent, 350, 300);
                Stage stage = new Stage();
                JMetro jMetro = new JMetro(Style.DARK);
                jMetro.setScene(scene);
                parent.getStyleClass().add(JMetroStyleClass.BACKGROUND);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.show();
                DialogController dialogController = fxmlLoader.<DialogController>getController();
                dialogController.setResurchifyList(this.resurchifyTable);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }




        });

    }

    public PreparedData(SimpleStringProperty title, SimpleStringProperty issn, SimpleStringProperty hindexsci, SimpleDoubleProperty impactFactor, SimpleIntegerProperty citations) {
        this.title = title;
        this.issn = issn;
        this.hindexsci = hindexsci;
        this.impactFactor = impactFactor;
        this.citations = citations;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getIssn() {
        return issn.get();
    }

    public SimpleStringProperty issnProperty() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn.set(issn);
    }

    public String getHindexsci() {
        return hindexsci.get();
    }

    public SimpleStringProperty hindexsciProperty() {
        return hindexsci;
    }

    public void setHindexsci(String hindexsci) {
        this.hindexsci.set(hindexsci);
    }

    public List<ResurchifyTable> getResurchifyTable() {
        return resurchifyTable;
    }

    public void setResurchifyTable(List<ResurchifyTable> resurchifyTable) {
        this.resurchifyTable = resurchifyTable;
        this.impactFactor = new SimpleDoubleProperty(resurchifyTable.get(0).getImpactFactor());
        this.citations = new SimpleIntegerProperty(resurchifyTable.get(0).getCitations());
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

    public Button getDialog() {
        return dialog;
    }

    public void setDialog(Button dialog) {
        this.dialog = dialog;
    }

    @Override
    public String toString() {
        return issn.getValue() +";" + hindexsci.getValue() +";"  + impactFactor.getValue() +";" + citations.getValue();
    }
}
