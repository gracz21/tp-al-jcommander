package pl.poznan.put.fc.tpal.jcommander.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Kamil Walkowiak
 */
public abstract class DialogUtil {
    public static boolean deleteDialog() {
        ResourceBundle bundle = BundleUtil.getInstance().getBundle();

        Alert dialog = new Alert(Alert.AlertType.WARNING);
        dialog.setTitle(bundle.getString("warningTitle"));
        dialog.setHeaderText(bundle.getString("deleteHeader"));
        dialog.setContentText(bundle.getString("deleteWarning"));

        ButtonType buttonYes = new ButtonType(bundle.getString("yesOption"));
        ButtonType buttonNo = new ButtonType(bundle.getString("noOption"), ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getButtonTypes().setAll(buttonYes, buttonNo);
        Optional<ButtonType> selectedOption = dialog.showAndWait();

        return selectedOption.isPresent() && selectedOption.get() == buttonYes;
    }

    public static ReplaceOptionsUtil.replaceOptions replaceDialog(String fileName, String[] sizes, String[] dates) {
        final ReplaceOptionsUtil.replaceOptions result;
        ResourceBundle bundle = BundleUtil.getInstance().getBundle();

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle(bundle.getString("warningTitle"));
        dialog.setHeaderText(bundle.getString("fileExistHeader"));
        dialog.setContentText(bundle.getString("replace") + "\n" + fileName + "\n" + sizes[0] + " B, " + dates[0] +
        "\n" + bundle.getString("with") + "\n" + fileName + "\n" + sizes[1] + " B, " + dates[1]);

        ButtonType buttonReplace = new ButtonType(bundle.getString("replaceOption"));
        ButtonType buttonKeep = new ButtonType(bundle.getString("keepOption"));
        ButtonType buttonSkip = new ButtonType(bundle.getString("skipOption"));
        ButtonType buttonReplaceAll = new ButtonType(bundle.getString("replaceAllOption"));
        ButtonType buttonKeepAll = new ButtonType(bundle.getString("keepAllOption"));
        ButtonType buttonSkipAll = new ButtonType(bundle.getString("skipAllOption"));
        ButtonType buttonCancel = new ButtonType(bundle.getString("cancelOption"), ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getButtonTypes().setAll(buttonReplace, buttonKeep, buttonSkip,
                buttonReplaceAll, buttonKeepAll, buttonSkipAll, buttonCancel);
        Optional<ButtonType> selectedOption = dialog.showAndWait();

        if(selectedOption.isPresent()) {
            if(selectedOption.get() == buttonReplace) {
                result = ReplaceOptionsUtil.replaceOptions.YES;
            } else if(selectedOption.get() == buttonKeep) {
                result = ReplaceOptionsUtil.replaceOptions.KEEP;
            } else if(selectedOption.get() == buttonSkip) {
                result = ReplaceOptionsUtil.replaceOptions.NO;
            } else if(selectedOption.get() == buttonReplaceAll) {
                result = ReplaceOptionsUtil.replaceOptions.YES_ALL;
            } else if(selectedOption.get() == buttonKeepAll) {
                result = ReplaceOptionsUtil.replaceOptions.KEEP_ALL;
            } else if(selectedOption.get() == buttonSkipAll) {
                result = ReplaceOptionsUtil.replaceOptions.NO_ALL;
            } else {
                result = ReplaceOptionsUtil.replaceOptions.CANCEL;
            }
        } else {
            result = ReplaceOptionsUtil.replaceOptions.CANCEL;
        }

        return result;
    }
}
