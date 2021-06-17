import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

class chooser {
    private Stage stage;
    private Label label;
    private Button close;
    private ColorPicker cp;
    
    public void about(MenuBar bar){
        stage= new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Image image1 = new Image(getClass().getResourceAsStream("stg.png"));

        stage.setTitle("Pick Color");
        stage.setOnCloseRequest(event ->{
                    event.consume();
                    close();
                }
        );
        cp = new ColorPicker(Color.GOLD);
        label= new Label();
        close = new Button("Apply");

        VBox box = new VBox(3);
        box.getChildren().addAll(cp,close);
        box.setAlignment(Pos.CENTER);

        close.setOnAction(event -> {
            bar.setBackground(new Background(new BackgroundFill(cp.getValue(), CornerRadii.EMPTY, Insets.EMPTY)));
            close();
        });

        Scene scene = new Scene(box,200,100);
        scene.getStylesheets().add("chooser.css");

        stage.setResizable(false);
        stage.getIcons().add(image1);
        stage.setScene(scene);
        stage.showAndWait();
    }
    private void close(){
        init ();
        stage.close();
    }
    public void init(){
        Preferences pref=Preferences.userNodeForPackage (this.getClass ());
        pref.put ("color",cp.getValue ().toString ());

        try {
            pref.exportNode (new FileOutputStream ("conf.txt"));
            pref.flush ();
        } catch (IOException e) {
            e.printStackTrace ( );
        } catch (BackingStoreException e) {
            e.printStackTrace ( );
        }
    }
}
