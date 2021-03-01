package bolbolestan.userInterface;


import bolbolestan.bolbolestanExceptions.InvalidInputException;
import bolbolestan.offering.OfferingEntity;
import bolbolestan.requestHandler.RequestHandler;
import bolbolestan.student.StudentEntity;
import bolbolestan.tools.OfferingDataRefiner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Scanner;

public class TerminalServer {
    final String ADD_OFFERING_CMD = "addOffering";
    final String ADD_STUDENT_CMD = "addStudent";
    final String GET_OFFERINGS_CMD = "getOfferings";
    final String GET_OFFERING_CMD = "getOffering";
    final String ADD_TO_WEEKLY_SCHEDULE = "addToWeeklySchedule";
    final String REMOVE_FROM_WEEKLY_SCHEDULE = "removeFromWeeklySchedule";
    final String GET_WEEKLY_SCHEDULE = "getWeeklySchedule";
    final String FINALIZE = "finalize";

    private final ObjectMapper mapper = new ObjectMapper();
    private final RequestHandler requestHandler = new RequestHandler();

    public void listen() {
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        String command = userInput.substring(0, userInput.indexOf(' '));
        String data = userInput.substring(userInput.indexOf(' ') + 1);

        try {
            switch (command) {
                case ADD_OFFERING_CMD -> {
                    this.addOffering(data);
                }
                case ADD_STUDENT_CMD -> {
                    this.addStudent(data);
                }
                case GET_OFFERING_CMD -> {
                    this.getOffering(data);
                }
                case GET_OFFERINGS_CMD -> {
                    this.getOfferings(data);
                }
                case ADD_TO_WEEKLY_SCHEDULE -> {
                    this.addToWeeklySchedule(data);
                }
                case REMOVE_FROM_WEEKLY_SCHEDULE -> {
                    this.removeFromWeeklySchedule(data);
                }
                case GET_WEEKLY_SCHEDULE -> {
                    this.getWeeklySchedule(data);
                }
                case FINALIZE -> {
                    this.finalizeSchedule(data);
                }
                default -> {
                    throw new InvalidInputException();
                }
            }
        } catch (Exception e) {
            System.out.println("{\n\"success\": true,\n\"error\": " + e.getMessage() + "\n}");
        }
    }

    private String getJsonField(String data, String fieldName) throws InvalidInputException {
        try {
            JsonNode jsonNode = mapper.readTree(data);
            return jsonNode.get(fieldName).textValue();
        } catch (JsonProcessingException e) {
            throw new InvalidInputException();
        }
    }

    private void printResponse(HashMap<String, Object> response) throws InvalidInputException {
        try {
            System.out.println(mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            throw new InvalidInputException();
        }
    }

    private void finalizeSchedule(String data) throws InvalidInputException {
        String studentId = getJsonField(data, "studentId");
        printResponse(this.requestHandler.finalizeSchedule(studentId));
    }

    private void getWeeklySchedule(String data) throws InvalidInputException {
        String studentId = getJsonField(data, "studentId");
        printResponse(this.requestHandler.getWeeklySchedule(studentId));
    }

    private void removeFromWeeklySchedule(String data) throws InvalidInputException {
        String studentId = getJsonField(data, "studentId");
        String offeringCode = getJsonField(data, "code");
        printResponse(this.requestHandler.removeFromWeeklySchedule(studentId, offeringCode));
    }

    private void addToWeeklySchedule(String data) throws InvalidInputException {
        String studentId = getJsonField(data, "studentId");
        String offeringCode = getJsonField(data, "code");
        printResponse(this.requestHandler.addToWeeklySchedule(studentId, offeringCode));
    }

    private void getOffering(String data) throws InvalidInputException {
        String studentId = getJsonField(data, "studentId");
        String offeringCode = getJsonField(data, "code");
        printResponse(this.requestHandler.getOffering(studentId, offeringCode));
    }

    private void getOfferings(String data) throws InvalidInputException {
        String studentId = getJsonField(data, "studentId");
        printResponse(this.requestHandler.getOfferings(studentId));
    }

    private void addStudent(String data) throws InvalidInputException {
        StudentEntity studentEntity;
        try {
            studentEntity = mapper.readValue(data, StudentEntity.class);
        } catch (JsonProcessingException e) {
            throw new InvalidInputException();
        }
        printResponse(this.requestHandler.addStudent(studentEntity));

    }

    private void addOffering(String data) throws InvalidInputException {
        try {
            printResponse(this.requestHandler.addOffering(this.refineNewOfferingData(data)));
        } catch (JsonProcessingException | ParseException e) {
            throw new InvalidInputException();
        }
    }

    private OfferingEntity refineNewOfferingData(String data) throws JsonProcessingException, ParseException {
        OfferingDataRefiner offeringDataRefiner = new OfferingDataRefiner(data);
        return offeringDataRefiner.getRefinedOfferingEntity();
    }

//    private String refineGetOfferingData(OfferingEntity offeringEntity) {
//
//    }

}
