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
import whu.entity.CartBean;

public class CartDao {

	public static CartBean getCartById(int cartId) {
		CartBean cb = null;
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		List<Map<String, Object>> list = dbSession.executeQuery(
				"select * from cart_tab where id=?", new Object[] { cartId });

		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			BigDecimal uid = (BigDecimal) map.get("ID");
			BigDecimal gid = (BigDecimal) map.get("GOODS_ID");
			BigDecimal num = (BigDecimal) map.get("NUM");
			cb = new CartBean(uid.intValue(), gid.intValue(),
					num.intValue());

		}
		return cb;
	}

	public static int addCart(CartBean cart) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		long rv = Long
				.parseLong(dbSession
						.insertRowReturnAutoKey(
								"insert into cart_tab values(seq_cart_id.nextval,?,?)",
								new Object[] { cart.getGoodsId(),
										cart.getNum()
										}, "ID")
						.toString());//返回id
		cart.setId((int) rv);
		return (int) rv;
	}

	public static List<CartBean> getAllCarts(int startRow, int count) {
		List<CartBean> carts = null;
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		String sqlText = dbSession
				.setResultRange(
						"select * from cart_tab",
						startRow, count);
		List<Map<String, Object>> list = dbSession.executeQuery(sqlText, null);

		if (list != null) {
			carts = new ArrayList<CartBean>();

			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				BigDecimal uid = (BigDecimal) map.get("ID");
				BigDecimal gid = (BigDecimal) map.get("GOODS_ID");
				BigDecimal num = (BigDecimal) map.get("NUM");
				CartBean cb = new CartBean(uid.intValue(), gid.intValue(),
						num.intValue());
				carts.add(cb);
			}

		}
		return carts;
	}

	public static int getCartsCount() {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		Map<String, Object> map = dbSession.uniqueResult(
				"select count(1) cnt from cart_tab", null);
		return Integer.parseInt(map.get("CNT").toString());
	}

	public static int deleteCart(int cartId) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		return dbSession.executeUpdate("delete from cart_tab where id=?",
				new Object[] { cartId });
	}

	public static int updateCart(CartBean cb) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		return dbSession
				.executeUpdate(
						"update cart_tab set goods_id=?,num=? where id=?",
						new Object[] { cb.getGoodsId(),
								cb.getNum(), cb.getId() });
	}

}

