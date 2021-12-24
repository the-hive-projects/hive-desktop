package org.thehive.hivedesktop.scene;

import com.jfoenix.controls.JFXListCell;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import eu.mihosoft.monacofx.MonacoFX;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
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
import org.thehive.hivedesktop.Consts;
import org.thehive.hivedesktop.Ctx;
import org.thehive.hivedesktop.ProfileDialogView;
import org.thehive.hivedesktop.util.ExecutionUtils;
import org.thehive.hiveserverclient.model.Session;
import org.thehive.hiveserverclient.model.Submission;
import org.thehive.hiveserverclient.payload.ChatMessage;

import java.io.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

        @FXML
        private Label labelSessionName;

        @FXML
        private Label labelSessionTime;

        @FXML
        private Label labelSessionOwner;

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
            ImageView view = new ImageView(Consts.LOGO);
            view.setFitHeight(20);
            view.setFitWidth(20);
            Label sessionCode = new Label();
            sessionCode.setText(!submission.getSession().getName().isEmpty() ? submission.getSession().getName() : "[none]");
            sessionCode.setGraphic(view);
            sessionCode.setGraphicTextGap(3);
            listCell.setGraphic(sessionCode);
            listCell.setMinWidth(165);
            listCell.setMinHeight(44);
            pane.getChildren().addAll(listCell);
            listCell.setOnMouseClicked(mouseEvent -> {
                setCode(codeEditor, "python", "vs-dark", submission.getContent());
                setSessionLabels(submission.getSession());
                setSubmissionLabels(submission);
                rightSplitPane.setVisible(true);
            });

            return listCell;
        }

        public Accordion createAccordionBox(Session session) {
            Accordion accordionCell = new Accordion();
            accordionCell.setStyle("-fx-background-color:#ffc107; -fx-background-radius:15; -fx-margin: 15px; -fx-margin-left:15px;");
            ImageView view = new ImageView(Consts.LOGO);
            view.setFitHeight(20);
            view.setFitWidth(20);
            accordionCell.setMinWidth(165);
            accordionCell.setMaxWidth(165);
            VBox insideAccordion = new VBox(5);
            insideAccordion.setStyle("-fx-background-color:#373737;");
            TitledPane p1 = new TitledPane();
            p1.setText(!session.getName().isEmpty()? session.getName():"[none]");
            p1.setGraphic(view);
            Ctx.getInstance().submissionService.takeAllBySession(session.getId(), appResponse -> {
                if (appResponse.status().isSuccess()) {
                    var list = new ArrayList<JFXListCell<Label>>();
                    for (var s : appResponse.response().get())
                        list.add(createBox(s));
                    ExecutionUtils.runOnUiThread(() ->
                            insideAccordion.getChildren().addAll(list.toArray(new JFXListCell[0])));
                }
            });
            p1.setContent(insideAccordion);
            p1.setGraphicTextGap(3);
            accordionCell.getPanes().addAll(p1);
            sessionListBox.getChildren().addAll(accordionCell);
            accordionCell.setOnMouseClicked(event -> setSessionLabels(session));
            return accordionCell;
        }

        public JFXListCell<Label> createBox(Submission submission) {

            JFXListCell<Label> listCell = new JFXListCell<>();
            listCell.setStyle("-fx-background-color:#ffc107; -fx-background-radius:15; -fx-margin: 15px;");
            ImageView view = new ImageView(Consts.LOGO);
            view.setFitHeight(20);
            view.setFitWidth(20);

            Label sessionCode = new Label();

            //TODO add session code from db
            sessionCode.setText(submission.getUser().getUsername());
            sessionCode.setGraphic(view);
            sessionCode.setGraphicTextGap(3);

            listCell.setGraphic(sessionCode);
            listCell.setMinWidth(165);
            listCell.setMinHeight(44);

            listCell.setOnMouseClicked(mouseEvent -> {
                setCode(codeEditor, "python", "vs-dark", submission.getContent());
                setSessionLabels(submission.getSession());
                setSubmissionLabels(submission);
                rightSplitPane.setVisible(true);
            });

            return listCell;
        }

        private void setSessionLabels(Session session) {
            labelSessionName.setText(!session.getName().isEmpty() ? session.getName() : "[none]");
            labelSessionOwner.setText(session.getUser().getUsername());
            labelSessionTime.setText(DateFormat.getDateInstance().format(new Date(session.getCreationTime())) + " - " + session.getDuration() / 60000 + " min");
        }

        private void setSubmissionLabels(Submission submission) {
            profileTab.setText(submission.getUser().getUsername());
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
                            .forEach(s -> ExecutionUtils.runOnUiThread(() -> createAccordionBox(s))));
        }

        @Override
        public void onUnload() {

        }

    }
}