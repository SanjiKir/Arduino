import java.sql.*;

/**
 * Created by wooow on 12.11.2016.
 */
public class DatabaseConnect {
    private Connection connection;
    private Statement statement;




        public void databaseSetup(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "19219292");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        }
        public void insertDatBaseValues(int id, int value){

            String sqlQuery = "INSERT INTO MOVEMENTINFO VALUES (" + id +"," + "CURRENT_TIMESTAMP," + value +")";
            try {
                statement = connection.createStatement();
                statement.executeUpdate(sqlQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    /**
     * @param
     */
    public void getDatabaseValue(){
            String sqlQuery = "select * from MOVEMENTINFO";
            ResultSet rs = null;

            try {
                statement = connection.createStatement();
                rs = statement.executeQuery(sqlQuery);

            while (rs.next()){
                System.out.println(rs.getInt(1));
                System.out.println(rs.getDate(2));
                System.out.println(rs.getInt(3));
            }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public int getLastKeyValue(){
            String sqlQuery = "SELECT COUNT(*) FROM MOVEMENTINFO";
            ResultSet rs = null;
            int value = 0 ;
            try {
                statement = connection.createStatement();
                rs = statement.executeQuery(sqlQuery);
                rs.next();
                value = rs.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return value;
        }

        }

