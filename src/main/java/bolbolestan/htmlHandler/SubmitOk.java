package bolbolestan.htmlHandler;

public class SubmitOk extends BasePage {

    public SubmitOk(String title) {
        templateName = "src/main/static/submitOk.html";
        htmlPage = readFromTemplate();
        htmlPage = htmlPage.replace("%title%", title);
    }
}
