package org.thehive.hivedesktop.scene;

import com.jfoenix.controls.JFXListCell;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import eu.mihosoft.monacofx.MonacoFX;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Ctx;
import org.thehive.hivedesktop.ProfileDialogView;
import org.thehive.hivedesktop.util.ExecutionUtils;
import org.thehive.hiveserverclient.model.Session;
import org.thehive.hiveserverclient.model.Submission;
import org.thehive.hiveserverclient.payload.ChatMessage;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

public class InboxScene extends FxmlMultipleLoadedScene {

    static final String FXML_FILENAME = "inbox.fxml";

    public InboxScene() {
        super(FXML_FILENAME);


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
        private VBox submissionListBox;

        @FXML
        private VBox sessionListBox;

        @FXML
        private MonacoFX codeEditor;

        @FXML
        private SplitPane rightSplitPane;

        @FXML
        private Hyperlink profileTab;

        public Controller() {
            super(Ctx.getInstance().sceneManager, SCENE_TYPE);
        }

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
        private MonacoFX setCode(MonacoFX editor, String language, String theme, String code) {
            //When click a person, his/her code will be loaded from db with setCode
            //int numTabs = dict.size();
            // monacoFXeditor.setId("monacoFX" + numTabs);
            //TODO load code from db
            editor.getEditor().getDocument().setText(code);
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


        public JFXListCell<Label> addSubmissionInPane(Pane pane, Submission submission) {

            JFXListCell<Label> listCell = new JFXListCell<>();
            listCell.setStyle("-fx-background-color:#ffc107; -fx-background-radius:15; -fx-margin: 15px;");
            Image img = new Image("https://avatars.githubusercontent.com/u/93194123?s=200&v=4");
            ImageView view = new ImageView(img);
            view.setFitHeight(20);
            view.setFitWidth(20);
            Label sessionCode = new Label();
            //TODO add session code from db
            sessionCode.setText(submission.getSession().getName());
            sessionCode.setGraphic(view);
            sessionCode.setGraphicTextGap(3);
            listCell.setGraphic(sessionCode);
            listCell.setMinWidth(165);
            listCell.setMinHeight(44);
            pane.getChildren().addAll(listCell);
            listCell.setOnMouseClicked(mouseEvent -> {
                rightSplitPane.setVisible(true);
                setCode(codeEditor, "python", "vs-dark", submission.getContent());
                //TODO user info
                // profileTab = createUser();

            });

            return listCell;
        }

        public JFXListCell<Label> addSessionInPane(Pane pane, Session session) {
            JFXListCell<Label> listCell = new JFXListCell<>();
            listCell.setStyle("-fx-background-color:#ffc107; -fx-background-radius:15; -fx-margin: 15px;");
            // TODO: 12/20/2021 only load once in static context
            Image img = new Image("https://avatars.githubusercontent.com/u/93194123?s=200&v=4");
            ImageView view = new ImageView(img);
            view.setFitHeight(20);
            view.setFitWidth(20);
            Label sessionCode = new Label();
            sessionCode.setText(session.getName());
            sessionCode.setGraphic(view);
            sessionCode.setGraphicTextGap(3);
            listCell.setGraphic(sessionCode);
            listCell.setMinWidth(165);
            listCell.setMinHeight(44);
            pane.getChildren().addAll(listCell);
            listCell.setOnMouseClicked(mouseEvent -> {

            });
            return listCell;
        }

        @Override
        public void onStart() {


            TerminalConfig darkConfig = new TerminalConfig();
            darkConfig.setBackgroundColor(Color.web("#1e1e1e"));
            darkConfig.setForegroundColor(Color.rgb(240, 240, 240));
            darkConfig.setCursorColor(Color.web("#ffc107"));
            TerminalBuilder terminalBuilder = new TerminalBuilder(darkConfig);
            TerminalTab terminal = terminalBuilder.newTerminal();
            terminalTab.getTabs().add(terminal);
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

            Ctx.getInstance().submissionService.takeAll(appResponse -> {
                if (appResponse.status().isSuccess()) {
                    Arrays.stream(appResponse.response().get())
                            .parallel()
                            .forEach(s ->
                                    ExecutionUtils.runOnUiThread(() ->
                                            addSubmissionInPane(submissionListBox, s)));
                }
            });
            Ctx.getInstance().sessionService.takeAll(appResponse ->
                    Arrays.stream(appResponse.response().get())
                            .parallel()
                            .forEach(s -> {

                            }));
        }

        @Override
        public void onUnload() {

        }

    }
}