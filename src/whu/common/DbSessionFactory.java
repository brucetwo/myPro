package whu.common;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DbSessionFactory {
	public static DbSession openDbSession()
	{
		DbSession dbSession=null;
		Connection conn=null;
		
		try {
			/*
			Class.forName("");
			conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","scott","oracle");
			*/
			javax.naming.Context ctx=null;
			DataSource ds=null;
			
			ctx=new InitialContext();
			
			ds=(DataSource)ctx.lookup("java:/comp/env/jdbc/exDs");
			conn=ds.getConnection();
			dbSession=new DbSession();
			dbSession.setConnection(conn);
		} 
		/*catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		*/catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dbSession;
	}
	
	public static DbSession getCurrentDbSession()
	{
		DbSession dbSession=null;
		dbSession=(DbSession)Context.get("dbSession");
		if(dbSession==null)
		{
			dbSession=openDbSession();
			bindDbSessionToThread(dbSession);
		}
		return dbSession;
		
	}
	
	public static void closeCurrentDbSession()
	{
		DbSession dbSession=null;
		dbSession=(DbSession)Context.get("dbSession");
		if(dbSession!=null)
		{
			dbSession.close();
		}
		unbindDbSessionFromThread();
	}
	
	public static void bindDbSessionToThread(DbSession dbSession)
	{
		Context.set("dbSession", dbSession);
	}
	
	public static void unbindDbSessionFromThread()
	{
		Context.remove("dbSession");
	}
}

