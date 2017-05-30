package org.dsc.util;

import java.sql.*;

/**
 * Utilidad para mantener documentacion relacionada con cualquier elemento de base de datos:
 * element : description (html format)
 */
public class SqlLite {

	public static void main(String[] args) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager
					.getConnection("jdbc:sqlite:C:\\sqlite\\libreria.sqlite");

			Statement stat = conn.createStatement();
			stat.execute("DELETE FROM autores");

			PreparedStatement prep = conn
					.prepareStatement("INSERT INTO autores (id_autor,nombre) VALUES (?, ?);");
			prep.setInt(1, 1);
			prep.setString(2, "Deitel");
			prep.addBatch();
			prep.setInt(1, 2);
			prep.setString(2, "Ceballos");
			prep.addBatch();
			prep.setInt(1, 3);
			prep.setString(2, "Joyanes Aguilar");
			prep.addBatch();

			conn.setAutoCommit(false);
			prep.executeBatch();
			conn.setAutoCommit(true);

			ResultSet rs = stat.executeQuery("select * from autores;");
			while (rs.next()) {
				System.out.println("ID_AUTOR...: " + rs.getString("id_autor"));
				System.out.println("NOMBRE.....: " + rs.getString("nombre"));
				System.out.println("-----------------------------------");
			}
			rs.close();
			stat.close();
			conn.close();

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}

	}
}
