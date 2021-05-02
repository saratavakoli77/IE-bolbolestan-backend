package IE.Bolbolestan.offeringRecord;

import IE.Bolbolestan.course.CourseRepository;
import IE.Bolbolestan.dbConnection.ConnectionPool;
import IE.Bolbolestan.dbConnection.Repository;
import IE.Bolbolestan.offering.OfferingEntity;
import IE.Bolbolestan.offering.OfferingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfferingRecordRepository extends Repository<OfferingRecordEntity, Integer> {
    private static final String TABLE_NAME = "offering_record";
    private static OfferingRecordRepository instance;

    public static OfferingRecordRepository getInstance() {
        if (instance == null) {
            try {
                instance = new OfferingRecordRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in OfferingRecordRepository.create query.");
            }
        }
        return instance;
    }

    private OfferingRecordRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(" +
                                "id INT AUTO_INCREMENT, \n" +
                                "class_code CHAR(10),\n" +
                                "course_code CHAR(50),\n" +
                                "student_id CHAR(50),\n" +
                                "status CHAR(50), \n" +
                                "grade DOUBLE, \n" +
                                "term INT, \n" +
                                "PRIMARY KEY(id), \n" +
                                "FOREIGN KEY(course_code, class_code) REFERENCES offering(course_code, class_code), \n" +
                                "FOREIGN KEY(student_id) REFERENCES student(id));",
                        TABLE_NAME)
        );

        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT* FROM %s o WHERE o.id = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, Integer id) throws SQLException {
        st.setInt(1, id);
    }

//    "class_code CHAR(10),\n" +
//            "course_code CHAR(50),\n" +
//            "student_id CHAR(50),\n" +
//            "status CHAR(50), \n" +
//            "grade DOUBLE, \n" +
//            "term INT, \n" +
//            "PRIMARY KEY(id), \n" +

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT INTO %s(" +
                "class_code, " +
                "course_code, " +
                "student_id, " +
                "status, " +
                "grade, " +
                "term, " +
                ") VALUES(?,?,?,?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, OfferingRecordEntity data) throws SQLException {
        String offeringCode = data.getOfferingCode();
        st.setString(1, offeringCode.substring(offeringCode.length() - 2));
        st.setString(2, offeringCode.substring(0, offeringCode.length() - 2));
        st.setString(3, data.getStudentId());
        st.setString(4, data.getStatus());
        st.setDouble(5, data.getGrade());
        st.setInt(6, data.getTerm());
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected OfferingRecordEntity convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new OfferingRecordEntity(
                rs.getInt(1),
                rs.getString(4),
                rs.getString(3) + rs.getString(2),
                rs.getDouble(6),
                rs.getString(5),
                rs.getInt(7)
        );
    }

    @Override
    protected ArrayList<OfferingRecordEntity> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<OfferingRecordEntity> offeringRecordEntities = new ArrayList<>();
        while (rs.next()) {
            offeringRecordEntities.add(this.convertResultSetToDomainModel(rs));
        }
        return offeringRecordEntities;
    }
}
