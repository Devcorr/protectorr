package protectorr.controllers;

import com.itextpdf.text.DocumentException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import org.apache.commons.io.FilenameUtils;

import com.pdfcorr.service.PdfProtectionService;
import java.io.File;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import de.jensd.fx.fontawesome.Icon;
import protectorr.validators.PasswordValidator;

public class ProtectionController {

    @FXML
    private GridPane mainWindow;

    @FXML
    private Text pdfToProtectName;

    @FXML
    private Text message;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Icon pdfIcon;

    private File pdfToProtectFile;

    @FXML
    protected void handleProtectPdfFileAction() {
        //call pdf protection service passing in the file object if not null,
        // and the validated password input string.
        File pdfFile = this.getPdfToProtectFile();
        PdfProtectionService protectionService = new PdfProtectionService();
        PasswordValidator validator = new PasswordValidator(passwordField.getText());
        if (pdfFile != null) {
            if (validator.isValidPassword()) {
                if (pdfFile.exists()) {
                    try {
                        protectionService.protectPdfDocument(passwordField.getText(), pdfFile);
                        message.setText("The file has been protected.");
                    } catch(IOException e) {
                        e.printStackTrace();
                        message.setText("The file is already protected by a password and cannot be protected.");
                    } catch(DocumentException de) {
                        de.printStackTrace();
                        message.setText("There is something wrong with the file, and it cannot be protected.");
                    }
                }
            } else {
                message.setText("The password did not meet the minimum requirements");
            }
        }
        else {
            message.setText("An open pdf file was not detected. Please open a pdf file and try again.");
        }
    }

    @FXML
    protected void handleBrowseGithubAction() {
        String url = "https://github.com/Devcorr/protectorr";
        openURLinDefaultBrowser(url);
    }

    @FXML
    protected void handleBrowseDevcorrAction() {
        String url = "http://devcorr.com";
        openURLinDefaultBrowser(url);
    }

    @FXML
    protected void handleOpenFileAction() {
        //open the pdf file
        this.setPdfToProtectFile(null);

        //TODO use a file extension filter instead of manually checking the extension.
        final FileChooser fileChooser = new FileChooser();
        File pdfFileToProtect = fileChooser.showOpenDialog(mainWindow.getScene().getWindow());
        final String pdfExtension = "pdf";

        //verify file type is pdf otherwise notify user.
        if (pdfFileToProtect == null || !FilenameUtils.getExtension(pdfFileToProtect.getName()).equalsIgnoreCase(pdfExtension)) {
            //TODO pull message text from messages.properties
            pdfIcon.setVisible(false);
            pdfToProtectName.setText("");
            message.setText("The file type you selected is not right. Please select a pdf file and try again.");
            return;
        }


        if (pdfFileToProtect != null) {
            StringBuffer fileInfoText = new StringBuffer();
            message.setText("");
            pdfIcon.setVisible(true);
            fileInfoText.append(pdfFileToProtect.getName());
            pdfToProtectName.setText(fileInfoText.toString());

            //save off file to the scope of this controller to pass into the pdf protection service.
            this.setPdfToProtectFile(pdfFileToProtect);
        }
    }

    private void openURLinDefaultBrowser(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        else{
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getPdfToProtectFile() {
        return pdfToProtectFile;
    }

    public void setPdfToProtectFile(File pdfToProtectFile) {
        this.pdfToProtectFile = pdfToProtectFile;
    }

}
