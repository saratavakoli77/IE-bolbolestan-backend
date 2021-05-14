package IE.Bolbolestan.dbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Repository<T, I> {
    abstract protected String getFindByIdStatement();

    abstract protected void fillFindByIdValues(PreparedStatement st, I id) throws SQLException;

    abstract protected String getInsertStatement();

    abstract protected void fillInsertValues(PreparedStatement st, T data) throws SQLException;

    abstract protected String getFindAllStatement();

    abstract protected T convertResultSetToDomainModel(ResultSet rs) throws SQLException;

    abstract protected ArrayList<T> convertResultSetToDomainModelList(ResultSet rs) throws SQLException;

    abstract protected String getFindObjectStatement();

    abstract protected void fillFindObject(PreparedStatement st, T data) throws SQLException;

    public Boolean isObjectExist(T obj) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindObjectStatement());
        fillFindObject(st, obj);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                return false;
            }
            T result = convertResultSetToDomainModel(resultSet);
            st.close();
            con.close();
            return result != null;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Repository.find query.");
            e.printStackTrace();
            throw e;
        }
    }

    public T getById(I id) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindByIdStatement());
        fillFindByIdValues(st, id);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                return null;
            }
            T result = convertResultSetToDomainModel(resultSet);
            st.close();
            con.close();
            return result;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Repository.find query.");
            e.printStackTrace();
            throw e;
        }
    }

    public void insert(T obj) throws SQLException {
        if (this.isObjectExist(obj)) {
            return;
        }
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertStatement());
        fillInsertValues(st, obj);
        try {
            st.execute();
            st.close();
            con.close();
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Repository.insert query.");
            e.printStackTrace();
        }
    }

    public List<T> getAll() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindAllStatement());
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet == null) {
                st.close();
                con.close();
                return new ArrayList<>();
            }
            List<T> result = convertResultSetToDomainModelList(resultSet);
            st.close();
            con.close();
            return result;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Repository.findAll query.");
            e.printStackTrace();
            throw e;
        }
    }
}
