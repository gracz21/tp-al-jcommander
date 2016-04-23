package pl.poznan.put.fc.tpal.jcommander.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * @author Kamil Walkowiak
 */
public class NameColumnEntry {
    private StringProperty fileName;
    private Image icon;

    public NameColumnEntry(String fileName, Icon swingIcon) {
        this.fileName = new SimpleStringProperty(fileName);
        this.icon = convertIcon(swingIcon);
    }

    public String getFileName() {
        return fileName.get();
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public Image getIcon() {
        return icon;
    }

    private static Image convertIcon(Icon swingIcon) {
        BufferedImage bufferedImage = new BufferedImage(swingIcon.getIconWidth(), swingIcon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        swingIcon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
}
