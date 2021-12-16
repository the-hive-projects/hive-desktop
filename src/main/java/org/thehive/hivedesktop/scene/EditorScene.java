package org.thehive.hivedesktop.scene;

import com.jfoenix.controls.JFXListCell;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import eu.mihosoft.monacofx.MonacoFX;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Consts;
import org.thehive.hivedesktop.Ctx;
import org.thehive.hivedesktop.component.AttendeeComponent;
import org.thehive.hivedesktop.component.ChatMessageComponent;
import org.thehive.hivedesktop.util.ExecutionUtils;
import org.thehive.hivedesktop.util.observable.*;
import org.thehive.hiveserverclient.model.Session;
import org.thehive.hiveserverclient.net.websocket.header.AppStompHeaders;
import org.thehive.hiveserverclient.net.websocket.header.PayloadType;
import org.thehive.hiveserverclient.net.websocket.subscription.StompSubscription;
import org.thehive.hiveserverclient.net.websocket.subscription.SubscriptionListener;
import org.thehive.hiveserverclient.payload.ChatMessage;
import org.thehive.hiveserverclient.payload.LiveSessionInformation;
import org.thehive.hiveserverclient.payload.ParticipationNotification;
import org.thehive.hiveserverclient.payload.Payload;

import java.io.*;
import java.util.*;

public class EditorScene extends FxmlMultipleLoadedScene {

    static final String FXML_FILENAME = "editor.fxml";

    public EditorScene() {
        super(FXML_FILENAME);
    }

    @Slf4j
    public static class Controller extends AbstractController {

        private static final Class<? extends AppScene> SCENE_TYPE = EditorScene.class;
        private final ObservableCollection<ChatMessageComponent> chatMessageComponentCollection;
        private final ObservableMap<String, AttendeeComponent> attendeeComponentMap;
        private final Dictionary<String, MonacoFX> dict = new Hashtable<String, MonacoFX>();

        Timer timer = new Timer();

        @FXML
        ScrollPane chatScroll;

        @FXML
        VBox chatBox;

        @FXML
        MFXButton btnSaveCode;

        @FXML
        SplitPane mainPane;

        @FXML
        TextArea messageArea;

        @FXML
        ButtonBar btnBar;

        @FXML
        private VBox attendeeList;

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

