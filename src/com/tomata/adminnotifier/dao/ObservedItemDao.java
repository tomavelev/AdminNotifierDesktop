package com.tomata.adminnotifier.dao;

import java.util.Date;
import com.tomata.adminnotifier.model.ObservedItem;
import com.tomata.adminnotifier.model.ComparationActivation;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ObservedItemDao {

	protected Connection conn;

	public ObservedItemDao(Connection conn) {
		this.conn = conn;
	}



	public void saveOrUpdate(String id, String column, String value) throws SQLException{
		String sql = "update `observed_item` set "+column+"= ? where id = ? ";
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, value);
			ps.setString(2, id);
			ps.executeUpdate();
		}
}

	public void saveOrUpdate(ObservedItem observedItem) throws SQLException {
		StringBuilder sqlSelect = new StringBuilder("select * from `observed_item` where id = ? ");
		StringBuilder sqlUpdate = new StringBuilder("update `observed_item` set  name = ?, url = ?, comparation_activation = ?, value = ?, change_after_meeting_condition = ?, active = ?, temp_value = ?, time_of_temp_value = ? where id = ? ");
		StringBuilder sqlInsert = new StringBuilder("insert into `observed_item`(name,url,comparation_activation,value,change_after_meeting_condition,active,temp_value,time_of_temp_value) values(?,?,?,?,?,?,?,?)");

		try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect.toString())) {
			try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate.toString())) {
				try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert.toString(),
						PreparedStatement.RETURN_GENERATED_KEYS)) {

					boolean isInsert = true;
					psSelect.setLong(1, observedItem.getId());
					try (ResultSet rs = psSelect.executeQuery()) {
						if (rs.next()) {
							isInsert = false;
						}
					}

					if (isInsert) {
					if(observedItem.getName() == null) {
					 psInsert.setNull(1, java.sql.Types.VARCHAR);
					} else {
						psInsert.setString(1, observedItem.getName());
} 
					if(observedItem.getUrl() == null) {
					 psInsert.setNull(2, java.sql.Types.VARCHAR);
					} else {
						psInsert.setString(2, observedItem.getUrl());
} 
						psInsert.setInt(3, observedItem.getComparationActivation().ordinal());
					if(observedItem.getValue() == null) {
					 psInsert.setNull(4, java.sql.Types.VARCHAR);
					} else {
						psInsert.setString(4, observedItem.getValue());
} 
						psInsert.setString(5, observedItem.isChangeAfterMeetingCondition() ? "Y" : "N");
						psInsert.setString(6, observedItem.isActive() ? "Y" : "N");
					if(observedItem.getTempValue() == null) {
					 psInsert.setNull(7, java.sql.Types.VARCHAR);
					} else {
						psInsert.setString(7, observedItem.getTempValue());
} 
					if(observedItem.getTimeOfTempValue() == null) {
					 psInsert.setNull(8, java.sql.Types.TIMESTAMP);
					} else {
						psInsert.setTimestamp(8, new java.sql.Timestamp(observedItem.getTimeOfTempValue().getTime()));
} 

						psInsert.executeUpdate();

						try (ResultSet rs = psInsert.getGeneratedKeys()) {
							if (rs.next()) {
					observedItem.setId(rs.getLong(1));
							}
						}
					} else {
					if(observedItem.getName() == null) {
					 psUpdate.setNull(1, java.sql.Types.VARCHAR);
					} else {
						psUpdate.setString(1, observedItem.getName());
					} 
					if(observedItem.getUrl() == null) {
					 psUpdate.setNull(2, java.sql.Types.VARCHAR);
					} else {
						psUpdate.setString(2, observedItem.getUrl());
					} 
						psUpdate.setInt(3, observedItem.getComparationActivation().ordinal());
					if(observedItem.getValue() == null) {
					 psUpdate.setNull(4, java.sql.Types.VARCHAR);
					} else {
						psUpdate.setString(4, observedItem.getValue());
					} 
						psUpdate.setString(5, observedItem.isChangeAfterMeetingCondition() ? "Y" : "N");
						psUpdate.setString(6, observedItem.isActive() ? "Y" : "N");
					if(observedItem.getTempValue() == null) {
					 psUpdate.setNull(7, java.sql.Types.VARCHAR);
					} else {
						psUpdate.setString(7, observedItem.getTempValue());
					} 
					if(observedItem.getTimeOfTempValue() == null) {
					 psUpdate.setNull(8, java.sql.Types.TIMESTAMP);
					} else {
						psUpdate.setTimestamp(8, new java.sql.Timestamp(observedItem.getTimeOfTempValue().getTime()));
					} 
						psUpdate.setLong(9, observedItem.getId());
						psUpdate.executeUpdate();
					}
				}
			}
		}
	}
	public void removeRow(String id) throws SQLException{
		String sql = "delete from `observed_item` where id = ? ";
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, id);
			ps.executeUpdate();
		}
	}

	public void removeRows(Collection<ObservedItem> items) throws SQLException{
		String sql = "delete from `observed_item` where id = ? ";
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
		conn.setAutoCommit(false);
		
		for (ObservedItem observedItem : items) {
			ps.setString(1, ""+observedItem.getId());
			ps.executeUpdate();
		}
		
		conn.commit();
		conn.setAutoCommit(true);
		}
	}

	public ObservedItem load(String id) throws SQLException{
		StringBuilder sqlSelect = new StringBuilder("select observed_item.id as observed_itemid, observed_item.name as observed_itemname, observed_item.url as observed_itemurl, observed_item.comparation_activation as observed_itemcomparation_activation, observed_item.value as observed_itemvalue, observed_item.change_after_meeting_condition as observed_itemchange_after_meeting_condition, observed_item.active as observed_itemactive, observed_item.temp_value as observed_itemtemp_value, observed_item.time_of_temp_value as observed_itemtime_of_temp_value  from `observed_item` where id = ? ");
		
		try (PreparedStatement ps = conn.prepareStatement(sqlSelect.toString())) {
			
			ps.setString(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if(rs.next()) {
					ObservedItem observedItem = new ObservedItem();
					
						set(observedItem, rs);					
					
					return observedItem;
				}
			}
		}
		return null;
	}

	public static void set(ObservedItem observedItem, ResultSet rs) throws SQLException {
					observedItem.setId(rs.getLong("observed_itemid"));
					observedItem.setName(rs.getString("observed_itemname"));
					observedItem.setUrl(rs.getString("observed_itemurl"));
					observedItem.setComparationActivation(ComparationActivation.values()[rs.getInt("observed_itemcomparation_activation")]);
					observedItem.setValue(rs.getString("observed_itemvalue"));
					observedItem.setChangeAfterMeetingCondition(rs.getString("observed_itemchange_after_meeting_condition").equals("Y"));
					observedItem.setActive(rs.getString("observed_itemactive").equals("Y"));
					observedItem.setTempValue(rs.getString("observed_itemtemp_value"));
					observedItem.setTimeOfTempValue(rs.getTimestamp("observed_itemtime_of_temp_value"));
	}
	public QueryResult<ObservedItem> selectWithParents(long limit, long offset, String orderStr) throws SQLException {
		List<ObservedItem> list = new ArrayList<ObservedItem>();

		ObservedItem observedItem = null;
		String sql = "select observed_item.id as observed_itemid, observed_item.name as observed_itemname, observed_item.url as observed_itemurl, observed_item.comparation_activation as observed_itemcomparation_activation, observed_item.value as observed_itemvalue, observed_item.change_after_meeting_condition as observed_itemchange_after_meeting_condition, observed_item.active as observed_itemactive, observed_item.temp_value as observed_itemtemp_value, observed_item.time_of_temp_value as observed_itemtime_of_temp_value  from `observed_item`  "+orderStr+"  limit ?, ?";

 		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, offset);
			ps.setLong(2, limit);

			try (ResultSet rs = ps.executeQuery()) {
			
				while(rs.next()) {
					observedItem = new ObservedItem();
					set(observedItem, rs);					
					list.add(observedItem);
				}
			
			}
		}
		long count = 0;

		sql = "select count(*) from `observed_item`";

 		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				if(rs.next()) {
					count = rs.getLong(1);
				}
			}
		}
		return new QueryResult<>(list, count);
	}

	public QueryResult<ObservedItem> select(long limit, long offset, String orderStr) throws SQLException {
		List<ObservedItem> list = new ArrayList<ObservedItem>();

		ObservedItem observedItem = null;
		String sql = "select observed_item.id as observed_itemid, observed_item.name as observed_itemname, observed_item.url as observed_itemurl, observed_item.comparation_activation as observed_itemcomparation_activation, observed_item.value as observed_itemvalue, observed_item.change_after_meeting_condition as observed_itemchange_after_meeting_condition, observed_item.active as observed_itemactive, observed_item.temp_value as observed_itemtemp_value, observed_item.time_of_temp_value as observed_itemtime_of_temp_value  from `observed_item` "+orderStr+"  limit ?, ?";

 		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, offset);
			ps.setLong(2, limit);

			try (ResultSet rs = ps.executeQuery()) {
			
				while(rs.next()) {
					observedItem = new ObservedItem();
						set(observedItem, rs);					
					list.add(observedItem);
				}
			
			}
		}
		long count = 0;

		sql = "select count(*) from `observed_item`";

 		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				if(rs.next()) {
					count = rs.getLong(1);
				}
			}
		}
		return new QueryResult<>(list, count);
	}

