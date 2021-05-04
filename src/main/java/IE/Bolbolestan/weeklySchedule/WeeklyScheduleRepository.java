package IE.Bolbolestan.weeklySchedule;

import IE.Bolbolestan.dbConnection.ConnectionPool;
import IE.Bolbolestan.dbConnection.Repository;

import java.sql.*;
import java.util.ArrayList;

public class WeeklyScheduleRepository extends Repository<WeeklyScheduleEntity, String> {
    private static final String TABLE_NAME = "weekly_schedule";
    private static WeeklyScheduleRepository instance;

    public static WeeklyScheduleRepository getInstance() {
        if (instance == null) {
            try {
                instance = new WeeklyScheduleRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in WeeklyScheduleRepository.create query.");
            }
        }
        return instance;
    }

    private WeeklyScheduleRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(" +
                                "student_id CHAR(50),\n" +
                                "status CHAR(50),\n" +
                                "PRIMARY KEY(student_id), \n" +
                                "FOREIGN KEY(student_id) REFERENCES student(id));",
                        TABLE_NAME)
        );

        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT* FROM %s w WHERE w.student_id = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String id) throws SQLException {
        st.setString(1, id);
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT INTO %s(" +
                "student_id, " +
                "status " +
                ") VALUES(?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, WeeklyScheduleEntity data) throws SQLException {
        st.setString(1, data.getStudentId());
        st.setString(2, data.getStatus());
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected WeeklyScheduleEntity convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new WeeklyScheduleEntity(
                rs.getString(1),
                rs.getString(2),
                WeeklyScheduleOfferingRepository
                        .getInstance().getWeeklyScheduleOfferingCodes(rs.getString(1))
        );
    }

    @Override
    protected ArrayList<WeeklyScheduleEntity> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<WeeklyScheduleEntity> weeklyScheduleEntities = new ArrayList<>();
        while (rs.next()) {
            weeklyScheduleEntities.add(this.convertResultSetToDomainModel(rs));
        }
        return weeklyScheduleEntities;
    }

    @Override
    protected String getFindObjectStatement() {
        return String.format("SELECT * FROM %s w WHERE w.student_id = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindObject(PreparedStatement st, WeeklyScheduleEntity data) throws SQLException {
        st.setString(1, data.getStudentId());
    }

    public WeeklyScheduleEntity getByStudentId(String studentId) throws SQLException {
        String queryStmt = String.format(
                "SELECT * FROM %s ws WHERE ws.student_id = ?;",
                TABLE_NAME
        );

        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(queryStmt);
        st.setString(1, studentId);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                return null;
            }
            WeeklyScheduleEntity weeklyScheduleEntity = convertResultSetToDomainModel(resultSet);
            st.close();
            con.close();
            return weeklyScheduleEntity;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in weekly_schedule.getByStudentId query.");
            e.printStackTrace();
            throw e;
        }
    }

}
