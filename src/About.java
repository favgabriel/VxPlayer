import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

class About {
    private Stage stage;
    private Label label;
    public void about(){
        stage= new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Image image1 = new Image(getClass().getResourceAsStream("stg.png"));

        stage.setTitle("About Vx player");
        stage.setOnCloseRequest(event ->{
            event.consume();
            close();
        }
        );
        Hyperlink link = new Hyperlink ();
        link.setText ("Gabriel40670@outlook.com");
        TextArea label = new TextArea ();
        label.setText(" Vx Player v 1.0" +
                "\n\n copywrite (c) 2019 \n" +
                " Developed by Favour Gabriel ");
        label.appendText ("\n\nAll rights regrading this software are reserved");
        label.setFont(new Font("georgia",12));
        label.setEditable (false);

        VBox box = new VBox(3);
        box.getChildren().addAll(label);
        box.setAlignment(Pos.CENTER);
        Scene scene = new Scene(box);
        scene.getStylesheets().add("chooser.css");
        stage.setResizable(false);
        stage.getIcons().add(image1);
        stage.setScene(scene);
        stage.showAndWait();
    }
    private void close(){
        stage.close();
    }
}
