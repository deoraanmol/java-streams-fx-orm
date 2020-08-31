package exceptions;

public class InvalidMemberException extends Exception{
    public InvalidMemberException(String studentId) {
        super("Cannot add studentId: "+studentId+" since it's already added to another team");
    }

    public InvalidMemberException(String studentId, String customCause) {
        super("Cannot add studentId: "+studentId+" since "+customCause);
    }
}
