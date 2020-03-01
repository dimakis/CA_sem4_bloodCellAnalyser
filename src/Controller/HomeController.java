package Controller;

import Main.Main;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import static Main.Main.pStage;

public class HomeController implements Initializable {

    public MenuItem openFinder, openTricolor;
    public ImageView imageView, imageViewEdited;
    public Button grayScaleBtn, cancelChanges;
    public Label saturationLabel, brightnessLabel, contrastLabel, sepiaLabel;
    public ColorAdjust colorAdjust = new ColorAdjust();
    public double fileSize;
    public MenuItem quit, openRGB;
    public Label metaData;
    public ImageView imageViewBlue;
    public Button tricolorBtn;
    public Slider redSlider, blueSlider, greenSlider, opacitySlider;
    public Button unionFindBtn;
    public Button noiseReductionBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("/home/dimdakis/Pictures/bloodCellsAssignment1/bloodCells4.jpg");
        Image im = new Image(file.toURI().toString());
        imageView.setImage(im);
        imageViewEdited.setImage(im);
        setOpenFinder();
        alterImageRedSlider();
//        unionFind();
        decreaseNoise();
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

    public void alterImageRedSlider() {
        redSlider.setMin(0);
        redSlider.setMax(255);
        redSlider.setOnMouseReleased(e -> {
            Image im = imageViewEdited.getImage();
            //reading color from loaded image
            PixelReader pixelReader = im.getPixelReader();
            WritableImage writableImage = new WritableImage(
                    (int) im.getWidth(), (int) im.getHeight());
            PixelWriter pixelWriter = writableImage.getPixelWriter();


            for (int y = 0; y < im.getHeight(); y++) {
                for (int x = 0; x < im.getWidth(); x++) {
                    Color color = pixelReader.getColor(x, y);
                    if (color.getRed() > 120) {
                        Color redEdit = Color.rgb((int) (redSlider.getValue()), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
                        pixelWriter.setColor(x, y, redEdit);
                    }

                }
            }
            imageViewEdited.setImage(writableImage);
        });
    }

    public void decreaseNoise() {
        noiseReductionBtn.setOnAction(e -> {
            Image im = imageViewEdited.getImage();
            System.out.println("im url: " + im.getUrl());
            im = new Image(im.getUrl(), 144, 144, false, false);
            imageViewEdited.setImage(im);
        });
    }
//    public void unionFind() {
//        unionFindBtn.setOnAction(e -> {
//            Image im = imageViewEdited.getImage();
//            //reading color from loaded image
//            PixelReader pixelReader = im.getPixelReader();
//            double[] pixelArray = new double[(int) (im.getWidth() * (int) im.getHeight())];
//
//            double redpix= 2;
//
//            int arraySlot = 0;
//            Color pixCol;
//            for (int y = 0; y < im.getHeight(); y++) {
//                for (int x = 0; x < im.getWidth(); x++) {
//                    pixCol = pixelReader.getColor(x, y);
//                    if ((pixCol.getRed() * 255) == 250)
//                        pixelArray[arraySlot] = whitePix;
//                    else if ((pixCol.getRed() * 255) == 200) {
//                        whitePix = pix;
//                        pixelArray[arraySlot] = whitePix;
//                    }else
//                        pix
//                    arraySlot++;
//                    pix++;
//                    //to get
//
//                }
//            }
//        });
//    }

    //for finding root
//                        if (y = 0 && x > 0  &&  x < im.getWidth() && )

    public void setTricolor() {
        tricolorBtn.setOnAction(e -> {
            Image im = imageViewEdited.getImage();
            //reading color from loaded image
            PixelReader pixelReader = im.getPixelReader();
            WritableImage writableImage = new WritableImage(
                    (int) im.getWidth(), (int) im.getHeight());
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            double pix = 1;
            double whitePix = 0;
            int[] pixelArray = new int[(int) (im.getWidth() * (int) im.getHeight())];
            int redPix = 0;
            int whitePixel = 0;
            int purplePixel = 0;
            int arraySlot = 0;
            int red = 4;
            int purple = 7;
            int white = 0;

            for (int y = 0; y < im.getHeight(); y++) {
                for (int x = 0; x < im.getWidth(); x++) {
                    Color color = pixelReader.getColor(x, y);
                    double r = color.getRed() * 255;
                    double g = color.getGreen() * 255;
                    double b = color.getBlue() * 255;
//                    System.out.println("x= " + x + ", y = " + y + ", b: " + b + ", g: " + g + ", r: " + r);
                    //finding a red cell and making it bright red
                    if (r > 80 && (r - b) > 10 && r > g && r > b) {
                        r = 200;
                        g = 10;
                        b = 10;
                        redPix++;
//                        pixelArray[arraySlot] = pix;
                        pixelArray[arraySlot] = red;
                        pix++;
                    }
                    //setting purple 'white blood cells' nuclei
                    if (b < 80 && b > g && b > r) {
                        b = 160;
                        r = 140;
                        g = 75;
//                        pixelArray[arraySlot] = pix;
                        pixelArray[arraySlot] = purple;
                        pix++;
                        purplePixel++;
                    }
                    //setting white background pixels
                    if (b > 80 && g > 80 && r > 80) {
                        b = r = g = 255;
//                        pixelArray[arraySlot] = whitePix;
                        pixelArray[arraySlot] = white;
                        whitePixel++;
                    }
                    Color c3 = Color.rgb((int) r, (int) g, (int) b);
                    pixelWriter.setColor(x, y, c3);
                    arraySlot++;

                }
            }
            for (i var: pixelArray) {
                System.out.println(var);
                
            }

            System.out.println("White Pixel count: " + whitePixel + ", Red Pixel count: " + redPix + ", Purple pixel count : " + purplePixel + ",ArraySize: " + pixelArray.length);
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
                            Color color1 = Color.rgb(newColor, newColor, newColor); //green, blue);
                            pixelWriter.setColor(x, y, color1);
                        }
                    }
                    imageViewEdited.setImage(writableImage);
                }
        );
    }

    public void setCancelChanges() {
        SepiaTone st = new SepiaTone();
        cancelChanges.setOnAction(e -> {
            redSlider.setValue(0);
            st.setLevel(0);
//            saturationSlider.setValue(0);
//            contrastSlider.setValue(0);
//            brightnessSlider.setValue(0);
            colorAdjust.setContrast(0);
            colorAdjust.setBrightness(0);
            colorAdjust.setSaturation(0);
            Image freshImage = imageView.getImage();
            imageViewEdited.setImage(null);
            imageViewEdited.setImage(freshImage);
            imageViewEdited.setEffect(colorAdjust);
//            sepiaLabel.setText("Sepia : ");
//            saturationLabel.setText("Saturation : ");
//            brightnessLabel.setText("Brightness : ");
//            contrastLabel.setText("Contrast : ");
        });
    }


    public void quit() {
        quit.setOnAction(e -> Platform.exit());
    }

}
