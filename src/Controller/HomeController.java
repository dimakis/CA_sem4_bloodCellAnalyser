package Controller;

import Utils.Find;
import Utils.Union;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import static Main.Main.pStage;

public class HomeController implements Initializable {

    public MenuItem openFinder, openTricolor, quit;
    public ImageView imageView, imageViewEdited;
    public Button grayScaleBtn, cancelChanges, imageSizeReduction, unionFindBtn, squareBloodCellsBtn, noiseBtn, rectangleBtn, tricolorBtn, countCellsBtn;
    public Label saturationLabel, brightnessLabel, contrastLabel, sepiaLabel, metaData;
    public ColorAdjust colorAdjust = new ColorAdjust();
    public double fileSize;
    public Slider noiseSlider, blueSlider, greenSlider, opacitySlider;
    //    public double[] redCellArray;
    public int[] redCellArray, whiteCellArray;
    public HashMap<Integer, Integer> redCellMap, whiteCellMap;
    public Pane ogImagePane, edImagePane, edImagePane2, ogImagePane2;
    public int avgCellSize = 0, totalCellSize = 0, index = 0, cellCount = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("/home/dimdakis/Pictures/bloodCellsAssignment1/bloodCells4.jpg");
        Image im = new Image(file.toURI().toString(), 400, 400, false, false);
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
        setCountCellsBtn();
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
                Image im = new Image(file.toURI().toString(), 400, 400, false, false);
                setCancelChanges();
                imageView.setImage(im);
                imageViewEdited.setImage(im);
                imageMeta();
//                edImagePane2.getChildren().removeAll();
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
//            for (int i = 0; i < arr.length - 1; i++) {
//
//                if (i % imageViewEdited.getImage().getWidth() == 0) {
//                    System.out.println();
//                }
//                System.out.print(Find.findIntArr(arr, i) + " ");
//            }
        }
    }

    public void setCountCellsBtn() {
        countCellsBtn.setOnAction(e -> {
            int whiteCellCount = countCells(whiteCellMap, noiseSlider.getValue());
            cellCount = 0;
            int redCellCount = countCells(redCellMap, noiseSlider.getValue());
            System.out.println("whiteCell count: " + whiteCellCount + ", RedCell count: " + redCellCount);
        });
    }

    public int countCells(HashMap<Integer, Integer> hmap, double percentNoiseReduction) {
        hmap.forEach((k, v) -> {
            if (v > percentNoiseReduction) {
                cellCount++;
            }
        });
        return cellCount;
    }

