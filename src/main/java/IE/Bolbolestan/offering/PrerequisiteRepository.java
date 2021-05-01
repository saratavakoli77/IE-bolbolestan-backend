package IE.Bolbolestan.offering;

import IE.Bolbolestan.dbConnection.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PrerequisiteRepository {
    private static final String TABLE_NAME = "prerequisite";
    private static PrerequisiteRepository instance;

    public static PrerequisiteRepository getInstance() {
        if (instance == null) {
            try {
                instance = new PrerequisiteRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in OfferingRepository.create query.");
            }
        }
        return instance;
    }

    private PrerequisiteRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(" +
                                "id INT AUTO_INCREMENT, \n" +
                                "main_course_id CHAR(50),\n" +
                                "prerequisite_course_id CHAR(50),\n" +
                                "FOREIGN KEY(main_course_id) REFERENCES offering(PersonID)" +
                                "PRIMARY KEY(id));",
                        TABLE_NAME)
        );

        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }
}
