import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
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

import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

//ändrade graph från string till location.
//i vpl: node2 blir klickad istället för node1

public class PathFinder extends Application {
    private final ListGraph<Location> graph = new ListGraph<>();
    private final BorderPane root = new BorderPane();
    private final Pane field = new Pane();

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

    private Image mapImage = new Image("file:europa.gif");
    private final ImageView view = new ImageView(mapImage);
    private boolean saved = true;
    private final List<Location> placesClicked = new LinkedList<>();

    @Override
    public void start(Stage stage) {
        field.setId("outputArea");
        menuBar.setId("menu");
        menu.setId("menuFile");
        newMap.setId("menuNewMap");
        open.setId("menuOpenFile");
        save.setId("menuSaveFile");
        saveImage.setId("menuSaveImage");
        exit.setId("menuExit");
        btnFindPath.setId("btnFindPath");
        btnChangeConnection.setId("btnChangeConnection");
        btnNewPlace.setId("btnNewPlace");
        btnShowConnection.setId("btnShowConnection");
        btnNewConnection.setId("btnNewConnection");

        //header pane
        VBox vbox = setMenuBar(menuBar);
        vbox.prefWidthProperty().bind(stage.widthProperty());

        FlowPane header = new FlowPane(vbox);
        header.setAlignment(Pos.TOP_LEFT);
        header.setMinWidth(600);

        //buttons pane
        HBox hbox = new HBox(btnFindPath, btnShowConnection, btnNewPlace, btnNewConnection, btnChangeConnection);
        hbox.setSpacing(10);
        FlowPane buttons = new FlowPane(hbox);
        buttons.setAlignment(Pos.TOP_CENTER);

        //create new map | menu item event
        newMap.setOnAction(e -> {
            if(view.getImage() != null)
                saved = false;
            root.prefHeightProperty().bind(mapImage.heightProperty());
            stage.setHeight(root.getPrefHeight());
            stage.setY(0);
            clearMap();
            view.setImage(mapImage);
            field.getChildren().add(view);
            saved = false;
        });

        //open graph | menu item event
        open.setOnAction(e -> {
            root.prefHeightProperty().bind(mapImage.heightProperty());
            stage.setHeight(root.getPrefHeight());
            stage.setY(0);
            openGraph();
        });

        //Save graph | menu item event
        save.setOnAction(e ->{
            saveMap();
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
        exit.setOnAction(e -> {
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });


        //Create new place | button event
        btnNewPlace.setOnAction(e -> mapNewLocation());

        btnNewConnection.setOnAction(e -> setNewConnectionDialog());

        btnShowConnection.setOnAction(e -> {
            if(placesClicked.size() == 2)
                connectionBox(false, false);
        });

        btnChangeConnection.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if(placesClicked.size() == 2) {
                if(graph.getEdgeBetween(placesClicked.get(0), placesClicked.get(1)) == null){
                    alert.show();
                } else {
                    connectionBox(false, true);
                }
            } else {
                alert.show();
            }
        });

        btnFindPath.setOnAction(e -> {
            if(placesClicked.size() == 2)
                findPath();
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Press two locations");
                alert.show();
            }
        });

        //output area
        field.getChildren().add(view);

        //set borderpane
        root.setTop(header);
        root.setCenter(buttons);
        view.setImage(null);
        root.setBottom(field);
        root.prefWidthProperty().bind(header.widthProperty());

        //set scene
        Scene scene = new Scene(root);
        stage.setTitle("PathFinder");
        stage.setScene(scene);
        stage.show();

        scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    private VBox setMenuBar(MenuBar menuBar){
        //set vbox and add menubar
        VBox vbox = new VBox(menuBar);

        //put menu items in menu, then menubar
        menu.getItems().addAll(newMap, open, save, saveImage, exit);
        menuBar.getMenus().addAll(menu);

        return vbox;
    }

    //creates new map/image
    private void clearMap() {
        if(checkSaved()){
            for(Location node : graph.getNodes()){
                graph.remove(node);
            }
            field.getChildren().clear();
            placesClicked.clear();
        }
    }

