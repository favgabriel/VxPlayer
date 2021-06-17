
    import javafx.application.Application;
    import javafx.application.Platform;
    import javafx.beans.Observable;
    import javafx.beans.property.DoubleProperty;
    import javafx.collections.MapChangeListener;
    import javafx.fxml.Initializable;
    import javafx.geometry.Insets;
    import javafx.geometry.Pos;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.Menu;
    import javafx.scene.control.MenuBar;
    import javafx.scene.control.MenuItem;
    import javafx.scene.control.TextArea;
    import javafx.scene.effect.DropShadow;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.KeyCode;
    import javafx.scene.input.MouseButton;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.*;
    import javafx.scene.media.Media;
    import javafx.scene.media.MediaException;
    import javafx.scene.media.MediaPlayer;
    import javafx.scene.media.MediaView;
    import javafx.scene.paint.Color;
    import javafx.scene.text.Font;
    import javafx.stage.FileChooser;
    import javafx.stage.Modality;
    import javafx.stage.Stage;
    import javafx.stage.StageStyle;
    import javafx.util.Duration;

    import javax.imageio.ImageIO;
    import java.awt.*;
    import java.awt.image.BufferedImage;
    import java.io.File;
    import java.io.IOException;
    import java.net.URL;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.Objects;
    import java.util.ResourceBundle;
    import java.util.prefs.Preferences;

    public class Main extends Application implements Initializable {
        private Button play;
        private Slider volume;
        private Slider time;
        private MediaPlayer player;
        private Media media;
        private MediaView view;
        private DoubleProperty width;
        private DoubleProperty height;
        private MenuBar bar;
        private MenuItem clear;
        private Menu recent;

        private Label timelabel;

        private CheckBox mute;

        private Stage primaryStage;
        private ImageView v;

        private ContextMenu pop;
        private MenuItem screen;
        private MenuItem popmute;

        private Duration duration;
        private VBox vBox;
        private Scene scene;

        private ArrayList<MenuItem> list;
        private Color value;
        MediaPlayer.Status status;

        private Alert alert;
        private TextArea area;


        private final About a= new About();

        private final chooser c = new chooser();
        //private FileWriter writer;

        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage Stage) {
            primaryStage = new Stage();
            primaryStage.setTitle("Vx Player");
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.setOnCloseRequest (event -> {
                System.out.println ("exiting" );
                primaryStage.close ();
                System.exit (0);
            });

            //settin error
            alert= new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText (null);
            alert.initStyle (StageStyle.UTILITY);
            area=new TextArea ();
            area.setEditable (false);
            area.setWrapText (true);
            area.setMaxSize (Double.MAX_VALUE,Double.MAX_VALUE);
            GridPane.setVgrow (area,Priority.ALWAYS);
            GridPane.setHgrow (area,Priority.ALWAYS);
            GridPane gridPane = new GridPane ();
            gridPane.setMaxWidth (Double.MAX_VALUE);
            gridPane.add (area,0,1);

            alert.getDialogPane ().setExpandableContent (gridPane);

            //loadin last value
            Preferences pref =Preferences.userNodeForPackage (chooser.class);
            value= Color.valueOf (pref.get ("color","gold"));

            Image image3 = new Image(getClass().getResourceAsStream("back.png"));
            ImageView imageView3 = new ImageView(image3);
            imageView3.setFitHeight(15);
            imageView3.setFitWidth(15);

            Image image4 = new Image(getClass().getResourceAsStream("forward.png"));
            ImageView imageView4 = new ImageView(image4);
            imageView4.setFitHeight(15);
            imageView4.setFitWidth(15);

            Image image = new Image(getClass().getResourceAsStream("stop.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(15);
            imageView.setFitWidth(15);

            Image image2 = new Image(getClass().getResourceAsStream("high.png"));
            ImageView imageView2 = new ImageView(image2);
            imageView2.setFitHeight(15);
            imageView2.setFitWidth(15);

            Image image1 = new Image(getClass().getResourceAsStream("stg.png"));

            play = new Button();
            play.setText(">");
            play.setFont (new Font ("Arial",19));

            play.setOnAction(event -> play());

            MenuItem file = new MenuItem ("Open Media...");

            MenuItem exit = new MenuItem ("Quit");

            clear= new MenuItem("Clear");
            clear.setOnAction(event -> {
                recent.getItems ().clear ();
                recent.getItems ().addAll(clear,new SeparatorMenuItem ());
                if (recent.getItems ().size ()<=2){
                    clear.setDisable (true);
                }
            });
            clear.setDisable(true);

            recent= new Menu("Open Recent");
            recent.getItems().addAll(clear,new SeparatorMenuItem());

            file.setOnAction(event -> file());

            exit.setOnAction(event -> {
                primaryStage.close ();
                System.exit (0);
            });

            Button stop = new Button ( );
            stop.setGraphic(imageView);
            stop.setOnAction(event ->stop() );

            MenuItem help = new MenuItem ("About");
            help.setOnAction(event ->a.about());

            MenuItem preference = new MenuItem ("Preferences");
            preference.setOnAction(event -> c.about(bar));
            preference.setDisable (false);

            MenuItem screenshot = new MenuItem ("Take SnapShot");
            screenshot.setOnAction(event -> {
                try {
                    shot();
                } catch (AWTException | IOException e) {
                    e.printStackTrace();
                }
            });

            RadioMenuItem fast = new RadioMenuItem ("Fast");
            fast.setOnAction(event -> fast());

            RadioMenuItem faster = new RadioMenuItem ("Faster");
            faster.setOnAction(event -> faster());

            RadioMenuItem slow = new RadioMenuItem ("Slow");
            slow.setOnAction(event -> slow());

            RadioMenuItem slower = new RadioMenuItem ("Slower");
            slower.setOnAction(event -> slower());

            RadioMenuItem normal = new RadioMenuItem ("Normal (default)");
            normal.setOnAction(event -> normal());
            normal.setSelected(true);

            ToggleGroup group = new ToggleGroup();

            fast.setToggleGroup(group);
            faster.setToggleGroup(group);
            slow.setToggleGroup(group);
            slower.setToggleGroup(group);
            normal.setToggleGroup(group);

            Menu menu = new Menu ( );
            menu.setText("Media");
            menu.getItems().addAll(file,recent,new SeparatorMenuItem(), exit);

            Menu menu1 = new Menu ( );
            menu1.setText("Help");
            menu1.getItems().addAll(help);

            Menu menu2 = new Menu ( );
            menu2.setText("Tools");
            menu2.getItems().addAll(preference, screenshot);

            Menu menu3 = new Menu ("Playback");
            menu3.getItems().addAll(faster, fast, normal, slow, slower);
            bar = new MenuBar();
            bar.getMenus().addAll(menu, menu2, menu3, menu1);
            bar.setBackground (new Background (new BackgroundFill (value,CornerRadii.EMPTY,Insets.EMPTY)));

            pop= new ContextMenu();

            MenuItem poplay = new MenuItem ("Play");
            poplay.setOnAction(event -> play());

            MenuItem popause = new MenuItem ("Pause");
            popause.setOnAction(event -> pause() );

            MenuItem popfastwine = new MenuItem ("Fastwine");
            popfastwine.setOnAction(event -> fastwine());

            MenuItem popbackwine = new MenuItem ("Backwine");
            popbackwine.setOnAction(event -> backwine());

            MenuItem popstop = new MenuItem ("Stop");
            popstop.setOnAction(event -> stop());

            MenuItem popexit = new MenuItem ("Quit");
            popexit.setOnAction (event -> System.exit (-1));

            MenuItem popshot = new MenuItem ("Take SnapShot");
            popshot.setOnAction(event -> {
                try {
                    shot();
                } catch (AWTException | IOException e1) {
                    e1.printStackTrace ( );
                }
            });

            screen = new MenuItem ("Enter Fullscreen");
            screen.setOnAction (event -> {
                if (!primaryStage.isFullScreen ()){
                    primaryStage.setFullScreen (true);
                    screen.setText ("Leave Fullscreen");
                }else {
                    primaryStage.setFullScreen (false);
                    screen.setText ("Enter Fullscreen");
                }
            });

            popmute= new MenuItem ("Mute");
            popmute.setOnAction (event -> {
                if (!player.isMute ()){
                    player.setMute (true);
                    mute.setSelected (true);
                    popmute.setText ("Unmute");
                }else {
                    player.setMute (false);
                    mute.setSelected (false);
                    popmute.setText ("Mute");
                }
            });

            pop.getItems().addAll(poplay, popause, popstop,new SeparatorMenuItem (),screen,new SeparatorMenuItem (),
                    popbackwine, popfastwine,popmute,new SeparatorMenuItem (), popshot,new SeparatorMenuItem (), popexit);

            pop.setAutoHide(true);

            v =new ImageView();
            v.setX(0.0);
            v.setY(0.0);
            v.setFitHeight(300.0);
            v.setFitWidth(300.0);
            v.setVisible (true);

            volume = new Slider();
            time = new Slider();
            timelabel = new Label();
            timelabel.setText("--:--");

            //setting view
            view= new MediaView(player);
            view.setFitWidth(1200);
            view.setFitHeight(560);

            //dropshadow effect
            DropShadow shadow = new DropShadow();
            shadow.setOffsetX(5.0);
            shadow.setOffsetY(5.0);
            shadow.setColor(Color.WHITE);
            //view.setEffect(shadow);

            Button fastwine = new Button ( );
            fastwine.setGraphic(imageView4);
            fastwine.setOnAction(event -> fastwine());

            Button backwine = new Button ( );
            backwine.setGraphic(imageView3);
            backwine.setOnAction(event -> backwine());

            Label vol = new Label ( );
            vol.setGraphic(imageView2);

            //setting mute functionality
            mute= new CheckBox("Mute");
            mute.setSelected(false);
            mute.setOnAction(event -> mute());

            StackPane layout = new StackPane();
            layout.getChildren().addAll(view,v);
            layout.setAlignment(Pos.CENTER);

            HBox box = new HBox (8);
            box.getChildren().addAll(stop, backwine,play, fastwine, vol,volume,mute,timelabel);
            box.setPadding(new Insets(10,20,10,20));
            box.setAlignment(Pos.CENTER);
            HBox.setHgrow(time, Priority.ALWAYS);
            box.alignmentProperty().isBound();

            vBox= new VBox();

            vBox.getChildren().addAll(new Separator(),time, box);
            vBox.setAlignment(Pos.CENTER);
            vBox.setStyle("-fx-background-color:white;");

            BorderPane pane = new BorderPane();
            pane.setBottom(vBox);
            pane.setCenter(layout);
            pane.setTop(bar);

            scene = new Scene (pane);
            scene.getStylesheets().addAll("effects.css");
            Stage finalPrimaryStage = primaryStage;

            scene.setOnMouseClicked(event -> {
                if (event.getClickCount()==2){
                    finalPrimaryStage.setFullScreen(true);
                    if (finalPrimaryStage.isFullScreen ()){
                        screen.setText ("Leave Fullscreen");
                    }
                    vBox.setVisible(false);
                    bar.setVisible(false);

                }else {
                    vBox.setVisible(true);
                    bar.setVisible(true);
                    double width= view.getFitWidth();
                    double height=view.getFitHeight();
                    view.setFitHeight(height);
                    view.setFitWidth(width);
                }
            });
            scene.setOnMouseMoved (event1 -> {
                if (finalPrimaryStage.isFullScreen ()){
                    vBox.setVisible(true);
                    bar.setVisible(true);
                }
            });

            scene.setOnKeyPressed(event -> {
                if (event.getCode()==KeyCode.F && !finalPrimaryStage.isFullScreen()) {
                    finalPrimaryStage.setFullScreen(true);
                    if (finalPrimaryStage.isFullScreen ()){
                        screen.setText ("Leave Fullscreen");
                    }
                    vBox.setVisible(false);
                    bar.setVisible(false);
                }
                else if (event.getCode()==KeyCode.SPACE&&player.getStatus ()== MediaPlayer.Status.PLAYING){
                    player.pause ();
                    play.setText (">");
                }
                else if (event.getCode ()==KeyCode.SPACE&&player.getStatus ()== MediaPlayer.Status.PAUSED){
                    player.play ();
                    play.setText ("||");
                }
                else {
                    vBox.setVisible(true);
                    bar.setVisible(true);
                    double width= view.getFitWidth();
                    double height=view.getFitHeight();
                    view.setFitHeight(height);
                    view.setFitWidth(width);
                }
            });

            scene.addEventHandler (MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton ()== MouseButton.SECONDARY){
                    pop.show (primaryStage,event.getScreenX (),event.getScreenY ());
                }
            });


            primaryStage.getIcons().add(image1);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        private void file(){
            FileChooser chooser = new FileChooser ( );
            chooser.setTitle ("Choose media file");
            chooser.setInitialDirectory (new File (System.getProperty ("user.home") + "/videos"));
            FileChooser.ExtensionFilter filter1= new FileChooser.ExtensionFilter("All Files","*.*");
            FileChooser.ExtensionFilter filter2= new FileChooser.ExtensionFilter("Select audio Files","*.mp3",
                    "*.wav","*.AC3");

            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select media file",
                    "*.MP4","*.3GP","*.OGG","*.WMV","*.WEBM","*.FLV","*.avi","*.HDV","*.MXF","*.MPEG-TS",
                    "*.MPEG-2PS","*.MPEG-2TS","*.LXF","*.VOB","*.mov","*.mkv","*.M2TS","*.FLAC");
            chooser.getExtensionFilters().addAll(filter1,filter,filter2);

            File file = chooser.showOpenDialog(new Stage());
            primaryStage.setTitle(file.getName() +"-Vx Player");

            String path2 = file.toURI ( ).toString ( );

            Path path1 = Paths.get(file.getAbsolutePath());

            recent(path1);
/*think you may move this one too*/
            for (MenuItem aList : list) {
                recent.getItems().addAll(aList);
            }

            if (recent.getItems().size()>10){
            	//removing the second index
                recent.getItems().remove(1);
            }

            media(path2);
/* yo smartest guy in the planet you want to move this line of code to the start method*/

            if (recent.getItems().size()>1){
                clear.setDisable(false);
            }

            list.forEach (menuItem -> menuItem.setOnAction (event ->{
                Path path = Paths.get (menuItem.getText ());
                String p = path.toUri ().toString ();
                primaryStage.setTitle(path.getFileName () +"-Vx Player");
                media (p);
            } ));

/*and here too*/            

        }

        private void media(String path){
            if (player!=null){
                player.stop();
            }
            if (path!=null){
                try{
                media = new Media(path);
                player = new MediaPlayer(media);
                width= view.fitWidthProperty();
                height= view.fitHeightProperty();

                view.setMediaPlayer(player);
                view.setPreserveRatio(true);
                view.setSmooth(true);
                //volume property
                volume.setValue(player.getVolume()*50);

                volume.valueProperty().addListener(observable -> {
                    if (volume.isPressed()) {
                        player.setVolume(volume.getValue() / 100);
                    }
                });

                //scene property
                time.valueProperty().addListener(observable -> {
                    if (time.isPressed()){
                        player.seek(player.getMedia().getDuration().multiply(time.getValue()/100));
                    }
                });

                player.currentTimeProperty().addListener((Observable ov)->{
                    values();
                    update();
                });

                player.setOnReady(()->{
                    duration=player.getMedia().getDuration();
                    update();
                });

                //get error from the mediaplayer
                player.setOnError(() -> {
                    //noinspection ThrowablePrintedToSystemOut
                    area.setText (player.getError().getMessage ());
                    alert.showAndWait ();
                });

                //get error from the media
                media.setOnError(() -> {
                        //noinspection ThrowablePrintedToSystemOut
                        area.setText(media.getError().getMessage());
                        alert.showAndWait();

                });

                //gets error from media view
                view.setOnError(event -> {
                    area.setText ( event.getMediaError().getMessage ());
                    alert.showAndWait ();
                });

                player.setOnEndOfMedia(()->{
                    play.setText(">");
                    v.setImage(null);
                });

                player.setOnPlaying(()-> player.getCurrentTime());
                media.getMetadata().addListener((MapChangeListener<String, Object>) change -> {
                    if (change.wasAdded()){
                        metadata(change.getKey(),change.getValueAdded());
                    }
                });

                player.setOnStopped(()-> System.out.println("Stopped"));
            }catch (MediaException e){
                    if (e.getType()==MediaException.Type.MEDIA_UNSUPPORTED){
                        area.setText("media format is not supported");
                        alert.showAndWait();
                    }else {
                        area.setText(e.getMessage());
                        alert.showAndWait();
                    }
                }
            }

            Objects.requireNonNull(player).setAutoPlay(true);

            play.setText("||");
        }

        private void metadata(String key,Object value){
            if (key.equals("image")){
                v.setImage ((Image) value);
            }
        }

        private void pause(){
            player.pause();
            play.setText (">");
        }

        private void play(){
            status = player.getStatus ( );
            //when player is playing
            if (status == MediaPlayer.Status.PLAYING){
                //dis method is always false
                if (player.getCurrentTime().greaterThanOrEqualTo(player.getTotalDuration())){
                    player.seek(player.getStartTime());
                    player.play();
                }else {
                    player.pause();
                    play.setText(">");
                }
            }//when the player is paused
            if (status == MediaPlayer.Status.PAUSED|| status == MediaPlayer.Status.HALTED||
                    status ==MediaPlayer.Status.STOPPED){
                player.play();
                play.setText("||");
            }
            player.getRate();
        }

        private void mute(){
           if (mute.isSelected()) {
               player.setMute (true);
               popmute.setText ("Unmute");
           }
           else {
               player.setMute (false);
               popmute.setText ("Mute");
           }
        }

        public void stop(){
            player.stop();
            //time.setValue(0);
            player.seek(player.getStartTime());
            play.setText(">");
        }

        private void shot() throws AWTException, IOException {
            //Circle c = new Circle();
            //Image im = c.snapshot();
            Robot robot = new Robot();
            Rectangle rect = new Rectangle((int)primaryStage.getX(),(int)primaryStage.getY(),(int)primaryStage.getWidth(),(int)primaryStage.getHeight());
            BufferedImage image = robot.createScreenCapture(rect);
            //Image im = SwingFXUtils.toFXImage(image,null);
            Date date = new Date();
            ImageIO.write(image,"png",new File(Paths.get(System.getProperty("user.home")+"/pictures/"+"vxplayer"+date.getTime() )+".png"));
            //v.setImage(im);
        }

        private void update(){
        	//update values on the seek bar
                Platform.runLater(() -> time.setValue(player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis() * 100));
        }

        private void values(){
        	//update time values
            if (time!=null){
                Platform.runLater(()->{
                    Duration current =player.getCurrentTime();
                    timelabel.setText(formattime(current,duration));
                });
            }
        }

        private void recent(Path p){
            list=new ArrayList<>();
            list.add(new MenuItem(""+p));
            /*yo smartest in the world uncomment this and test it, if it works remove the string array with the loop*/

            if (list.size()==10){
            	list.add(0,new MenuItem(""+p));
            }
            /*String[] str = new String[10];
            for (int i=0;i<10;i++) {
                str[i]=p.toString ();
                str[0]=p.toString ();
            }*/
        }

        private void fast(){
            player.setRate(1.5);
        }

        private void faster(){
            player.setRate(2.);
        }

        private void slow(){
            player.setRate(0.65);
        }

        private void slower(){
            player.setRate(0.5);
        }

        private void normal(){
            player.setRate(1.0);
        }

        private void fastwine(){
            player.seek(player.getCurrentTime().multiply(1.125));
        }

        private void backwine(){
            player.seek(player.getCurrentTime().divide(1.125));
        }

        private static String formattime(Duration elapsed,Duration duration){
            long elapsedint = (int) Math.floor(elapsed.toSeconds());
            long elapsedhour= elapsedint/(60*60);
            if (elapsedhour>0){
                elapsedint-=elapsedhour*60*60;
            }
            long elapsedminute=elapsedint/60;
            long elapsedsecond=elapsedint%60;

            if (duration.greaterThan(Duration.ZERO)){
                long durationint =(long) Math.floor(duration.toSeconds());
                long durahour=durationint/(60*60);
                if (durahour>0){
                    durationint-=durahour*60*60;
                }
                long duraminute=durationint/60;
                //long durasec=durationint-durahour*60*60-duraminute*60;
                long durasec=durationint%60;
                if (durahour>0){
                    return String.format("%d:%02d:%02d || %d:%02d:%02d",
                            elapsedhour,elapsedminute,elapsedsecond,
                            durahour,duraminute,durasec);
                }else {
                    return String.format("%02d:%02d || %02d:%02d",elapsedminute,elapsedsecond,
                            duraminute,durasec);
                }
            }else {
                if (elapsedhour>0){
                    return String.format("%d:%02d:%02d",elapsedhour,
                            elapsedminute,elapsedsecond);
                }else {
                    return String.format("%02d:%02d",elapsedminute,
                            elapsedsecond);
                }
            }
        }

        private void menus(){
        	
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }

    }