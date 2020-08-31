package exceptions;

public class RepeatedMemberException extends Exception {
    public RepeatedMemberException(String studentId) {
        super("Student ID: "+studentId+" is already present in this Team");
    }
}
