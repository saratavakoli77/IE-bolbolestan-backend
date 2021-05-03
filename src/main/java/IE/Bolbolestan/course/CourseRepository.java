package IE.Bolbolestan.course;

import IE.Bolbolestan.dbConnection.ConnectionPool;
import IE.Bolbolestan.dbConnection.Repository;

import java.sql.*;
import java.util.ArrayList;


public class CourseRepository extends Repository<CourseEntity, String> {
    private static final String TABLE_NAME = "Course";
    private static CourseRepository instance;

    public static CourseRepository getInstance() {
        if (instance == null) {
            try {
                instance = new CourseRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in CourseRepository.create query.");
            }
        }
        return instance;
    }


    private CourseRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(" +
                                "code CHAR(50),\n" +
                                "name CHAR(225),\n" +
                                "units INT,\n" +
                                "examTimeStart DATETIME,\n" +
                                "examTimeEnd DATETIME,\n" +
                                "capacity INT,\n" +
                                "type CHAR(50),\n" +
                                "PRIMARY KEY(code));",
                        TABLE_NAME)
        );

        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT* FROM %s student WHERE student.id = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String id) throws SQLException {
        st.setString(1, id);
    }



    @Override
    protected String getInsertStatement() {
        return String.format("INSERT INTO %s(" +
                "code, " +
                "name, " +
                "units, " +
                "examTimeStart, " +
                "examTimeEnd, " +
                "capacity, " +
                "type, " +
                ") VALUES(?,?,?,?,?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, CourseEntity data) throws SQLException {
        st.setString(1, data.getCode());
        st.setString(2, data.getName());
        st.setInt(3, data.getUnits());
        st.setTimestamp(4, new Timestamp(data.getExamTimeStart().getTime()));
        st.setTimestamp(5, new Timestamp(data.getExamTimeEnd().getTime()));
        st.setInt(6, data.getCapacity());
        st.setString(7, data.getType());
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected CourseEntity convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new CourseEntity(
                rs.getString(1),
                rs.getString(2),
                rs.getInt(3),
                rs.getTimestamp(4),
                rs.getTimestamp(5),
                rs.getInt(6),
                PrerequisiteRepository.getInstance().getCoursePrerequisite(rs.getString(1)),
                rs.getString(7)
        );
    }

    @Override
    protected ArrayList<CourseEntity> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<CourseEntity> courseEntities = new ArrayList<>();
        while (rs.next()) {
            courseEntities.add(this.convertResultSetToDomainModel(rs));
        }
        return courseEntities;
    }

    public void setCoursePrerequisites(CourseEntity courseEntity) {
        PrerequisiteRepository prerequisiteRepository = PrerequisiteRepository.getInstance();
        for (String prerequisiteCode: courseEntity.getPrerequisites()) {
            try {
                prerequisiteRepository.insert(
                        new PrerequisiteEntity(courseEntity.getCode(), prerequisiteCode)
                );
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
