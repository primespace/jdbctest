import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.prepeo.jdbc.ConnectionPool;

public class TestData {

	public void insertRandom() {

		int rankId = getRankId(0);
		System.out.println("rankId " + rankId);
		
		
	}
	
	private int getRankId(final int offset) {
		
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.getConnection();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String sql = "select fn_toDays(date_add(now(), interval ? day)) rankId from dual";
		// String sql = "select fn_toWeeks(date_add(now(), interval ? day)) rankId from dual";
		
		int rankId = 0;
		
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, offset);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				rankId = rs.getInt("rankId");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		
		return rankId;
	}
	
	private boolean isExist(final int rankId, final int spaceId) {
		
		return false;
	}
	
	private void insertTestData(final int spaceId, final int heartCount)
	{
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

}
