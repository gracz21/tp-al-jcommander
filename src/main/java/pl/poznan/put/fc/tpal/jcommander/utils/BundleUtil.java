package pl.poznan.put.fc.tpal.jcommander.utils;

import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * @author Kamil Walkowiak
 */
public class BundleUtil extends Observable {

    private static BundleUtil ourInstance = new BundleUtil();
    private Locale currentLocale;
    private Locale englishLocale;
    private Locale polishLocale;
    private ResourceBundle bundle;

    public static BundleUtil getInstance() {
        return ourInstance;
    }

    private BundleUtil() {
        this.englishLocale = new Locale("en");
        this.polishLocale = new Locale("pl");
        this.currentLocale = englishLocale;
        this.bundle = ResourceBundle.getBundle("strings", currentLocale);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void setCurrentLocale(String locale) {
        if (locale.equals("en")) {
            this.currentLocale = englishLocale;
        } else {
            this.currentLocale = polishLocale;
        }
        updateBundle();
    }

    synchronized private void updateBundle() {
        this.bundle = ResourceBundle.getBundle("strings", currentLocale);
        this.setChanged();
        this.notifyObservers();
    }
}
