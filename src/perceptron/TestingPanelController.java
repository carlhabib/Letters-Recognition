package perceptron;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static perceptron.Main.*;

public class TestingPanelController {
    public ImageView trgtImgView;
    public ImageView srcImgView;
    public TextField path_Field;
    private File f;

    public TestingPanelController () {

    }

    public void initialize() {

    }

    public void goTesting(ActionEvent actionEvent) throws IOException {

        Detection detection = new Detection(f.getAbsolutePath());
        int[][] array2Dimg;
        array2Dimg = detection.convertImageTo2D();
        ArrayList arrayInt = detection.convert2DToBinary(array2Dimg);
        int[] array1DImg = detection.convertArrTo1D(arrayInt);

        double[] doubleArray = new double[array1DImg.length];
        for (int j = 0; j < doubleArray.length; j++) {

            doubleArray[j] = array1DImg[j];

        }
        Matrix temp = perceptron.test(doubleArray);

        double [] out = temp.toArray();

        int count=0;
        int index = -1;

        for(int i=0; i<out.length; i++){
            if(count<=1 && out[i]==1){
                    count++;
                    index = i;
            }
            if (count>1){
                index = -1;
                break;
            }
        }

        Image outimg;
        if(index==-1) {
            outimg = new Image("File:letters/unknown.png");
        }else {
            outimg = new Image("File:letters/"+index+".png");
        }
        trgtImgView.setImage(outimg);
    }

    public void goTraining(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TrainingPanel.fxml"));
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

    public void savePerceptron(ActionEvent actionEvent) throws Exception {
        wxf = new WriteXMLFile();
        wxf.writeXML(perceptron);

    }

    public void setPath(ActionEvent actionEvent) throws Exception{
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Training Set");
        Stage stage = new Stage();

        f = fc.showOpenDialog(stage);

        if(f != null) {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(f);
            path_Field.setText(f.getAbsolutePath());
        }
        assert f != null;
        srcImgView.setImage(new Image("File:" + f.getPath()));
    }


}
