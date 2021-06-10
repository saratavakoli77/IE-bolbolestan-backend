package IE.Bolbolestan.student;

import IE.Bolbolestan.dbConnection.ConnectionPool;
import IE.Bolbolestan.dbConnection.Repository;
import IE.Bolbolestan.tools.HttpClient;
import IE.Bolbolestan.tools.Password;
import IE.Bolbolestan.tools.refiners.StudentRefiner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository extends Repository<StudentEntity, String> {
    private static final String TABLE_NAME = "student";
    private static StudentRepository instance;

    public static StudentRepository getInstance() {
        if (instance == null) {
            try {
                instance = new StudentRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in StudentRepository.create query.");
            }
        }
        return instance;
    }

    private StudentRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s(" +
                                "id CHAR(50),\n" +
                                "name CHAR(225),\n" +
                                "secondName CHAR(225),\n" +
                                "birthDate CHAR(225),\n" +
                                "field CHAR(225),\n" +
                                "faculty CHAR(225),\n" +
                                "level CHAR(225),\n" +
                                "status CHAR(225),\n" +
                                "img CHAR(225),\n" +
                                "email CHAR(100) NOT NULL UNIQUE,\n" +
                                "password CHAR(100),\n" +
                                "PRIMARY KEY(id));",
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
                "id, " +
                "name, " +
                "secondName, " +
                "birthDate, " +
                "field, " +
                "faculty, " +
                "level, " +
                "status, " +
                "img, " +
                "email, " +
                "password " +
                ") VALUES(?,?,?,?,?,?,?,?,?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, StudentEntity data) throws SQLException {
        st.setString(1, data.getId());
        st.setString(2, data.getName());
        st.setString(3, data.getSecondName());
        st.setString(4, data.getBirthDate());
        st.setString(5, data.getField());
        st.setString(6, data.getFaculty());
        st.setString(7, data.getLevel());
        st.setString(8, data.getStatus());
        st.setString(9, data.getImg());
        st.setString(10, data.getEmail());
        st.setString(11, Password.hash(data.getPassword()));
    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected StudentEntity convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new StudentEntity(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6),
                rs.getString(7),
                rs.getString(8),
                rs.getString(9),
                rs.getString(10),
                rs.getString(11)
        );
    }

    @Override
    protected ArrayList<StudentEntity> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<StudentEntity> studentEntities = new ArrayList<>();
        while (rs.next()) {
            studentEntities.add(this.convertResultSetToDomainModel(rs));
        }
        return studentEntities;
    }

    @Override
    protected String getFindObjectStatement() {
        return String.format("SELECT * FROM %s student WHERE student.id = ?;", TABLE_NAME);
    }

    protected String getFindByEmailStatement() {
        return String.format("SELECT * FROM %s student WHERE student.email = ?;", TABLE_NAME);
    }

    @Override
    protected void fillFindObject(PreparedStatement st, StudentEntity data) throws SQLException {
        st.setString(1, data.getStudentId());
    }

    public StudentEntity findStudentByEmail(String email) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindByEmailStatement());
        fillFindByIdValues(st, email);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                return null;
            }
            StudentEntity result = this.convertResultSetToDomainModel(resultSet);
            st.close();
            con.close();
            return result;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Student.findByEmail query.");
            e.printStackTrace();
            throw e;
        }
    }

    public static void getDataFromApi() {
        HttpClient http = new HttpClient();
        String fetchProjectsUrl = "students";

        try {
            String response = http.get(fetchProjectsUrl);
            List<StudentEntity> students = new StudentRefiner(response).getRefinedEntities();
            StudentRepository studentRepository = StudentRepository.getInstance();
            for (StudentEntity studentEntity: students) {
                studentRepository.insert(studentEntity);
            }
            System.out.println("Fetched " + students.size() + " students");
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    public void updateObjectPassword(StudentEntity studentEntity) throws SQLException {
        String queryStmt = String.format(
                "UPDATE %s st SET st.password = ? WHERE st.id = ?;",
                TABLE_NAME
        );

        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(queryStmt);
        st.setString(1, Password.hash(studentEntity.getPassword()));
        st.setString(2, studentEntity.getStudentId());
        try {
            st.executeUpdate();
            st.close();
            con.close();
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in student.updateObjectPassword query.");
            e.printStackTrace();
            throw e;
        }
    }
}
