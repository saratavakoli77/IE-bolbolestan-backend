package bolbolestan.htmlHandler;

import java.util.HashMap;
import java.util.Map;

public class ChangePlanPage extends BasePage {
    private final String formString = """
            <td>
            <form action="" method="POST" >
            <input id="form_course_code" type="hidden" name="course_code" value="${courseCode}">
            <input id="form_class_code" type="hidden" name="class_code" value="${classCode}">
            <button type="submit">Remove</button>
            </form>
            </td>
            """;

    public ChangePlanPage(String title, Map<String, Object> map) {
        templateName = "src/main/static/changePlan.html";
        data = map;
        htmlPage = htmlPage.replace("%title%", title);
        generateBody();
    }

    private String generateTable() {
        StringBuilder table = new StringBuilder();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            HashMap<String, String> offeringData = (HashMap) entry.getValue();
            String newForm = formString.replace("${courseCode}", offeringData.get("courseCode"))
                    .replace("${classCode}", offeringData.get("classCode"));
            table.append("<tr>")
                    .append("<td>").append(offeringData.get("courseCode")).append("</td>")
                    .append("<td>").append(offeringData.get("classCode")).append("</td>")
                    .append("<td>").append(offeringData.get("name")).append("</td>")
                    .append("<td>").append(offeringData.get("units")).append("</td>")
                    .append("<td>").append(newForm).append("</td>");
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