        public Controller() {
            super(Ctx.getInstance().sceneManager, SCENE_TYPE);
            this.chatMessageComponentCollection = new ObservableCollectionWrapper<>(new LinkedList<>());
            chatMessageComponentCollection.registerObserver(new CollectionObserverAdapter<>() {
                @Override
                public void onAdded(ChatMessageComponent chatMessageComponent) {
                    ExecutionUtils.runOnUi(() -> chatBox.getChildren().add(chatMessageComponent.getParentNode()));
                }
            });
            this.attendeeComponentMap = new ObservableMapWrapper<>(new HashMap<>());
            attendeeComponentMap.registerObserver(new MapObserverAdapter<>() {
                @Override
                public void onAdded(String username, AttendeeComponent attendeeComponent) {
                    ExecutionUtils.runOnUi(() -> attendeeList.getChildren().add(attendeeComponent.getParentNode()));
                }

                @Override
                public void onRemoved(String s, AttendeeComponent attendeeComponent) {
                    ExecutionUtils.runOnUi(() -> attendeeList.getChildren().remove(attendeeComponentMap.get(s).getParentNode()));
                }
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
        private void saveCode() throws IOException {
           /* FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Python Files", "*.py"));
            fileChooser.setInitialFileName("code");
            fileChooser.setInitialDirectory(file);7*/

            // fileChooser.showSaveDialog(this.messageArea.getScene().getWindow());
            var currentTabName = editorPane.getSelectionModel().getSelectedItem().getText();
            MonacoFX currentEditor = dict.get(currentTabName);

            var currentEditorContent = currentEditor.getEditor().getDocument().getText();
            //System.out.println(currentEditorContent);

            var codeDoc = currentEditor.getEditor().getDocument();
            codeDoc.setLanguage("python");

            String userHomeFolder = System.getProperty("user.home");
            File fout = new File(userHomeFolder + "/Desktop", "code.py");
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write(codeDoc.getText());
            bw.close();

            btnSaveCode.setMinWidth(300);
            btnSaveCode.setText("code.py Saved Succesfully to Desktop!");


            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            btnSaveCode.setText("Save");
                            btnSaveCode.setMinWidth(0);

                        }
                    });

                }
            }, 5000);

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


        public void createBox() {

            JFXListCell<Label> listCell = new JFXListCell<Label>();
            listCell.setStyle("-fx-background-color:#ffc107; -fx-background-radius:15; -fx-margin: 15px;");

            //File file = new File("img/logo.png");
            Image img = new Image("https://avatars.githubusercontent.com/u/93194123?s=200&v=4");

            ImageView view = new ImageView(img);
            view.setFitHeight(20);
            view.setFitWidth(20);

            listCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    //setCode(codeEditor, "python", "vs-dark");
                    //TODO user info
                    // profileTab = createUser();

                }
            });


        }

        @FXML
        private MonacoFX addTab() {
            int numTabs = dict.size();
            Tab tab = new Tab("Yours " + numTabs);
            tab.setId(String.valueOf(numTabs));
            var settedEditor = setEditor("python", "vs-dark");
            tab.setContent(settedEditor);
            addToDict(tab.getText(), settedEditor);

            if (numTabs >= 1) {
                //TODO: set not editable editor

            }

            editorPane.getTabs().add(tab);
            tab.setClosable(false);
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
            var chatMessage = new ChatMessage();
            String message = messageArea.getText();
            messageArea.clear();
            chatMessage.setText(message);
            Ctx.getInstance().webSocketService.getConnection().get().getSessionSubscription().get().send(chatMessage);
        }

        @Override
        public void onStart() {

            addTab();
            addTab();
            addTab();
            createBox();

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


            /*btnAddNewTab.setOnMouseClicked(mouseEvent -> {
                    addTab();
            });*/

            btnSaveCode.setOnMouseClicked(mouseEvent -> {
                try {
                    saveCode();
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
            var id = (String) dataMap.get(Consts.JOINED_SESSION_LIVE_ID_SCENE_DATA_KEY);
            Ctx.getInstance().webSocketService.getConnection().ifPresent(connection -> {
                connection.subscribeToSession(id, new SubscriptionListener() {
                    @Override
                    public void onSubscribe(StompSubscription stompSubscription) {

                    }

                    @Override
                    public void onSend(Payload payload) {

                    }

                    @Override
                    public void onReceive(AppStompHeaders appStompHeaders, Payload payload) {
                        if (appStompHeaders.getPayloadType() == PayloadType.CHAT_MESSAGE) {
                            var chatMessage = (ChatMessage) payload;
                            Ctx.getInstance().imageService.take(chatMessage.getFrom(), result -> {
                                if (result.status().isSuccess()) {
                                    chatMessageComponentCollection.add(new ChatMessageComponent(chatMessage, result.entity().get()));
                                } else {
                                    log.warn("Profile image cannot be taken");
                                }
                            });
                        } else if (appStompHeaders.getPayloadType() == PayloadType.LIVE_SESSION_INFORMATION) {
                            var liveSessionInfo = (LiveSessionInformation) payload;
                            liveSessionInfo.getParticipants().parallelStream().forEach(p -> {
                                Ctx.getInstance().imageService.take(p, result -> {
                                    if (result.status().isSuccess()) {
                                        attendeeComponentMap.add(p, new AttendeeComponent(p, result.entity().get()));
                                    } else {
                                        log.warn("Profile image cannot be taken");
                                    }
                                });
                            });
                        } else if (appStompHeaders.getPayloadType() == PayloadType.PARTICIPATION_NOTIFICATION) {
                            var participationNotification = (ParticipationNotification) payload;
                            if (participationNotification.isJoined()) {
                                Ctx.getInstance().imageService.take(participationNotification.getParticipant(), result -> {
                                    if (result.status().isSuccess()) {
                                        attendeeComponentMap.add(participationNotification.getParticipant(), new AttendeeComponent(participationNotification.getParticipant(), result.entity().get()));
                                    } else {
                                        log.warn("Profile image cannot be taken");
                                    }
                                });
                            } else {
                                attendeeComponentMap.remove(participationNotification.getParticipant());
                            }
                        }
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