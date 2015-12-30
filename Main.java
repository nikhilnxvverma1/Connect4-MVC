import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by NikhilVerma on 02/10/15.
 */
public class Main extends Application{
    private static final String MAIN_VIEW_LOCATION = "gameplay.fxml";
    private static final String STYLESHEET = "cool.css";
    public static final double WIDTH = 700;
    public static final double HEIGHT = 500;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource(MAIN_VIEW_LOCATION));
        Parent root= (Parent) fxmlLoader.load();
        root.getStylesheets().add(STYLESHEET);
        Scene scene=new Scene(root,WIDTH,HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();//layout containers wont be initialized until primary stage is shown
        MainController controller = (MainController) fxmlLoader.getController();
        controller.init();
    }
}
