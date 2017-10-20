package pl.poznan.put.fc.tpal.jcommander.utils.comparators;

import pl.poznan.put.fc.tpal.jcommander.models.NameColumnEntry;

import java.util.Comparator;

/**
 * @author Kamil Walkowiak
 */
public class NameComparator implements Comparator<NameColumnEntry> {
    @Override
    public int compare(NameColumnEntry o1, NameColumnEntry o2) {
        if (o1.isDirectory()) {
            if (o2.isDirectory()) {
                return o1.getFileName().compareTo(o2.getFileName());
            } else {
                return -1;
            }
        } else {
            if (!o2.isDirectory()) {
                return o1.getFileName().compareTo(o2.getFileName());
            } else {
                return 1;
            }
        }
    }
}
