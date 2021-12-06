package org.thehive.hivedesktop.util;

import io.github.palexdev.materialfx.controls.MFXLabel;
import javafx.scene.paint.Color;
import lombok.NonNull;
import org.thehive.hivedesktop.Consts;

public class InfoLabelHandler {

    public final MFXLabel label;
    public final Color defaultColor;
    public final Color successColor;
    public final Color warningColor;

    public InfoLabelHandler(@NonNull MFXLabel label) {
        this(label, Consts.INFO_LABEL_DEFAULT_COLOR, Consts.INFO_LABEL_SUCCESS_COLOR, Consts.INFO_LABEL_ERROR_COLOR);
    }

    public InfoLabelHandler(@NonNull MFXLabel label, @NonNull Color defaultColor, @NonNull Color successColor, @NonNull Color warningColor) {
        this.label = label;
        this.defaultColor = defaultColor;
        this.successColor = successColor;
        this.warningColor = warningColor;
    }

    public void setDefaultText(@NonNull String text) {
        label.setTextFill(defaultColor);
        label.setText(text);
    }

    public void setSuccessText(@NonNull String text) {
        label.setTextFill(successColor);
        label.setText(text);
    }

    public void setWaringText(@NonNull String text) {
        label.setTextFill(warningColor);
        label.setText(text);
    }

    public void setText(@NonNull Status status, @NonNull String text) {
        switch (status) {
            case DEFAULT:
                setDefaultText(text);
                return;
            case SUCCESS:
                setSuccessText(text);
                return;
            case WARNING:
                setWaringText(text);
                return;
            default:
                throw new IllegalArgumentException("Unhandled status argument, status: " + status.name());
        }
    }

    public void clearText() {
        label.setText(Consts.EMPTY_STRING);
    }

    public enum Status {
        DEFAULT,
        SUCCESS,
        WARNING
    }

}
