package Controller;

import Main.Main;
import Utils.Find;
import Utils.Union;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.*;

import static Main.Main.*;

public class HomeController implements Initializable {

    public MenuItem openFinder, openTricolor, quit;
    public ImageView imageView, imageViewEdited, imageViewEdited1;
    public Button grayScaleBtn, cancelChanges, imageSizeReduction, unionFindBtn, noiseBtn, rectangleBtn, tricolorBtn, countCellsBtn, recTriColor, numberCells1, numberCells;
    public Label metaData, cellCountDisplayLbl, cellHoverLbl;
    public double fileSize;
    public Slider noiseSlider, purpleSlider, redSlider;
    public int[] redCellArray, whiteCellArray;
    public HashMap<Integer, Integer> redCellMap, whiteCellMap;
    public Pane ogImagePane, edImagePane, edImagePane2, ogImagePane2, edImagePane1;
    public int totalCellSize = 0, index = 0, cellCount = 0;
    public boolean alreadyDrawn = false, alreadyDrawn2 = false, cellsCounted = false, cellsSeqCount = false, cellsSeqCount1 = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("/home/dimdakis/Pictures/bloodCellsAssignment1/bloodCells4.jpg");
        Image im = new Image(file.toURI().toString(), 400, 400, false, false);
        imageView.setImage(im);
        imageViewEdited.setImage(im);
        imageMeta();
        setTriColorScene();
        setOpenFinder();
        setUnionFindBtn();
        setImageSizeReduction();
        setGrayScale();
        cancelChanges();
        setTricolor();
        setCountCellsBtn();
        noiseReduction();
        setDrawRectangle();
        recTriColor();
        numberRecsEditedPane();
        numberRecsTricolorPane();
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
                imageViewEdited1.setImage(im);
                imageMeta();
            } catch (Exception ignored) {
            }
        });
    }

    public void setTricolor() {
        tricolorBtn.setOnAction(e -> {
            Image im = imageView.getImage();
            try {
                if (redCellArray.equals(null) && whiteCellArray.equals(null)) {
                    redCellArray = new int[(int) (im.getWidth() * (int) im.getHeight())];
                    whiteCellArray = new int[(int) (im.getWidth() * (int) im.getHeight())];
                }
                displayWritableImage();
            } catch (Exception e1) {
                redCellArray = new int[(int) (im.getWidth() * (int) im.getHeight())];
                whiteCellArray = new int[(int) (im.getWidth() * (int) im.getHeight())];
                displayWritableImage();
            }
        });
    }

