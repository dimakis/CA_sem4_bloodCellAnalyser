package Controller;

import Utils.Find;
import Utils.Union;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import static Main.Main.pStage;

public class HomeController implements Initializable {

    public MenuItem openFinder, openTricolor;
    public ImageView imageView, imageViewEdited;
    public Button grayScaleBtn, cancelChanges, imageSizeReduction, unionFindBtn, squareBloodCellsBtn, noiseBtn, rectangleBtn;
    public Label saturationLabel, brightnessLabel, contrastLabel, sepiaLabel;
    public ColorAdjust colorAdjust = new ColorAdjust();
    public double fileSize;
    public MenuItem quit, openRGB;
    public Label metaData;
    public Button tricolorBtn;
    public Slider noiseSlider, blueSlider, greenSlider, opacitySlider;
    //    public double[] redCellArray;
    public int[] redCellArray, whiteCellArray;
    public HashMap<Integer, Integer> redCellMap;
    public HashMap<Integer, Integer> whiteCellMap;
    public Pane ogImagePane, edImagePane;
    public Pane edImagePane2;
    public Pane ogImagePane2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("/home/dimdakis/Pictures/bloodCellsAssignment1/bloodCells4.jpg");
        Image im = new Image(file.toURI().toString());
        imageView.setImage(im);
        imageViewEdited.setImage(im);
        setOpenFinder();
//        alterImageRedSlider();
        setUnionFindBtn();
        setImageSizeReduction();
        setGrayScale();
        setCancelChanges();
        setTricolor();
        setSquareBloodCellsBtn();
        noiseReduction();
        setDrawRectangle();
        quit();
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


    public void setImageSizeReduction() {
        imageSizeReduction.setOnAction(e -> {
            Image im = imageViewEdited.getImage();
            System.out.println("im url: " + im.getUrl());
            im = new Image(im.getUrl(), 144, 144, false, false);
            imageViewEdited.setImage(im);
        });
    }

    public void setUnionFindBtn() {
        unionFindBtn.setOnAction(e -> {
            setTricolor();
            unionQuick(redCellArray);
            System.out.println();
            unionQuick(whiteCellArray);
        });
    }


    public void unionQuick(int[] arr) {
        if (arr.length > 0) {
            Image im = imageViewEdited.getImage();
            int width = (int) im.getWidth();
            for (int id = 0; id < arr.length - 1; id++) {

                //union for non white pixels
                if (arr[id] >= 0) {

                    //union beside
                    if ((id + 1) % width != 0 && arr[id + 1] >= 0)
                        Union.quickUnion(arr, id, id + 1);

                    //union below
                    if (id + width < arr.length && arr[id + width] >= 0)
                        Union.quickUnion(arr, id, id + width);
                }
            }
            //print array to console
            for (int i = 0; i < arr.length - 1; i++) {

                if (i % imageViewEdited.getImage().getWidth() == 0) {
                    System.out.println();
                }
                System.out.print(Find.findIntArr(arr, i) + " ");
            }
        }
    }

    public void setSquareBloodCellsBtn() {
        squareBloodCellsBtn.setOnAction(e -> {
            whiteCellMap = arrayToHashMap(whiteCellArray);
            System.out.println();
            redCellMap = arrayToHashMap(redCellArray);
        });
    }

    public void setDrawRectangle() {
        rectangleBtn.setOnAction(e -> {
            redCellMap = arrayToHashMap(redCellArray);
            drawRectangles(redCellArray, redCellMap,Color.GREEN);
        });
    }

