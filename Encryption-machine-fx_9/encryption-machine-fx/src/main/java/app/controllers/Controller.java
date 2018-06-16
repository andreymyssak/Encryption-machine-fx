package app.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import app.objects.Logger;
import app.service.AesCipherService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;

public class Controller {
    @FXML
    private JFXButton btnChooseDirectory;
    @FXML
    private JFXButton btnEncrypt;
    @FXML
    private JFXButton btnDecrypt;
    @FXML
    private JFXButton btnChooseFiles;
    @FXML
    private JFXButton btnOpenDirectory;

    @FXML
    private JFXTextField txtFieldUrlDirectory;
    @FXML
    private JFXTextField txtFieldSelectedFiles;
    @FXML
    private JFXTextField txtFieldMySecretKey;

    @FXML
    private JFXTextArea txtAreaLogs;

    @FXML
    private JFXRadioButton radioButtonYes;
    @FXML
    private JFXRadioButton radioButtonNo;

    @FXML
    private ToggleGroup toggleGroup;

    private AesCipherService aesCipherService = new AesCipherService();
    private List<File> files;
    private Logger logger = new Logger();

    @FXML
    private void initialize() {
        logger.setTxtAreaLogs(txtAreaLogs);
//        SecureRandom secureRandom = new SecureRandom();
//
//        byte[] initVector = {1, 9, 65, 13, 43, 74, 12, 45, 2, 34, 75, 13, 10, 78, 25, 23};
//
//        aesCipherService.getAesCipher().setInitVector(initVector);
        logger.printLog("Поехали!" + "\n");
    }

    @FXML
    private void onActionChooseDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(btnChooseDirectory.getScene().getWindow());

        if (dir != null) {
            txtFieldUrlDirectory.setText(dir.getAbsolutePath());
            logger.printLog("Выбранная директория: " + "\n" + dir.getAbsolutePath() + "\n");
        } else {
            txtFieldUrlDirectory.setText(null);
            logger.printLog("Директория не выбрана!" + "\n");
        }
    }

    @FXML
    private void onActionChooseFiles(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        files = fileChooser.showOpenMultipleDialog(btnEncrypt.getScene().getWindow());

        try {
            StringBuilder stringBuilder = new StringBuilder();

            if (files.size() != 0) {
                logger.printLog("Выбранные файлы: ");
                for (int i = 0; i < files.size(); i++) {
                    stringBuilder.append("\"").append(files.get(i).getName()).append("\"").append(" ").append("\n");

                    if(i != files.size() - 1){
                        logger.printLog((i + 1) + ") \"" + files.get(i).getPath() + "\"");
                    } else logger.printLog((i + 1) + ") \"" + files.get(i).getPath() + "\"" + "\n");
                }
            } else {
                logger.printLog("Файлы не выбраны!" + "\n");
                txtFieldSelectedFiles.setText("");
            }

            txtFieldSelectedFiles.setText(stringBuilder.toString());
        } catch (Exception ex) {
            logger.printLog("Файлы не выбраны!" + "\n");
            txtFieldSelectedFiles.setText("");
        }

    }

    @FXML
    private void onActionDecrypt(ActionEvent event) {
        try {
            if (toggleGroup.getSelectedToggle() == radioButtonYes) {
                aesCipherService.getAesCipher().setSecretKey(txtFieldMySecretKey.getText());
            }

            AesCipherService.initAesCipher(files, txtFieldUrlDirectory, "DECRYPT_MODE");
            logger.printLog("Файлы успешно расшифрованы!" + "\n");
        } catch (Throwable cause) {
            logger.printLog(cause.getMessage() + "\n");
        }
    }

    @FXML
    private void onActionEncrypt(ActionEvent event) {

        try {
            if (toggleGroup.getSelectedToggle() == radioButtonYes) {
                aesCipherService.getAesCipher().setSecretKey(txtFieldMySecretKey.getText());
            }

            AesCipherService.initAesCipher(files, txtFieldUrlDirectory, "ENCRYPT_MODE");
            logger.printLog("Файлы успешно зашифрованы!" + "\n");
        } catch (Throwable cause) {
            logger.printLog(cause.getMessage() + "\n");
        }
    }

    @FXML
    private void onActionOpenDirectory(ActionEvent event) {

        try {

            if (!txtFieldUrlDirectory.getText().trim().isEmpty()) {
                try {
                    Desktop.getDesktop().open(new File(txtFieldUrlDirectory.getText() + "\\"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else logger.printLog("Директория не выбрана. Выберите директорию!" + "\n");

        } catch (Exception ex) {
            logger.printLog("Директория не выбрана. Выберите директорию!" + "\n");
        }


    }

    @FXML
    private void onActionClickNo(ActionEvent event) {
        txtFieldMySecretKey.setDisable(true);
        aesCipherService.getAesCipher().setDefaultSecretKey();
    }


    @FXML
    private void onActionClickYes(ActionEvent event) {
        txtFieldMySecretKey.setDisable(false);
    }
}