//    public int sliderValue(Slider slider)    {
//
//    }

    public WritableImage displayTricolor(Image im, int[] redCellArr, int[] whiteCellArr, int redAmount, int purpleAmount) {
//        Image im = imageView.getImage();
        //reading color from loaded image
        PixelReader pixelReader = im.getPixelReader();
        WritableImage writableImage = new WritableImage(
                (int) im.getWidth(), (int) im.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        double aspectRatio = im.getWidth() / im.getHeight();
//        int redAmount = (int) redSlider.getValue();
//        int purpleAmount = (int) purpleSlider.getValue();
        if (redCellArr.equals(null) && whiteCellArr.equals(null)) {
            redCellArr = new int[(int) (im.getWidth() * (int) im.getHeight())];
            whiteCellArr = new int[(int) (im.getWidth() * (int) im.getHeight())];
        }
        int whitePixel = -1;

        for (int y = 0; y < im.getHeight(); y++) {
            for (int x = 0; x < im.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);
                double r = color.getRed() * 255;
                double g = color.getGreen() * 255;
                double b = color.getBlue() * 255;
                //finding a red cell and making it bright red
                if (r > redAmount && (r - b) > redAmount - 60 && r > g && r > b) {
                    r = 200;
                    g = 10;
                    b = 10;
                    redCellArr[(y * (int) im.getWidth()) + x] = (y * (int) im.getWidth()) + x;
                    whiteCellArr[(y * (int) im.getWidth()) + x] = whitePixel;
                }
                //setting purple 'white blood cells' nuclei
                else if (Math.min(r, b - 12) - g > purpleAmount) {// && b > g && b > r) {
                    b = 160;
                    r = 140;
                    g = 75;
                    redCellArr[(y * (int) im.getWidth()) + x] = whitePixel;
                    whiteCellArr[(y * (int) im.getWidth()) + x] = (y * (int) im.getWidth()) + x;
                }
                //setting white background pixels
                else {
                    b = r = g = 255;
                    redCellArr[(y * (int) im.getWidth()) + x] = whitePixel;
                    whiteCellArr[(y * (int) im.getWidth()) + x] = whitePixel;
                }
                Color c3 = Color.rgb((int) r, (int) g, (int) b);
                pixelWriter.setColor(x, y, c3);

            }
        }
//        unionQuick(redCellArr);
//        unionQuick(whiteCellArr);
        return writableImage;
    }

    // unions disjoint sets from an inputted array based on the root of cell
    public void unionQuick(int[] arr, Image im) {
        if (arr.length > 0) {
//            Image im = imageViewEdited.getImage();
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

    //either draws rentangles or clears panes of rectangles
    public void rectangle_a_fy(Pane pane, boolean drawn) {
        if (!drawn) {
            index = 0;
            redCellMap = arrayToHashMap(redCellArray);
            drawRectangles(redCellArray, redCellMap, Color.GREEN, (int) noiseSlider.getValue(), pane);
            whiteCellMap = arrayToHashMap(whiteCellArray);
            drawRectangles(whiteCellArray, whiteCellMap, Color.RED, (int) noiseSlider.getValue(), pane);
            if (pane.equals(edImagePane))
                alreadyDrawn2 = true;
            else alreadyDrawn = true;
        } else {
            pane.getChildren().clear();
            if (pane.equals(edImagePane))
                alreadyDrawn2 = false;
            else alreadyDrawn = false;
            index = 0;
        }
    }

    public void drawRectangles(int[] arr, HashMap<Integer, Integer> rootMap, Color color, int percentNoiseReduction, Pane pane) {
        Image image = imageViewEdited.getImage();
        double aspectRatio = image.getWidth() / image.getHeight();
        double width = Math.min(imageViewEdited.getFitWidth(), imageViewEdited.getFitHeight() * aspectRatio);
//        double height = Math.min(imageViewEdited.getFitHeight(), imageViewEdited.getFitWidth() / aspectRatio);
        Collection<Integer> col = (rootMap.values());
        int cc = 0;
        int valuesOverPerc = 0;
        for (Object i : col
        ) {
            if ((int) i > percentNoiseReduction) {
                cc = cc + (int) i;
                valuesOverPerc++;
            }
        }
        int aCellSize = cc / valuesOverPerc;
        rootMap.forEach((k, v) -> {
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
                Rectangle rectangle = new Rectangle(xRoot, yRoot, cellWidth, cellHeight);
                totalCellSize = totalCellSize + (cellHeight * cellWidth);
                rectangle.setFill(Color.TRANSPARENT);
                if (cellHeight * cellWidth > (aCellSize * 2.1) && color != Color.RED) {
                    rectangle.setStroke(Color.BLUE);
                } else rectangle.setStroke(color);
                pane.getChildren().add(rectangle);

            }
        });
        cellCount = 0;
    }

    public void numberRectanglesSequentially(Pane pane) {
        ArrayList<Rectangle> recList = new ArrayList();
        index = 1;
        for (Node node : pane.getChildren()) {
            if (node instanceof Rectangle) {
                recList.add((Rectangle) node);
            }
        }
        // arrayList of rectangles sorted by y values of rectangles
        recList.sort((x, y) -> (int) (x.getY() - y.getY()));
        for (Rectangle rec : recList) {
            numberRectangles((int) rec.getX(), (int) rec.getY(), (int) rec.getLayoutX() + 18, (int) rec.getLayoutY() + 13, pane);
            index++;
        }
        if (pane.equals(edImagePane))
            cellsSeqCount = true;
        else cellsSeqCount1 = true;
    }


    // adds labels of numbering to pane
    public void numberRectangles(int xroot, int yroot, int cellWidth, int cellHeight, Pane pane) {
        Label label = new Label();
        label.setLayoutX(xroot + cellWidth - 18);
        label.setLayoutY(yroot + cellHeight - 15);
        label.setStyle("-fx-font-weight: bold");
        label.setText(String.valueOf(index));
        pane.getChildren().add(label);
    }

    //looks for child instances of rectangles of the edImagePane and counts the amount of cells in each cluster
    public void countInCluster() {
        int temp = 0, avg = 0, count = 0, inCluster = 0;
        for (Node node : edImagePane.getChildren()
        )
            if (node instanceof Rectangle && ((Rectangle) node).getStroke().equals(Color.GREEN)) {
                temp = temp + (int) ((Rectangle) node).getHeight() * (int) ((Rectangle) node).getWidth();
                count++;
            }
        for (Node node : edImagePane.getChildren()) {
            if (node instanceof Rectangle && count > 0) {
                avg = temp / count;
                inCluster = (int) ((((((Rectangle) node).getHeight() * ((Rectangle) node).getWidth())) / avg) * 1.2);
                if (((Rectangle) node).getStroke().equals(Color.RED) || inCluster < 1) inCluster = 1;
                Tooltip tooltip = new Tooltip("Estimated blood cells: " + inCluster);
                Tooltip.install(node, tooltip);
            }
        }
    }

    // method takes in an array representing pixels,
    // if pixel is not white and if pixel value is currently unique, a key is added(of that pixel value)
    // to a newly created hash map.
    // subsequently if the pixel value is not unique the value is incremented.
    // the value is the total size, or amount of pixels in the cell.
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
        return cellMap;
    }

    public WritableImage colorNoiseWhite(HashMap<Integer, Integer> hMap, HashMap<Integer, Integer> hMap2, int[] arr, int[] arr2, int percentNoiseReduction) {
        Image im = imageViewEdited1.getImage();
        WritableImage writableImage;
        writableImage = (WritableImage) im;
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        unionQuick(arr, imageViewEdited.getImage());
        unionQuick(redCellArray, imageViewEdited.getImage());
        unionQuick(arr2, imageViewEdited.getImage());
        unionQuick(whiteCellArray, imageViewEdited.getImage());

        // go through array checking if current the root of current pixel is present in the hashmap of cells, it isn't white and the total amount of
        // pixels(size of the bloodcell) is less than the amount user would like to clear. Once a pixel meet these criteria it is colored white.
        for (int y = 0; y < im.getHeight(); y++) {
            for (int x = 0; x < im.getWidth(); x++) {
                int pix = y * (int) im.getWidth() + x;
                if (Find.findIntArr(arr, pix) > -1 && hMap.containsKey(Find.findIntArr(arr, pix)) && (hMap.get(Find.findIntArr(arr, pix)) < percentNoiseReduction))
                    pixelWriter.setColor(x, y, Color.WHITE);
                if (Find.findIntArr(arr2, pix) > -1 && hMap2.containsKey(Find.findIntArr(arr2, pix)) && hMap2.get(Find.findIntArr(arr2, pix)) < percentNoiseReduction) {
                    pixelWriter.setColor(x, y, Color.WHITE);
                }
            }
        }
        unionQuick(redCellArray, imageViewEdited.getImage());
        unionQuick(whiteCellArray, imageViewEdited.getImage());
        return writableImage;
    }

    public void reduceNoise() {
        unionQuick(whiteCellArray, imageViewEdited.getImage());
        unionQuick(redCellArray, imageViewEdited.getImage());
        whiteCellMap = arrayToHashMap(whiteCellArray);
        redCellMap = arrayToHashMap(redCellArray);
        int noisePercent = (int) noiseSlider.getValue();
        WritableImage wrIm = colorNoiseWhite(redCellMap, whiteCellMap, redCellArray, whiteCellArray, noisePercent);
        imageViewEdited1.setImage(wrIm);
        triColCon.triColorImageView.setImage(wrIm);
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
                            Color color1 = Color.rgb(newColor, newColor, newColor);
                            pixelWriter.setColor(x, y, color1);
                        }
                    }
                    imageViewEdited.setImage(writableImage);
                }
        );
    }

    public void setCancelChanges() {
        Image freshImage = imageView.getImage();
        imageViewEdited.setImage(null);
        imageViewEdited.setImage(freshImage);
        imageViewEdited1.setImage(null);
        imageViewEdited1.setImage(freshImage);
        edImagePane.getChildren().clear();
        edImagePane1.getChildren().clear();
        cellCountDisplayLbl.setText("");
        cellsCounted = false;
        cellHoverLbl.setText("");
        redSlider.setValue(80);
        purpleSlider.setValue(35);
        noiseSlider.setValue(50);
        index = 0;
        cellCount = 0;
    }

    public void cancelChanges() {
        cancelChanges.setOnAction(e -> {
            setCancelChanges();
        });
    }

    public void setTriColorScene() {
        openTricolor.setOnAction(e -> {
            sStage.setScene(Main.triColorScene);
            sStage.show();
        });
    }

    public void imageMeta() {
        Image im = imageView.getImage();
        metaData.setText("File Name : " + im.getUrl() + "\nFile Size : " + fileSize + "\nPixels : " + (int) im.getWidth() + "x" + (int) im.getHeight());
        System.out.println(im.getUrl());
    }

    public void setImageSizeReduction() {
        imageSizeReduction.setOnAction(e -> {
            Image newIm = reduceIm(imageViewEdited.getImage());
            imageViewEdited.setImage(newIm);
        });
    }

    public Image reduceIm(Image im) {
        return new Image(im.getUrl(), 144, 144, false, false);
    }

    public void noiseReduction() {
        noiseBtn.setOnAction(e -> {
            reduceNoise();
        });
    }

    public void setUnionFindBtn() {
        unionFindBtn.setOnAction(e -> {
            unionQuick(redCellArray, imageViewEdited.getImage());
            System.out.println();
            unionQuick(whiteCellArray, imageViewEdited.getImage());
        });
    }

    public void numberRecsTricolorPane() {
        numberCells.setOnAction(e -> {
            if (!cellsSeqCount)
                numberRectanglesSequentially(edImagePane);
            else {
                edImagePane.getChildren().removeIf(num -> num instanceof Label);
                cellsSeqCount = false;
            }
        });
    }

    public void numberRecsEditedPane() {
        numberCells1.setOnAction(e -> {
            if (!cellsSeqCount1)
                numberRectanglesSequentially(edImagePane1);
            else {
                edImagePane1.getChildren().removeIf(num -> num instanceof Label);
                cellsSeqCount1 = false;
            }
        });
    }

    public void setCountCellsBtn() {
        countCellsBtn.setOnAction(e -> {
            try {
                if (!cellsCounted) {
                    int whiteCellCount = 0;
                    int redCellCount = 0;
                    whiteCellCount = countCells(whiteCellMap, noiseSlider.getValue());
                    countInCluster();
                    index = 0;
                    redCellCount = countCells(redCellMap, noiseSlider.getValue());
                    cellCountDisplayLbl.setText("WhiteCell count: " + whiteCellCount + ", RedCell count: " + redCellCount);
                    cellHoverLbl.setText("Hover pointer over a rectangle to see estimate.");
                    cellsCounted = true;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    //counts amount of cells in image
    public int countCells(HashMap<Integer, Integer> hmap, double percentNoiseReduction) {
        hmap.forEach((k, v) -> {
            if (v > percentNoiseReduction) {
                cellCount++;
            }
        });
        return cellCount;
    }

    public void recTriColor() {
        recTriColor.setOnAction(e -> {
            try {
                rectangle_a_fy(edImagePane1, alreadyDrawn);
            } catch (Exception e1) {
                displayWritableImage();
                reduceNoise();
                rectangle_a_fy(edImagePane1, alreadyDrawn);
            }
        });
    }

    public void displayWritableImage() {
        WritableImage writableImage = displayTricolor(imageView.getImage(), redCellArray, whiteCellArray, (int) redSlider.getValue(), (int) purpleSlider.getValue());
        triColCon.triColorImageView.setImage(writableImage);
        imageViewEdited1.setImage(writableImage);
    }

    public void setDrawRectangle() {
        rectangleBtn.setOnAction(e -> {
            try {
                rectangle_a_fy(edImagePane, alreadyDrawn2);
            } catch (Exception e1) {
                displayWritableImage();
                reduceNoise();
                rectangle_a_fy(edImagePane, alreadyDrawn2);
            }
        });
    }

    public void quit() {
        quit.setOnAction(e -> Platform.exit());
    }
}

