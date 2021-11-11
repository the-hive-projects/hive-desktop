package org.thehive.hivedesktop.controllers;

import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import eu.mihosoft.monacofx.MonacoFX;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.thehive.hivedesktop.EditorView;

import java.net.URL;
import java.util.ResourceBundle;


public class VSEditor implements Initializable {



    @FXML
    MonacoFX monacoFXeditor;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // set initial text
        monacoFXeditor.getEditor().getDocument().setText(
                "#include <stdio.h>\n" +
                        "int main() {\n" +
                        "   // printf() displays the string inside quotation\n" +
                        "   printf(\"Hello, World!\");\n" +
                        "   return 0;\n" +
                        "}");




        // use a predefined language like 'c'
        monacoFXeditor.getEditor().setCurrentLanguage("c");
        monacoFXeditor.getEditor().setCurrentTheme("vs-dark");








    }
}
