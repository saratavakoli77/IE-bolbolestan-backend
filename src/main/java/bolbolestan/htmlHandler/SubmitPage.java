package bolbolestan.htmlHandler;

import bolbolestan.offeringRecord.OfferingRecordEntity;
import bolbolestan.student.StudentEntity;

import java.util.List;
import java.util.Map;

public class SubmitPage extends BasePage {

    public SubmitPage(String title, Map<String, Object> map) {
        templateName = "src/main/static/submit.html";
        data = map;
        htmlPage = htmlPage.replace("%title%", title);
        generateBody();
    }

    @Override
    protected void generateBody() {
        String body = readFromTemplate();
        body = body.replace("${studentId}",(String) data.get("studentId"));
        body = body.replace("${unitsSum}", (String) data.get("unitsSum"));

        htmlPage = htmlPage.replace("%body%", body);
    }
}
