package IE.Bolbolestan.course;

import IE.Bolbolestan.dbConnection.ConnectionPool;
import IE.Bolbolestan.dbConnection.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrerequisiteRepository extends Repository<PrerequisiteEntity, Integer> {
    private static final String TABLE_NAME = "prerequisite";
    private static PrerequisiteRepository instance;

    public static PrerequisiteRepository getInstance() {
        if (instance == null) {
            try {
                instance = new PrerequisiteRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in PrerequisiteRepository.create query.");
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
                                "main_course_code CHAR(50),\n" +
                                "prerequisite_course_code CHAR(50),\n" +
                                "FOREIGN KEY(main_course_code) REFERENCES course(code),\n" +
                                "FOREIGN KEY(prerequisite_course_code) REFERENCES course(code),\n" +
                                "PRIMARY KEY(main_course_code, prerequisite_course_code));",
                        TABLE_NAME)
        );

        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT* FROM %s p WHERE p.id = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, Integer id) throws SQLException {
        st.setInt(1, id);
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT INTO %s(" +
                "main_course_code, " +
                "prerequisite_course_code, " +
                ") VALUES(?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, PrerequisiteEntity data) throws SQLException {
        st.setString(1, data.getMainCourseCode());
        st.setString(2, data.getPrerequisiteCourseCode());
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected PrerequisiteEntity convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new PrerequisiteEntity(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3)
        );
    }

    @Override
    protected ArrayList<PrerequisiteEntity> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<PrerequisiteEntity> prerequisiteEntities = new ArrayList<>();
        while (rs.next()) {
            prerequisiteEntities.add(this.convertResultSetToDomainModel(rs));
        }
        return prerequisiteEntities;
    }

    public ArrayList<String> getCoursePrerequisite(String courseCode) throws SQLException {
        String queryStmt = String.format(
                "SELECT p.prerequisite_course_code FROM %s p WHERE p.main_course_code = ?;",
                TABLE_NAME
        );

        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(queryStmt);
        st.setString(1, courseCode);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                return null;
            }

            ArrayList<String> prerequisiteCodes = new ArrayList<>();
            while (resultSet.next()) {
                prerequisiteCodes.add(resultSet.getString(1));
            }
            st.close();
            con.close();
            return prerequisiteCodes;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in prerequisite.getCourse query.");
            e.printStackTrace();
            throw e;
        }
    }
}
