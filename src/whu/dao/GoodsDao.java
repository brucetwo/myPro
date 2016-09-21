package whu.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import whu.common.DbSession;
import whu.common.DbSessionFactory;
import whu.entity.GoodsBean;

public class GoodsDao {
	public static int addGoods(GoodsBean goods) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		long rv = Long.parseLong(dbSession.insertRowReturnAutoKey(
				"insert into goods_tab values(seq_goods_id.nextval,?,?,?,?)",
				new Object[] { goods.getName(), goods.getPrice(),
						goods.getType(), goods.getNote(), }, "ID").toString());// 返回id
		goods.setId((int) rv);
		return (int) rv;
	}

	public static int deleteGoods(int goodsId) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		return dbSession.executeUpdate("delete from goods_tab where id=?",
				new Object[] { goodsId });
	}

	public static GoodsBean getGoodsById(int goodsId) {
		GoodsBean bean = null;
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		List<Map<String, Object>> list = dbSession.executeQuery(
				"select * from goods_tab where id=?", new Object[] { goodsId });

		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			BigDecimal id = (BigDecimal) map.get("ID");
			bean = new GoodsBean(id.intValue(), (String) map.get("NAME"),
					(String) map.get("PRICE"), (String) map.get("TYPE"),
					(String) map.get("NOTE"));

		}
		return bean;
	}

	public static List<GoodsBean> getAllGoodss(int startRow, int count) {
		List<GoodsBean> goodss = null;
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		String sqlText = dbSession.setResultRange("select * from goods_tab",
				startRow, count);
		List<Map<String, Object>> list = dbSession.executeQuery(sqlText, null);

		if (list != null) {
			goodss = new ArrayList<GoodsBean>();

			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				BigDecimal id = (BigDecimal) map.get("ID");
				GoodsBean bean = new GoodsBean(id.intValue(),
						(String) map.get("NAME"), (String) map.get("PRICE"),
						(String) map.get("TYPE"), (String) map.get("NOTE"));
				goodss.add(bean);
			}

		}
		return goodss;
	}

	public static int getGoodssCount() {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		Map<String, Object> map = dbSession.uniqueResult(
				"select count(1) cnt from goods_tab", null);
		return Integer.parseInt(map.get("CNT").toString());
	}

	public static int updateGoods(GoodsBean bean) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		return dbSession.executeUpdate(
				"update goods_tab set price=?,type=?,note=? where id=?",
				new Object[] { bean.getPrice(), bean.getType(), bean.getNote(),
						bean.getId() });
	}

	public static void setGoodsPhoto(GoodsBean goods, InputStream photoStream)
			throws SQLException {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		dbSession.writeOracleBlobColumn(
				"select pic from goods_tab where id=? for update",
				new Object[] { goods.getId() }, photoStream);
	}

	public static void getGoodsPhoto(int id, OutputStream photoStream) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		dbSession.readOracleBlobColumn("select pic from goods_tab where id=?",
				new Object[] { id }, photoStream);

	}
}
