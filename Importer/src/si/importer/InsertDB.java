package si.importer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import si.importer.model.FileData;

/**
 * DB layer.
 * 
 * @author Andrej Kogovsek
 *
 */
public class InsertDB {

	
	static {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get SQL connection.
	 * @return
	 * @throws SQLException
	 */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:file:db/testdb", "SA", "");
	}


	/**
	 * Drop table.
	 * 
	 * @throws SQLException
	 */
	public void dropTabel() throws SQLException {
		Connection connection = this.getConnection();
		Statement statement = connection.createStatement();

		statement.executeUpdate("DROP TABLE fo_random");
	}
	
	/**
	 * Create table.
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void createTable() throws SQLException, ClassNotFoundException {
		Connection connection = this.getConnection();
		Statement statement = connection.createStatement();

		statement.executeUpdate("CREATE TABLE fo_random (" + "MATCH_ID BIGINT NOT NULL, MARKET_ID BIGINT NOT NULL,"
				+ "OUTCOME_ID BIGINT NOT NULL, SPECIFIERS VARCHAR(1000), DATE_INSERT TIMESTAMP NOT NULL " + ");");
	}
	

	/**
	 * Batch for inserting data to DB.
	 * 
	 * @param dataList
	 * @throws Exception
	 */
	public void batch(ArrayList<FileData> dataList) throws Exception {
		Connection connection = this.getConnection();
		connection.setAutoCommit(false);
		
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO fo_random ");
		sql.append("(MATCH_ID, MARKET_ID, OUTCOME_ID, SPECIFIERS, DATE_INSERT) ");
		sql.append("VALUES (?,?,?,?,?) ");
		PreparedStatement statement = connection.prepareStatement(sql.toString());
		
		for(int i=0; i<dataList.size(); i++) {
			FileData data = dataList.get(i);
			
			statement.setLong(1, data.getMatchId());
			statement.setLong(2, data.getMarketId());
			statement.setLong(3, data.getOutcomeId());
			if(data.getSpecifiers() != null) {
				statement.setString(4, data.getSpecifiers());
			} else {
				statement.setNull(4,java.sql.Types.VARCHAR);
			}
			
			statement.setTimestamp(5,getCurrentTimeStamp());
			statement.addBatch();
			if(i % 1000 == 0) {
				statement.executeBatch();
			}
		}
		
		statement.executeBatch();
		connection.commit();
		
	}
	
	/**
	 * Get MIN date_insert from DB.
	 * @return
	 * @throws SQLException
	 */
	public Date getMinDateInsert() throws SQLException {
		Connection connection = this.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT MIN(DATE_INSERT) FROM fo_random"); 
		
		Date date = null;
		while(rs.next()) {
			date = new Date(rs.getTimestamp(1).getTime());
		}
		
		return date;
	}
	
	/**
	 * Get MAX date_insert from DB.
	 * @return
	 * @throws SQLException
	 */
	public Date getMaxDateInsert() throws SQLException {
		Connection connection = this.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT MAX(DATE_INSERT) FROM fo_random"); 
		
		Date date = null;
		while(rs.next()) {
			date = new Date(rs.getTimestamp(1).getTime());
		}
		
		return date;
	}

	/**
	 * Helper method for getting current time stamp. 
	 * @return
	 */
	private static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}


}
