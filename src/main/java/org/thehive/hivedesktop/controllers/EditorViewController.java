package org.thehive.hivedesktop.controllers;

import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import eu.mihosoft.monacofx.Document;
import eu.mihosoft.monacofx.MonacoFX;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Scanner;


public class EditorViewController implements Initializable {

    @FXML
    MFXButton btnRunCode;

    @FXML
    MFXButton btnAddNewTab;


    public TabPane terminalPane = new TabPane();

    @FXML
    private TabPane editorPane;

    @FXML
    Tab firstTab;



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
        terminalPane.getTabs().add(terminal);





    }
}
