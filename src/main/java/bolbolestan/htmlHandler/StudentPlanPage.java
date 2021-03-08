package bolbolestan.htmlHandler;

import bolbolestan.offeringRecord.OfferingRecordEntity;
import bolbolestan.student.StudentEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentPlanPage extends BasePage {
    public StudentPlanPage(String title, Map<String, Object> map) {
        templateName = "src/main/static/plan.html";
        data = map;
        htmlPage = htmlPage.replace("%title%", title);
        generateBody();
    }

    private String generateTable() {
        StringBuilder table = new StringBuilder();
        for (String dayOfWeek: data.keySet()) {
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
