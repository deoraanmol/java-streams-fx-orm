package utils;

public class FileUtils {
    public static String clearSpaces(String input) {
        if (input == null) {
            return input;
        }
        return input.replaceAll("(\\r|\\n)", "");
    }
}
