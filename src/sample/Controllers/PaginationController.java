package sample.Controllers;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import sample.GUI.PaginatedTableView;
import sample.Main;
import sample.Model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.toMap;
import static java.lang.Math.min;




public class PaginationController {

    private String fileName;

//    private String fileName = "C:\\Users\\k-str\\OneDrive\\Pulpit\\data.csv";
    Map<Integer, List<Periodic>>paginatedData;
    private MainController mainController;


    @FXML
    Pagination paginator;

    public void injectMainController(MainController mainController){

        CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
        File jarFile = null;
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();
            fileName=jarDir+"\\data.csv";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }



        this.mainController = mainController;

        paginatedData= new HashMap<>();


        paginator.pageCountProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> initPagination());

//        paginator.currentPageIndexProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> initPagination());


    }



    public void initializeSearchDataset(FilterData filterData){
        List<String[]>raw = initializeDataSet();
        List<Periodic>periodics = convertDataSet(raw);
        List<Periodic>dataSet;
        if(filterData.getSearchOption() == 1){
            dataSet = filterInner(periodics,filterData);
        }else{
            dataSet= filterOuter(periodics,filterData);
        }
        paginate(dataSet);


    }




    public void printFilterData(){

        initializeSearchDataset(mainController.getFilterData());



    }


    private List<String[]> initializeDataSet (){
        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
        AtomicInteger counter = new AtomicInteger(0);// custom separator
        List<Periodic> periodics = new ArrayList<>();

        try(CSVReader reader = new CSVReaderBuilder(
                new FileReader(fileName))
                .withCSVParser(csvParser)   // custom CSV parser
                .withSkipLines(1)           // skip the first line, header info
                .build()) {
            List<String[]> r = reader.readAll();
            return r;
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Nie odnaleziono bazowego pliku CSV");
            alert.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Problem IO");
            alert.show();
        }
        return null;
    }

    List<Periodic>convertDataSet(List<String[]> rawData){
        List<Category> enumList = Arrays.asList((Category.class.getEnumConstants()));

        List<Periodic>periodics = rawData.stream().map(x -> {

            Periodic periodic = new Periodic();
            periodic.setTitle(x[1]);
            periodic.setIssn(x[2]);
            periodic.setEIssn(x[3]);
            periodic.setAlternativeTitle(x[4]);
            periodic.setAlternativeIssn(x[5]);
            periodic.setAlternativeEIssn(x[6]);
            periodic.setCredit(Long.valueOf(x[7]));
            Set<Category> categories = new HashSet<>();
            for(int i=0; i< x.length; i++) {
                int tmp = i + 8;
                if (tmp < 52) {
                    if (!x[tmp].isEmpty()) {
                        categories.add(enumList.get(i));
                    }

                }
            }
            periodic.setCategories(categories);

            return periodic;
        }).collect(Collectors.toList());

        return periodics;
    }


    List<Periodic> filterInner(List<Periodic> data, FilterData filterData){
        List<Periodic>output = data.stream().filter(x-> x.getCredit()> filterData.getMinPointsVal() && x.getCredit()<filterData.getMaxPointsVal())
                .filter(x->x.getCategories().containsAll(filterData.getChosenCats())).collect(Collectors.toList());

        return output;
    }


    List<Periodic>filterOuter(List<Periodic>data, FilterData filterData){
        List<Periodic>output = data.stream().filter(x-> x.getCredit()> filterData.getMinPointsVal() && x.getCredit()<filterData.getMaxPointsVal())
            .filter(x-> x.getCategories().stream().anyMatch(filterData.getChosenCats()::contains)).collect(Collectors.toList());

        return output;
    }


  private void paginate(List<Periodic> list){
        Map<Integer, List<Periodic>>result = IntStream.iterate(0, i -> i + 10)
                .limit((list.size() + 10 - 1) / 10)
                .boxed()
                .collect(toMap(i -> i / 10,
                        i -> list.subList(i, min(i + 10, list.size()))));

        paginatedData = result;



      initPagination();
  }






    private Node createPage(int pageIndex){


        HBox hBox = new HBox();
        PaginatedTableView paginatedTableView = new PaginatedTableView();
        paginatedTableView.setDataSet(paginatedData.get(pageIndex-1));
        hBox.getChildren().add(paginatedTableView);


        return hBox;

    }


    private void initPagination(){
        paginator.setPageCount((paginatedData.size()/10+1));
        System.out.println((paginatedData.size()/10+1));




        AtomicInteger counter = new AtomicInteger(0);

        paginator.setPageFactory((pageIndex)->{
            System.out.println();
            HBox hBox = new HBox();
            PaginatedTableView paginatedTableView = new PaginatedTableView();
            paginatedTableView.setDataSet(paginatedData.get(counter.getAndIncrement()));
            hBox.getChildren().add(paginatedTableView);


            return hBox;
        });


    }










}
