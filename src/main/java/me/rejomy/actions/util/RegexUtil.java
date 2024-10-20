package me.rejomy.actions.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class RegexUtil {

    /**
     * Finds the first occurrence of a substring within a given message
     * that matches the specified regular expression. The method captures
     * the first group from the matched substring and returns it.
     *
     * @param message the input string in which to search for the pattern
     * @param regex the regular expression to be used for matching
     * @return the first captured group from the matched substring, or
     *         null if no match is found
     */
    public String findFirst(String message, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}
