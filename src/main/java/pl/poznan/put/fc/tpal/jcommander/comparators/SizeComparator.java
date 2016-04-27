package pl.poznan.put.fc.tpal.jcommander.comparators;

import java.util.Comparator;

/**
 * @author Kamil Walkowiak
 */
public class SizeComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        long size1 = 0;
        long size2 = 0;
        if (!o1.equals("<DIR>")) {
            size1 = Long.parseLong(o1);
        }
        if (!o2.equals("<DIR>")) {
            size2 = Long.parseLong(o2);
        }

        if(size1 < size2) {
            return -1;
        } else if (size1 == size2) {
            return 0;
        } else return 1;
    }
}
