package IE.Bolbolestan.tools;

import IE.Bolbolestan.course.DaysOfWeek;

import java.util.List;

public class ListParser {
    public static String getStringFromList(List<String> objectList) {
        StringBuilder result = new StringBuilder();
        for (String obj: objectList) {
            result.append(obj).append("|");
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    public static String getStringFromListOfDaysOfWeek(List<DaysOfWeek> objectList) {
        StringBuilder result = new StringBuilder();
        for (DaysOfWeek obj: objectList) {
            result.append(obj).append("|");
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }
}
