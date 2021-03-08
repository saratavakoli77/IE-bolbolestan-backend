package bolbolestan.htmlHandler;

public class SubmitFailed extends BasePage {

    public SubmitFailed(String title) {
        templateName = "src/main/static/submitFailed.html";
        htmlPage = readFromTemplate();
        htmlPage = htmlPage.replace("%title%", title);
    }
}
