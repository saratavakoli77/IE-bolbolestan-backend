package IE.Bolbolestan.bolbolestanExceptions;

public class CapacityMismatchException extends Exception {
    public CapacityMismatchException(String classCode) {
        super("تعداد ثبت نام ها در رس با کد " + classCode + "بیشتر از ظرفیت درس شده است. ");
    }
}
