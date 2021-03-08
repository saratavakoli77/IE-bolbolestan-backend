package bolbolestan.htmlHandler;

import java.util.Map;

public class SubmitPage extends BasePage {

    public SubmitPage(String title, Map<String, Object> map) {
        templateName = "src/main/static/submit.html";
        style = """
                li {
                        	padding: 5px
                        }
                """;
        data = map;
        htmlPage = htmlPage.replace("%title%", title);
        generateBody();
        generateStyle();
    }

    @Override
    protected void generateBody() {
        String body = readFromTemplate();
        body = body.replace("${studentId}",(String) data.get("studentId"));
        body = body.replace("${unitsSum}", (String) data.get("unitsSum"));

        htmlPage = htmlPage.replace("%body%", body);
    }
}
