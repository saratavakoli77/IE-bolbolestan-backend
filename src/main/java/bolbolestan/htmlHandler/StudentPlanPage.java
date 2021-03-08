package bolbolestan.htmlHandler;

import bolbolestan.course.DaysOfWeek;
import bolbolestan.offeringRecord.OfferingRecordEntity;
import bolbolestan.student.StudentEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentPlanPage extends BasePage {
    public StudentPlanPage(String title, Map<String, Object> map) {
        templateName = "src/main/static/plan.html";
        style = """
                table{
                            width: 100%;
                            text-align: center;
                        }
                        table, th, td{
                            border: 1px solid black;
                            border-collapse: collapse;
                        }
                """;
        data = map;
        htmlPage = htmlPage.replace("%title%", title);
        generateBody();
        generateStyle();
    }

    private String generateTable() {
        StringBuilder table = new StringBuilder();
        List<DaysOfWeek> daysOfWeeks = Arrays.asList(DaysOfWeek.values());
        for (DaysOfWeek dayOfWeekEnum: daysOfWeeks) {
            String dayOfWeek = dayOfWeekEnum.name();
            table.append("" + "<tr>\n" + "<td>")
                    .append(dayOfWeek)
                    .append(("</td>\n"));
            HashMap<String, String> classHours = (HashMap<String, String>) data.get(dayOfWeek);
            for (String classHour: classHours.keySet()) {
                table.append("<td>")
                        .append(classHours.get(classHour))
                        .append(("</td>\n"));
            }
        }

        return table.toString();
    }

    @Override
    protected void generateBody() {
        String body = readFromTemplate();
        body = body.replace("%table%", generateTable());
        htmlPage = htmlPage.replace("%body%", body);
    }
}
