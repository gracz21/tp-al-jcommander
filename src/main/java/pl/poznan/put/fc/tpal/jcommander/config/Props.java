package pl.poznan.put.fc.tpal.jcommander.config;

import pl.poznan.put.fc.tpal.jcommander.utils.BundleUtil;

import java.util.UUID;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Props {

    private static String sessionId = UUID.randomUUID().toString();

    private static final String SESSION_ID = "session_id";

    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String X = "x";
    private static final String Y = "y";

    private static final String LANGUAGE = "language";

    public static boolean isStorageOk = true;
    public static String errorMessage = "";

    private static final Preferences prefs = Preferences.userNodeForPackage(Props.class);

    public static double width;
    public static double height;
    public static double x;
    public static double y;

    //TODO maximized/minimized


    public static void loadPreferences() {
        width = prefs.getDouble(WIDTH, 800);
        height = prefs.getDouble(HEIGHT, 600);
        x = prefs.getDouble(X, -1);
        y = prefs.getDouble(Y, -1);

        BundleUtil.getInstance().setCurrentLocale(prefs.get(LANGUAGE, "en"));

        try {
            prefs.put(SESSION_ID, sessionId);
            prefs.flush();
        } catch(BackingStoreException e) {
            //e.printStackTrace();
            // java.util.prefs.FileSystemPreferences syncWorld
            // WARNING: Couldn't flush system prefs: java.util.prefs.BackingStoreException:
            // /etc/.java/.systemPrefs/<temporary> create failed.
            isStorageOk = false;
            errorMessage = e.getMessage();
        }
    }

    public static void savePreferences() {
        if (isStorageOk) {
            try {
                prefs.putDouble(WIDTH, width);
                prefs.putDouble(HEIGHT, height);
                prefs.putDouble(X, x);
                prefs.putDouble(Y, y);

                prefs.put(LANGUAGE, BundleUtil.getInstance().getCurrentLocale().getLanguage());
                prefs.flush();
            } catch (BackingStoreException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO clean registry when uninstall
    //prefs.remove(BACKING_STORE_AVAIL);
    // prefs.clear();
    // prefs.removeNode();

    //TODO export/import settings
    //prefs.exportNode();
    //Preferences.importPreferences();
}
