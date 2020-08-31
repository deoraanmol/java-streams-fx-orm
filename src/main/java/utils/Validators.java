package utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Validators {
    public static boolean isEmpty(String text) {
        if (text != null && text.trim().length() > 0) {
            return false;
        }
        return true;
    }

    public static boolean isAnyStringEmpty(List<String> texts) {
        for (String text: texts) {
            if(isEmpty(text)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAllStringsEmpty(List<String> texts) {
        if(texts == null || texts.size() == 0) {
            return true;
        }
        for (String text: texts) {
            if(!isEmpty(text)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasEqualStrings(List<String> texts) {
        boolean allEqual = texts.stream().distinct().limit(2).count() <= 1;
        return allEqual;
    }


    public static boolean isInvalidSelectionForAddition(Map selection) {
        Set<String> selectedKeys = selection.keySet();
        return selectedKeys.size() != 1;
    }

    public static boolean isInvalidSelectionForSwap(Map selection) {
        Set<String> selectedKeys = selection.keySet();
        return selectedKeys.size() != 2;
    }

}
