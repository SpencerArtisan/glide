package com.bigcustard.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private final String string;
    private final String[] delimiters;

    public Tokenizer(String string, String[] delimiters) {
        this.string = string;
        this.delimiters = delimiters;
    }

    public List<String> run() {
        // First, create a regular expression that matches the union of the delimiters
        // Be aware that, in case of delimiters containing others (example && and &),
        // the longer may be before the shorter (&& should be before &) or the regexpr
        // parser will recognize && as two &.
        Arrays.sort(delimiters, (o1, o2) -> -o1.compareTo(o2));
        // Build a string that will contain the regular expression
        StringBuilder regexpr = new StringBuilder();
        regexpr.append('(');
        for (String delim : delimiters) { // For each delimiter
            if (regexpr.length() != 1) regexpr.append('|'); // Add union separator if needed
            for (int i = 0; i < delim.length(); i++) {
                // Add an escape character if the character is a regexp reserved char
                regexpr.append('\\');
                regexpr.append(delim.charAt(i));
            }
        }
        regexpr.append(')'); // Close the union
        Pattern p = Pattern.compile(regexpr.toString());

        // Now, search for the tokens
        List<String> res = new ArrayList<String>();
        Matcher m = p.matcher(string);
        int pos = 0;
        while (m.find()) { // While there's a delimiter in the string
            if (pos != m.start()) {
                // If there's something between the current and the previous delimiter
                // Add it to the tokens list
                res.add(string.substring(pos, m.start()));
            }
            res.add(m.group()); // watch the delimiter
            pos = m.end(); // Remember end of delimiter
        }
        if (pos != string.length()) {
            // If it remains some characters in the string after last delimiter
            // Add this to the token list
            res.add(string.substring(pos));
        }
        // Return the result
        return res;
    }
}
