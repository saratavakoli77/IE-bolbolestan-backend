package IE.Bolbolestan.tools;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateParser {
    public static Map<String, Date> getDatesFromString(String rangeDate) throws ParseException {
        String[] dates = rangeDate.split("-");
        Map<String, Date> dateMap = new HashMap<>();
        dateMap.put("start", getDateFromString(dates[0]));
        dateMap.put("end", getDateFromString(dates[1]));
        return  dateMap;
    }

    public static Boolean doseDatesCollide(
            Date session1Start,
            Date session1End,
            Date session2Start,
            Date session2End) {
        return isDateBetween(session1Start, session1End, session2Start) ||
               isDateBetween(session2Start, session2End, session1Start);
    }

    public static Boolean isDateBetween(Date session1Start, Date session1End, Date session2) {
        return (session2.after(session1Start) || session2.compareTo(session1Start) == 0) &&
               (session2.before(session1End) || session2.compareTo(session1Start) == 0);
    }

    public static Date getDateFromString(String dateString) throws ParseException {
        if (!dateString.contains(":")) {
            dateString += ":00";
        }
        String pattern = "HH:mm";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.parse(dateString);
    }

    public static String getStringFromDates(Date start, Date end) {
        String pattern = "HH:mm";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String startString = format.format(start);
        String endString = format.format(end);
        return startString + "-" + endString;
    }

    public static Date getDateFromExamFormat(String dateString) throws ParseException {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.parse(dateString);
    }

    public static String getStringFromExamDate(Date date) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
