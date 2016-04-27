package pl.poznan.put.fc.tpal.jcommander.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import pl.poznan.put.fc.tpal.jcommander.utils.BundleUtil;
import pl.poznan.put.fc.tpal.jcommander.views.SingleTabView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class RootController  implements Observer {
    @FXML
    private ArrayList<TabPane> tabPanes;
    @FXML
    private Menu fileMenu;
    @FXML
    private Menu changeLanguageMenu;
    @FXML
    private Menu helpMenu;
    @FXML
    private MenuItem close;
    @FXML
    private MenuItem about;
    @FXML
    private RadioMenuItem changeToEnglish;
    @FXML
    private RadioMenuItem changeToPolish;

    @FXML
    private void initialize() throws IOException {
        for(TabPane tabPane: tabPanes) {
            tabPane.getTabs().stream().forEach(tab ->tab.setClosable(false));
            setTabContent(tabPane.getTabs().get(0));

            Tab addNewTab = tabPane.getTabs().get(1);
            tabPane.setOnMouseClicked(event -> {
                if(addNewTab.isSelected()) {
                    Tab newTab = createTab();
                    int position = tabPane.getTabs().size() - 1;
                    tabPane.getTabs().add(position, newTab);
                    try {
                        setTabContent(newTab);
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    tabPane.getSelectionModel().clearAndSelect(position);
                }
                event.consume();
            });
        }

        if(BundleUtil.getInstance().getCurrentLocale().getLanguage().equals("en")) {
            changeToEnglish.setSelected(true);
        } else {
            changeToPolish.setSelected(true);
        }
    }

    @FXML
    private void handleMenuClose() {
        Platform.exit();
    }

    @FXML
    private void handleMenuToEnglish() {
        BundleUtil bundleUtil = BundleUtil.getInstance();
        if(!bundleUtil.getCurrentLocale().getLanguage().equals("en")) {
            BundleUtil.getInstance().setCurrentLocale("en");
        }
    }

    @FXML
    private void handleMenuToPolish() {
        BundleUtil bundleUtil = BundleUtil.getInstance();
        if(!bundleUtil.getCurrentLocale().getLanguage().equals("pl")) {
            BundleUtil.getInstance().setCurrentLocale("pl");
        }
    }

    private Tab createTab() {
        Tab tab = new Tab();
        tab.setContent(new BorderPane());
        return tab;
    }

    private void setTabContent(Tab tab) throws IOException {
        SingleTabView singleTabView = new SingleTabView();

        BorderPane borderPane = (BorderPane) tab.getContent();
        borderPane.setCenter(singleTabView.getLayout());
        singleTabView.getLayout().prefWidthProperty().bind(borderPane.widthProperty());
        singleTabView.getLayout().prefHeightProperty().bind(borderPane.heightProperty());
        tab.textProperty().bind(singleTabView.getController().currentDirectoryProperty());
    }

    @Override
    public void update(Observable o, Object arg) {
        ResourceBundle bundle = BundleUtil.getInstance().getBundle();

        fileMenu.setText(bundle.getString("menu.file"));
        changeLanguageMenu.setText(bundle.getString("menu.changeLanguage"));
        helpMenu.setText(bundle.getString("menu.help"));

        close.setText(bundle.getString("file.close"));
        about.setText(bundle.getString("help.about"));
    }
}