    public void drawRectangles(int[] arr, HashMap<Integer, Integer> rootMap, Color color) {
        Image im = imageViewEdited.getImage();
        int width = (int) im.getWidth();
        int height = (int) im.getHeight();
//        int cellWidth = 0;
//        int cellHeight = 0;
//        int cellWidth = 0;
        Pane rectPane = edImagePane;
        rootMap.forEach((k, v) -> {
            int cellWidth = 0;
            int cellHeight = 0;
            if (v > 10) {
//                int xRoot = Find.findIntArr(arr,k);
                int xRoot = Find.findIntArr(arr, k) % width;
                int yRoot = (Find.findIntArr(arr,k) - xRoot)/height ;
                for (int i = 0; i < arr.length - 1; i++) {
                    if (arr[i] < arr.length-1 && arr[i] > -1) {
                        if (arr[i] == arr[i + 1])
                            cellWidth++;
                        if ( arr[i] < arr.length-width && arr[i] == arr[i + width]) {
                            cellHeight++;
                        }
//                        cellWidth++;
                    }
                }
                Rectangle rectangle = new Rectangle(xRoot, yRoot, cellWidth, cellHeight);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(color);
                edImagePane2.getChildren().add(rectangle);
                edImagePane.getChildren().add(rectangle);
                rectPane.getChildren().add(rectangle);
//                System.out.println("x: " + x + ", y: " + y);
//                xRoot
                int recWidth = 0;
            }
        });


//        for (int y = 0; y < im.getHeight(); y++) {
//            for (int x = 0; x < im.getWidth(); x++) {
//
//                //union for non white pixels
//                if (arr[(y * width) + x] >= 0) {
//
//                    //union beside
//                    if ((arr[y * width + (x + 1)]) % width != 0 && arr[y * width + (x + 1)] >= 0 && arr[y * width + x] == arr[y * width + (x + 1)])
//rect
//
//                    //union below
//                    if (id + width < arr.length && arr[id + width] >= 0)
//                        Union.quickUnion(arr, id, id + width);
//                }
//            }
//        }
//        rectPane.getChildren().add(rect());


//        System.out.println("cellMap of array " + arr + " " + cellMap.asMap());
    }

    public HashMap<Integer, Integer> arrayToHashMap(int[] arr) {
        HashMap<Integer, Integer> cellMap = new HashMap<>();
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] != -1) {
                if (!cellMap.containsKey(arr[i])) {
                    cellMap.put(arr[i], 1);
                } else if (cellMap.containsKey(arr[i])) {
                    int cells = cellMap.get(arr[i]);
                    cellMap.put(arr[i], cells + 1);
                }
            }
        }
