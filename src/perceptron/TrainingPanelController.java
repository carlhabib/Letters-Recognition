package perceptron;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static perceptron.Main.*;

public class TrainingPanelController {

    public ToggleGroup trainingTG;
    public TextField path_Field;
    public Label validationLabel;
    public TextField validationTF;
    public ListView <String> imagesListView;
    public ImageView imagesImageView;
    public TextField accuracyTF;
    public RadioButton splitRB;
    public RadioButton kfoldsRB;
    private List<File> files;

    public TrainingPanelController(){
        files = new ArrayList<>();
        splitRB = new RadioButton();
        kfoldsRB = new RadioButton();
        inputsArrayList = new ArrayList<>();
        outputsArrayList = new ArrayList<>();

    }

    public void initialize(){
        kfoldsRB.setUserData(1);
        splitRB.setUserData(0);
        if (!files.isEmpty()){
            fillView();
        }
    }

    public void setPath(ActionEvent actionEvent) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Training Set");
        Stage stage = new Stage();

        files = fc.showOpenMultipleDialog(stage);

        fillView();
        inputsArrayList.clear();
        outputsArrayList.clear();
        for(File f : files) {
            Detection detection = new Detection(f.getAbsolutePath());
            int[][] array2Dimg;
            array2Dimg = detection.convertImageTo2D();
            ArrayList arrayInt = detection.convert2DToBinary(array2Dimg);
            int[] array1DImg;
            array1DImg = detection.convertArrTo1D(arrayInt);

            double[] outputsArray = new double[26];
            Arrays.fill(outputsArray,0);
            double[] doubleArray = new double[array1DImg.length];
            for (int j = 0; j < doubleArray.length; j++) {

                doubleArray[j] = array1DImg[j];

            }
            double m = files.indexOf(f);
            int l = files.indexOf(f);
            if(m/26.0==0.0){
                Arrays.fill(outputsArray,1);
            }else {
                int d = l % 26;
                outputsArray[d] = 1;
            }
            inputsArrayList.add(doubleArray);
            outputsArrayList.add(outputsArray);
        }

    }

    public void train_Perceptron(ActionEvent actionEvent) {


        double k = Double.parseDouble(validationTF.getText());

        double numberOfTests = 0;
        double counter = 0;
        if((int)trainingTG.getSelectedToggle().getUserData()==0) {
            int percentage = (int) (inputsArrayList.size()*k);
            for (int i = 0; i < percentage/100; i++) {
                System.out.println(files.get(i).getName());
                perceptron.trainPerceptron(inputsArrayList.get(i), outputsArrayList.get(i));
            }

            for (int i=percentage/100; i<inputsArrayList.size(); i++){
                System.out.println(files.get(i).getName());
                Matrix temp = perceptron.test(inputsArrayList.get(i));
                double [] tempor = temp.getData();
                numberOfTests++;
                double err = ErrorMachine.computeError(errorFunction, tempor, outputsArrayList.get(i));
                if (ErrorMachine.errorAcceptance(err, errorThreshold))
                    counter++;
            }
        }
        else{
            double numberOfPoints = inputsArrayList.size();
            int currentMin = 0;
            while (currentMin + k < numberOfPoints) {
                for (int i = 0; i < numberOfPoints; i++) {
                    if (i < currentMin || i > (currentMin + k)) {
                        System.out.println(files.get(i).getName());
                        perceptron.trainPerceptron(inputsArrayList.get(i), outputsArrayList.get(i));
                    }
                }
                for (int i = currentMin; i < currentMin + k; i++) {
                    System.out.println(files.get(i).getName());
                    double[] temp = perceptron.test(inputsArrayList.get(i)).getData();
                    numberOfTests++;
                    double err = ErrorMachine.computeError(errorFunction, temp, outputsArrayList.get(i));
                    if (ErrorMachine.errorAcceptance(err, errorThreshold))
                        counter++;
                }
                currentMin+=k;
            }
        }
        double err = counter/numberOfTests;
        accuracyTF.setText(""+err);
    }

    public void goTestPanel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TestingPanel.fxml"));
        stage.setTitle("Perceptron");
        stage.setScene(new Scene(root, 800, 550));
        stage.show();
    }

    public void goHome(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("HomePanel.fxml"));
        stage.setTitle("Perceptron");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void fillView(){

        List<String> names = new ArrayList<>();
        for (File f : files) {
            names.add(f.getName());
            System.out.println(f.getName());
        }
        ObservableList<String> observableNames = FXCollections.observableArrayList(names);
        imagesListView.setItems(observableNames);

        imagesListView.setOnMouseClicked(new ListViewHandler(){
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                int im = imagesListView.getSelectionModel().getSelectedIndex();
                imagesImageView.setImage(new Image("File:testing/"+files.get(im).getName()));
            }
        });
    }
}