public QueryResult<ObservedItem> filter(String column, String value,long limit, long offset, String orderStr) throws SQLException {
		List<ObservedItem> list = new ArrayList<ObservedItem>();

		ObservedItem observedItem = null;
		String sql = "select observed_item.id as observed_itemid, observed_item.name as observed_itemname, observed_item.url as observed_itemurl, observed_item.comparation_activation as observed_itemcomparation_activation, observed_item.value as observed_itemvalue, observed_item.change_after_meeting_condition as observed_itemchange_after_meeting_condition, observed_item.active as observed_itemactive, observed_item.temp_value as observed_itemtemp_value, observed_item.time_of_temp_value as observed_itemtime_of_temp_value  from `observed_item` where "+column+" = ? "+orderStr+" limit ?, ? ";

 		try (PreparedStatement ps = conn.prepareStatement(sql)) {

 			ps.setString(1, value);
			ps.setLong(2, offset);
			ps.setLong(3, limit);

			try (ResultSet rs = ps.executeQuery()) {
			
				while(rs.next()) {
					observedItem = new ObservedItem();
					
					set(observedItem, rs);					
					list.add(observedItem);
				}
			
			}
		}
		long count = 0;

		sql = "select count(*) from `observed_item` where "+column+" = ? ";

 		try (PreparedStatement ps = conn.prepareStatement(sql)) {
 			ps.setString(1, value);
			try (ResultSet rs = ps.executeQuery()) {
				if(rs.next()) {
					count = rs.getLong(1);
				}
			}
		}
		return new QueryResult<>(list, count);
	}
