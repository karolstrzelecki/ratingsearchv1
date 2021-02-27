package sample.Controllers;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sample.Model.Category;
import sample.Model.Periodic;
import sample.Model.PreparedData;
import sample.Model.ResurchifyTable;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class ResultTableController  implements Initializable {

    String fileName = "C:\\Users\\k-str\\OneDrive\\Pulpit\\data.csv";


    @FXML
    private TableView tableView;

    @FXML
    private TableColumn<PreparedData, SimpleStringProperty> title;

    @FXML
    private TableColumn<PreparedData, SimpleStringProperty> issn;

    @FXML
    private TableColumn<PreparedData, SimpleStringProperty> hindexsci;


    @FXML
    private TableColumn<PreparedData, SimpleDoubleProperty> impactFactor;
    @FXML
    private TableColumn<PreparedData, SimpleIntegerProperty> citations;

    @FXML TableColumn<PreparedData, Button> buttonDialog;





    @Override
    public void initialize(URL location, ResourceBundle resources) {

        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
        AtomicInteger counter = new AtomicInteger(0);// custom separator
        List<Category> enumList = Arrays.asList((Category.class.getEnumConstants()));
        List<Periodic> periodics = new ArrayList<>();
        title.setCellValueFactory(new PropertyValueFactory<PreparedData, SimpleStringProperty>("title"));
        issn.setCellValueFactory(new PropertyValueFactory<PreparedData, SimpleStringProperty>("issn"));
        hindexsci.setCellValueFactory(new PropertyValueFactory<PreparedData, SimpleStringProperty>("hindexsci"));
        impactFactor.setCellValueFactory(new PropertyValueFactory<PreparedData, SimpleDoubleProperty>("impactFactor"));
        citations.setCellValueFactory(new PropertyValueFactory<PreparedData, SimpleIntegerProperty>("citations"));
        buttonDialog.setCellValueFactory(new PropertyValueFactory<>("dialog"));

        try(CSVReader reader = new CSVReaderBuilder(
                new FileReader(fileName))
                .withCSVParser(csvParser)   // custom CSV parser
                .withSkipLines(1)           // skip the first line, header info
                .build()){
            List<String[]> r = reader.readAll();
            Set<Category>categories = new HashSet<>();
            categories.add(Category.archeo);
            categories.add(Category.medicalScience);


            periodics = filterByPointsAndCategories(r,categories,100 );


//            ObservableList<PreparedData> data = FXCollections.observableArrayList(periodics.stream().map(x->{
//                PreparedData preparedData = new PreparedData(x.getTitle(), x.getIssn());
//                return preparedData;
//            }).collect(Collectors.toList()));
//            tableView.getItems().setAll(data);

            ObservableList<PreparedData> data = FXCollections.observableArrayList();
            tableView.getItems().setAll(data);
            periodics.stream().forEach(x -> {
                PreparedData preparedData = new PreparedData(x.getTitle(), x.getIssn(), getFromScimagor(x.getIssn()), new Button("tabela"));

                List<ResurchifyTable>datas = getFromResurchify(x.getIssn());
                preparedData.setResurchifyTable(datas);

                tableView.getItems().add(preparedData);
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public List<Periodic> filterByPointsAndCategories(List<String[]> set, Set<Category> filteringCats, int points){
        List<Category> enumList = Arrays.asList((Category.class.getEnumConstants()));

        List<Periodic>periodics = set.stream().map(x -> {

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
        }).filter(x-> x.getCredit()> points)
                .filter(x->x.getCategories().containsAll(filteringCats))
                .collect(Collectors.toList());
        return periodics;
    }

    public List<ResurchifyTable> getFromResurchify(String issn) {


        String svProtocol1 = "https://www.resurchify.com/";
        String htmlRes1 = htmlgetter("https://www.resurchify.com/impact-factor-search.php?query=" + issn.replaceAll("[\\s\\-()]", ""));
        Document document1 = Jsoup.parse(htmlRes1);
        Elements sectionsRes = document1.select("a");
        String searchVarRes = sectionsRes.get(4).attr("href");
        svProtocol1 = new StringBuilder(svProtocol1).append(searchVarRes).toString();
        String htmlRes2 = htmlgetter(svProtocol1);
        Document documentRes2 = Jsoup.parse(htmlRes2);
        Element table = documentRes2.select("table").get(0);
        Elements rows = table.select("tr");
        List<ResurchifyTable> resurchifyTable = new ArrayList<>();

        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = rows.get(i).select("td");
            if (cols.size() == 3) {
                String str1 = cols.get(1).text();
                if (isNumeric(str1)) {
                    ResurchifyTable rt = new ResurchifyTable();
                    rt.setYear(Integer.parseInt(cols.get(0).text()));
                    rt.setCitations(Integer.parseInt(cols.get(2).text()));
                    rt.setImpactFactor(Double.parseDouble(cols.get(1).text()));
                    resurchifyTable.add(rt);


                }
            } else {
                System.out.println("Cuś się zjebało");
                System.out.println(svProtocol1);

                System.out.println(row.getAllElements().text());
            }
        }
        return resurchifyTable;
    }


    public String getFromScimagor(String issn){

        String svProtocol = "https://www.scimagojr.com/";
        String html = htmlgetter("https://www.scimagojr.com/journalsearch.php?q=" +issn.replaceAll("[\\s\\-()]", ""));
        Document doc = Jsoup.parse(html);
        Elements sections = doc.select("a");
        String searchVar2 = sections.get(8).attr("href");
        svProtocol = new StringBuilder(svProtocol).append(searchVar2).toString();
        String html2 = htmlgetter(svProtocol);
        Document document = Jsoup.parse(html2);
        Elements element1 = document.select("p.hindexnumber");
        return element1.text();
    }









    private static String htmlgetter(String a){
        URL url;
        StringBuilder sb =  new StringBuilder();
        try {

            url = new URL(a);
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            br.close();


        } catch (MalformedURLException e) {
//            Wpisz błędy i rzuć okienka
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String html = sb.toString();
        return html;

    }

    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public void printSth(){
        System.out.println("bla bla bla");
    }




}
