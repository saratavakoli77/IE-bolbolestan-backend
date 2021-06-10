package bolbolestan.tools;

import java.util.ArrayList;
import java.util.List;

public class GetExceptionMessages {
    public static List<String> getExceptionMessages(List<Exception> exceptionList) {
        List<String> exceptionMessageList = new ArrayList<>();
        for (Exception exception: exceptionList) {
            exceptionMessageList.add(exception.getMessage());
        }
        return exceptionMessageList;
    }
}
