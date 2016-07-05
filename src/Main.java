
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.dbcp2.Utils;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.prepeo.jdbc.ConnectionPool;

/**
 * @author Andres.Cespedes
 * @version 1.0 $Date: 14/01/2015
 * @since 1.7
 *
 */
public class Main {
	
	public static void main(String[] args) {
	
		ConnectionPool pool = ConnectionPool.getInstance();
		
		pool.set("localhost", 3306, "fanluv", "root", "dlstkdv1");
		
		if(pool.startup() == false) {
			return;
		}
		
		Connection conn = pool.getConnection();
		pool.closeConnection(conn);;
		
	}
}