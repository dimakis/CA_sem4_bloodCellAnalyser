package Controller;

import Main.Main;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static Main.Main.pStage;

public class HomeController implements Initializable {

    public MenuItem openFinder, openTricolor;
    public ImageView imageView, imageViewEdited;
    public Button grayScaleBtn, cancelChanges;
    public Slider saturationSlider, sepiaSlider, brightnessSlider, contrastSlider;
    public Label saturationLabel, brightnessLabel, contrastLabel, sepiaLabel;
    public Pane imageMetaData;
    public ColorAdjust colorAdjust = new ColorAdjust();
    public double fileSize;
    public MenuItem quit, openRGB;
    public Label metaData;
    public ImageView imageViewBlue;
    public Button tricolorBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File file = new File("/home/dimdakis/Pictures/bloodCellsAssignment1/bloodCells4.jpg");
        Image im = new Image(file.toURI().toString());
        imageView.setImage(im);
        imageViewEdited.setImage(im);
        setOpenFinder();
//        setSaturation();
//        setBrightness();
//        setContrast();
//        setSepia();
        setGrayScale();
//        setCancelChanges();
        setTricolor();
        quit();
//        setOpenRGB();

        System.out.println("in Initialize");
    }

    public void setOpenFinder() {
        openFinder.setOnAction(e -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open File");
                File file = fileChooser.showOpenDialog(pStage);
                fileSize = file.length();
                System.out.println("FileSize : " + fileSize);
                Image im = new Image(file.toURI().toString());
                imageView.setImage(im);
                imageView.setPreserveRatio(true);
                imageViewEdited.setImage(im);
                imageViewEdited.setPreserveRatio(true);
                imageMeta();
            } catch (Exception ignored) {
            }
        });
    }

    public void imageMeta() {
        Image im = imageView.getImage();
        metaData.setText("File Name : " + im.getUrl() + "\nFile Size : " + fileSize + "\nPixels : " + (int) im.getWidth() + "x" + (int) im.getHeight());
        System.out.println(im.getUrl());
    }

    public void setTricolor() {
        tricolorBtn.setOnAction(e -> {
            Image im = imageViewEdited.getImage();
            //reading color from loaded image
            PixelReader pixelReader = im.getPixelReader();
            WritableImage writableImage = new WritableImage(
                    (int) im.getWidth(), (int) im.getHeight());
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            int[] whiteSpace = new int[(int) (im.getWidth() * (int) im.getHeight())];


            for (int y = 0; y < im.getHeight(); y++) {
                for (int x = 0; x < im.getWidth(); x++) {
                    Color color = pixelReader.getColor(x, y);
                    double r = color.getRed() * 255;
                    double g = color.getGreen() * 255;
                    double b = color.getBlue() * 255;
                    System.out.println("x= " + x + ", y = " + y + ", b: " + b + ", g: " + g + ", r: " + r);
                    if (r > 160 && (r - b) > 25 && r > g && r > b) {
                        r = 200;
                        g = 10;
                        b = 10;
                    }
                    if (b > 120 &&  b < 200 && b > g && b > r) {
                        b = 230;
                        r = 140;
                        g = 75;
                    }
                    if ( b > 230 && g > 230 && r > 230) {
                        b = r = g = 250;
                    }
                    Color c3 = Color.rgb((int) r, (int) g, (int) b);
                    pixelWriter.setColor(x, y, c3);
                }
            }
            imageViewEdited.setImage(writableImage);
    });
}


    public void setGrayScale() {
        grayScaleBtn.setOnAction(e -> {
                    Image im = imageViewEdited.getImage();
                    PixelReader pixelReader = im.getPixelReader();
                    WritableImage writableImage = new WritableImage(
                            (int) im.getWidth(), (int) im.getHeight());
                    PixelWriter pixelWriter = writableImage.getPixelWriter();


                    for (int y = 0; y < im.getHeight(); y++) {
                        for (int x = 0; x < im.getWidth(); x++) {
                            Color color = pixelReader.getColor(x, y);
                            double r = color.getRed();
                            double g = color.getGreen();
                            double b = color.getBlue();
                            int newColor = (int) ((r + g + b) / 3 * 255);
//                            int green = (int) ((r + g + b) / 3 * 255);
//                            int blue = (int) ((r + g + b) / 3 * 255);
                            Color color1 = Color.rgb(newColor, newColor, newColor); //green, blue);
                            pixelWriter.setColor(x, y, color1);
                        }
                    }
                    imageViewEdited.setImage(writableImage);
                }
        );
    }


    public void quit() {
        quit.setOnAction(e -> Platform.exit());
    }

}
