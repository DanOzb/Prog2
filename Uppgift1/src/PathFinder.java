import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;

public class PathFinder extends Application {

    private final BorderPane root = new BorderPane();

    private final MenuBar menuBar = new MenuBar();

    private final MyButton btnFindPath = new MyButton("Find Path");
    private final MyButton btnShowConnection = new MyButton("Show Connection");
    private final MyButton btnNewPlace = new MyButton("New Place");
    private final MyButton btnNewConnection = new MyButton("New Connection");
    private final MyButton btnChangeConnection = new MyButton("Change Connection");

    private final Menu menu = new Menu("File");
    private final MenuItem newMap = new MenuItem("New Map");
    private final MenuItem open = new MenuItem("Open");
    private final MenuItem save = new MenuItem("Save");
    private final MenuItem saveImage = new MenuItem("Save Image");
    private final MenuItem exit = new MenuItem("Exit");

    @Override
    public void start(Stage stage) {
        //header pane
        VBox vBox = setMenuBar(menuBar);
        vBox.prefWidthProperty().bind(stage.widthProperty());

        FlowPane header = new FlowPane(vBox);
        header.setAlignment(Pos.TOP_LEFT);

        //buttons pane
        HBox hBox = new HBox(btnFindPath, btnShowConnection, btnNewPlace, btnNewConnection, btnChangeConnection);
        hBox.setSpacing(10);
        FlowPane buttons = new FlowPane(hBox);
        buttons.setAlignment(Pos.TOP_CENTER);

        //create new map | menu item event
        newMap.setOnAction(event -> {
            CreateNewMap();
            stage.setHeight(root.getPrefHeight());
            stage.setY(0);
        });

        //open graph | menu item event
        open.setOnAction(event -> {
            //skriv en metod för att öppna kartan från europa.graph
        });

        //Save graph | menu item event
        save.setOnAction(event -> {
            saveGraph();
        });

        //save snapshot menu | item event
        saveImage.setOnAction(event -> {
            WritableImage screenShot = stage.getScene().snapshot(null);
            File file = new File("capture.png");
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(screenShot, null), "png", file);
            } catch (IOException e) {
                System.err.println("Something went wrong in Image io write");
            }
        });

        //exit application | menu item event
        exit.setOnAction(event -> {

        });


        //Create new place | button event
        btnNewPlace.setOnAction(event ->{

        });

        //set borderpane
        root.setTop(header);
        root.setCenter(buttons);

        //set scene
        Scene scene = new Scene(root);
        stage.setTitle("PathFinder");
        stage.setScene(scene);
        stage.show();
    }

    private VBox setMenuBar(MenuBar menuBar){
        //set vbox and add menubar
        VBox vBox = new VBox(menuBar);

        //set menu id's
        menuBar.setId("menu");
        menu.setId("menuFile");
        newMap.setId("menuNewMap");
        open.setId("menuOpen");
        save.setId("menuSave");
        saveImage.setId("menuSaveImage");
        exit.setId("menuExit");

        //put menu items in menu, then menubar
        menu.getItems().addAll(newMap, open, save, saveImage, exit);
        menuBar.getMenus().addAll(menu);

        return vBox;
    }

    private void CreateNewMap(){
        try{
            FileInputStream file = new FileInputStream("europa.gif");
            Image image = new Image(file);
            ImageView view = new ImageView(image);
            root.setBottom(new FlowPane(view));
            root.prefHeightProperty().bind(image.heightProperty());
        } catch (FileNotFoundException e){
            System.err.println("Cause of file not found exception: " + e.getCause());
        }
    }

    private void saveGraph(){
        try(
                FileWriter file = new FileWriter("europa.graph");
                BufferedWriter writer = new BufferedWriter(file);
                ) {
            //save graph in europa.graph

        } catch (FileNotFoundException e) {
            System.err.println("Cause of file not found exception: " + e.getCause());
        } catch (IOException e) {
            System.err.println("Cause of IO exception: " + e.getCause());
        }
    }

    private void mapNewLocation(){
        
    }

    //create custom button class
    static class MyButton extends Button {

        MyButton(String item) {
            super(item);
            setPadding(new Insets(10));
            setAlignment(Pos.CENTER);
            setId("btn" + item.replaceAll(" ", ""));
        }
    }

}
