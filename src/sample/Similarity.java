package sample;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Similarity extends Application{
    public static ReadXls r;
    private static double[] e;                 //欧几里得算法数据
    private static double[] c;                //余弦相似度数据
    private static double[] p;               //皮尔逊相关系数数据
    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    AreaChart<Number, Number> areaChart = new AreaChart<>(xAxis, yAxis);    //图表
    private TableView<dataString> tableView = new TableView<>();           //数据表
    private final ObservableList<dataString> data = FXCollections.observableArrayList(); //数据表数据
    private int key = 0;

    public static void main(String[] args) throws Exception {
        r = new ReadXls();                //读取文件
        r.Printf();

        e = new double[r.columns];
        c = new double[r.columns];
        p = new double[r.columns];

        for(int i = 1; i < r.columns; i++){                         //用三种方法处理数据
            EuclideanMetric E = new EuclideanMetric();
            e[i] = E.method(r.output[0], r.output[i]);

            CosineSimilarity C = new CosineSimilarity();
            c[i] = CosineSimilarity.method(r.output[0], r.output[i]);

            PearsonCorrelationScore P = new PearsonCorrelationScore();
            p[i] = PearsonCorrelationScore.method(r.output[0], r.output[i]);

        }

        for(int i = 1; i < r.columns; i++){
            System.out.printf("%d %.7f  %.7f  %.7f\n", i+1, e[i], c[i], p[i]);
        }

        launch(args);

    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Similarity");
        GridPane gridPane = new GridPane();                 //设置GridPane
        gridPane.setAlignment(Pos.CENTER);

        HBox upperHBox = new HBox();
        ComboBox<String> cbo1 = new ComboBox<>();
        ComboBox<String> cbo2 = new ComboBox<>();

        for (int i = 1; i <= 20; i++){                    //下拉单添加数据
            cbo1.getItems().add("GL-" + i);
        }
        cbo1.setValue("Choose GL");
        for (int i = 1; i <= 20; i++){                  //下拉单添加数据
            cbo2.getItems().add("GL-" + i);
        }
        cbo2.setValue("Choose GL");
        upperHBox.getChildren().addAll(cbo1, cbo2);

        TableColumn data1Col = new TableColumn("Data1");                   //数据表设置
//        data1Col.setId("lol");
        data1Col.setMinWidth(130);
        data1Col.setCellValueFactory(new PropertyValueFactory<>("data1"));
        data1Col.setId("data11");
        TableColumn data2Col = new TableColumn("Data2");
        data2Col.setMinWidth(130);
        data2Col.setId("data22");
        data2Col.setCellValueFactory(new PropertyValueFactory<>("data2"));
        tableView.setPrefSize(0, 800);
        tableView.setItems(data);
        tableView.getColumns().addAll(data1Col, data2Col);

        VBox leftVBox = new VBox();                                                       //左边垂直Box设置
        leftVBox.setPadding(new Insets(10, 0, 0, 10));
        leftVBox.getChildren().addAll(upperHBox, tableView);
        gridPane.add(leftVBox, 0, 0);

        areaChart.setTitle("AreaChart");                                            //图表设置
        areaChart.setPrefSize(400, 600);
        areaChart.setId("qaq");

        cbo1.setOnAction(e->{
            if(key == 1){
                areaChart.getData().remove(0);
            }

            XYChart.Series series1 = new XYChart.Series();        //图的数据
            int k = cbo1.getValue().length() == 4 ? Integer.parseInt(cbo1.getValue().charAt(3) + "" ): Integer.parseInt(cbo1.getValue().charAt(3) + "" + cbo2.getValue().charAt(4));
            series1.setName("GL-" + k);

            for(int i = 0; i < r.rows; i++){                    //添加图的数据
                series1.getData().add(new XYChart.Data(i+1, this.r.output[k-1][i]));
            }

            areaChart.getData().add(series1);
        });

        cbo2.setOnAction(e ->{
            if(areaChart.getData().size() == 2){               //确保图的数据只有两个
                areaChart.getData().remove(1);
            }

            XYChart.Series series2 = new XYChart.Series();
            int k = cbo2.getValue().length() == 4 ? Integer.parseInt(cbo2.getValue().charAt(3) + "" ): Integer.parseInt(cbo2.getValue().charAt(3) + "" + cbo2.getValue().charAt(4));
            series2.setName("GL-" + k);

            for(int i = 0; i < r.rows; i++){                 //添加图的数据
                series2.getData().add(new XYChart.Data(i+1, this.r.output[k-1][i]));
            }

            areaChart.getData().add(series2);

            if(data.size() != 0){
//                data.removeAll();     //？
                data.remove(0,35);
            }
            for(int i = 0; i < r.rows; i++){                    //更新表的数据
                data.add(new dataString(""+this.r.output[0][i], ""+this.r.output[k-1][i]));
            }

        });

        HBox buttomRightHBox = new HBox();                                                        //创建右下角的图片Box
        Button imageButton1 = new Button("", new ImageView("img/euclidean.jpg"));
        imageButton1.setPrefSize(200, 200);
//        imageButton1.setOnMouseEntered(event -> {});
        imageButton1.setOnMouseClicked(e->{
            if(areaChart.getData().size() == 1) areaChart.getData().remove(0, 1);         //删除数据
            else areaChart.getData().remove(0, 2);
//            areaChart.getData().removeAll();
            XYChart.Series seriesE = new XYChart.Series();
            seriesE.setName("EuclideanMetric");
            for(int i = 1; i < 20; i++){                                               //添加新的图的数据
                seriesE.getData().add(new XYChart.Data(i+1, Similarity.e[i]));
            }
            areaChart.getData().add(seriesE);
            key = 1;
        });
        Button imageButton2 = new Button("", new ImageView("img/cosine.jpg"));
        imageButton2.setPrefSize(200, 200);
        imageButton2.setOnMouseClicked(e->{
            if(areaChart.getData().size() == 1) areaChart.getData().remove(0, 1);     //删除数据
            else areaChart.getData().remove(0, 2);
//            areaChart.getData().removeAll();
            XYChart.Series seriesC = new XYChart.Series();
            seriesC.setName("CosineSimilarity");
            for(int i = 1; i < 20; i++){                                               //添加新的图的数据
                seriesC.getData().add(new XYChart.Data(i+1, Similarity.c[i]));
            }
            areaChart.getData().add(seriesC);
            key = 1;

        });
        Button imageButton3 = new Button("", new ImageView("img/pearson.jpg"));
        imageButton3.setPrefSize(200, 200);
        imageButton3.setOnMouseClicked(e-> {
            if(areaChart.getData().size() == 1) areaChart.getData().remove(0, 1);              //删除数据
            else areaChart.getData().remove(0, 2);
//            areaChart.getData().removeAll();
                    XYChart.Series seriesP = new XYChart.Series();
                    seriesP.setName("PearsonCorrelationScore");
                    for (int i = 1; i < 20; i++) {                                               //添加新的图的数据
                        seriesP.getData().add(new XYChart.Data(i + 1, Similarity.p[i]));
                    }
                    areaChart.getData().add(seriesP);
                    key = 1;
        });
        buttomRightHBox.getChildren().addAll(imageButton1, imageButton2, imageButton3);

        VBox rightVBox = new VBox();                                 //设置面板右边的Box
        rightVBox.getChildren().addAll(areaChart, buttomRightHBox);
        gridPane.add(rightVBox, 1, 0);


        Scene scene  = new Scene(gridPane,1200,800);
        scene.getStylesheets().add("CSS/Translate.css");
        stage.setScene(scene);
        stage.show();

    }

    public static class dataString{

        private final SimpleStringProperty data1;                //数据表的数据1
        private final SimpleStringProperty data2;               //数据表的数据2

        private dataString(String x, String y){
            this.data1 = new SimpleStringProperty(x);
            this.data2 = new SimpleStringProperty(y);
        }

        public String getData1() {
            return data1.get();
        }

        public SimpleStringProperty data1Property() {
            return data1;
        }

        public void setData1(String data1) {
            this.data1.set(data1);
        }

        public String getData2() {
            return data2.get();
        }

        public SimpleStringProperty data2Property() {
            return data2;
        }

        public void setData2(String data2) {
            this.data2.set(data2);
        }
    }
}
