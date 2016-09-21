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
import whu.entity.NewsBean;

public class NewsDao {
	public static int addNews(NewsBean news) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		long rv = Long.parseLong(dbSession.insertRowReturnAutoKey(
				"insert into news_tab values(seq_news_id.nextval,?,?,?,?)",
				new Object[] { news.getTitle(), news.getAuthor(),
						news.getTime(), news.getContent() }, "ID").toString());// 返回id
		news.setId((int) rv);
		return (int) rv;
	}

	public static int deleteNews(int newsId) {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		return dbSession.executeUpdate("delete from news_tab where id=?",
				new Object[] { newsId });
	}

	public static NewsBean getNewsById(int newsId) {
		NewsBean bean = null;
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		List<Map<String, Object>> list = dbSession.executeQuery(
				"select * from news_tab where id=?", new Object[] { newsId });

		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			BigDecimal id = (BigDecimal) map.get("ID");

			bean = new NewsBean(id.intValue(),
					(String) map.get("TIME"), 
					(String) map.get("AUTHOR"), 
					(String) map.get("TIME"), 
					(String) map.get("CONTENT")
					);

		}
		return bean;
	}

	public static List<NewsBean> getAllNewss(int startRow, int count) {
		List<NewsBean> newss = null;
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		String sqlText = dbSession.setResultRange("select * from news_tab",
				startRow, count);
		List<Map<String, Object>> list = dbSession.executeQuery(sqlText, null);

		if (list != null) {
			newss = new ArrayList<NewsBean>();

			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(0);
				BigDecimal id = (BigDecimal) map.get("ID");
				NewsBean bean = new NewsBean(id.intValue(),
						(String) map.get("TIME"), 
						(String) map.get("AUTHOR"), 
						(String) map.get("TIME"), 
						(String) map.get("CONTENT")
						);
				newss.add(bean);
			}

		}
		return newss;
	}

	public static int getNewssCount() {
		DbSession dbSession = DbSessionFactory.getCurrentDbSession();
		Map<String, Object> map = dbSession.uniqueResult(
				"select count(1) cnt from news_tab", null);
		return Integer.parseInt(map.get("CNT").toString());
	}

//	 public static int updateNews(NewsBean bean) {
//	 DbSession dbSession = DbSessionFactory.getCurrentDbSession();
//	 return dbSession.executeUpdate(
//	 "update news_tab set price=?,type=?,note=? where id=?",
//	 new Object[] { bean.getPrice(), bean.getType(), bean.getNote(),
//	 bean.getId() });
//	 }

	 public static void setNewsPhoto(NewsBean news,
	 InputStream photoStream) throws SQLException {
	 DbSession dbSession = DbSessionFactory.getCurrentDbSession();
	 dbSession.writeOracleBlobColumn(
	 "select pic from news_tab where id=? for update",
	 new Object[] { news.getId() }, photoStream);
	 }
	
	 public static void getNewsPhoto(int id, OutputStream photoStream) {
	 DbSession dbSession = DbSessionFactory.getCurrentDbSession();
	 dbSession.readOracleBlobColumn(
	 "select pic from news_tab where id=?", new Object[] { id },
	 photoStream);
	
	 }
}
