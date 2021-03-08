package bolbolestan.htmlHandler;

import java.util.HashMap;
import java.util.Map;

public class CourseListPage extends BasePage {

    public CourseListPage(String title, Map<String, Object> map) {
        templateName = "src/main/static/courses.html";
        style = """
                table{
                            width: 100%;
                            text-align: center;
                        }
                """;
        data = map;
        htmlPage = htmlPage.replace("%title%", title);
        generateBody();
        generateStyle();
    }

    private String generateTable() {
        StringBuilder table = new StringBuilder();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            HashMap<String, String> offeringData = (HashMap) entry.getValue();
            table.append("<tr>")
                    .append("<td>").append(offeringData.get("courseCode")).append("</td>")
                    .append("<td>").append(offeringData.get("classCode")).append("</td>")
                    .append("<td>").append(offeringData.get("name")).append("</td>")
                    .append("<td>").append(offeringData.get("units")).append("</td>")
                    .append("<td>").append(offeringData.get("capacity")).append("</td>")
                    .append("<td>").append(offeringData.get("type")).append("</td>")
                    .append("<td>").append(offeringData.get("days")).append("</td>")
                    .append("<td>").append(offeringData.get("time")).append("</td>")
                    .append("<td>").append(offeringData.get("examStart")).append("</td>")
                    .append("<td>").append(offeringData.get("examEnd")).append("</td>")
                    .append("<td>").append(offeringData.get("prerequisites")).append("</td>")
                    .append("<td>").append("<a href=").append(offeringData.get("link")).append(">Link").append("</td>");
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
