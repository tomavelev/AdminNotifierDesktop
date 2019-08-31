package com.tomata.adminnotifier.dao;

import java.sql.*;
import java.util.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DB {

	private static boolean isInitialized = false;

	public static final Connection init() throws Exception {
		Connection c = DriverManager.getConnection("jdbc:sqlite:AdminNotifierDesktop.db", "", "");
		if (!isInitialized) {
			isInitialized = true;
			StringBuilder sb = new StringBuilder();
			sb = new StringBuilder();
			sb.append("create table IF NOT EXISTS `observed_item` ( ");
			sb.append("`id` INTEGER PRIMARY KEY AUTOINCREMENT ");
			sb.append(",");
			sb.append("`name` VARCHAR(255)");
			sb.append(",");
			sb.append("`url` VARCHAR(255)");
			sb.append(",");
			sb.append("`comparation_activation` INTEGER(1)");
			sb.append(",");
			sb.append("`value` VARCHAR(255)");
			sb.append(",");
			sb.append("`change_after_meeting_condition` CHAR(1) NULL DEFAULT 'N'");
			sb.append(",");
			sb.append("`active` CHAR(1) NULL DEFAULT 'N'");
			sb.append(",");
			sb.append("`temp_value` VARCHAR(255)");
			sb.append(",");
			sb.append("`time_of_temp_value` TIMESTAMP");
			sb.append("");
			sb.append(")");
			c.prepareStatement(sb.toString()).execute();
			c.close();
			return init();
		}
		return c;
	}

	public static String trim(String inStr) {
		if (inStr.length() > 50) {
			return inStr.substring(0, 50);
		} else {
			return inStr;
		}
	}

	private static boolean propertyExists(Connection c, String dbName, String table, String property)
			throws SQLException {

		try (PreparedStatement ps = c.prepareStatement("select * from `" + table + "` limit 1")) {

			try (ResultSet set = ps.executeQuery()) {

				// ResultSetMetaData metaData2 = set.getMetaData();
				DatabaseMetaData metaData = c.getMetaData();

				try (ResultSet columns = metaData.getColumns(dbName, null, table, null)) {

					while (columns.next()) {

						if (columns.getString(4).equals(property)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private static void createPropery(Connection c, String table, String property, String additionalStuff)
			throws SQLException {
		try (PreparedStatement ps = c
				.prepareStatement("alter table " + table + " add column " + property + " " + additionalStuff)) {
			ps.executeUpdate();
		}
	}

	private static void createTable(Connection c, String table) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("create table " + table)) {
			ps.executeUpdate();
		}
	}

	private static boolean tableExists(Connection c, String dbName, String t) throws SQLException {
		DatabaseMetaData metaData = c.getMetaData();
		try (ResultSet tables = metaData.getTables(dbName, null, null, new String[] { "TABLE" })) {
			while (tables.next()) {
				if (tables.getString(3).equals(t)) {
					return true;
				}
			}
		}
		return false;
	}

	public static String escape(Object inStr) {
		if (inStr == null)
			return "";
		else
			return escape(inStr.toString());
	}

	public static String escape(String inStr) {
		if (inStr == null || inStr.length() == 0) {
			return "";
		}
		int inLen = inStr.length();
		StringBuilder sb = new StringBuilder(inLen);

		char[] chars = inStr.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			switch (chars[i]) {
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			default:
				sb.append(chars[i]);
				break;
			}
		} // for
		return sb.toString();
	}
}
