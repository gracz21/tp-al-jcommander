package pl.poznan.put.fc.tpal.jcommander.util;

import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * @author Kamil Walkowiak
 */
public class BundleUtil extends Observable {
    private static BundleUtil ourInstance = new BundleUtil();
    private Locale locale;
    private ResourceBundle bundle;

    public static BundleUtil getInstance() {
        return ourInstance;
    }

    private BundleUtil() {
        this.locale = new Locale("pl");
        this.bundle = ResourceBundle.getBundle("strings", locale);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = new Locale(locale);
        updateBundle();
    }

    synchronized private void updateBundle() {
        this.bundle = ResourceBundle.getBundle("strings", locale);
        this.setChanged();
        this.notifyObservers();
    }
}
