package bolbolestan.htmlHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class BasePage {
    protected String htmlPage;
    protected String templateName;
    Map<String , Object> data;
    protected String style;

    public BasePage() {
        templateName = "src/main/static/base.html";
        htmlPage = readFromTemplate();
    }

    public String readFromTemplate() {
        StringBuilder data = new StringBuilder();
        try {
            File myObj = new File(templateName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data.append(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data.toString();
    }

    public String getPage() {
        return htmlPage;
    }

    protected void generateBody() {
    }

    protected void generateStyle() {
        htmlPage.replace("%style%", style);
    }
}
