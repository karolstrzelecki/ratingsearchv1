package sample.GUI;

import com.opencsv.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sample.Main;
import sample.Model.CsvPreparedData;
import sample.Model.Periodic;
import sample.Model.PreparedData;
import sample.Model.ResurchifyTable;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaginatedTableView extends VBox  {

    @FXML
    TableView tableView;

    @FXML
    TableColumn<PreparedData, SimpleStringProperty> title;

    @FXML
    TableColumn<PreparedData, SimpleStringProperty> issn;

    @FXML
    TableColumn<PreparedData, SimpleStringProperty> hindexsci;


    @FXML
    TableColumn<PreparedData, SimpleDoubleProperty> impactFactor;
    @FXML
    TableColumn<PreparedData, SimpleIntegerProperty> citations;

    @FXML TableColumn<PreparedData, Button> buttonDialog;


    private String cache;
//    private String cache = "cache.csv";
    private List<CsvPreparedData>cacheList = new ArrayList<>();


    public PaginatedTableView(){


        CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
        File jarFile = null;
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();
            cache=jarDir+"\\cache.csv";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "paginated_table_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        tableView.itemsProperty().addListener( (observable, oldValue, newValue) -> initData());



    }



    public void setDataSet(List<Periodic>dataSet){
        initData();

        if(dataSet!=null){
            dataSet.stream().forEach(x -> {
                Optional<CsvPreparedData> csvPreparedData = cacheList.stream().filter(o -> o.getIssn().equals(x.getIssn())).findFirst();

                PreparedData preparedData;
                if(csvPreparedData.isPresent()){
                    CsvPreparedData tmp = csvPreparedData.get();
                    preparedData = new PreparedData(x.getTitle(), x.getIssn(), tmp.getHindexsci().toString(), new Button("tabela"));
                    preparedData.setResurchifyTable(tmp.getResurchifyTable());
                }else{



                    List<ResurchifyTable>datas = getFromResurchify(x.getIssn());
                    String hIndex = getFromScimagor(x.getIssn());
                    if(datas.isEmpty() || hIndex.isEmpty() ){

                        preparedData = new PreparedData(x.getTitle(), x.getIssn(),"brak", new Button("tabela"));
                        List<ResurchifyTable> rt = new ArrayList<>();
                        rt.add(new ResurchifyTable(0,0.0,0));
                        preparedData.setResurchifyTable(rt);
                    }else{
                        preparedData = new PreparedData(x.getTitle(), x.getIssn(), hIndex, new Button("tabela"));
                        preparedData.setResurchifyTable(datas);
                        saveCacheCSV(convertObjectToCSV(preparedData));
                    }
                }
                tableView.getItems().add(preparedData);


            });
        }
    }

    public void initData(){

        title.setCellValueFactory(new PropertyValueFactory<PreparedData, SimpleStringProperty>("title"));
        issn.setCellValueFactory(new PropertyValueFactory<PreparedData, SimpleStringProperty>("issn"));
        hindexsci.setCellValueFactory(new PropertyValueFactory<PreparedData, SimpleStringProperty>("hindexsci"));
        impactFactor.setCellValueFactory(new PropertyValueFactory<PreparedData, SimpleDoubleProperty>("impactFactor"));
        citations.setCellValueFactory(new PropertyValueFactory<PreparedData, SimpleIntegerProperty>("citations"));
        buttonDialog.setCellValueFactory(new PropertyValueFactory<>("dialog"));
        ObservableList<PreparedData> data = FXCollections.observableArrayList();
        tableView.getItems().setAll(data);
        initCache();
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
                System.out.println("Problem z połączeniem z Resurchify");
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




    private String resurchifyTableParser(List<ResurchifyTable> table){

      String joined = table.stream().map(Objects::toString).collect(Collectors.joining("/"));


        return joined;
    }









    public String convertObjectToCSV(PreparedData preparedData){
        String csv= preparedData.toString();
        String tmp = resurchifyTableParser(preparedData.getResurchifyTable());
        csv = csv.concat(";"+tmp);


        return csv;

    }


    public void saveCacheCSV(String csvLine){

        try {
            FileWriter outputfile = new FileWriter("cache.csv",true);
            outputfile.append(csvLine);
            outputfile.append("\n");
            outputfile.flush();
            outputfile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void initCache(){

        List<CsvPreparedData>csvPreparedDataList = new ArrayList<>();
        CSVParser csvParserPD = new CSVParserBuilder().withSeparator(';').build();

        try(CSVReader reader = new CSVReaderBuilder(
                new FileReader(cache))
                .withCSVParser(csvParserPD)   // custom CSV parser
                .withSkipLines(1)           // skip the first line, header info
                .build()){
            String[] line;
            while((line=reader.readNext()) != null){
                CsvPreparedData data = new CsvPreparedData();
                data.setIssn(line[0]);
                data.setHindexsci(Integer.parseInt(line[1]));
                data.setImpactFactor(Double.parseDouble(line[2]));
                data.setCitations(Integer.parseInt(line[3]));
              List<ResurchifyTable>resurchifyTableList = createResurchifyList( readRTLines(line[4]));
              data.setResurchifyTable(resurchifyTableList);
              csvPreparedDataList.add(data);

            }

            cacheList=csvPreparedDataList;




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
        e.printStackTrace();
    }


    }


    private List<String>readRTLines(String csv){
        CSVParser csvParserResurchifyData = new CSVParserBuilder().withSeparator('/').build();

        List<String>tmp = new ArrayList<>();



        try(CSVReader reader = new CSVReaderBuilder(
                new  StringReader(csv))
                .withCSVParser(csvParserResurchifyData)   // custom CSV parser
                .withSkipLines(0)           // skip the first line, header info
                .build()){
            String[] line;
            while((line=reader.readNext()) != null){
              tmp.add(line[0]);
            }




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    return tmp;
    }

    List<ResurchifyTable>createResurchifyList(List<String> rows){
        List<ResurchifyTable>resurchifyTableList = new ArrayList<>();

        rows.stream().forEach( x->{


            CSVParser csvParserList = new CSVParserBuilder().withSeparator(',').build();

            try(CSVReader reader = new CSVReaderBuilder(
                    new  StringReader(x))
                    .withCSVParser(csvParserList)   // custom CSV parser
                    .withSkipLines(0)           // skip the first line, header info
                    .build()){
                String[] line;
                while((line=reader.readNext()) != null){
                    ResurchifyTable rt = new ResurchifyTable();
                    rt.setYear(Integer.parseInt(line[0]));
                    rt.setImpactFactor(Double.parseDouble(line[1]));
                    rt.setCitations(Integer.parseInt(line[2]));
                    resurchifyTableList.add(rt);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        });

        return resurchifyTableList;
    }

}
