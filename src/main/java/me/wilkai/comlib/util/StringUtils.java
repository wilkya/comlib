package me.wilkai.comlib.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    /**
     * Calculates the Levenshtein / Edit distance between two strings.
     * That is the minimum amount of edits required to make a identical to b.
     *
     * @return The distance between the two strings.
     */
    public static int stringDistance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    /**
     * Finds the closest matching string in a list.
     *
     * @param string      The string to compare.
     * @param comparisons The strings to compare it to.
     * @return The closest match, or null if arguments were invalid.
     */
    public static String closestMatch(String string, List<String> comparisons) {

        if (string == null) return null;
        if (string.length() == 0) return null;
        if (comparisons == null) return null;
        if (comparisons.size() == 0) return null;

        String closest = comparisons.get(0);
        int closestDistance = stringDistance(string, closest);

        for (int i = 1; i < comparisons.size(); i++) {
            String comparison = comparisons.get(i);

            int distance = stringDistance(string, comparison);

            if (distance < closestDistance) {
                closest = comparison;
                closestDistance = distance;
            }
        }

        return closest;
    }

    /**
     * Splits a string on a given delimiter, unless it is surrounded by quotation marks. (", ' or `)
     *
     * @param string    The string to split.
     * @param delimiter The chat to split the string on.
     * @return A list containing the split string.
     */
    public static List<String> quotedSplit(String string, char delimiter) {

        final String quotes = "\"'`"; // Characters that will not be included in the final list, that we use to flip the quoted flag.

        if (string == null) return null; // If the string doesn't exist don't try and read it.
        if (string.length() == 0) return new ArrayList<>(); // If the string has no text don't bother reading it.
        if (quotes.indexOf(delimiter) != -1)
            return null; // Return null if the delimiter is in the list of quotes, because that doesn't make sense.

        StringBuilder builder = new StringBuilder(); // The string currently being read. (As a StringBuilder to optimise speed)
        ArrayList<String> list = new ArrayList<>(); // The split list of Strings.
        boolean quoted = false; // Are we reading quoted text right now?

        for (char c : string.toCharArray()) {
            if (quotes.indexOf(c) != -1) { // If the character matches " ' or `
                quoted = !quoted; // Invert the quoted flag.
                continue; // Don't execute the rest of the loop because we don't want to put the quotation marks in the final list.
            }

            if (c == delimiter && !quoted) { // If a delimiter has been found and we are not reading a quote.
                list.add(builder.toString()); // Add the String to the list.
                builder = new StringBuilder(); // Reset the string builder.
                continue; // Continue because putting the delimiter in the list doesn't make sense.
            }

            // If none of the above, add the char to the string.
            builder.append(c);
        }

        list.add(builder.toString());

        // Return the split list.
        return list;
    }

    public static String formatList(List<?> list) {
        if(list.size() == 1) {
            return list.get(0).toString();
        }

        StringBuilder b = new StringBuilder();

        for(int i = 0; i < list.size(); i++) {
            Object object = list.get(i);

            if(i == list.size() - 1) {
                b.append(" and ");
            } else if(i != 0) {
                b.append(", ");
            }

            b.append(object);
        }

        return b.toString();
    }

}
