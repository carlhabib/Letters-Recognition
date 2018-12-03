package perceptron;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {


    static ArrayList<Layer> layersArrayList;
    static Perceptron perceptron;
    static String filepath = "Perceptrons.xml";
    static Document doc;
    static DocumentBuilder docBuilder;
    static DocumentBuilderFactory docFactory;
    static Stage stage;
    static WriteXMLFile wxf;
    static ReadXMLFile rdx;

    static String errorFunction;
    static double errorThreshold;
    static ArrayList<double[]> inputsArrayList;
    static ArrayList<double[]> outputsArrayList;
    static List<File> files;



    @Override
    public void start(Stage primaryStage) throws Exception{
        wxf = new WriteXMLFile();
        wxf.numOfPerceptrons();
        files = new ArrayList<>();

        inputsArrayList = new ArrayList<>();
        outputsArrayList = new ArrayList<>();
        Parent root = FXMLLoader.load(getClass().getResource("HomePanel.fxml"));
        primaryStage.setTitle("Perceptron");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        stage = primaryStage;

    }

    public static void main(String[] args) {
        launch(args);
    }
}