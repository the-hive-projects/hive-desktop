package org.thehive.hivedesktop.controllers;

import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import eu.mihosoft.monacofx.MonacoFX;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.thehive.hivedesktop.EditorView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class EditorViewController implements Initializable {

    @FXML
    MFXButton runCode;

    @FXML
    MFXButton btnAddNewTab;


    public TabPane terminalPane = new TabPane();

    @FXML
    private TabPane editorPane;

    @FXML
    Tab firstTab;



    @FXML
    private MonacoFX setEditor(String language,String theme)
    {
        MonacoFX monacoFXeditor = new MonacoFX();
        int numTabs = editorPane.getTabs().size();
        monacoFXeditor.setId(String.valueOf(numTabs));
        monacoFXeditor.getEditor().getDocument().setText(
                "#include <stdio.h>\n" +
                        "int main() {\n" +
                        "   // printf() displays the string inside quotation\n" +
                        "   printf(\"Hello, World!\");\n" +
                        "   return 0;\n" +
                        "}");


        // use a predefined language like 'c'
        monacoFXeditor.getEditor().setCurrentLanguage(language);
        monacoFXeditor.getEditor().setCurrentTheme(theme);
        System.out.println(monacoFXeditor.getId());

        return monacoFXeditor;
    }

    @FXML
    private String addTab() throws IOException {
        int numTabs = editorPane.getTabs().size();
        Tab tab = new Tab("Tab "+ numTabs);
        tab.setId(String.valueOf(numTabs));
        System.out.println(tab.getId());
        tab.setContent(setEditor("python","vs-dark"));
        editorPane.getTabs().add(tab);

        return tab.getId();
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





    }
}
