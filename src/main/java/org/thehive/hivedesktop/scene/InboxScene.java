package org.thehive.hivedesktop.scene;

import com.jfoenix.controls.JFXListCell;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import eu.mihosoft.monacofx.MonacoFX;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Ctx;
import org.thehive.hivedesktop.ProfileDialogView;
import org.thehive.hiveserverclient.payload.ChatMessage;

import java.io.*;
import java.util.Collection;
import java.util.Map;

public class InboxScene extends FxmlMultipleLoadedScene {

    static final String FXML_FILENAME = "inbox.fxml";

    public InboxScene() {
        super(FXML_FILENAME);
        //Authentication.INSTANCE.authenticate(HeaderUtils.httpBasicAuthenticationToken("user", "password"));
    }

    @Slf4j
    public static class Controller extends AbstractController {

        private static final Class<? extends AppScene> SCENE_TYPE = InboxScene.class;


        @FXML
        private MFXButton btnRunCode;

        @FXML
        private MFXButton btnGoHome;

        @FXML
        private MFXButton btnGoHome2;

        @FXML
        private TabPane terminalTab;

        @FXML
        private VBox attendedList;

        @FXML
        private VBox createdListVBox;

        @FXML
        private MonacoFX codeEditor;

        @FXML
        private SplitPane rightSplitPane;

        @FXML
        private Hyperlink profileTab;

        @FXML
        private static void readCode(File fin) throws IOException {
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
        private Hyperlink createUser(ChatMessage chat, byte[] profileImageContent) {
            Hyperlink userName = new Hyperlink();
            Font font = Font.font("Helvetica", FontWeight.BOLD,
                    FontPosture.REGULAR, 10);
            userName.setFont(font);
            userName.setText(chat.getFrom());
            userName.setPadding(new Insets(10, 10, 5, 10));
            userName.setTextFill(Color.web("#ffc107"));
            var image = new Image(new ByteArrayInputStream(profileImageContent));
            userName.setGraphic(new ImageView(image));
            userName.setOnMouseClicked(mouseEvent -> {
                ProfileDialogView profileDialogView = new ProfileDialogView();
                try {
                    profileDialogView.start(new Stage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return userName;
        }

        @FXML
        private MonacoFX setCode(MonacoFX editor, String language, String theme) {
            //When click a person, his/her code will be loaded from db with setCode
            //int numTabs = dict.size();
            // monacoFXeditor.setId("monacoFX" + numTabs);
            //TODO load code from db
            editor.getEditor().getDocument().setText("CODE FROM DB");
            // use a predefined language like 'c'
            editor.getEditor().setCurrentLanguage(language);
            editor.getEditor().setCurrentTheme(theme);
            codeEditor = editor;
            return codeEditor;
        }

        @FXML
        private File runEditorCode(TerminalTab terminal) throws IOException {

            MonacoFX currentEditor = codeEditor;

            var currentEditorContent = currentEditor.getEditor().getDocument().getText();
            System.out.println(currentEditorContent);

            var codeDoc = currentEditor.getEditor().getDocument();
            codeDoc.setLanguage("python");

            File fout = new File("out.py");
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(codeDoc.getText());
            bw.close();
            terminal.getTerminal().command("python " + fout + "\r");
            return fout;
        }

        public Accordion createAccordionBox() {

            Accordion accordionCell = new Accordion();
            accordionCell.setStyle("-fx-background-color:#ffc107; -fx-background-radius:15; -fx-margin: 15px; -fx-margin-left:15px;");



            Image img = new Image("https://avatars.githubusercontent.com/u/93194123?s=200&v=4");

            ImageView view = new ImageView(img);
            view.setFitHeight(20);
            view.setFitWidth(20);

            accordionCell.setMinWidth(165);
            accordionCell.setMaxWidth(165);



            VBox insideAccordion = new VBox(5);
            insideAccordion.setStyle("-fx-background-color:#373737;");
            TitledPane p1 = new TitledPane();
            p1.setText("Session Code 1"); //TODO Session Code
            p1.setGraphic(view);


            insideAccordion.getChildren().addAll(createBox("xx"),createBox("mm"),createBox("kk"));
            p1.setContent(insideAccordion);
            p1.setGraphicTextGap(3);



            accordionCell.getPanes().addAll(p1);


            createdListVBox.getChildren().addAll(accordionCell);
            return accordionCell;


        }

        public  JFXListCell<Label> createBox(String deneme) {

            JFXListCell<Label> listCell = new JFXListCell<Label>();
            listCell.setStyle("-fx-background-color:#ffc107; -fx-background-radius:15; -fx-margin: 15px;");

            //File file = new File("img/logo.png");
            Image img = new Image("https://avatars.githubusercontent.com/u/93194123?s=200&v=4");

            ImageView view = new ImageView(img);
            view.setFitHeight(20);
            view.setFitWidth(20);

            Label sessionCode = new Label();

            //TODO add session code from db
            sessionCode.setText(deneme);
            sessionCode.setGraphic(view);
            sessionCode.setGraphicTextGap(3);

            listCell.setGraphic(sessionCode);
            listCell.setMinWidth(165);
            listCell.setMinHeight(44);


            attendedList.getChildren().addAll(listCell);

            listCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    rightSplitPane.setVisible(true);
                    setCode(codeEditor, "python", "vs-dark");
                    //TODO user info
                    // profileTab = createUser();

                }
            });


            return  listCell;
        }

        @Override
        public void onStart() {

            createAccordionBox();
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


            terminalTab.getTabs().add(terminal);

            log.info("onStart InBox");





           /* btnAttendeeDetails.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    rightSplitPane.setVisible(true);
                }
            });*/


            Ctx.getInstance().sceneManager.getStage().resizableProperty();

            btnRunCode.setOnMouseClicked(mouseEvent -> {
                try {
                    readCode(runEditorCode(terminal));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            btnGoHome.setOnMouseClicked(mouseEvent -> {
                Ctx.getInstance().sceneManager.load(MainScene.class);
            });

            btnGoHome2.setOnMouseClicked(mouseEvent -> {
                Ctx.getInstance().sceneManager.load(MainScene.class);
            });

            createBox("xx");


            profileTab.setOnMouseClicked(mouseEvent -> {
                ProfileDialogView profileDialogView = new ProfileDialogView();
                try {
                    profileDialogView.start(new Stage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onLoad(Map<String, Object> data) {

        }

        @Override
        public void onUnload() {

        }


    }
}