//    public void countCells(HashMap<Integer, Integer> hmap) {
//        countCellsBtn.setOnAction(
//                hmap.forEach((k, v) -> {
//                    System.out.println("key: " + k + ", v" + v);
//                    int vTotal = 0;
//                    if (v > 10) {
//                        vTotal += v;
//                        vTotal = vTotal / hmap.size();
//                    }
////                    cellCount = vTotal;
//
////                    System.out.println("cellCount: " + cellCount);
//
//                    })
//        );
////        return cellCount;
//    }

    public void setSquareBloodCellsBtn() {
        squareBloodCellsBtn.setOnAction(e -> {
            whiteCellMap = arrayToHashMap(whiteCellArray);
            System.out.println();
            redCellMap = arrayToHashMap(redCellArray);
        });
    }

    public void setDrawRectangle() {
        rectangleBtn.setOnAction(e -> {
            Image im = imageViewEdited.getImage();
            redCellMap = arrayToHashMap(redCellArray);
            drawRectangles(redCellArray, redCellMap, Color.GREEN, (int) noiseSlider.getValue());
            whiteCellMap = arrayToHashMap(whiteCellArray);
            drawRectangles(whiteCellArray, whiteCellMap, Color.BLUE, (int) noiseSlider.getValue());
            System.out.println("array" + redCellArray.length);
        });
    }

    public void drawRectangles(int[] arr, HashMap<Integer, Integer> rootMap, Color color, int percentNoiseReduction) {
        Image image = imageViewEdited.getImage();
        double aspectRatio = image.getWidth() / image.getHeight();
        double width = Math.min(imageViewEdited.getFitWidth(), imageViewEdited.getFitHeight() * aspectRatio);
        double height = Math.min(imageViewEdited.getFitHeight(), imageViewEdited.getFitWidth() / aspectRatio);
        edImagePane.resize(400, 400);
        edImagePane.setLayoutX(0);
        edImagePane.setLayoutY(0);

        rootMap.forEach((k, v) -> {
            boolean initialHeight = false;
            int cellWidth = 1;
            int cellHeight = 1;
            int yRoot = 0;
            int xRoot = -1;
            if (v > percentNoiseReduction) {
                for (int i = 0; i < arr.length - 1; i++)
                    if (Find.findIntArr(arr, i) == k) {
                        int x = i % (int) width, y = i / (int) width;
                        if (xRoot == -1) {
                            xRoot = x;
                            yRoot = y;
                        } else {
                            if (x > xRoot + cellWidth) cellWidth = x - xRoot;
                            if (x < xRoot) {
                                cellWidth += (xRoot - x);
                                xRoot = x;
                            }
                            if (y > yRoot + cellHeight) cellHeight = y - yRoot;
                        }
                    }
//                Rectangle rectangle2 = new Rectangle(0, 0, 400, 400);
                Rectangle rectangle = new Rectangle(xRoot, yRoot, cellWidth, cellHeight);
//                rectangle.setOnMouseClicked(e -> {

//                    edImagePane.getChildren().add(label);
//                });
                System.out.println("rectangle width " + cellWidth + ", height" + cellHeight);
                float rec = cellWidth + cellHeight / 1000;
                float[] rectArr = new float[rootMap.size()];
                rectArr[rootMap.size() - 1] = rec;
                totalCellSize = totalCellSize + (cellHeight * cellWidth);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(color);
                index++;
                Label label = new Label();
                label.setLayoutX(xRoot + cellWidth - 18);
                label.setLayoutY(yRoot + cellHeight - 15);
                label.setStyle("-fx-font-weight: bold");
                label.setText(String.valueOf(index));
                edImagePane.getChildren().add(label);
//                                edImagePane.getChildren().add(rectangle2);
                edImagePane.getChildren().add(rectangle);
//                edImagePane.getChildren().add(rectangle);
//                rectPane.getChildren().add(rectangle);
//                System.out.println("x: " + x + ", y: " + y);
         //   }
           // initialHeight = false;
            int cellPerCluster = (int) ((rectangle.getHeight() + rectangle.getWidth()) / countCells(rootMap, noiseSlider.getValue()));
            Tooltip tooltip = new Tooltip("Estimated blood cells: " + cellPerCluster);
            Tooltip.install(rectangle, tooltip);
            rectangle.setOnMouseClicked(e -> {
                System.out.println("tooltip" + tooltip.getText());
                System.out.println("totalCell size: " + totalCellSize);
            });
        }
        });
        avgCellSize = totalCellSize / countCells(rootMap, noiseSlider.getValue());
        cellCount = 0;
        System.out.println("avgCellSize: " + avgCellSize);
    }

    public void countInCluster() {
//        edImagePane.getChildren().
    }

    public int arrayFrequency(float[] fArr, float n) {
        boolean visited[] = new boolean[fArr.length];
        Arrays.fill(visited, false);

        int count = 0;
        for (int i = 0; i < fArr.length; i++) {

            if (visited[i] == true)
                continue;

            for (int j = i + 1; j < fArr.length; j++) {
                if (fArr[i] == fArr[j]) {
                    visited[j] = true;
                    count++;
                }
            }
            System.out.println("frequency of " + n + ", ");
        }
        return count;
    }

    public HashMap<Integer, Integer> arrayToHashMap(int[] arr) {
        HashMap<Integer, Integer> cellMap = new HashMap<>();
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] != -1) {
                if (!cellMap.containsKey(Find.findIntArr(arr, i))) {
                    cellMap.put(Find.findIntArr(arr, i), 1);
                } else if (cellMap.containsKey(Find.findIntArr(arr, i))) {
                    int cells = cellMap.get(Find.findIntArr(arr, i));
                    cellMap.put(Find.findIntArr(arr, i), cells + 1);
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
//        hMap.putAll(hMap2);
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
//                if (Find.findIntArr(arr, pix) > -1 || Find.findIntArr(arr2, pix) > -1) {//] > -1) {
//                if (Find.findIntArr(arr, pix) == 17718){
//                        System.out.println("Hello");
//                    if (Find.findIntArr(arr, pix) > -1) {//] > -1) {
//                    if ((hMap.containsKey(arr[pix]) || hMap.containsKey(arr2[pix])) && ((hMap.get(arr[pix]) < percentNoiseReduction) || (hMap.get(arr2[pix]) < percentNoiseReduction))) {
                if (Find.findIntArr(arr, pix) > -1 && hMap.containsKey(Find.findIntArr(arr, pix)) && (hMap.get(Find.findIntArr(arr, pix)) < percentNoiseReduction))
//                    if (arr[pix] < arr.length)
//                        arr[pix] = -1;
                    pixelWriter.setColor(x, y, Color.WHITE);
//                hMap.put(Find.findIntArr(arr,pix), 0);
                //|| (hMap2.containsKey(Find.findIntArr(arr2, pix)) && hMap2.get(Find.findIntArr(arr2, pix)) < percentNoiseReduction)) {//[pix]) && (hMap.get(arr[pix]) < percentNoiseReduction))){ //|| (hMap.containsKey(arr2[pix]) && (hMap.get(arr2[pix]) < percentNoiseReduction))) {
//                                    System.out.println("Pixel in noise reduction: x: " + x + ", y: " + y );||
//                        if (Find.findIntArr(arr2, pix) > -1) {
                if (Find.findIntArr(arr2, pix) > -1 && hMap2.containsKey(Find.findIntArr(arr2, pix)) && hMap2.get(Find.findIntArr(arr2, pix)) < percentNoiseReduction) {

//                                    System.out.println("hmap key: " + hMap.containsKey(arr[y * (int) im.getWidth() + x]));
//                    System.out.println("in first if white noise reduction");
//                    if (arr2[pix] < arr2.length) arr2[pix] = -1;

                    pixelWriter.setColor(x, y, Color.WHITE);
//                    hMap2.put(Find.findIntArr(arr2,pix), 0);
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
            int noisePercent = (int) noiseSlider.getValue();
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
            double aspectRatio = im.getWidth() / im.getHeight();
            double width = Math.min(imageViewEdited.getFitWidth(), imageViewEdited.getFitHeight() * aspectRatio);
            double height = Math.min(imageViewEdited.getFitHeight(), imageViewEdited.getFitWidth() / aspectRatio);
            redCellArray = new int[(int) (im.getWidth() * (int) im.getHeight())];
            System.out.println("redcellArray: " + redCellArray.length);
            whiteCellArray = new int[(int) (im.getWidth() * (int) im.getHeight())];
            System.out.println("WhitecellArray: " + whiteCellArray.length);
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
                    else if (Math.min(r, b - 12) - g > 35) {// && b > g && b > r) {
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
            edImagePane.getChildren().clear();
            index = 0;
            cellCount = 0;
//            edImagePane2.getChildren().remove(obj);

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

