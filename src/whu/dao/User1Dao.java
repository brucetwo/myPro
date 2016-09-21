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
import whu.entity.User1Bean;

public class User1Dao {
	

	public static User1Bean getUserById(int userId)
	{
		User1Bean ub=null;
		DbSession dbSession=DbSessionFactory.getCurrentDbSession();
		List<Map<String,Object>> list=dbSession.executeQuery("select user_id,user_name,user_password,user_permission from user_tab where user_id=?", new Object[]{userId});
		
		if(list.size()>0)
		{
			Map<String,Object> map=list.get(0);
			BigDecimal uid=(BigDecimal)map.get("USER_ID");
			BigDecimal upermission=(BigDecimal)map.get("USER_PERMISSION");
			ub=new User1Bean(uid.intValue(),(String)map.get("USER_NAME"),(String)map.get("USER_PASSWORD"),upermission.intValue());
			
		}
		return ub;
	}
	public static User1Bean getUserByName(String username)
	{
		
		User1Bean ub=null;
		DbSession dbSession=DbSessionFactory.getCurrentDbSession();
		List<Map<String,Object>> list=dbSession.executeQuery("select * from user_tab where user_name=?", new Object[]{username});
		
		if(list.size()>0)
		{
			Map<String,Object> map=list.get(0);
			BigDecimal uid=(BigDecimal)map.get("USER_ID");
			BigDecimal upermission=(BigDecimal)map.get("USER_PERMISSION");
			ub=new User1Bean(uid.intValue(),(String)map.get("USER_NAME"),(String)map.get("USER_PASSWORD"),upermission.intValue());
			
		}
		return ub;
	}
	
	public static int addUser(User1Bean user)
	{
		DbSession dbSession=DbSessionFactory.getCurrentDbSession();
		long rv=Long.parseLong(dbSession.insertRowReturnAutoKey("insert into user_tab values(seq_user_id.nextval,?,?,EMPTY_BLOB(),?)"
								, new Object[]{user.getUserName(),user.getUserPassword(),user.getUserPermission()}
								,"USER_ID").toString());
	
		//System.out.println(rv);
		user.setUserId((int)rv);
		return (int)rv;
	}
	
	
	public static void setUserPhoto(User1Bean user,InputStream photoStream) throws SQLException
	{
		DbSession dbSession=DbSessionFactory.getCurrentDbSession();
		dbSession.writeOracleBlobColumn("select user_photo from user_tab where user_id=? for update", new Object[]{user.getUserId()}, photoStream);
	}
	
	
	public static void getUserPhoto(int userId,OutputStream photoStream)
	{
		DbSession dbSession=DbSessionFactory.getCurrentDbSession();
		dbSession.readOracleBlobColumn("select user_photo from user_tab where user_id=?", new Object[]{userId}, photoStream);
		
	}
	
	public static List<User1Bean> getAllUsers(int startRow,int count)
	{
		List<User1Bean> users=null;
		DbSession dbSession=DbSessionFactory.getCurrentDbSession();
		String sqlText=dbSession.setResultRange("select user_id,user_name,user_password,user_permission from user_tab", startRow, count);
		List<Map<String,Object>> list=dbSession.executeQuery(sqlText, null);
		
		if(list!=null)
		{
			users=new ArrayList<User1Bean>();
			
			for(int i=0;i<list.size();i++)
			{
				Map<String,Object> map=list.get(i);
				BigDecimal uid=(BigDecimal)map.get("USER_ID");
				BigDecimal upermission=(BigDecimal)map.get("USER_PERMISSION");
				User1Bean ub=new User1Bean(uid.intValue(),(String)map.get("USER_NAME"),(String)map.get("USER_PASSWORD"),upermission.intValue());
				users.add(ub);
			}
			
		}
		return users;
	}
	
	public static int getUsersCount()
	{
		DbSession dbSession=DbSessionFactory.getCurrentDbSession();
		Map<String,Object> map=dbSession.uniqueResult("select count(1) cnt from user_tab", null);
		return Integer.parseInt(map.get("CNT").toString());
	}
	
	public static int deleteUser(int userId)
	{
		DbSession dbSession=DbSessionFactory.getCurrentDbSession();
		return dbSession.executeUpdate("delete from user_tab where user_id=?", new Object[]{userId});
	}
	
	public static int updateUser(User1Bean ub)
	{
		DbSession dbSession=DbSessionFactory.getCurrentDbSession();
		return dbSession.executeUpdate("update user_tab set user_password=?,user_permission=? where user_id=?", new Object[]{ub.getUserPassword(),ub.getUserPermission(),ub.getUserId()});
	}
	

}
