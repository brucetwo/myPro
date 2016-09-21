package whu.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import whu.common.DbSession;
import whu.common.DbSessionFactory;
import whu.entity.MessageBean;

public class MessageDao {
	public static int addMessage(MessageBean message) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		long rv = Long
				.parseLong(dbSession
						.insertRowReturnAutoKey(
								"insert into message_tab values(seq_message_id.nextval,?,?,?,?)",
								new Object[] { message.getCustomId(),
										message.getGoodsId(),
										message.getTime(), message.getContent() },
								"ID").toString());// 返回id
		message.setId((int) rv);
		return (int) rv;
	}

	public static int deleteMessage(int messageId) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		return dbSession.executeUpdate("delete from message_tab where id=?",
				new Object[] { messageId });
	}

	public static MessageBean getMessageById(int messageId) {
		MessageBean bean = null;
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		List<Map<String, Object>> list = dbSession.executeQuery(
				"select * from message_tab where id=?",
				new Object[] { messageId });

		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			BigDecimal id = (BigDecimal) map.get("ID");
			BigDecimal cid = (BigDecimal) map.get("CUSTOM_ID");
			BigDecimal gid = (BigDecimal) map.get("GOODS_ID");
			bean = new MessageBean(id.intValue(), cid.intValue(),
					gid.intValue(), (String) map.get("TIME"),
					(String) map.get("CONTENT"));

		}
		return bean;
	}

	public static List<MessageBean> getAllMessages(int startRow, int count) {
		List<MessageBean> messages = null;
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		String sqlText = dbSession.setResultRange("select * from message_tab",
				startRow, count);
		List<Map<String, Object>> list = dbSession.executeQuery(sqlText, null);

		if (list != null) {
			messages = new ArrayList<MessageBean>();

			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				BigDecimal id = (BigDecimal) map.get("ID");
				BigDecimal cid = (BigDecimal) map.get("CUSTOM_ID");
				BigDecimal gid = (BigDecimal) map.get("GOODS_ID");
				MessageBean bean = new MessageBean(id.intValue(), cid.intValue(),
						gid.intValue(), (String) map.get("TIME"),
						(String) map.get("CONTENT"));
				messages.add(bean);
			}

		}
		return messages;
	}

	public static int getMessagesCount() {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		Map<String, Object> map = dbSession.uniqueResult(
				"select count(1) cnt from message_tab", null);
		return Integer.parseInt(map.get("CNT").toString());
	}

//	public static int updateMessage(MessageBean bean) {
//		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
//		return dbSession.executeUpdate(
//				"update message_tab set price=?,type=?,note=? where id=?",
//				new Object[] { bean.getPrice(), bean.getType(), bean.getNote(),
//						bean.getId() });
//	}

//	public static void setMessagePhoto(MessageBean message,
//			InputStream photoStream) throws SQLException {
//		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
//		dbSession.writeOracleBlobColumn(
//				"select pic from message_tab where id=? for update",
//				new Object[] { message.getId() }, photoStream);
//	}
//
//	public static void getMessagePhoto(int id, OutputStream photoStream) {
//		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
//		dbSession.readOracleBlobColumn(
//				"select pic from message_tab where id=?", new Object[] { id },
//				photoStream);
//
//	}
}
