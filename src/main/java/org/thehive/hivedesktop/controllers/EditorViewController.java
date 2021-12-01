package org.thehive.hivedesktop.controllers;

import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import eu.mihosoft.monacofx.MonacoFX;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.thehive.hivedesktop.ProfileDialogView;
import org.thehive.hivedesktop.scene.MainScene;

import java.io.*;
import java.net.URL;
import java.util.*;


public class EditorViewController implements Initializable {

    @FXML
    private MFXButton btnRunCode;

    @FXML
    private MFXButton btnAddNewTab;

    @FXML
    private MFXButton btnSendMessage;
    @FXML
    private MFXButton btnLeaveSession;


    public TabPane terminalPane = new TabPane();

    @FXML
    private TabPane editorPane;

    @FXML
    Tab firstTab;

    @FXML
    ScrollPane chatScroll;
    @FXML
    VBox chatBox;

    @FXML
    TextArea messageArea;





    Dictionary<String, MonacoFX> dict
            = new Hashtable<String, MonacoFX>();


    @FXML
    private MonacoFX setEditor(String language,String theme)
    {
        MonacoFX monacoFXeditor = new MonacoFX();
        int numTabs = dict.size();
        monacoFXeditor.setId("monacoFX"+String.valueOf(numTabs));
        monacoFXeditor.getEditor().getDocument().setText(
                "num = float(input(\"Enter a number: \"))\r" +
                        "if num > 0:\n" +
                        "   print(\"Positive number\")\n" +
                        "elif num == 0:\n" +
                        "   print(\"Zero\")\n" +
                        "else:\n" +
                        "   print(\"Negative number\")\n");


        // use a predefined language like 'c'
        monacoFXeditor.getEditor().setCurrentLanguage(language);
        monacoFXeditor.getEditor().setCurrentTheme(theme);
       //System.out.println(monacoFXeditor.getId());

        return monacoFXeditor;
    }

    @FXML
    private MonacoFX addTab() throws IOException {
        int numTabs = dict.size();
        Tab tab = new Tab("Tab "+ numTabs);
        tab.setId(String.valueOf(numTabs));
        //System.out.println(tab.getId());
        var settedEditor = setEditor("python","vs-dark");
        tab.setContent(settedEditor);
        addToDict(tab.getText(),settedEditor);
        editorPane.getTabs().add(tab);

        return settedEditor;
    }

    @FXML
    private void addToDict(String tabName, MonacoFX editorName)
    {


        // Inserting values into the Dictionary
        dict.put(tabName, editorName);
        System.out.println(dict);


    }


    @FXML
    private static void readFile1(File fin) throws IOException {
        FileInputStream fis = new FileInputStream(fin);

        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        br.close();
    }


    @FXML
    private File runEditorCode(TerminalTab terminal) throws IOException {
        var currentTabName = editorPane.getSelectionModel().getSelectedItem().getText();
        MonacoFX currentEditor = dict.get(currentTabName);
        //System.out.println(currentEditor.getId());

        var currentEditorContent = currentEditor.getEditor().getDocument().getText();
        System.out.println(currentEditorContent);

        var codeDoc = currentEditor.getEditor().getDocument();
        codeDoc.setLanguage("python");

        File fout = new File("out.py");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(codeDoc.getText());
        bw.close();



        terminal.getTerminal().command("python "+fout+"\r");

        return fout;


    }

    @FXML
    public void switchToPage(MouseEvent event, String fxml) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TextArea createLabel()
    {


        TextArea messageLabel = new TextArea();


        messageLabel.setEditable(false);
        messageLabel.setWrapText(true);
        messageLabel.wrapTextProperty().set(true);

        messageLabel.setPrefHeight(50);

        Font font = Font.font("Helvetica", FontWeight.NORMAL,
                FontPosture.REGULAR, 12);

        messageLabel.setFont(font);
        messageLabel.setPadding(new Insets(10, 10, 5, 10));
        //messageLabel.setTextFill(Color.web("#ffc107"));
        /*Border labelBorder = new Border(new BorderStroke(Color.web("#ffc107"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));

        messageLabel.setBorder(labelBorder);*/
        messageLabel.setStyle("-fx-background-color:transparent;  -fx-text-area-background:#373737; text-area-background:#373737; -fx-text-fill:#ffc107;  ");


        return messageLabel;
    }
    @FXML
    private Line createLine()
    {
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(100);
        line.setEndY(100);
        line.setEndX(300);
        line.setStyle("-fx-background-color:#ffc107; -fx-stroke: #ffc107; -fx-opacity: 0.5; ");

        return line;
    }

    @FXML
    private Hyperlink createUser()
    {
        //TODO Hyperlink for username who sent message
        Hyperlink userName = new Hyperlink();
        userName.setText("  Onur Sercan Yılmaz");

        Font font = Font.font("Helvetica", FontWeight.BOLD,
                FontPosture.REGULAR, 10);

        userName.setFont(font);
        userName.setPadding(new Insets(10, 10, 5, 10));
        userName.setTextFill(Color.web("#ffc107"));

        //TODO Hyperlink for image who sent message
        Image img = new Image("https://p.kindpng.com/picc/s/78-786207_user-avatar-png-user-avatar-icon-png-transparent.png"); //image who sent the message form db
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        userName.setGraphic(view);

        userName.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ScreenController screenController = new ScreenController();
                ProfileDialogView profileDialogView = new ProfileDialogView();
                try {
                    screenController.showProfileDialogView(profileDialogView);
                    //userName.setDisable(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });



        return userName;
    }

    @FXML
    private void addMessageToChatBox()
    {

        var userName = createUser();
        var messageLabel = createLabel();
        var line = createLine();

        String message = messageArea.getText();
        messageLabel.setText(message);
        messageArea.setText(null);

        chatBox.getChildren().addAll(userName,messageLabel,line);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        btnAddNewTab.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    addTab();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        btnSendMessage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                addMessageToChatBox();
            }
        });








        //        Dark Config

        TerminalConfig darkConfig = new TerminalConfig();
        darkConfig.setBackgroundColor(Color.web("#1e1e1e"));
        darkConfig.setForegroundColor(Color.rgb(240, 240, 240));
        darkConfig.setCursorColor(Color.web("#ffc107"));


//        CygWin Config
        TerminalConfig cygwinConfig = new TerminalConfig();
        cygwinConfig.setWindowsTerminalStarter("C:\\cygwin64\\bin\\bash -i");
        cygwinConfig.setFontSize(14);


//        Default Config
        TerminalConfig defaultConfig = new TerminalConfig();


        TerminalBuilder terminalBuilder = new TerminalBuilder(darkConfig);
        TerminalTab terminal = terminalBuilder.newTerminal();
//        terminal.onTerminalFxReady(() -> {
//            terminal.getTerminal().command("java -version\r");
//        });




        btnRunCode.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    readFile1(runEditorCode(terminal));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        btnLeaveSession.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                ScreenController screenController = new ScreenController();
                MainScene sessionView = new MainScene();
                try {
                    screenController.switchToISessionView(event, sessionView);
                    btnLeaveSession.getScene().getWindow().hide();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });



        terminalPane.getTabs().add(terminal);





    }
}
