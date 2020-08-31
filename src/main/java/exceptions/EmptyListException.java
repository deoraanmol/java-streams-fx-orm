package exceptions;

public class EmptyListException extends Exception {
    public EmptyListException(String objectType, String fileName) {
        super("The list of "+objectType+" is empty!"+" Please ensure some data is existing in file: "+fileName);
    }
}
