package pl.poznan.put.fc.tpal.jcommander.comparators;

import pl.poznan.put.fc.tpal.jcommander.models.NameColumnEntry;

import java.util.Comparator;

/**
 * @author Kamil Walkowiak
 */
public class NameComparator implements Comparator<NameColumnEntry> {
    @Override
    public int compare(NameColumnEntry o1, NameColumnEntry o2) {
        return o1.getFileName().compareTo(o2.getFileName());
    }
}
