package Controller;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HomeControllerTest {
    public Image im;
    public HomeController hm;
    public ImageView imageViewEdited, triColorImageView, imageViewEdited1;
    public int[] redCellArray, whiteCellArray;
    public Slider redSlider, purpleSlider;
    public TriColorController triColCon;
    public HashMap<Integer, Integer> redCellMap, whiteCellMap;
    public JFXPanel jfxPanel;
    public File file;
    //    int redAmount, purpleAmount;


    @Before
    public void setUp() throws Exception {
        jfxPanel = new JFXPanel();
        file = new File("/home/dimdakis/Pictures/bloodCellsAssignment1/bloodCells4.jpg");
        im = new Image(file.toURI().toString(), 400, 400, false, false);
        hm = new HomeController();
        triColCon = new TriColorController();
        imageViewEdited = new ImageView();
        imageViewEdited.setImage(im);
        triColorImageView = new ImageView();
        imageViewEdited1 = new ImageView();
        redCellArray = new int[400 * 400];
        whiteCellArray = new int[400 * 400];
        redSlider = new Slider();
        purpleSlider = new Slider();
        redSlider.setValue(80);
        purpleSlider.setValue(35);
    }

    @After
    public void tearDown() throws Exception {
        jfxPanel = null;
        file = null;
        im = null;
        hm = null;
        triColCon = null;
        imageViewEdited = imageViewEdited1 = null;
        triColorImageView = null;
        redCellArray = whiteCellArray = null;
        redSlider = purpleSlider = null;
    }

    @Test
    public void reduceIm() {
        Image newIm = hm.reduceIm(im);
        assertEquals(144, newIm.getWidth(), 0);
    }

    @Test
    public void arrayToHashMapRedCellMapSize() {
        imageViewEdited.setImage(im);
        redSlider.setValue(80);
        purpleSlider.setValue(35);
        hm.displayTricolor(im, redCellArray, whiteCellArray, (int) redSlider.getValue(), (int) purpleSlider.getValue());
        hm.unionQuick(redCellArray, im);
        hm.unionQuick(whiteCellArray, im);
        HashMap redCellMap = hm.arrayToHashMap(redCellArray);
        assertEquals(105, redCellMap.size());
    }

    @Test
    public void arrayToHashMapWhiteCellMapSize() {
        imageViewEdited.setImage(im);
        redSlider.setValue(80);
        purpleSlider.setValue(35);
        hm.displayTricolor(im, redCellArray, whiteCellArray, (int) redSlider.getValue(), (int) purpleSlider.getValue());
        hm.unionQuick(redCellArray, im);
        hm.unionQuick(whiteCellArray, im);
        whiteCellMap = hm.arrayToHashMap(whiteCellArray);
        assertEquals(36, whiteCellMap.size());
    }

    @Test
    public void displayTricolorCreatesArrayRedArray() {
        hm.displayTricolor(im, redCellArray, whiteCellArray, (int) redSlider.getValue(), (int) purpleSlider.getValue());
        assertEquals(redCellArray.length, 400 * 400);
    }
    @Test
    public void displayTricolorCreatesArrayWhiteArray() {
        hm.displayTricolor(im, redCellArray, whiteCellArray, (int) redSlider.getValue(), (int) purpleSlider.getValue());
        assertEquals(whiteCellArray.length, 400 * 400);
    }

    @Test
    public void displayTricolorReturnsWritableImage() {
        WritableImage wi = hm.displayTricolor(im, redCellArray, whiteCellArray, (int) redSlider.getValue(), (int) purpleSlider.getValue());
        assertTrue(wi instanceof WritableImage);
    }
}