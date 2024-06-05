import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

public class PathFinder extends Application {
    private final ListGraph<String> graph = new ListGraph<>();
    private final Map<String, Circle> locations = new HashMap<>();
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

    private Image mapImage;
    private boolean saved = true;
    private final List<Circle> placesClicked = new ArrayList<>();
    private final List<String> namesClicked = new ArrayList<>();

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
            checkSaved();
            CreateNewMap();
            saved = false;
            stage.setHeight(root.getPrefHeight());
            stage.setY(0);
        });

        //open graph | menu item event
        open.setOnAction(event -> {
            checkSaved();
            openGraph();
            stage.setHeight(root.getPrefHeight());
            stage.setY(0);
        });

        //Save graph | menu item event
        save.setOnAction(event ->{
            saveMap();
            saved = true;
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
            checkSaved();
        });


        //Create new place | button event
        btnNewPlace.setOnAction(event -> {
            mapNewLocation();
            saved = false;
        });

        btnNewConnection.setOnAction(event -> {
            setNewConnectionDialog();
            saved = false;
        });

        btnShowConnection.setOnAction(event -> {
            if(placesClicked.size() == 2)
                connectionBox(false, false);
        });

        btnChangeConnection.setOnAction(event -> {
            if(placesClicked.size() == 2) {
                connectionBox(false, true);
                saved = false;
            }
        });

        btnFindPath.setOnAction(event -> {
            if(placesClicked.size() == 2)
                findPath();
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Press two locations");
                alert.show();
            }
        });



        //set borderpane
        root.setTop(header);
        root.setCenter(buttons);

        //set scene
        Scene scene = new Scene(root);
        stage.setTitle("PathFinder");
        stage.setScene(scene);
        stage.show();
        scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
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

    //creates new map/image
    private void CreateNewMap(){
        try{
            FileInputStream file = new FileInputStream("europa.gif");
            mapImage = new Image(file);
            ImageView view = new ImageView(mapImage);
            root.setBottom(new FlowPane(view));
            root.prefHeightProperty().bind(mapImage.heightProperty());
        } catch (FileNotFoundException e){
            System.err.println("Cause of file not found exception: " + e.getCause());
        }
    }

    //saves map | not done
    private void saveMap(){
        try(
                FileWriter file = new FileWriter("europa.graph");
                BufferedWriter writer = new BufferedWriter(file)
                ) {
            //save graph in europa.graph
            writer.write("file:europa.graph");
            writer.newLine();
            for(String key : locations.keySet()){
                writer.write(key + ";" + locations.get(key).getCenterX() + ";" + locations.get(key).getCenterY() + ";");
            }
            writer.newLine();
            writer.write(graph.toString());
        } catch (FileNotFoundException e) {
            System.err.println("Cause of file not found exception: " + e.getCause());
        } catch (IOException e) {
            System.err.println("Cause of IO exception: " + e.getCause());
        }
    }

    //maps new location in graph image | not done (check if circles are connected)
    private void mapNewLocation(){
        root.getScene().setCursor(new ImageCursor(mapImage, mapImage.getWidth(), mapImage.getHeight()));
        btnNewPlace.setDisable(true);
        root.getScene().setCursor(Cursor.CROSSHAIR);

        //mouse click event on root pane
        root.setOnMouseClicked(event -> {
            String place = "";
            if(!(place = openWindow()).isEmpty()){
                createCircle(event.getX(), event.getY(), place);
            }
            btnNewPlace.setDisable(false);
            root.getScene().setCursor(Cursor.DEFAULT);
            root.setOnMouseClicked(Event::consume);
        });
    }

    //set connection
    private void setNewConnectionDialog(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (placesClicked.size() == 2) {
            connectionBox(true, true);
        } else {
            //customize later
            alert.show();
        }
    }

    private void setNewConnection(Pair<String, String> pair){
        String nameText = pair.getKey();
        String timeText = pair.getValue();
        graph.connect(namesClicked.getFirst(), namesClicked.getLast(), nameText, Integer.parseInt(timeText));
        Line connection = new Line();
        connection.setStartX(placesClicked.getFirst().getCenterX());
        connection.setStartY(placesClicked.getFirst().getCenterY());
        connection.setEndX(placesClicked.getLast().getCenterX());
        connection.setEndY(placesClicked.getLast().getCenterY());
        connection.setStroke(Color.BLACK);
        connection.setStrokeWidth(4);

        root.getChildren().add(connection);
    }

    //opens text dialog for naming new place
    private String openWindow(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("");
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

    //creates connection dialog box for multiple buttons
    private void connectionBox(boolean nameEnable, boolean timeEnable){
        String transportation = "";
        String transportationTime = "";

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Connection");

        Edge<String> edge = graph.getEdgeBetween(namesClicked.getFirst(), namesClicked.getLast());
        if(edge != null){
            transportation = edge.getName();
            transportationTime = Integer.toString(edge.getWeight());
        }

        dialog.setHeaderText("Connection from " + namesClicked.getFirst()+ " to " + namesClicked.getLast());

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        TextField nameField = new TextField();
        if(!nameEnable){
            nameField.setText(transportation);
            nameField.setEditable(false);
        }
        nameField.setPromptText("Name: ");
        TextField timeField = new TextField();
        if(!timeEnable){
            timeField.setText(transportationTime);
            timeField.setEditable(false);
        }
        timeField.setPromptText("Time: ");


        gridPane.add(nameField, 1, 0);
        gridPane.add(timeField, 1, 1);

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                nameField.getText();
                timeField.getText();
                return new Pair<>(nameField.getText(), timeField.getText());
            }
            return null;
        });

        if(!graph.pathExists(namesClicked.getFirst(), namesClicked.getLast()))
            dialog.showAndWait().ifPresent(this::setNewConnection);
        else
            dialog.show();

        resetCircles();
    }

    private void resetCircles(){
        placesClicked.getFirst().setFill(Color.RED);
        placesClicked.getLast().setFill(Color.RED);
        placesClicked.removeFirst();
        placesClicked.removeLast();
        namesClicked.removeFirst();
        namesClicked.removeLast();
    }

    private void findPath(){
        Alert box = new Alert(Alert.AlertType.INFORMATION);
        box.setHeaderText("The path from " + namesClicked.getFirst() + " to " + namesClicked.getLast() + ":");
        String context = "";
        int total = 0;
        for(Edge<String> edge : graph.getPath(namesClicked.getFirst(), namesClicked.getLast())){
            context += edge.toString() + "\n";
            total += edge.getWeight();
        }
        box.setContentText(context + "\nTotal: " + total);
        box.show();
    }

    private void openGraph() {
        CreateNewMap();
        try {
            FileReader rd = new FileReader("europa.graph");
            BufferedReader reader = new BufferedReader(rd);
            reader.readLine();

            String[] nodes = reader.readLine().split(";");
            for (int i = 0; i < nodes.length; i += 3) {

                String name = nodes[i];
                double coordinate1 = Double.parseDouble(nodes[i + 1]);
                double coordinate2 = Double.parseDouble(nodes[i + 2]);

                createCircle(coordinate1, coordinate2, name);
            }

            String read;
            while((read = reader.readLine()) != null){
                String[] items = read.split(";");
                String from = items[0];
                String destination = items[1];
                String transportation = items[2];
                int distance = Integer.parseInt(items[3]);
                if(graph.getEdgeBetween(from, destination) == null)
                    graph.connect(from, destination, transportation, distance);
            }
            reader.close();
            rd.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createCircle (double x, double y, String name){
        Circle circle = new Circle(x, y, 10, Color.RED);
        Text label = new Text(name);
        label.setTranslateX(x + 2);
        label.setTranslateY(y + 15);
        label.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD,10));
        root.getChildren().add(circle);
        root.getChildren().add(label);
        graph.add(name);
        locations.put(name, circle);

        //mouse click event on circles/places
        circle.setOnMouseClicked(e -> {
            if(placesClicked.contains(circle)){
                placesClicked.remove(circle);
                namesClicked.remove(label.getText());
                circle.setFill(Color.RED);
            }
            else if(!(placesClicked.size() == 2)){
                placesClicked.add(circle);
                namesClicked.add(label.getText());
                circle.setFill(Color.BLUE);
            }
        });
    }

    private boolean checkSaved(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning!");
        alert.setHeaderText("Unsaved changes, continue anyway?");
        //if ok, delete contents of file. close alert
        //if cancel, keep everything and close alert
        if(!saved){
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                saved = true;
                return true;
            }
            return false;
        }
        return true;
    }

    private void deleteFile(){
        try{
            new FileWriter("europa.graph", false).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeWindowEvent(WindowEvent event){
        if(checkSaved()){
            deleteFile();
        } else {
            event.consume();
        }
    }

}
