package bolbolestan.userInterface;


import bolbolestan.htmlHandler.*;
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

        app.get("/courses", ctx -> {
            Map<String, Object> data = new HashMap<>();

            try {
                data = requestHandler.getCourseList();
                ctx.html(new CourseListPage("Courses", data).getPage());
            } catch (Exception e) {
                System.out.println("error");
                e.fillInStackTrace();
            }
        });

        app.get("/course/:code/:classCode", ctx -> {
            Map<String, Object> data = new HashMap<>();

            try {
                data = requestHandler.getCourseDetail(ctx.pathParam("code"), ctx.pathParam("classCode"));
                ctx.html(new CourseDetailPage("Course", data).getPage());
            } catch (Exception e) {
                System.out.println("error");
                e.fillInStackTrace();
            }
        });

        app.get("/plan/:student-id", ctx -> {
            String studentId = ctx.pathParam("student-id");
            Map<String, Object> data = new HashMap<>();

            try {
                data = requestHandler.getStudentWeeklySchedule(studentId);
                ctx.html(new StudentPlanPage("Plan", data).getPage());
            } catch (Exception e) {
                System.out.println(e);
                e.fillInStackTrace();
            }
        });


        app.post("/course/:code/:classCode", ctx -> {
            Map<String, Object> data = new HashMap<>();

            try {
                String studentId = ctx.formParam("std_id");
                System.out.println(studentId);
                requestHandler.addToWeeklySchedule(
                        studentId, ctx.pathParam("code") + ctx.pathParam("classCode")
                );
            } catch (Exception e) {
                System.out.println(e);
                e.fillInStackTrace();
            }
        });

        app.get("/change_plan/:student-id", ctx -> {
            String studentId = ctx.pathParam("student-id");
            Map<String, Object> data = new HashMap<>();

            try {
                data = requestHandler.getWeeklyScheduleOfferings(studentId);
                ctx.html(new ChangePlanPage("Change Plan", data).getPage());
            } catch (Exception e) {
                System.out.println(e);
                e.fillInStackTrace();
            }
        });

        app.post("/change_plan/:student-id", ctx -> {
            String studentId = ctx.pathParam("student-id");
            String courseCode = ctx.formParam("course_code");
            String classCode = ctx.formParam("class_code");
            Map<String, Object> data = new HashMap<>();

            try {
                data = requestHandler.removeFromWeeklySchedule(
                        studentId, courseCode + classCode);
                ctx.html(new ChangePlanPage("Change Plan", data).getPage());
            } catch (Exception e) {
                System.out.println(e);
                e.fillInStackTrace();
            }
        });

    }

}
