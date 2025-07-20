package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ToDoDAO {
	
	public static void init() {
		String sql = "CREATE TABLE IF NOT EXISTS Todos("
				+ "id INT PRIMARY KEY,"
				+ "name TEXT NOT NULL,"
				+ "start DATE NOT NULL,"
				+ "end DATE NOT NULL,"
				+ "done BOOLEAN NOT NULL,"
				+ "priority TEXT CHECK(priority IN ('LOW', 'MIDDLE', 'HIGH')) NOT NULL)";
		
		try(Connection conn = DatabaseConnection.connect();
			Statement stmt = conn.createStatement()) {
			
			stmt.execute(sql);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
