package bolbolestan.htmlHandler;

import java.util.Map;

public class CourseDetailPage extends BasePage {

    public CourseDetailPage(String title, Map<String, Object> map) {
        templateName = "src/main/static/course.html";
        data = map;
        htmlPage = htmlPage.replace("%title%", title);
        generateBody();
    }


    @Override
    protected void generateBody() {
        String body = readFromTemplate();
        body = body.replace("${code}", (String) data.get("courseCode"));
        body = body.replace("${class_code}", (String) data.get("classCode"));
        body = body.replace("${units}", (String) data.get("units"));
        body = body.replace("${days}", (String) data.get("days"));
        body = body.replace("${time}", (String) data.get("time"));

        htmlPage = htmlPage.replace("%body%", body);
    }
}


//<li id="code">Code: ${code}</li>
//<li id="class_code">Class Code: ${class_code}</li>
//<li id="units">units: ${units}</li>
//<li id="days">Days: ${days}</li>
//<li id="time">Time: ${time}</li>
