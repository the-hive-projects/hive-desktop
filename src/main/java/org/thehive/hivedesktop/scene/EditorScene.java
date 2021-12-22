package org.thehive.hivedesktop.scene;

import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import eu.mihosoft.monacofx.MonacoFX;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
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
import org.thehive.hiveserverclient.Authentication;
import org.thehive.hiveserverclient.model.Session;
import org.thehive.hiveserverclient.model.Submission;
import org.thehive.hiveserverclient.net.websocket.header.AppStompHeaders;
import org.thehive.hiveserverclient.net.websocket.header.PayloadType;
import org.thehive.hiveserverclient.net.websocket.subscription.StompSubscription;
import org.thehive.hiveserverclient.net.websocket.subscription.SubscriptionListener;
import org.thehive.hiveserverclient.payload.*;

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
        private final ObservableCollection<String> receiverCollection;
        private final ObservableMap<String, AttendeeComponent> attendeeComponentMap;
        private final Map<String, Tab> usernameTabMap;

        Timer timer = new Timer();
        @FXML
        ScrollPane chatScroll;
        @FXML
        VBox chatBox;
        @FXML
        MFXButton btnSaveCode;
        @FXML
        MFXButton btnSubmit;
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
                    ExecutionUtils.runOnUiThread(() -> chatBox.getChildren().add(chatMessageComponent.getParentNode()));
                }
            });
            this.receiverCollection = new ObservableCollectionWrapper<>(Collections.synchronizedList(new ArrayList<>()));
            receiverCollection.registerObserver(receivers -> {
                // TODO: 12/22/2021 update receiver list
            });
            this.attendeeComponentMap = new ObservableMapWrapper<>(new HashMap<>());
            attendeeComponentMap.registerObserver(new MapObserverAdapter<>() {
                @Override
                public void onAdded(String username, AttendeeComponent attendeeComponent) {
                    ExecutionUtils.runOnUiThread(() -> {
                        attendeeList.getChildren().add(attendeeComponent.getParentNode());
                        addTab(username);
                    });
                }

                @Override
                public void onRemoved(String username, AttendeeComponent attendeeComponent) {
                    ExecutionUtils.runOnUiThread(() -> {
                        attendeeList.getChildren().remove(attendeeComponent.getParentNode());
                        removeTab(username);
                    });
                }

                @Override
                public void onCleared(Map<String, AttendeeComponent> map) {
                    ExecutionUtils.runOnUiThread(() -> attendeeList.getChildren().clear());
                }
            });
            this.usernameTabMap = new HashMap<>();
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
            var currentTabIndex = editorPane.getSelectionModel().getSelectedIndex();
            MonacoFX currentEditor = (MonacoFX) editorPane.getTabs().get(currentTabIndex).getContent();

            var currentEditorContent = currentEditor.getEditor().getDocument().getText();

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
                    ExecutionUtils.runOnUiThread(() -> {
                        btnSaveCode.setText("Save");
                        btnSaveCode.setMinWidth(0);
                    });
                }
            }, 5000);

        }

        @FXML
        private MonacoFX setEditor(String language, String theme) {
            MonacoFX monacoFxEditor = new MonacoFX();
            monacoFxEditor.getEditor().getDocument().setText(
                    "num = float(input(\"Enter a number: \"))\r" +
                            "if num > 0:\n" +
                            "   print(\"Positive number\")\n" +
                            "elif num == 0:\n" +
                            "   print(\"Zero\")\n" +
                            "else:\n" +
                            "   print(\"Negative number\")\n");
            // use a predefined language like 'c'
            monacoFxEditor.getEditor().setCurrentLanguage(language);
            monacoFxEditor.getEditor().setCurrentTheme(theme);
            return monacoFxEditor;
        }

        private Tab addTab(String tabName) {
            var tab = usernameTabMap.get(tabName);
            if (tab != null)
                return tab;
            tab = new Tab(tabName);
            tab.setId(tabName);
            var editor = setEditor("python", "vs-dark");
            tab.setContent(editor);
            tab.setClosable(false);
            usernameTabMap.put(tabName, tab);
            var userTab = tabName.equals(Authentication.INSTANCE.getUsername());
            if (!userTab)
                tab.getContent().addEventFilter(EventType.ROOT, Event::consume);
            else
                ((MonacoFX) tab.getContent()).getEditor().getDocument().textProperty()
                        .addListener((observableValue, s, t1) -> {
                            if (receiverCollection.size() > 0) {
                                var payload = new CodeBroadcastingInformation();
                                payload.setText(t1);
                                Ctx.getInstance().webSocketService.getConnection().get().getSessionSubscription().get().send(payload);
                            }
                        });
            if (userTab) {
                editorPane.getTabs().add(0, tab);
                if (editorPane.getSelectionModel().getSelectedIndex() != 0)
                    editorPane.getSelectionModel().select(0);
            } else
                editorPane.getTabs().add(tab);
            return tab;
        }

        private void removeTab(String tabName) {
            var tab = usernameTabMap.remove(tabName);
            if (tab != null)
                editorPane.getTabs().remove(tab);
        }

        @FXML
        private File runEditorCode(TerminalTab terminal) throws IOException {
            var currentTabIndex = editorPane.getSelectionModel().getSelectedIndex();
            MonacoFX currentEditor = (MonacoFX) editorPane.getTabs().get(currentTabIndex).getContent();

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
        private void sendMessage() {
            String message = messageArea.getText();
            if (message.isEmpty())
                return;
            var chatMessage = new ChatMessage();
            messageArea.clear();
            chatMessage.setText(message);
            Ctx.getInstance().webSocketService.getConnection().get().getSessionSubscription().get().send(chatMessage);
        }

        @FXML
        void onBtnSubmitClick(MouseEvent event) {
            var tab = usernameTabMap.get(Authentication.INSTANCE.getUsername());
            var content = ((MonacoFX) tab.getContent()).getEditor().getDocument().getText();
            var submission = new Submission();
            submission.setContent(content);
            btnSubmit.setDisable(true);
            Ctx.getInstance().submissionService.submit(submission, appResponse -> {
                if (appResponse.status().isSuccess()) {
                    log.info("Successfully submitted");
                } else if (appResponse.status().isError()) {
                    log.info("Error while submitting, message: {}", appResponse.message().get());
                    btnSubmit.setDisable(false);
                } else {
                    log.warn("Submission got failed", appResponse.exception().get());
                    btnSubmit.setDisable(false);
                }
            });
        }

        @Override
        public void onStart() {
            TerminalConfig darkConfig = new TerminalConfig();
            darkConfig.setBackgroundColor(Color.web("#1e1e1e"));
            darkConfig.setForegroundColor(Color.rgb(240, 240, 240));
            darkConfig.setCursorColor(Color.web("#ffc107"));

            /*
            TerminalConfig cygwinConfig = new TerminalConfig();
            cygwinConfig.setWindowsTerminalStarter("C:\\cygwin64\\bin\\bash -i");
            cygwinConfig.setFontSize(14);
            */

            TerminalBuilder terminalBuilder = new TerminalBuilder(darkConfig);
            TerminalTab terminal = terminalBuilder.newTerminal();
            terminalPane.getTabs().add(terminal);

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
                if (Ctx.getInstance().webSocketService.getConnection().isPresent() &&
                        Ctx.getInstance().webSocketService.getConnection().get().getSessionSubscription().isPresent()) {
                    Ctx.getInstance().webSocketService.getConnection().get().getSessionSubscription().get().unsubscribe();
                }
                Ctx.getInstance().sceneManager.load(MainScene.class);
            });
            btnSendMessage.setOnMouseClicked(mouseEvent -> sendMessage());
            btnSubmit.setDisable(true);

            editorPane.getSelectionModel().selectedItemProperty().addListener(
                    (ov, t, t1) -> {
                        if (t == null)
                            return;
                        var preTabId = t.getId();
                        var newTabId = t1.getId();
                        var username = Authentication.INSTANCE.getUsername();
                        if (!preTabId.equals(username)) {
                            var payload = new CodeReceivingRequest();
                            payload.setBroadcaster(preTabId);
                            payload.setStart(false);
                            Ctx.getInstance().webSocketService.getConnection().get().getSessionSubscription().get()
                                    .send(payload);
                        }
                        if (!newTabId.equals(username)) {
                            var payload = new CodeReceivingRequest();
                            payload.setBroadcaster(newTabId);
                            payload.setStart(true);
                            Ctx.getInstance().webSocketService.getConnection().get().getSessionSubscription().get()
                                    .send(payload);
                        }
                    }
            );

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
                        Ctx.getInstance().submissionService.takeThis(appResponse -> {
                            if (appResponse.status().isSuccess()) {
                                if (appResponse.response().get() == Submission.EMPTY) {
                                    ExecutionUtils.runOnUiThread(() -> btnSubmit.setDisable(false));
                                } else {
                                    ExecutionUtils.runOnUiThread(() -> {
                                        var tab = usernameTabMap.get(Authentication.INSTANCE.getUsername());
                                        if (tab == null)
                                            tab = addTab(Authentication.INSTANCE.getUsername());
                                        ((MonacoFX) tab.getContent()).getEditor().getDocument().setText(appResponse.response().get().getContent());
                                    });
                                }
                                // TODO: 12/20/2021 show submission on ui
                            }
                        });
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
                                    try {
                                        chatMessageComponentCollection.add(new ChatMessageComponent(chatMessage, result.response().get()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    log.warn("Profile image cannot be taken");
                                }
                            });
                        } else if (appStompHeaders.getPayloadType() == PayloadType.LIVE_SESSION_INFORMATION) {
                            var liveSessionInfo = (LiveSessionInformation) payload;
                            attendeeComponentMap.clear();
                            liveSessionInfo.getParticipants().parallelStream().forEach(p -> {
                                Ctx.getInstance().imageService.take(p, result -> {
                                    if (result.status().isSuccess()) {
                                        attendeeComponentMap.add(p, new AttendeeComponent(p, result.response().get()));
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
                                        attendeeComponentMap.add(participationNotification.getParticipant(), new AttendeeComponent(participationNotification.getParticipant(), result.response().get()));
                                    } else {
                                        log.warn("Profile image cannot be taken");
                                    }
                                });
                            } else {
                                attendeeComponentMap.remove(participationNotification.getParticipant());
                            }
                        } else if (appStompHeaders.getPayloadType() == PayloadType.CODE_BROADCASTING_NOTIFICATION) {
                            var codeBroadcastingNotification = (CodeBroadcastingNotification) payload;
                            receiverCollection.clear();
                            receiverCollection.addAll(codeBroadcastingNotification.getReceivers());
                            if (codeBroadcastingNotification.getReceivers().size() > 0) {
                                var sendPayload = new CodeBroadcastingInformation();
                                var tab = usernameTabMap.get(Authentication.INSTANCE.getUsername());
                                var text = ((MonacoFX) tab.getContent()).getEditor().getDocument().getText();
                                sendPayload.setText(text);
                                Ctx.getInstance().webSocketService.getConnection().get().getSessionSubscription().get().send(sendPayload);
                            }
                        } else if (appStompHeaders.getPayloadType() == PayloadType.CODE_BROADCASTING_INFORMATION) {
                            var codeBroadcastingInformation = (CodeBroadcastingInformation) payload;
                            var tab = usernameTabMap.get(codeBroadcastingInformation.getBroadcaster());
                            ExecutionUtils.runOnUiThread(() -> ((MonacoFX) tab.getContent()).getEditor().getDocument().setText(codeBroadcastingInformation.getText()));
                        }
                    }

                    @Override
                    public void onUnsubscribe(StompSubscription stompSubscription) {

                    }
                });
            });
            messageArea.setWrapText(true);
            messageArea.setOnKeyPressed(ke -> {
                if (ke.getCode().equals(KeyCode.ENTER))
                    sendMessage();
            });
        }

        @Override
        public void onUnload() {

        }

    }
}