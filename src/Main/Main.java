package Main;


import Controller.HomeController;
import Controller.TriColorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public static Stage pStage, sStage;
    public static HomeController homeCon;
    public static TriColorController triColCon;
    public static Scene homeScene, triColorScene;

    @Override
    public void start(Stage pStage) throws Exception{
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("../fxml/mainHomeScreen.fxml"));
        Parent ml = mainLoader.load();
        homeCon = mainLoader.getController();
        homeScene = new Scene(ml);

        pStage.setTitle("Hello World");
        pStage.setScene(homeScene);
        pStage.show();

        sStage = new Stage();
        FXMLLoader triColorCon = new FXMLLoader(getClass().getResource("../fxml/triColorScene.fxml"));
        Parent triPar = triColorCon.load();
        triColCon = triColorCon.getController();
        triColorScene = new Scene(triPar);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
