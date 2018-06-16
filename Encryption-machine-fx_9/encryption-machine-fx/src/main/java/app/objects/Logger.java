package app.objects;

import com.jfoenix.controls.JFXTextArea;

public class Logger {
    private JFXTextArea txtAreaLogs;

    public Logger() {
    }

    public void printLog(String text) {
        txtAreaLogs.appendText(text + "\n");
    }

    public JFXTextArea getTxtAreaLogs() {
        return txtAreaLogs;
    }

    public void setTxtAreaLogs(JFXTextArea txtAreaLogs) {
        this.txtAreaLogs = txtAreaLogs;
    }
}
