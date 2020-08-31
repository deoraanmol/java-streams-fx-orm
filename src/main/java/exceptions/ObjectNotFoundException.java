package exceptions;

public class ObjectNotFoundException extends Exception {
    public ObjectNotFoundException(String objectType, String objectId, String fileName) {
        super(objectType + " ID: " + objectId + " does not exist in: " + fileName);
    }
}
