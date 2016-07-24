
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;

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
	
	public static ArrayList<Integer> rankIds = new ArrayList<>();

	public static void main(String[] args) {
		
		System.out.println(new Date().toString());
	
		ConnectionPool pool = ConnectionPool.getInstance();
		
		pool.set("localhost", 3306, "fanluv", "root", "dlstkdv1");
		
		if(pool.startup() == false) {
			return;
		}
		
		selectRankIds();
		
		int curRankId = 0;
		int preRankId = 0;
		
		
		
		if(rankIds.size() == 0) {
			return;
		}
		
		if(rankIds.size() == 1) {
			curRankId = 0;
		}
		else {
			curRankId = rankIds.get(0);
			preRankId = rankIds.get(1);
		}
		
		System.out.println(" " + curRankId + " " + preRankId);
		
		if(preRankId != 0) {
			if(isCompleteRankCalc(preRankId) == false) {
				calcRank(preRankId);
			}
		}
		
		if(curRankId != 0) {
			if(isCompleteRankCalc(curRankId) == false) {
				calcRank(curRankId);
				if(preRankId != 0) {
					calcRankUp(curRankId, preRankId);
				}
			}
		}
		
		System.out.println(new Date().toString());
	}
	
	private static void calcRankUp(int curRankId, int preRankId) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.getConnection();
		
		PreparedStatement stmt = null;
		
		try {
			String sql = "call sp_calcSpaceRankUp(@ret, ?, ?)";
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, preRankId);
			stmt.setInt(2, curRankId);
			
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

	private static void calcRank(int rankId) {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.getConnection();
		
		PreparedStatement stmt = null;
		
		try {
			String sql = "call sp_calcSpaceRank(@ret, ?)";
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, rankId);
			
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

	private static boolean isCompleteRankCalc(Integer rankId) {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.getConnection();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		int count = 0;
		
		try {
			String sql = "select count(*) from space_rank_complete where rank_id = ?";
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, rankId);
			
			rs = stmt.executeQuery();
			
			if(rs.next() == false) {
				System.out.println("error !");
			}
			
			count = rs.getInt(1);
			
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
		
		
		return count > 0;
	}

	public static void selectRankIds()
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.getConnection();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select fn_getRankId(-1), fn_getRankId(-2) from dual";
			
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			if(rs.next() == false) {
				System.out.println("error !");
			}
			
			rankIds.add(rs.getInt(1));
			rankIds.add(rs.getInt(2));
			
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