package bolbolestan.htmlHandler;

public class Error404 extends BasePage {

    public Error404(String title) {
        templateName = "src/main/static/404.html";
        htmlPage = readFromTemplate();
        htmlPage = htmlPage.replace("%title%", title);
    }
}