    //saves map | not done
    private void saveMap(){
        try(
                FileWriter file = new FileWriter("europa.graph");
                BufferedWriter writer = new BufferedWriter(file)
                ) {
            //save graph in europa.graph
            writer.write(mapImage.getUrl());
            writer.newLine();
            for(Location node : graph.getNodes()){
                writer.write(node.toString() + ";" + node.getCenterX() + ";" + node.getCenterY() + ";");
            }
            writer.newLine();
            String graphText = graph.toString();
            StringBuilder fileText = new StringBuilder();
            Scanner scanner = new Scanner(graphText);
            while(scanner.hasNextLine()){
                String[] items = scanner.nextLine().split(";");
                if(items.length > 0){
                    String node = items[0];
                    for(int i = 1; i < items.length; i++){
                        //System.out.println(Arrays.toString(items[i].split(" ")));
                        String destination = items[i].split(" ")[1];
                        String transportation = items[i].split(" ")[3];
                        String weight = items[i].split(" ")[5];
                        fileText.append(node).append(";").append(destination).append(";").append(transportation).append(";").append(weight).append("\n");
                    }
                }
            }
            writer.write(fileText.toString());
        } catch (FileNotFoundException e) {
            System.err.println("Cause of file not found exception: " + e.getCause());
        } catch (IOException e) {
            System.err.println("Cause of IO exception: " + e.getCause());
        }
    }

    //maps new location in graph image | not done (check if circles are connected)
    private void mapNewLocation(){
        field.getScene().setCursor(new ImageCursor(mapImage, mapImage.getWidth(), mapImage.getHeight()));
        btnNewPlace.setDisable(true);
        field.getScene().setCursor(Cursor.CROSSHAIR);

        //mouse click event on root pane
        field.setOnMouseClicked(event -> {
            String place;
            if(!(place = openWindow()).isEmpty()){
                //System.out.println(place);
                createLocation(event.getX(), event.getY(), place);
            }
            btnNewPlace.setDisable(false);
            field.getScene().setCursor(Cursor.DEFAULT);
            field.setOnMouseClicked(Event::consume);
        });
    }

    //set connection
    private void setNewConnectionDialog(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (placesClicked.size() == 2) {
            if(graph.getEdgeBetween(placesClicked.get(0), placesClicked.get(1)) != null){
                alert.setHeaderText("There is already a connection between the selected places!!");
                alert.show();
            } else{
                connectionBox(true, true);
            }
        } else {
            alert.setHeaderText("Two places must be selected!!");
            alert.show();
        }
    }

    private void setConnection(String nameText, String timeText, boolean newPlace){
        if(nameText.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Name can not be empty");
            alert.show();
        } else if(!timeText.matches("-?\\d+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Time must be a number");
            alert.show();
        } else {
            if(newPlace){
                if(graph.getEdgeBetween(placesClicked.get(0), placesClicked.get(1)) == null){
                    //om new connection vald och det inte finns en connection
                    graph.connect(placesClicked.get(0), placesClicked.get(1), nameText, Integer.parseInt(timeText));

                    double startX = placesClicked.get(0).getCenterX();
                    double startY = placesClicked.get(0).getCenterY();
                    double endX = placesClicked.get(1).getCenterX();
                    double endY = placesClicked.get(1).getCenterY();
                    createLine(startX, startY, endX, endY);
                }
            } else{
                //om change connection vald
                if(graph.getEdgeBetween(placesClicked.get(0), placesClicked.get(1)) != null){
                    graph.setConnectionWeight(placesClicked.get(0), placesClicked.get(1), Integer.parseInt(timeText));
                }
            }
        }
    }

