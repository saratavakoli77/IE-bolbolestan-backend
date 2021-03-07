package bolbolestan.userInterface;


import bolbolestan.student.StudentModel;
import io.javalin.Javalin;


public class WebServer {
    private Javalin app;

    public void start(final int port) {

                String template = "" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Project</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>hello world</h1>\n" +
                "</body>\n" +
                "</html>";

        app = Javalin.create().start(port);
        app.get("/", ctx -> ctx.html(
                template
        ));

    }
}
