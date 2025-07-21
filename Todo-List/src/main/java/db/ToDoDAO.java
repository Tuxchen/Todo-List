package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import utilities.Priority;

public class ToDoDAO {
	
	public static void init() {
		String sql = "CREATE TABLE IF NOT EXISTS Todos("
				+ "id INTEGER PRIMARY KEY,"
				+ "name TEXT NOT NULL,"
				+ "start DATE NOT NULL,"
				+ "end DATE NOT NULL,"
				+ "description TEXT,"
				+ "done BOOLEAN NOT NULL,"
				+ "priority TEXT CHECK(priority IN ('LOW', 'MIDDLE', 'HIGH')) NOT NULL)";
		
		try(Connection conn = DatabaseConnection.connect();
			Statement stmt = conn.createStatement()) {
			
			stmt.execute(sql);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean insertNewTodo(String name, String start, String end, String description, boolean done, Priority priority) {
		String sql = "INSERT INTO Todos(name, start, end, description, done, priority)"
				+ "VALUES(?, ?, ?, ?, ?, ?)";
		
		try(Connection conn = DatabaseConnection.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, start);
			pstmt.setString(3, end);
			pstmt.setString(4, description);
			pstmt.setBoolean(5, done);
			pstmt.setString(6, priority.name());
			pstmt.execute();
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean updateTodo(int id, String name, String start, String end, String description, boolean done, Priority priority) {
		String sql = "UPDATE Todos SET "
				+ "name=?, start=?, end=?, description=?, done=?, priority=? WHERE id=?";
		
		try(Connection conn = DatabaseConnection.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, start);
			pstmt.setString(3, end);
			pstmt.setString(4, description);
			pstmt.setBoolean(5, done);
			pstmt.setString(6, priority.name());
			pstmt.setInt(7, id);
			pstmt.execute();
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static ArrayList<ArrayList<Object>> selectAllTodos() {
		String sql = "SELECT * FROM Todos";
		ArrayList<ArrayList<Object>> results = new ArrayList<>();
		
		try(Connection conn = DatabaseConnection.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql)) {
			
			while(rs.next()) {
				ArrayList<Object> row = new ArrayList<>();
				row.add(rs.getInt("id"));
				row.add(rs.getString("name"));
				row.add(rs.getString("start"));
				row.add(rs.getString("end"));
				row.add(rs.getString("description"));
				row.add(rs.getBoolean("done"));
				row.add(rs.getString("priority"));
				
				results.add(row);
			}
			
			return results;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean markAsDone(int id) {
		String sql = "UPDATE Todos SET done=1 WHERE id=?";
		
		try(Connection conn = DatabaseConnection.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.execute();
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean deleteTodo(int id) {
		String sql = "DELETE FROM Todos WHERE id=?";
		
		try(Connection conn = DatabaseConnection.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.execute();
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static ArrayList<ArrayList<Object>> searchTodos(String name) {
		String sql = "SELECT * FROM Todos WHERE name LIKE ?";
		
		ArrayList<ArrayList<Object>> results = new ArrayList<>();
		
		try(Connection conn = DatabaseConnection.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + name + "%");
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ArrayList<Object> row = new ArrayList<>();
				row.add(rs.getInt("id"));
				row.add(rs.getString("name"));
				row.add(rs.getString("start"));
				row.add(rs.getString("end"));
				row.add(rs.getString("description"));
				row.add(rs.getBoolean("done"));
				row.add(rs.getString("priority"));
				
				results.add(row);
			}
			
			return results;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}
}