    //opens text dialog for naming new place
    private String openWindow(){
        Dialog<ButtonType> dialog = new Dialog<>();
        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField();

        dialog.getDialogPane().getChildren().add(nameField);
        dialog.getDialogPane().setContent(nameField);
        dialog.setTitle("Name");
        dialog.setHeaderText("Name of place:");
        dialog.getDialogPane().setPadding(new Insets(20, 150, 10, 10));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        //System.out.println(dialog.getDialogPane().getChildrenUnmodifiable());
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent()){
            dialog.close();
            return nameField.getText();
        } else {
            dialog.close();
            return "";
        }
    }

    //create custom button class
    static class MyButton extends Button {

        MyButton(String item) {
            super(item);
            setPadding(new Insets(10));
            setAlignment(Pos.CENTER);
        }
    }

    //creates connection dialog box for multiple buttons
    private void connectionBox(boolean nameEnable, boolean timeEnable){
        String transportation = "";
        String transportationTime = "";

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Connection");

        Edge<Location> edge = graph.getEdgeBetween(placesClicked.get(0), placesClicked.get(1));
        if(edge != null){
            transportation = edge.getName();
            transportationTime = Integer.toString(edge.getWeight());
        }

        dialog.setHeaderText("Connection from " + placesClicked
                .get(0).name+ " to " + placesClicked.get(1).name);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        root.getChildren().add(gridPane);
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

        boolean newPlace = nameEnable && timeEnable;
        //om change connection vald eller new connection annars visa tid och transportation
        if(timeEnable)
            dialog.showAndWait().ifPresent(e -> setConnection(nameField.getText(), timeField.getText(), newPlace));
        else
            dialog.show();
    }

    private void findPath(){
        Alert box = new Alert(Alert.AlertType.INFORMATION);
        box.setHeaderText("The path from " + placesClicked.get(0).name + " to " + placesClicked.get(1).name + ":");
        StringBuilder context = new StringBuilder();
        int total = 0;
        for(Edge<Location> edge : graph.getPath(placesClicked.get(0), placesClicked.get(1))){
            context.append(edge.toString()).append("\n");
            total += edge.getWeight();
        }
        box.setContentText(context + "Total: " + total);
        box.show();
    }

    private void openGraph() {
        clearMap();
        try {
            FileReader rd = new FileReader("europa.graph");
            BufferedReader reader = new BufferedReader(rd);
            mapImage = new Image(reader.readLine());
            view.setImage(mapImage);
            field.getChildren().add(view);

            String[] nodes = reader.readLine().split(";");
            for (int i = 0; i < nodes.length; i += 3) {

                String name = nodes[i];
                double coordinate1 = Double.parseDouble(nodes[i + 1]);
                double coordinate2 = Double.parseDouble(nodes[i + 2]);
                createLocation(coordinate1, coordinate2, name);
            }

            String read;
            double startX = 0;
            double startY = 0;
            double endX = 0;
            double endY = 0;
            while((read = reader.readLine()) != null){
                String[] items = read.split(";");
                String from = items[0];
                String destination = items[1];
                String transportation = items[2];
                int distance = Integer.parseInt(items[3]);

                Location node1 = checkLocationExists(from);
                Location node2 = checkLocationExists(destination);

                if(graph.getEdgeBetween(node1, node2) == null) {
                    graph.connect(node1, node2, transportation, distance);

                    startX = node1.getCenterX();
                    startY = node1.getCenterY();
                    endX = node2.getCenterX();
                    endY = node2.getCenterY();
                    createLine(startX, startY, endX, endY);
                }
            }
            reader.close();
            rd.close();

        } catch (FileNotFoundException e) {
            System.err.println("File not found at");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file");
            e.printStackTrace();
        }

    }

    private Location checkLocationExists(String name){
        for(Location node : graph.getNodes()){
            if(Objects.equals(node.toString(), name))
                return node;
        }
        return null;
    }

    private void createLocation(double x, double y, String name){
        Location location = new Location(name);
        Text label = new Text();
        location.setFill(Color.BLUE);
        location.setRadius(10);
        location.setId(name);
        location.setCenterX(x);
        location.setCenterY(y);
        location.setLabel(label);
        label.setText(name);
        label.setTranslateX(x + 2);
        label.setTranslateY(y + 15);
        label.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD,10));
        label.setDisable(true);
        //System.out.println(label.getText());
        field.getChildren().addAll(location, label);
        graph.add(location);

        //mouse click event on circles/places
        location.setOnMouseClicked(e -> {
            if(placesClicked.contains(location)){
                placesClicked.remove(location);
                location.setFill(Color.BLUE);
            }
            else if(placesClicked.size() != 2){
                placesClicked.add(location);
                location.setFill(Color.RED);
            }
        });
    }

    private boolean checkSaved(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Warning!");
        alert.setContentText("Unsaved changes, continue anyway?");
        //if ok, save and continue 
        //if cancel, keep everything and close alert
        if(!saved){
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                saved = true;
            }
            return result.get() == ButtonType.OK;
        }
        return true;
    }

    private void closeWindowEvent(WindowEvent event){
        if(!checkSaved()){
            event.consume();
        }
    }

    private void createLine(double startX, double startY, double endX, double endY){
        Line connection = new Line(startX, startY, endX, endY);
        connection.setStroke(Color.BLACK);
        connection.setStrokeWidth(4);
        connection.setDisable(true);
        field.getChildren().add(connection);
    }

    static class Location extends Circle{
        private final String name;
        private Text label;
        Location(String name){
            this.name = name;
            this.label = new Text();
            this.label.setText(name);
        }
        public void setLabel(Text label){
            this.label = label;
        }

        @Override
        public String toString(){
            return name;
        }
    }

}


