
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		
		TestData data = new TestData();
		data.insertRandom();
		
//		for(int i = 0; i < 100; ++i) {
//			test1();
//		}
		
//		insertTestData();
		
	}
	
	static void insertTestData()
	{
		System.out.println("insertTestData");
		
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.getConnection();
		
		PreparedStatement stmt = null;
		
		try {
			
			String sql = "insert into space_rank_heart(rank_id, space_id, heart_count, rank, rank_up, dense_rank) ";
			sql += "values(fn_firstWeekToDays(now()), ?, ?, ?, ?, ?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, 1);
			stmt.setInt(2, 1);
			stmt.setInt(3, 1);
			stmt.setInt(4, 1);
			stmt.setInt(5, 1);

			stmt.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pool.closeConnection(conn);
	}
	
	static void test1()
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.getConnection();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			
			String sql = "select name from gender_type where gender_type = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, 1);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				String name = rs.getString("name");
				System.out.println("name " + name);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pool.closeConnection(conn);
	}
}