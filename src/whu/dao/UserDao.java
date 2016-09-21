package whu.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import whu.common.Context;
import whu.common.DbSession;
import whu.common.DbSessionFactory;
import whu.entity.UserBean;

public class UserDao {

	public static UserBean getUserById(int userId) {
		UserBean ub = null;
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		List<Map<String, Object>> list = dbSession.executeQuery(
				"select * from user_tab where id=?", new Object[] { userId });

		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			BigDecimal uid = (BigDecimal) map.get("ID");
			BigDecimal ulv = (BigDecimal) map.get("LV");
			BigDecimal uidentity = (BigDecimal) map.get("IDENTITY");
			ub = new UserBean(uid.intValue(), ulv.intValue(),
					uidentity.intValue(), (String) map.get("NAME"),
					(String) map.get("PASSWORD"), (String) map.get("PHONE"),
					(String) map.get("ADDRESS"));

		}
		return ub;
	}

	public static UserBean getUserByName(String username) {

		UserBean ub = null;
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		List<Map<String, Object>> list = dbSession.executeQuery(
				"select * from user_tab where user_name=?",
				new Object[] { username });

		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			BigDecimal uid = (BigDecimal) map.get("ID");
			BigDecimal ulv = (BigDecimal) map.get("LV");
			BigDecimal uidentity = (BigDecimal) map.get("IDENTITY");
			ub = new UserBean(uid.intValue(), ulv.intValue(),
					uidentity.intValue(), (String) map.get("NAME"),
					(String) map.get("PASSWORD"), (String) map.get("PHONE"),
					(String) map.get("ADDRESS"));

		}
		return ub;
	}

	public static int addUser(UserBean user) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		long rv = Long
				.parseLong(dbSession
						.insertRowReturnAutoKey(
								"insert into user_tab values(seq_user_id.nextval,?,?,?,?,?,?,EMPTY_BLOB())",
								new Object[] { user.getLv(),
										user.getIdentity(),
										user.getName(),
										user.getPassword(),
										user.getPhone(),
										user.getAddress(),
										}, "ID")
						.toString());//返回id
		user.setId((int) rv);
		return (int) rv;
	}

	public static void setUserPhoto(UserBean user, InputStream photoStream)
			throws SQLException {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		dbSession.writeOracleBlobColumn(
				"select pic from user_tab where id=? for update",
				new Object[] { user.getId() }, photoStream);
	}

	public static void getUserPhoto(int id, OutputStream photoStream) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		dbSession.readOracleBlobColumn(
				"select pic from user_tab where id=?",
				new Object[] { id }, photoStream);

	}

	public static List<UserBean> getAllUsers(int startRow, int count) {
		List<UserBean> users = null;
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		String sqlText = dbSession
				.setResultRange(
						"select * from user_tab",
						startRow, count);
		List<Map<String, Object>> list = dbSession.executeQuery(sqlText, null);

		if (list != null) {
			users = new ArrayList<UserBean>();

			for (int i = 0; i < list.size(); i++) {

				Map<String, Object> map = list.get(i);
				BigDecimal uid = (BigDecimal) map.get("ID");
				BigDecimal ulv = (BigDecimal) map.get("LV");
				BigDecimal uidentity = (BigDecimal) map.get("IDENTITY");
				UserBean ub = new UserBean(uid.intValue(), ulv.intValue(),
						uidentity.intValue(), (String) map.get("NAME"),
						(String) map.get("PASSWORD"), (String) map.get("PHONE"),
						(String) map.get("ADDRESS"));
				users.add(ub);
			}

		}
		return users;
	}

	public static int getUsersCount() {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		Map<String, Object> map = dbSession.uniqueResult(
				"select count(1) cnt from user_tab", null);
		return Integer.parseInt(map.get("CNT").toString());
	}

	public static int deleteUser(int userId) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		return dbSession.executeUpdate("delete from user_tab where id=?",
				new Object[] { userId });
	}

	public static int updateUser(UserBean ub) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		return dbSession
				.executeUpdate(
						"update user_tab set password=?,phone=?,address=?,identity=? where id=?",
						new Object[] { ub.getPassword(),
								ub.getPhone(),ub.getAddress(),ub.getIdentity(), ub.getId() });
	}

}
