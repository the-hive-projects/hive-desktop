package org.thehive.hivedesktop.scene;

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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Consts;
import org.thehive.hivedesktop.Ctx;
import org.thehive.hivedesktop.ProfileDialogView;
import org.thehive.hivedesktop.chat.ChatObservableList;
import org.thehive.hivedesktop.util.ExecutionUtils;
import org.thehive.hivedesktop.util.ImageUtils;
import org.thehive.hiveserverclient.model.Session;
import org.thehive.hiveserverclient.net.websocket.header.AppStompHeaders;
import org.thehive.hiveserverclient.net.websocket.header.PayloadType;
import org.thehive.hiveserverclient.net.websocket.subscription.StompSubscription;
import org.thehive.hiveserverclient.net.websocket.subscription.SubscriptionListener;
import org.thehive.hiveserverclient.payload.Chat;
import org.thehive.hiveserverclient.payload.Payload;

import java.io.*;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class EditorScene extends FxmlMultipleLoadedScene {

    static final String FXML_FILENAME = "editor.fxml";

    public EditorScene() {
        super(FXML_FILENAME);
        //Authentication.INSTANCE.authenticate(HeaderUtils.httpBasicAuthenticationToken("user", "password"));
    }

    @Slf4j
    public static class Controller extends AbstractController {

        private static final Class<? extends AppScene> SCENE_TYPE = EditorScene.class;
        private final ChatObservableList chatObservableList;
        @FXML
        Tab firstTab;
        @FXML
        ScrollPane chatScroll;
        @FXML
        VBox chatBox;
        @FXML
        TextArea messageArea;
        @FXML
        private MFXButton btnRunCode;
        @FXML
        private MFXButton btnAddNewTab;
        @FXML
        private MFXButton btnSendMessage;
        @FXML
        private MFXButton btnLeaveSession;
        @FXML
        private TabPane editorPane;
        @FXML
        private TabPane terminalPane;

        private final Dictionary<String, MonacoFX> dict = new Hashtable<String, MonacoFX>();

        public Controller() {
            super(Ctx.getInstance().sceneManager, SCENE_TYPE);
            this.chatObservableList = new ChatObservableList();
            chatObservableList.registerObserver(chat -> {
                Ctx.getInstance().imageService.take(chat.getFrom(), result -> {
                    if (result.status().isSuccess()) {
                        try {
                            var scaledContent = ImageUtils.scaleImageContent(result.entity().get().getContent(), 20, 20);
                            ExecutionUtils.run(() -> {

                                var usernameHyperLink = createUser(chat, scaledContent);
                                var messageLabel = createLabel(chat.getText());
                                var line = createLine();
                                chatBox.getChildren().addAll(usernameHyperLink, messageLabel, line);
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        throw new IllegalStateException();
                    }
                });
            });
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
        private MonacoFX setEditor(String language, String theme) {
            MonacoFX monacoFXeditor = new MonacoFX();
            int numTabs = dict.size();
            monacoFXeditor.setId("monacoFX" + numTabs);
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
            return monacoFXeditor;
        }

        @FXML
        private MonacoFX addTab() throws IOException {
            int numTabs = dict.size();
            Tab tab = new Tab("Tab " + numTabs);
            tab.setId(String.valueOf(numTabs));
            var settedEditor = setEditor("python", "vs-dark");
            tab.setContent(settedEditor);
            addToDict(tab.getText(), settedEditor);
            editorPane.getTabs().add(tab);
            return settedEditor;
        }

        @FXML
        private void addToDict(String tabName, MonacoFX editorName) {
            // Inserting values into the Dictionary
            dict.put(tabName, editorName);
            System.out.println(dict);
        }

        @FXML
        private File runEditorCode(TerminalTab terminal) throws IOException {
            var currentTabName = editorPane.getSelectionModel().getSelectedItem().getText();
            MonacoFX currentEditor = dict.get(currentTabName);

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

        @FXML
        private TextArea createLabel(String text) {
            TextArea messageLabel = new TextArea();
            messageLabel.setEditable(false);
            messageLabel.setWrapText(true);
            messageLabel.wrapTextProperty().set(true);
            messageLabel.setPrefHeight(50);
            Font font = Font.font("Helvetica", FontWeight.NORMAL,
                    FontPosture.REGULAR, 12);
            messageLabel.setFont(font);
            messageLabel.setPadding(new Insets(10, 10, 5, 10));
            messageLabel.setStyle("-fx-background-color:transparent;  -fx-text-area-background:#373737; text-area-background:#373737; -fx-text-fill:#ffc107;  ");
            messageLabel.setText(text);
            return messageLabel;
        }

        @FXML
        private Line createLine() {
            Line line = new Line();
            line.setStartX(0);
            line.setStartY(100);
            line.setEndY(100);
            line.setEndX(300);
            line.setStyle("-fx-background-color:#ffc107; -fx-stroke: #ffc107; -fx-opacity: 0.5; ");
            return line;
        }

        @FXML
        private Hyperlink createUser(Chat chat, byte[] profileImageContent) {
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

       /* @FXML
        private Hyperlink createUser() {

            //TODO Hyperlink for username who sent message
            Hyperlink userName = new Hyperlink();
            userName.setText();

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

            userName.setOnMouseClicked(mouseEvent -> {

                ProfileDialogView profileDialogView = new ProfileDialogView();
                try {
                    profileDialogView.start(new Stage());
                    //userName.setDisable(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            });


            return userName;
        }*/

        @FXML
        private void sendMessage() {
            var chat = new Chat();
            String message = messageArea.getText();
            chat.setText(message);
            Ctx.getInstance().webSocketService.getConnection().get().getSessionSubscription().get().send(chat);
        }

        @Override
        public void onStart() {

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

            terminalPane.getTabs().add(terminal);


            btnAddNewTab.setOnMouseClicked(mouseEvent -> {
                try {
                    addTab();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


            btnRunCode.setOnMouseClicked(mouseEvent -> {
                try {
                    readFile1(runEditorCode(terminal));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            btnLeaveSession.setOnMouseClicked(event -> {
                //TODO load sessionview
                Ctx.getInstance().sceneManager.load(MainScene.class);
                //TODO disconnect session connection
            });

            btnSendMessage.setOnMouseClicked(mouseEvent -> sendMessage());



        }

        @Override
        public void onLoad(Map<String, Object> dataMap) {
            log.info("onLoad Editor");
            var session = (Session) dataMap.get(Consts.JOINED_SESSION_SCENE_DATA_KEY);
            Ctx.getInstance().webSocketService.getConnection().ifPresent(connection -> {
                connection.subscribeToSession(session.getId(), new SubscriptionListener() {
                    @Override
                    public void onSubscribe(StompSubscription stompSubscription) {

                    }

                    @Override
                    public void onSend(Payload payload) {

                    }

                    @Override
                    public void onReceive(AppStompHeaders appStompHeaders, Payload payload) {
                        if (appStompHeaders.getPayloadType() == PayloadType.CHAT)
                            chatObservableList.add((Chat) payload);
                    }

                    @Override
                    public void onUnsubscribe(StompSubscription stompSubscription) {

                    }
                });
            });
        }

        @Override
        public void onUnload() {

        }

    }
}