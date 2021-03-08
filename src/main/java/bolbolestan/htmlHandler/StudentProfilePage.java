package bolbolestan.htmlHandler;

import bolbolestan.offeringRecord.OfferingRecordEntity;
import bolbolestan.student.StudentEntity;

import java.util.List;
import java.util.Map;

public class StudentProfilePage extends BasePage {

    public StudentProfilePage(String title, Map<String, Object> map) {
        templateName = "src/main/static/profile.html";
        data = map;
        htmlPage = htmlPage.replace("%title%", title);
        generateBody();
    }

    private String generateTable() {
        StringBuilder table = new StringBuilder();
        List<OfferingRecordEntity> offeringRecordEntities = (List<OfferingRecordEntity>) data.get("courses");
        for (OfferingRecordEntity studentCourse: offeringRecordEntities) {
            table.append("" + "<tr>\n" + "<th>")
                    .append(studentCourse.getOfferingCode())
                    .append("</th>\n")
                    .append("<th>")
                    .append(studentCourse.getGrade())
                    .append("</th>\n")
                    .append("</tr>\n");
        }

        return table.toString();
    }

    @Override
    protected void generateBody() {
        String body = readFromTemplate();
        body = body.replace("${studentId}", ((StudentEntity) data.get("student")).getStudentId());
        body = body.replace("${studentName}", ((StudentEntity) data.get("student")).getName());
        body = body.replace("${studentSecondName}", ((StudentEntity) data.get("student")).getSecondName());
        body = body.replace("${studentBirthDate}", ((StudentEntity) data.get("student")).getBirthDate());
        body = body.replace("${studentGpa}", data.get("gpa").toString());
        body = body.replace("${studentTpu}", data.get("tpu").toString());
        body = body.replace("%table%", generateTable());
        htmlPage = htmlPage.replace("%body%", body);
    }
}