//        cellMap.forEach((k, v) -> {
//            System.out.println("Key: " + k + ", Value: [" + v + "]");
//        });
        return cellMap;
    }

    public WritableImage colorNoiseWhite(HashMap<Integer, Integer> hMap, HashMap<Integer, Integer> hMap2, int[] arr, int[] arr2, int percentNoiseReduction) {
//        hMap = arrayToHashMap(arr);
        hMap.putAll(hMap2);
        Image im = imageViewEdited.getImage();
        PixelReader pr = im.getPixelReader();
//        int per = percentNoiseReduction;
        WritableImage writableImage;
        writableImage = (WritableImage) im;
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        unionQuick(arr);
        unionQuick(arr2);

        for (int y = 0; y < im.getHeight(); y++) {
            for (int x = 0; x < im.getWidth(); x++) {
                int pix = y * (int) im.getWidth() + x;
                if (Find.findIntArr(arr, pix) > -1 || Find.findIntArr(arr2, pix) > -1) {//] > -1) {
//                    if ((hMap.containsKey(arr[pix]) || hMap.containsKey(arr2[pix])) && ((hMap.get(arr[pix]) < percentNoiseReduction) || (hMap.get(arr2[pix]) < percentNoiseReduction))) {
                    if ((hMap.containsKey(Find.findIntArr(arr, pix)) && (hMap.get(Find.findIntArr(arr, pix)) < percentNoiseReduction)) || (hMap.containsKey(Find.findIntArr(arr2, pix)) && hMap.get(Find.findIntArr(arr2, pix)) < percentNoiseReduction)) {//[pix]) && (hMap.get(arr[pix]) < percentNoiseReduction))){ //|| (hMap.containsKey(arr2[pix]) && (hMap.get(arr2[pix]) < percentNoiseReduction))) {
//                                    System.out.println("Pixel in noise reduction: x: " + x + ", y: " + y );
//                                    System.out.println("hmap key: " + hMap.containsKey(arr[y * (int) im.getWidth() + x]));
//                    System.out.println("in first if white noise reduction");
                        pixelWriter.setColor(x, y, Color.WHITE);
                    }
                }
            }
        }
        return writableImage;
    }

    public void noiseReduction() {
//        setSquareBloodCellsBtn();
        noiseBtn.setOnAction(e -> {
            whiteCellMap = arrayToHashMap(whiteCellArray);
            System.out.println();
            redCellMap = arrayToHashMap(redCellArray);
            int noisePercent = (int) noiseSlider.getValue() / 15;
//            WritableImage wrImWhite = colorNoiseWhite(whiteCellMap, whiteCellArray, noisePercent);
            WritableImage wrIm = colorNoiseWhite(redCellMap, whiteCellMap, redCellArray, whiteCellArray, noisePercent);
            imageViewEdited.setImage(wrIm);

        });
    }


    public void displayTricolor() {

    }

    public void setTricolor() {
        tricolorBtn.setOnAction(e -> {
            Image im = imageViewEdited.getImage();
            //reading color from loaded image
            PixelReader pixelReader = im.getPixelReader();
            WritableImage writableImage = new WritableImage(
                    (int) im.getWidth(), (int) im.getHeight());
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            redCellArray = new int[(int) (im.getWidth() * (int) im.getHeight())];
            whiteCellArray = new int[(int) (im.getWidth() * (int) im.getHeight())];
            int whitePixel = -1;

            for (int y = 0; y < im.getHeight(); y++) {
                for (int x = 0; x < im.getWidth(); x++) {
                    Color color = pixelReader.getColor(x, y);
                    double r = color.getRed() * 255;
                    double g = color.getGreen() * 255;
                    double b = color.getBlue() * 255;
                    //finding a red cell and making it bright red
                    if (r > 80 && (r - b) > 20 && r > g && r > b) {
                        r = 200;
                        g = 10;
                        b = 10;
                        redCellArray[(y * (int) im.getWidth()) + x] = (y * (int) im.getWidth()) + x;
                        whiteCellArray[(y * (int) im.getWidth()) + x] = whitePixel;
                    }
                    //setting purple 'white blood cells' nuclei
                    else if (Math.min(r, b - 12) - g > 38) {// && b > g && b > r) {
                        b = 160;
                        r = 140;
                        g = 75;
//                        redCellArray[(y* (int)im.getWidth()) + x] = (y* (int)im.getWidth()) + x;
                        redCellArray[(y * (int) im.getWidth()) + x] = whitePixel;
                        whiteCellArray[(y * (int) im.getWidth()) + x] = (y * (int) im.getWidth()) + x;
                    }
                    //setting white background pixels
                    else {//(b > 80 && g > 80 && r > 80) {
                        b = r = g = 255;
                        redCellArray[(y * (int) im.getWidth()) + x] = whitePixel;
                        whiteCellArray[(y * (int) im.getWidth()) + x] = whitePixel;
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

//legit
//    public void unionQuick() {
//        unionFindBtn.setOnAction(e -> {
//            if (redCellArray.length > 0) {
//                Image im = imageViewEdited.getImage();
//                int width = (int) im.getWidth();
//                for (int id = 0; id < redCellArray.length-1; id++) {
//
//                    //union for non white pixels
//                    if (redCellArray[id] >= 0) {
//
//                        //union beside
//                        if ((id + 1) % width != 0 && redCellArray[id +1] >=0)
//                            Union.quickUnion(redCellArray, id, id + 1);
//
//                        //union below
//                        if (id + width < redCellArray.length && redCellArray[id +width] >=0)
//                            Union.quickUnion(redCellArray, id, id + width);
//                    }
//                }
//
//
//                for (int i = 0; i < redCellArray.length; i++) {
//
//                    if (i % imageViewEdited.getImage().getWidth() == 0) {
//                        System.out.println();
//                    }
//                    System.out.print(Find.findIntArr(redCellArray,i) + " ");
//                }
//            }
//        });
//    }
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
//                } else if (arr[y * (int) im.getWidth() + x] != -1) {
//
////                    System.out.println("in array for red else");
//                    pixelWriter.setColor(x, y, Color.RED);
//                } else if (arr2[y * (int) im.getWidth() + x] != -1) {
//                    pixelWriter.setColor(x, y, Color.PURPLE);
////                    System.out.println("in array2 for purple else");
//                }
//                } else {
//                    pixelWriter.setColor(x, y, Color.WHITE);
//                    System.out.println("in final else");
//                }
//                           if ((hMap.get(arr[y * (int) im.getWidth() + x]) < percentNoiseReduction)) {
////                                    System.out.println("Pixel in noise reduction: x: " + x + ", y: " + y );
//                                pixelWriter.setColor(x, y, Color.WHITE);
//                        }
}
