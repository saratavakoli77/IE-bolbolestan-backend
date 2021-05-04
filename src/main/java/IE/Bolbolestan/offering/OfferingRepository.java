package IE.Bolbolestan.offering;

import IE.Bolbolestan.course.CourseEntity;
import IE.Bolbolestan.course.CourseRepository;
import IE.Bolbolestan.dbConnection.ConnectionPool;
import IE.Bolbolestan.dbConnection.Repository;
import IE.Bolbolestan.tools.HttpClient;
import IE.Bolbolestan.tools.refiners.OfferingRefiner;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OfferingRepository extends Repository<OfferingEntity, List<String>> {
    private static final String TABLE_NAME = "offering";
    private static OfferingRepository instance;

    public static OfferingRepository getInstance() {
        if (instance == null) {
            try {
                instance = new OfferingRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in OfferingRepository.create query.");
            }
        }
        return instance;
    }

    private OfferingRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(" +
                                "course_code CHAR(50),\n" +
                                "instructor CHAR(50),\n" +
                                "string_class_time_days CHAR(225),\n" +
                                "class_time_start DATETIME,\n" +
                                "class_time_end DATETIME,\n" +
                                "class_code CHAR(10),\n" +
                                "registered INT,\n" +
                                "PRIMARY KEY(course_code, class_code), \n" +
                                "FOREIGN KEY(course_code) REFERENCES course(code));",
                        TABLE_NAME)
        );

        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT* FROM %s o WHERE o.course_code = ? and o.class_code = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, List<String> ids) throws SQLException {
        st.setString(1, ids.get(0));
        st.setString(2, ids.get(1));
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT INTO %s(" +
                "course_code, " +
                "instructor, " +
                "string_class_time_days, " +
                "class_time_start, " +
                "class_time_end, " +
                "class_code, " +
                "registered" +
                ") VALUES(?,?,?,?,?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, OfferingEntity data) throws SQLException {
        st.setString(1, data.getCode());
        st.setString(2, data.getInstructor());
        st.setString(3, data.getStringClassTimeDays());
        st.setTimestamp(4, new Timestamp(data.getClassTimeStart().getTime()));
        st.setTimestamp(5, new Timestamp(data.getClassTimeEnd().getTime()));
        st.setString(6, data.getClassCode());
        st.setInt(7, data.getRegistered());
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected OfferingEntity convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new OfferingEntity(
                CourseRepository.getInstance().getById(rs.getString(1)),
                rs.getString(2),
                rs.getString(3),
                rs.getTimestamp(4),
                rs.getTimestamp(5),
                rs.getString(6),
                rs.getInt(7)
        );
    }

    @Override
    protected ArrayList<OfferingEntity> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<OfferingEntity> offeringEntities = new ArrayList<>();
        while (rs.next()) {
            offeringEntities.add(this.convertResultSetToDomainModel(rs));
        }
        return offeringEntities;
    }

    @Override
    protected String getFindObjectStatement() {
        return String.format("SELECT* FROM %s o WHERE o.course_code = ? and o.class_code = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindObject(PreparedStatement st, OfferingEntity data) throws SQLException {
        st.setString(1, data.getCode());
        st.setString(2, data.getClassCode());
    }

    public static void getDataFromApi() {
        HttpClient http = new HttpClient();
        String fetchProjectsUrl = "courses";
        try {
            String response = http.get(fetchProjectsUrl);
            List<OfferingEntity> offerings = new OfferingRefiner(response).getRefinedEntities();
            CourseRepository courseRepository = CourseRepository.getInstance();
            OfferingRepository offeringRepository = OfferingRepository.getInstance();
            for (OfferingEntity offeringEntity: offerings) {
                //todo: bebinim dorste ya na
                courseRepository.insert(offeringEntity);
                offeringRepository.insert(offeringEntity);
            }

            for (OfferingEntity offeringEntity: offerings) {
                courseRepository.setCoursePrerequisites(offeringEntity);
            }
            System.out.println("Fetched " + offerings.size() + " courses");
        } catch (Exception e) {
            System.out.println("error");
            e.fillInStackTrace();
        }
    }
}