public QueryResult<ObservedItem> filterWithParents(String column, String value,long limit, long offset, String orderStr) throws SQLException {
		List<ObservedItem> list = new ArrayList<ObservedItem>();

		ObservedItem observedItem = null;
		String sql = "select observed_item.id as observed_itemid, observed_item.name as observed_itemname, observed_item.url as observed_itemurl, observed_item.comparation_activation as observed_itemcomparation_activation, observed_item.value as observed_itemvalue, observed_item.change_after_meeting_condition as observed_itemchange_after_meeting_condition, observed_item.active as observed_itemactive, observed_item.temp_value as observed_itemtemp_value, observed_item.time_of_temp_value as observed_itemtime_of_temp_value  from `observed_item`  where "+column+" = ? "+orderStr+" limit ?, ? ";

 		try (PreparedStatement ps = conn.prepareStatement(sql)) {

 			ps.setString(1, value);
			ps.setLong(2, offset);
			ps.setLong(3, limit);

			try (ResultSet rs = ps.executeQuery()) {
			
				while(rs.next()) {
					observedItem = new ObservedItem();
					set(observedItem, rs);					list.add(observedItem);
				}
			
			}
		}
		long count = 0;

		sql = "select count(*) from `observed_item` where "+column+" = ? ";

 		try (PreparedStatement ps = conn.prepareStatement(sql)) {
 			ps.setString(1, value);
			try (ResultSet rs = ps.executeQuery()) {
				if(rs.next()) {
					count = rs.getLong(1);
				}
			}
		}
		return new QueryResult<>(list, count);
	}
}
