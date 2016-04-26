package pl.poznan.put.fc.tpal.jcommander.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Kamil Walkowiak
 */
public abstract class DialogUtil {
    public static ReplaceOptionsUtil.replaceOptions replaceDialog() {
        final ReplaceOptionsUtil.replaceOptions[] result = new ReplaceOptionsUtil.replaceOptions[1];
        ResourceBundle bundle = BundleUtil.getInstance().getBundle();

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle(bundle.getString("warningTitle"));
        dialog.setHeaderText(bundle.getString("deleteHeader"));
        dialog.setContentText(bundle.getString("deleteWarning"));

        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");
        ButtonType buttonYesAll = new ButtonType("Yes all");
        ButtonType buttonNoAll = new ButtonType("No all");

        dialog.getButtonTypes().setAll(buttonYes, buttonNo, buttonYesAll, buttonNoAll);
        Optional<ButtonType> selectedOption = dialog.showAndWait();

        if(selectedOption.isPresent()) {
            if(selectedOption.get() == buttonYes) {
                result[0] = ReplaceOptionsUtil.replaceOptions.YES;
            } else if(selectedOption.get() == buttonNo) {
                result[0] = ReplaceOptionsUtil.replaceOptions.NO;
            } else if(selectedOption.get() == buttonYesAll) {
                result[0] = ReplaceOptionsUtil.replaceOptions.YES_ALL;
            } else {
                result[0] = ReplaceOptionsUtil.replaceOptions.NO_ALL;
            }
        } else {
            result[0] = ReplaceOptionsUtil.replaceOptions.NO;
        }

        return result[0];
    }
}
