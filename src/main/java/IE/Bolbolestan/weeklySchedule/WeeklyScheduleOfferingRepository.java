package IE.Bolbolestan.weeklySchedule;

import IE.Bolbolestan.dbConnection.ConnectionPool;
import IE.Bolbolestan.dbConnection.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WeeklyScheduleOfferingRepository extends Repository<WeeklyScheduleOfferingEntity, Integer> {
    private static final String TABLE_NAME = "weekly_schedule_offering";
    private static WeeklyScheduleOfferingRepository instance;

    public static WeeklyScheduleOfferingRepository getInstance() {
        if (instance == null) {
            try {
                instance = new WeeklyScheduleOfferingRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in WeeklyScheduleOfferingRepository.create query.");
            }
        }
        return instance;
    }

    private WeeklyScheduleOfferingRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(" +
                                "id INT AUTO_INCREMENT, \n" +
                                "class_code CHAR(10),\n" +
                                "course_code CHAR(50),\n" +
                                "student_id CHAR(50),\n" +
                                "PRIMARY KEY(id), \n" +
                                "FOREIGN KEY(student_id) REFERENCES student(id), \n" +
                                "FOREIGN KEY(course_code, class_code) REFERENCES offering(course_code, class_code));",
                        TABLE_NAME)
        );

        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT * FROM %s wo WHERE wo.id = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, Integer id) throws SQLException {
        st.setInt(1, id);
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT INTO %s(" +
                "class_code, " +
                "course_code, " +
                "student_id " +
                ") VALUES(?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, WeeklyScheduleOfferingEntity data) throws SQLException {
        String offeringCode = data.getOfferingCode();
        st.setString(2, offeringCode.substring(0, offeringCode.length() - 2));
        st.setString(1, offeringCode.substring(offeringCode.length() - 2));
        st.setString(3, data.getStudentId());
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected WeeklyScheduleOfferingEntity convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new WeeklyScheduleOfferingEntity(
                rs.getInt(1),
                rs.getString(3) + rs.getString(2),
                rs.getString(4)
        );
    }

    @Override
    protected ArrayList<WeeklyScheduleOfferingEntity> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<WeeklyScheduleOfferingEntity> weeklyScheduleOfferingEntities = new ArrayList<>();
        while (rs.next()) {
            weeklyScheduleOfferingEntities.add(this.convertResultSetToDomainModel(rs));
        }
        return weeklyScheduleOfferingEntities;
    }

    @Override
    protected String getFindObjectStatement() {
        return String.format("" +
                "SELECT * FROM %s wo WHERE wo.course_code = ? and wo.class_code = ? and wo.student_id = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindObject(PreparedStatement st, WeeklyScheduleOfferingEntity data) throws SQLException {
        String offeringCode = data.getOfferingCode();
        st.setString(1, offeringCode.substring(0, offeringCode.length() - 2));
        st.setString(2, offeringCode.substring(offeringCode.length() - 2));
        st.setString(3, data.getStudentId());
    }

    public ArrayList<String> getWeeklyScheduleOfferingCodes(String studentId) throws SQLException {
        String queryStmt = String.format(
                "SELECT wo.course_code, wo.class_code FROM %s wo WHERE wo.student_id = ?;",
                TABLE_NAME
        );

        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(queryStmt);
        st.setString(1, studentId);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                st.close();
                con.close();
                return new ArrayList<>();
            }

            ArrayList<String> offeringCodes = new ArrayList<>();
            while (resultSet.next()) {
                offeringCodes.add(resultSet.getString(1) + resultSet.getString(2));
            }
            st.close();
            con.close();
            return offeringCodes;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in weekly_schedule_offering.getCourse query.");
            e.printStackTrace();
            throw e;
        }
    }
}

