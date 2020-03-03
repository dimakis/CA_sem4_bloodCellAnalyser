package Controller;

import Main.Main;
import Utils.Find;
import Utils.Union;
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
import java.util.*;

import static Main.Main.pStage;

public class HomeController implements Initializable {

    public MenuItem openFinder, openTricolor;
    public ImageView imageView, imageViewEdited;
    public Button grayScaleBtn, cancelChanges, noiseReductionBtn, unionFindBtn;
    public Label saturationLabel, brightnessLabel, contrastLabel, sepiaLabel;
    public ColorAdjust colorAdjust = new ColorAdjust();
    public double fileSize;
    public MenuItem quit, openRGB;
    public Label metaData;
    public Button tricolorBtn;
    public Slider redSlider, blueSlider, greenSlider, opacitySlider;
//    public double[] redCellArray;
    public int[] redCellArray, whiteCellArray;
    public Pane ogImagePane,edImagePane;
    public HashMap <String, LinkedList<Integer>> roots = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("/home/dimdakis/Pictures/bloodCellsAssignment1/bloodCells4.jpg");
        Image im = new Image(file.toURI().toString());
        imageView.setImage(im);
        imageViewEdited.setImage(im);
        setOpenFinder();
        alterImageRedSlider();
//        unionDisjointSets();
        unionQuick();
        decreaseNoise();
//        setSaturation();
//        setBrightness();
//        setContrast();
//        setSepia();
        setGrayScale();
        setCancelChanges();
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

    //for finding root
//                        if (y = 0 && x > 0  &&  x < im.getWidth() && )
    public void unionQuick() {
        unionFindBtn.setOnAction(e -> {
            if (redCellArray.length > 0) {
                Image im = imageViewEdited.getImage();
                int width = (int) im.getWidth();
                for (int id = 0; id < redCellArray.length-1; id++) {

                    //union for non white pixels
                    if (redCellArray[id] >= 0) {

                        //union beside
                        if ((id + 1) % width != 0 && redCellArray[id +1] >=0)
                            Union.quickUnion(redCellArray, id, id + 1);

                        //union below
                        if (id + width < redCellArray.length && redCellArray[id +width] >=0)
                            Union.quickUnion(redCellArray, id, id + width);
                    }
                }


                for (int i = 0; i < redCellArray.length; i++) {

                    if (i % imageViewEdited.getImage().getWidth() == 0) {
                        System.out.println();
                    }
                    System.out.print(Find.findIntArr(redCellArray,i) + " ");
                }
            }
        });
    }

//    public void unionDisjointSets() {
//        unionFindBtn.setOnAction(e -> {
//            if (redCellArray.length > 0) {
//                Image im = imageViewEdited.getImage();
//                int width = (int) im.getWidth();
//                for (int id = 0; id < redCellArray.length; id++) {
//
//                    //union for non white pixels
//                    if (redCellArray[id] > 0) {
//
//                        //union beside
//                        if (id + 1 < width && (id + 1) % width != 0)
//                            Union.unionByHeightAndSize(redCellArray, id, id + 1);
//
//                        //union below
//                        if (id + width > redCellArray.length && redCellArray[id + width] > 0)
//                            Union.unionByHeightAndSize(redCellArray, id, id + width);
//                    }
//                }
//                for (int i = 0; i < redCellArray.length; i++) {
//                    if (i % width != 0) System.out.println(Arrays.toString(redCellArray));
//                    else System.out.println("\n");
//                }
//            }
//        });
//    }

    public void setTricolor() {
        tricolorBtn.setOnAction(e -> {
            Image im = imageViewEdited.getImage();
            //reading color from loaded image
            PixelReader pixelReader = im.getPixelReader();
            WritableImage writableImage = new WritableImage(
                    (int) im.getWidth(), (int) im.getHeight());
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            int pix = 1;
            redCellArray = new int[(int) (im.getWidth() * (int) im.getHeight())];
            whiteCellArray = new int[(int) (im.getWidth() * (int) im.getHeight())];
            int whitePixel = -1;

            for (int y = 0; y < im.getHeight(); y++) {
                for (int x = 0; x < im.getWidth(); x++) {
                    Color color = pixelReader.getColor(x, y);
                    double r = color.getRed() * 255;
                    double g = color.getGreen() * 255;
                    double b = color.getBlue() * 255;
//                    System.out.println("x= " + x + ", y = " + y + ", b: " + b + ", g: " + g + ", r: " + r);
                    //finding a red cell and making it bright red
                    if (r > 80 && (r - b) > 20 && r > g && r > b) {
                        r = 200;
                        g = 10;
                        b = 10;
//                        redPix++;
//                        redCellArray[arraySlot] = pix;
                        redCellArray[(y* (int)im.getWidth()) + x] = (y* (int)im.getWidth()) + x;
                        whiteCellArray[(y* (int)im.getWidth()) + x] = whitePixel;

                    }
                    //setting purple 'white blood cells' nuclei
                    else if (Math.min(r,b-12)-g>40){// && b > g && b > r) {
                        b = 160;
                        r = 140;
                        g = 75;
//                        redCellArray[(y* (int)im.getWidth()) + x] = (y* (int)im.getWidth()) + x;
                        redCellArray[(y* (int)im.getWidth()) + x] = whitePixel;
                        whiteCellArray[(y* (int)im.getWidth()) + x] = (y* (int)im.getWidth()) + x;
                    }
                    //setting white background pixels
                    else {//(b > 80 && g > 80 && r > 80) {
                        b = r = g = 255;
                        redCellArray[(y* (int)im.getWidth()) + x] = whitePixel;
                        whiteCellArray[(y* (int)im.getWidth()) + x] = whitePixel;
                    }
                    Color c3 = Color.rgb((int) r, (int) g, (int) b);
                    pixelWriter.setColor(x, y, c3);

                }
            }


            imageViewEdited.setImage(writableImage);
            System.out.println("array : " + redCellArray.length);
//            for (int i = 0; i < redCellArray.length; i++) {
////            int i = 0;
////            while (i < redCellArray.length)   {
//                if (i % imageView.getImage().getWidth() == 0) {
//                    System.out.println();
//                }
//                System.out.print(redCellArray[i] + " ");
////            i++;
//            }
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
//        SepiaTone st = new SepiaTone();
        cancelChanges.setOnAction(e -> {
//            st.setLevel(0);
//            saturationSlider.setValue(0);
//            contrastSlider.setValue(0);
//            brightnessSlider.setValue(0);
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
