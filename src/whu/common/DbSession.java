package whu.common;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sql.BLOB;
public class DbSession {
	
	
	private Connection connection;

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * executeQuery：执行select语句，返回查询结果
	 * @author sungeng
	 * @param sqlText  sql文本，里面可带？参数
	 * @param params   参数数组，为sql文本中的？依次赋值
	 * @return 返回查询结果List，其中每个元素为map表示查询结果中的一行，是字段名与值的对应关系
	*/
	public List< Map<String,Object> > executeQuery(String sqlText,Object[] params) {
		
		PreparedStatement stmt=null;
		ResultSet rs=null;
		
		List<Map<String,Object>> dataList=null;
		try {
			stmt=connection.prepareStatement(sqlText);
		
			if(params!=null)
			{
				for(int i=0;i<params.length;i++)
					stmt.setObject(i+1, params[i]);
			}
			
			rs=stmt.executeQuery();
			if(rs!=null)
			{
				ResultSetMetaData rsmd=rs.getMetaData();
				dataList=new ArrayList<Map<String,Object>>();
				while(rs.next())
				{
					 Map<String,Object> map=new HashMap<String,Object>();
					 
					 for(int i=0;i<rsmd.getColumnCount();i++)
					 {
						 String colName=rsmd.getColumnName(i+1);
						 Object colValue=rs.getObject(i+1);
						 map.put(colName, colValue);
					 }
					 dataList.add(map);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			close(null,stmt,rs);
		}
		return dataList;
		
	}
	
	public Map<String,Object> uniqueResult(String sqlText,Object[] params) {
		
		PreparedStatement stmt=null;
		ResultSet rs=null;
		
		Map<String,Object> map=null;
		try {
			stmt=connection.prepareStatement(sqlText);
		
			if(params!=null)
			{
				for(int i=0;i<params.length;i++)
					stmt.setObject(i+1, params[i]);
			}
			
			rs=stmt.executeQuery();
			if(rs.next())
			{
				map=new HashMap<String,Object>();
				ResultSetMetaData rsmd=rs.getMetaData();
				 for(int i=0;i<rsmd.getColumnCount();i++)
				 {
					 String colName=rsmd.getColumnName(i+1);
					 Object colValue=rs.getObject(i+1);
					 map.put(colName, colValue);
				 }
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			close(null,stmt,rs);
		}
		return map;
	}
	
	
	/**
	 * executeUpdate：执行增删改的sql语句，返回操作影响的行数
	 * @author sungeng
	 * @param sqlText  sql文本，里面可带？参数
	 * @param params   参数数组，为sql文本中的？依次赋值
	 * @return 返回操作影响的行数
	*/
	public int executeUpdate(String sqlText,Object[] params) {
		PreparedStatement stmt=null;
	
		
		int rc=0;
		try {
			stmt=connection.prepareStatement(sqlText);
		
			if(params!=null)
			{
				for(int i=0;i<params.length;i++)
					stmt.setObject(i+1, params[i]);
			}
			
			rc=stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			close(null,stmt,null);
		}
		return rc;
		
	}
	
	/**
	 * insertRowReturnAutoKey：执行insert语句向数据库中插入1行，并得到所插入行的由dbms自动生成的字段的值
	 * @author sungeng
	 * @param sqlText  String 必须为insert型的sql文本，里面可带？参数
	 * @param params   Object[] 参数数组，为sql文本中的？依次赋值
	 * @param keyName  String  由dbms自动生成的字段的名称
	 * @return Object 返回由dbms自动生成的字段的值
	*/
	public Object insertRowReturnAutoKey(String sqlText,Object[] params,String keyName)
	{
		Object rv=null;
		
		PreparedStatement stmt=null;
		
		ResultSet rs=null;
		
		int rc=0;
		try {
			stmt=connection.prepareStatement(sqlText,new String[]{keyName});
		
			if(params!=null)
			{
				for(int i=0;i<params.length;i++)
					stmt.setObject(i+1, params[i]);
			}
			rc=stmt.executeUpdate();
			
			if(rc==1)
			{
				rs=stmt.getGeneratedKeys();
				if(rs.next())
					rv=rs.getObject(1);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			close(null,stmt,null);
		}
		return rv;
		
	}
	
	
	/**
	 * writeOracleBlobColumn：向Oracle数据库表中写入一个blob字段
	 * @author sungeng
	 * @param sqlText  String select……for update格式的sql文本，里面可带？参数，用于将要写入的blob字段查询出来（必须为查询结果中的第一个字段）
	 * @param params   Object[] 参数数组，为sql文本中的？依次赋值
	 * @param in   InputStream  将输入流in的内容写入到blob字段
	 * @return void
	*/
	public void writeOracleBlobColumn(String sqlText,Object[] params,InputStream in) throws SQLException
	{
		PreparedStatement stmt=null;
		ResultSet rs=null;
		
		this.beginTransaction();
		
		try
		{
			stmt=connection.prepareStatement(sqlText);
			
			if(params!=null)
			{
				for(int i=0;i<params.length;i++)
					stmt.setObject(i+1, params[i]);
			}
			
			rs=stmt.executeQuery();
			if(rs.next())
			{
				Blob blob=rs.getBlob(1);
				
				BLOB oraBlob=(BLOB)blob;
				
				BufferedOutputStream out=new BufferedOutputStream( blob.setBinaryStream(1L) );
				byte [] buffer=new byte[oraBlob.getChunkSize()];
				int c;
				while( (c=in.read(buffer))!=-1)
				{
					out.write(buffer,0,c);
				}
				out.flush();
				out.close();
			}
			
			this.commit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			this.rollback();
		}
		finally
		{
			close(null,stmt,null);
		}
	}
	
	
	/**
	 * readOracleBlobColumn：从Oracle数据库表中读出一个blob字段
	 * @author sungeng
	 * @param sqlText  String select格式的sql文本，里面可带？参数，用于查询blob字段（必须为查询结果中的第一个字段）
	 * @param params   Object[] 参数数组，为sql文本中的？依次赋值
	 * @param out   OutputStream  将blob字段的内容写到out输出流之中
	 * @return void
	*/
	public void readOracleBlobColumn(String sqlText,Object[] params,OutputStream out)
	{
		
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try
		{
			stmt=connection.prepareStatement(sqlText);
			
			if(params!=null)
			{
				for(int i=0;i<params.length;i++)
					stmt.setObject(i+1, params[i]);
			}
			
			rs=stmt.executeQuery();
			if(rs.next())
			{
				Blob blob=rs.getBlob(1);
				//BLOB oraBlob=(BLOB)blob;
				
				InputStream in=blob.getBinaryStream(1L, blob.length());
				byte [] buffer=new byte[1024];
				int c;
				while( (c=in.read(buffer))!=-1)
				{
					out.write(buffer,0,c);
				}
				in.close();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			close(null,stmt,null);
		}
	}
	
	
	public String setResultRange(String sqlText,int startRow,int count)
	{
		int lastRow=startRow+count-1;
		
		String convertSql="select A.*,rownum rn from (" + sqlText+ " ) A where  rownum<=" +lastRow;
		convertSql="select * from (" + convertSql+ " ) where rn>="+ startRow;
		return convertSql;
	}
	
	
	
	public void beginTransaction() throws SQLException
	{
		this.connection.setAutoCommit(false);
	}
	
	public void commit() throws SQLException
	{
		try {
			this.connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.connection.setAutoCommit(true);
	}
	
	public void rollback() throws SQLException
	{
		try {
			this.connection.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.connection.setAutoCommit(true);
	}
	
	public void close()
	{
		close(this.connection,null,null);
	}
	
	
	
	public static void close(Connection conn,Statement stmt,ResultSet rs)
	{
		try
		{
			if(rs!=null)
				rs.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		try
		{
			if(stmt!=null)
				stmt.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		try
		{
			if(conn!=null)
				conn.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
}

