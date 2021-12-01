package org.thehive.hivedesktop.controllers;

import eu.mihosoft.monacofx.MonacoFX;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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
