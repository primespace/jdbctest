import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import com.prepeo.jdbc.ConnectionPool;
import com.prepeo.jdbc.Helper;

public class TestData {
	
	private int[] spaceIds = new int[] {
			1, 2, 3, 4, 5, 6, 7, 8, 9, 10
	};

	public void insertRandom() {

		int rankId = getRankId(0);
		
		Random rand = new Random();
		
		for(int spaceId : spaceIds) {
						
			insertHeart(rankId, spaceId, rand.nextInt(100000));
		}
	}
	
	private void insertHeart(int rankId, int spaceId, int nextInt) {

		if(isExist(rankId, spaceId)) {
			
		}
		
	}
	
	private int getRankId(final int offset) {
		
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.getConnection();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String sql = "select fn_getRankId(?) rankId from dual";
		
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
		
		Helper.close(rs);
		Helper.close(stmt);
		
		pool.closeConnection(conn);
		
		return rankId;
	}
	
	private boolean isExist(final int rankId, final int spaceId) {
		
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.getConnection();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		int count = 0;
		try {
			String sql = "select count(rank_id) from space_rank_heart where rank_id = ? and space_id = ? ";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,  rankId);
			stmt.setInt(2, spaceId);
			
			while(rs.next()) {
				count = rs.getInt(1);
			}
						
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		Helper.close(rs);
		Helper.close(stmt);
		pool.closeConnection(conn);
		
		
		return (count > 0);
	}
	
	private void updateTestData(final int rankId, final int spaceId, final int heartCount)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.getConnection();
		
		PreparedStatement stmt = null;
		
		try {
			
			String sql = "update space_rank_heart set heart_count = ? where rank_id = ? and spaceId = ? ";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, heartCount);
			stmt.setInt(2, rankId);
			stmt.setInt(3, spaceId);

			stmt.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		Helper.close(stmt);
	
		pool.closeConnection(conn);
	}
	
	private void insertTestData(final int rankId, final int spaceId, final int heartCount)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.getConnection();
		
		PreparedStatement stmt = null;
		
		try {
			
			String sql = "insert into space_rank_heart(rank_id, space_id, heart_count, rank, rank_up, dense_rank) ";
			sql += "values(?, ?, ?, ?, ?, ?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, rankId);
			stmt.setInt(2, spaceId);
			stmt.setInt(3, heartCount);
			stmt.setInt(4, 0);
			stmt.setInt(5, 0);
			stmt.setInt(6, 0);

			stmt.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		Helper.close(stmt);
	
		pool.closeConnection(conn);
	}

}
