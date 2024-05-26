import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PathFinder extends Application {

    private final MenuBar menuBar = new MenuBar();

    private final MyButton btnFindPath = new MyButton("Find Path");
    private final MyButton btnShowConnection = new MyButton("Show Connection");
    private final MyButton btnNewPlace = new MyButton("New Place");
    private final MyButton btnNewConnection = new MyButton("New Connection");
    private final MyButton btnChangeConnection = new MyButton("Change Connection");


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

        //set borderpane
        BorderPane bp = new BorderPane();
        bp.setTop(header);
        bp.setCenter(buttons);

        //set scene
        Scene scene = new Scene(bp);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setTitle("PathFinder");
        stage.setScene(scene);
        stage.show();
    }

    private VBox setMenuBar(MenuBar menuBar){
        //set vbox and add menubar
        VBox vBox = new VBox(menuBar);

        //set menu items
        Menu menu = new Menu("File");
        MenuItem newMap = new MenuItem("New Map");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem saveImage = new MenuItem("Save Image");
        MenuItem exit = new MenuItem("Exit");

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
    };

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
