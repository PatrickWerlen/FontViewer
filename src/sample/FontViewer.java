package sample;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class FontViewer extends Application {
  private static final int SMALL_SIZE = 24;
  private static final int MEDIUM_SIZE = 36;
  private static final int LARGE_SIZE = 48;

  private DoubleProperty bigJavaSize = new SimpleDoubleProperty(LARGE_SIZE);
  private StringProperty bigJavaFont = new SimpleStringProperty("Arial");
  private StringProperty bigJavaBold = new SimpleStringProperty("NORMAL");
  private StringProperty bigJavaItalic = new SimpleStringProperty("NORMAL");

  private Label createBigJavaLabel(){
    Label label = new Label("BigJava");
    bigJavaSize.addListener((observable, oldValue, newValue) -> label.setFont(Font.font(bigJavaFont.getValue(), FontWeight.findByName(bigJavaBold.getValue()), FontPosture.findByName(bigJavaItalic.getValue()),(double)newValue)));
    bigJavaBold.addListener(((observable, oldValue, newValue) -> label.setFont(Font.font(bigJavaFont.getValue(), FontWeight.findByName(newValue),FontPosture.findByName(bigJavaItalic.getValue()), bigJavaSize.getValue()))));
    bigJavaItalic.addListener((observable, oldValue, newValue) -> label.setFont(Font.font(bigJavaFont.getValue(), FontWeight.findByName(bigJavaBold.getValue()), FontPosture.findByName(newValue), bigJavaSize.getValue())));
    bigJavaFont.addListener((observable, oldValue, newValue) -> label.setFont(Font.font(newValue, FontWeight.findByName(bigJavaBold.getValue()), FontPosture.findByName(bigJavaItalic.getValue()), bigJavaSize.getValue())));
    return label;
  }

  private ImageView createImage(){
    ImageView image = new ImageView(this.getClass().getResource("BigJava.jpg").toExternalForm());
    return image;
  }

  private Label  createFontSizeLabel(Slider slider){
    Label label = new Label();
    label.textProperty().bind(Bindings.concat("Font Size: ", slider.valueProperty().asString("%3.0f")));

    return label;
  }

  private Slider createSlider(){
    Slider slider = new Slider();
    slider.setOrientation(Orientation.VERTICAL);
    slider.setMax(70.0D);
    slider.setMin(10.0D);
    slider.setMajorTickUnit(10.0D);
    slider.setMinorTickCount(5);
    slider.setShowTickLabels(true);
    slider.setShowTickMarks(true);
    bigJavaSize.bindBidirectional(slider.valueProperty());

    return slider;
  }

  private HBox createSettingsMenu(Slider slider, CheckBox[] checkBoxes){
      HBox line_0 = new HBox(createFontSizeLabel(slider));
      line_0.setAlignment(Pos.CENTER);
      ComboBox fontBox = new ComboBox(FXCollections.observableArrayList("Serif", "SansSerif", "Monospaced"));
      fontBox.setEditable(true);
      fontBox.valueProperty().bindBidirectional(bigJavaFont);
      HBox line_1 = new HBox(fontBox);
      line_1.setAlignment(Pos.CENTER);
      line_1.setPadding(new Insets(15.0D, 50.0D, 15.0D, 50.0D));
      HBox line_2 = new HBox(checkBoxes[0], checkBoxes[1]);
      line_2.setPadding(new Insets(15.0D, 25.0D, 25.0D, 15.0D));
      line_2.setAlignment(Pos.CENTER);
      line_2.setSpacing(40.0D);
      HBox line_3 = new HBox(createToggleGroup());
      line_3.setPadding(new Insets(15.0D, 25.0D, 25.0D, 15.0D));
      line_3.setAlignment(Pos.CENTER);
      line_3.setSpacing(25.0D);
      VBox parameters = new VBox(line_0, line_1, line_2, line_3);
      HBox hBox = new HBox(slider, parameters);

      return hBox;
  }

  private BorderPane createRootpane(){
    Slider slider = createSlider();
    CheckBox[] checkBoxes = createCheckBoxes();
    MenuBar menuBar = createMenuBar(checkBoxes);

    BorderPane rootPane = new BorderPane();
    rootPane.setBottom(createSettingsMenu(slider, checkBoxes));
    rootPane.setTop(menuBar);

    return rootPane;
  }

  private CheckBox[] createCheckBoxes(){
    CheckBox[] checkBoxes = new CheckBox[2];
    checkBoxes[0] = new CheckBox("Italic");
    checkBoxes[1] = new CheckBox("Bold");

    return checkBoxes;
  }

  private RadioButton[] createToggleGroup(){
    ToggleGroup toggleGroup = new ToggleGroup();
    RadioButton[] radioButtons = new RadioButton[3];
    radioButtons[0] = new RadioButton("Small");
    radioButtons[0].setUserData(SMALL_SIZE);
    radioButtons[0].setToggleGroup(toggleGroup);
    radioButtons[1] = new RadioButton("Medium");
    radioButtons[1].setUserData(MEDIUM_SIZE);
    radioButtons[1].setToggleGroup(toggleGroup);
    radioButtons[2] = new RadioButton("Large");
    radioButtons[2].setUserData(LARGE_SIZE);
    radioButtons[2].setSelected(true);
    radioButtons[2].setToggleGroup(toggleGroup);

    toggleGroup.selectedToggleProperty().addListener(new javafx.beans.value.ChangeListener<Toggle>() {
      @Override
      public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        bigJavaSize.setValue((int)newValue.getUserData());
      }
    });

    return radioButtons;
  }

  private MenuBar createMenuBar(CheckBox[] checkBoxes){
    MenuBar menuBar = new MenuBar();
    Menu font = new Menu("Font");
    Menu mFace = new Menu("Face");
    Menu mStyle = new Menu("Style");
    CheckMenuItem mBold = new CheckMenuItem("bold");
    mBold.setAccelerator(KeyCombination.keyCombination("Ctrl+B"));
    CheckMenuItem mItalic = new CheckMenuItem("italic");
    mItalic.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
    mStyle.getItems().addAll(mItalic,mBold);
    SeparatorMenuItem separator = new SeparatorMenuItem();
    font.getItems().addAll(mFace, separator, mStyle);
    menuBar.getMenus().addAll(font);

    //binds between Menus and  buttons
    mBold.selectedProperty().bindBidirectional(checkBoxes[1].selectedProperty());
    mBold.selectedProperty().addListener(new javafx.beans.value.ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                bigJavaBold.setValue("BOLD");
            }else{
                bigJavaBold.setValue("NORMAL");
            }
        }
    });

    mItalic.selectedProperty().bindBidirectional(checkBoxes[0].selectedProperty());
    mItalic.selectedProperty().addListener(new javafx.beans.value.ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                bigJavaItalic.setValue("ITALIC");
            }else{
                bigJavaItalic.setValue("NORMAL");
            }
        }
    });

    return menuBar;
  }

  public void start(Stage primaryStage) throws Exception {

    //combobox with diffrent fonts. todo binding to menu mFace
    ComboBox fontBox = new ComboBox(FXCollections.observableArrayList("Serif", "SansSerif", "Monospaced"));
    fontBox.setEditable(true);

    //create rootPane
    BorderPane center = new BorderPane();
    center.setCenter(createBigJavaLabel());
    center.setRight(createImage());
    center.setPadding(new Insets(0.0D, 0.0D, 25.0D, 0.0D));

    BorderPane rootPane = createRootpane();
    rootPane.setCenter(center);

    Scene scene = new Scene(rootPane);
    primaryStage.sizeToScene();
    primaryStage.setScene(scene);
    primaryStage.setTitle("Font Viewer");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
