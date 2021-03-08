package bolbolestan.userInterface;


import bolbolestan.htmlHandler.StudentProfilePage;
import bolbolestan.requestHandler.RequestHandler;
import io.javalin.Javalin;

import java.util.HashMap;
import java.util.Map;


public class WebServer {
    private Javalin app;
    private final RequestHandler requestHandler = new RequestHandler();

    public void start(final int port) {
        app = Javalin.create().start(port);

        app.get("/profile/:student-id", ctx -> {
            String studentId = ctx.pathParam("student-id");
            Map<String, Object> data = new HashMap<>();

            try {
                data = requestHandler.getStudentProfile(studentId);
                ctx.html(new StudentProfilePage("Profile", data).getPage());
            } catch (Exception e) {
                System.out.println("error");
                e.fillInStackTrace();
            }
        });

//        app.get("/courses", ctx -> {
//            Map<String, Object> data = new HashMap<>();
//
//            try {
//                data = requestHandler.getStudentProfile(studentId);
//                ctx.html(new StudentProfilePage("Profile", data).getPage());
//            } catch (Exception e) {
//                System.out.println("error");
//                e.fillInStackTrace();
//            }
//        });
    }
}
