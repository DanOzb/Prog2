import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.Objects;
import java.util.Optional;

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

    private Image graph;
    private ImageView view;

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
            //write a method to open graph europa.graph
        });

        //Save graph | menu item event
        save.setOnAction(event -> saveGraph());

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
            mapNewLocation();
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
            graph = new Image(file);
            view = new ImageView(graph);
            root.setBottom(new FlowPane(view));
            root.prefHeightProperty().bind(graph.heightProperty());
        } catch (FileNotFoundException e){
            System.err.println("Cause of file not found exception: " + e.getCause());
        }
    }

    private void saveGraph(){
        try(
                FileWriter file = new FileWriter("europa.graph");
                BufferedWriter writer = new BufferedWriter(file)
                ) {
            //save graph in europa.graph

        } catch (FileNotFoundException e) {
            System.err.println("Cause of file not found exception: " + e.getCause());
        } catch (IOException e) {
            System.err.println("Cause of IO exception: " + e.getCause());
        }
    }

    private void mapNewLocation(){
        root.getScene().setCursor(new ImageCursor(graph, graph.getWidth(), graph.getHeight()));
        btnNewPlace.setDisable(true);
        root.getScene().setCursor(Cursor.CROSSHAIR);

        root.setOnMouseClicked(event -> {
            String place = "";
            if(!(place = openWindow()).isEmpty()){
                root.getScene().setCursor(Cursor.DEFAULT);
                btnNewPlace.setDisable(false);
                Circle circle = new Circle(event.getX(), event.getY(), 10, Color.RED);
                Text label = new Text(place);
                label.setTranslateX(event.getX() + 2);
                label.setTranslateY(event.getY() + 15);
                label.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD,10));
                root.getChildren().add(circle);
                root.getChildren().add(label);
                root.setOnMouseClicked(Event::consume);
            }
        });
    }

    private String openWindow(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Name");
        dialog.setContentText("Name of place: ");
        Optional<String> result = dialog.showAndWait();
        String temp = "";
        if(result.isPresent()){
            temp = result.get();
        }
        return temp;
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
