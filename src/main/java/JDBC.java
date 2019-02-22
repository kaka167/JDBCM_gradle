import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.codahale.metrics.ConsoleReporter;
import java.util.concurrent.TimeUnit;
import com.codahale.metrics.*;
import com.soulgalore.jdbcmetrics.*;
import org.slf4j.*;

public class JDBC {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:jdbcmetrics:mysql://localhost:3306/kaka?jdbcmetrics=yes";

    static final String USER = "root";
    static final String PASS = "kaka@97167";

//    static final MetricRegistry metrics = new MetricRegistry();

    public static void main(String[] args){

        JDBCMetrics jm = JDBCMetrics.getInstance();
        MetricRegistry metrics = jm.getRegistry();

        final Meter requests = metrics.meter("requests");
        final ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();

        reporter.start(1, TimeUnit.SECONDS);


        Connection conn ;
        Statement stmt = null;

        try{

            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * from test;";
            ResultSet rs = stmt.executeQuery(sql);
//            requests.mark();


            while(rs.next()){

                int id  = rs.getInt("id");
                String first = rs.getString("firstname");


                System.out.print("ID: " + id);
                System.out.print(", firstname: " + first);
            }

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
        }

    }
}
