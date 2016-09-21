package whu.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import whu.common.DbSession;
import whu.common.DbSessionFactory;
import whu.entity.OrderBean;

public class OrderDao {
	public static int addOrder(OrderBean order) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		long rv = Long
				.parseLong(dbSession
						.insertRowReturnAutoKey(
								"insert into order_tab values(seq_order_id.nextval,?,?,?,?,?,?,?)",
								new Object[] { order.getGoodsId(),
										order.getCustomId(),
										order.getSellerId(), order.getTime(),
										order.getNum(), order.getState(),
										order.getTotal() }, "ID").toString());// 返回id
		order.setId((int) rv);
		return (int) rv;
	}

	public static int deleteOrder(int orderId) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		return dbSession.executeUpdate("delete from order_tab where id=?",
				new Object[] { orderId });
	}

	public static OrderBean getOrderById(int orderId) {
		OrderBean bean = null;
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		List<Map<String, Object>> list = dbSession.executeQuery(
				"select * from order_tab where id=?", new Object[] { orderId });

		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			BigDecimal id = (BigDecimal) map.get("ID");
			BigDecimal cid = (BigDecimal) map.get("CUSTOM_ID");
			BigDecimal gid = (BigDecimal) map.get("GOODS_ID");
			BigDecimal sid = (BigDecimal) map.get("SELLER_ID");
			bean = new OrderBean(id.intValue(), gid.intValue(), cid.intValue(),
					sid.intValue(), (String) map.get("TIME"),
					(String) map.get("NUM"), (String) map.get("STATE"),
					(String) map.get("TOTAL"));

		}
		return bean;
	}

	public static List<OrderBean> getAllOrders(int startRow, int count) {
		List<OrderBean> orders = null;
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		String sqlText = dbSession.setResultRange("select * from order_tab",
				startRow, count);
		List<Map<String, Object>> list = dbSession.executeQuery(sqlText, null);

		if (list != null) {
			orders = new ArrayList<OrderBean>();

			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				BigDecimal id = (BigDecimal) map.get("ID");
				BigDecimal cid = (BigDecimal) map.get("CUSTOM_ID");
				BigDecimal gid = (BigDecimal) map.get("GOODS_ID");
				BigDecimal sid = (BigDecimal) map.get("SELLER_ID");
				OrderBean bean = new OrderBean(id.intValue(), gid.intValue(),
						cid.intValue(), sid.intValue(),
						(String) map.get("TIME"), (String) map.get("NUM"),
						(String) map.get("STATE"), (String) map.get("TOTAL"));

				orders.add(bean);
			}

		}
		return orders;
	}

	public static int getOrdersCount() {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		Map<String, Object> map = dbSession.uniqueResult(
				"select count(1) cnt from order_tab", null);
		return Integer.parseInt(map.get("CNT").toString());
	}

	// public static int updateOrder(OrderBean bean) {
	// DbSession dbSession = DbSessionFactory.getCurrentDbSession();
	// return dbSession.executeUpdate(
	// "update order_tab set price=?,type=?,note=? where id=?",
	// new Object[] { bean.getPrice(), bean.getType(), bean.getNote(),
	// bean.getId() });
	// }

	// public static void setOrderPhoto(OrderBean order,
	// InputStream photoStream) throws SQLException {
	// DbSession dbSession = DbSessionFactory.getCurrentDbSession();
	// dbSession.writeOracleBlobColumn(
	// "select pic from order_tab where id=? for update",
	// new Object[] { order.getId() }, photoStream);
	// }
	//
	// public static void getOrderPhoto(int id, OutputStream photoStream) {
	// DbSession dbSession = DbSessionFactory.getCurrentDbSession();
	// dbSession.readOracleBlobColumn(
	// "select pic from order_tab where id=?", new Object[] { id },
	// photoStream);
	//
	// }
}
