package org.dsc.db.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import schemacrawler.schema.Column;

public class OracleSchemaDao {
	
	public static String getTableSynonym(final Connection conn, final String tableName) throws Exception {
		String syn = null;
		Statement stmt = null;
		try {
			String query = "select synonym_name from user_synonyms where table_name='"
					+ tableName + "'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				syn = rs.getString(1);
				break;
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			stmt.close();
		}
		return syn;
	}
	
	// COMMENT ON TABLE tableName IS 'Comentario sobre la tabla';
	public static String getTableComment(final Connection conn, final String tableName) throws Exception {
		String comment = null;
		Statement stmt = null;
		try {
			String query = "select comments from user_tab_comments where table_name='"
					+ tableName + "'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				comment = rs.getString(1);
				break;
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			stmt.close();
		}
		return comment;
	}
	
	public static String getViewDefinition(final Connection conn, final String viewName) throws Exception {
		String viewDef = null;
		Statement stmt = null;
		try {
			String query = "select text from all_views where view_name='"
					+ viewName + "'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				viewDef = rs.getString(1);
				break;
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			stmt.close();
		}
		return viewDef;
	}
	
	public static String getColumnComment(final Connection conn, final String tableName, String columnName) throws Exception {
		String comment = null;
		Statement stmt = null;
		try {
			String query = "select comments from user_col_comments where table_name='"
				+ tableName + "' and column_name='" + columnName + "'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				comment = rs.getString(1);
				break;
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			stmt.close();
		}
		return comment;
	}
	
	public static long getRowNum(final Connection conn, final String tableName) throws Exception {
		long length = 0;
		Statement stmt = null;
		try {
			String query = "select sum(1) from " + tableName;
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				length = rs.getLong(1);
				break;
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			stmt.close();
		}
		return length;
	}	
	
	public static long getColumnSize(final Connection conn, final String tableName,  final Column column) {
		long size = 0;
		String type = column.getType().toString();
		String width = column.getWidth();
		if (null != width && !"".equals(width)) {
			try {
				if ("VARCHAR2".equals(type)) {
					// max. 4000
//					width = width.replace('(', ' ').replace(')', ' ').trim();
					size = getAvgColumnLength(conn, tableName, column.getName());
					
				} else if ("NUMBER".equals(type)) {
					width = "21";
					size = Long.parseLong(width);
					
				} else if ("CHAR".equals(type)) {
					// max. 2Kb
//					width = width.replace('(', ' ').replace(')', ' ').trim();
					size = getAvgColumnLength(conn, tableName, column.getName());
				
				} else if ("CLOB".equals(type)) {
					// max. 4Gb
//					width = "25000";
					size = getAvgColumnLength(conn, tableName, column.getName());
					
				} else if ("BLOB".equals(type)) {
					// max. 4Gb
//					width = "25000";
					size = getAvgColumnLength(conn, tableName, column.getName());
					
				} else if ("DATE".equals(type)) {
					width = "7";
					size = Long.parseLong(width);
					
				}
			} catch (Exception e) {}
			// ejie adaptaciones para calculo dimension tabla 
		}
		return size;
	}
	
	private static long getAvgColumnLength(final Connection conn, final String tableName, String columnName) throws Exception {
		long length = 0;
		Statement stmt = null;
		try {
			String query = "select avg(length(" + columnName + ")) from " + tableName + " where rownum < 1000";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				length = rs.getLong(1);
				break;
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			stmt.close();
		}
		return length;
	}	
}
