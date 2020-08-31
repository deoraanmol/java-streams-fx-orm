package exceptions;

public class StudentConflictException extends Exception {
    public StudentConflictException(String studentId) {
        super("Cannot add student: "+studentId+" since it conflicts with other team members");
    }
}
