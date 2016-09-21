package whu.biz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import whu.dao.User1Dao;
import whu.entity.User1Bean;

public class User1Biz {

	
	public static Object[] validateUser(String name,String pwd)
	{
		Object [] rv=new Object[2];
		User1Bean ub=User1Dao.getUserByName(name);
		if(ub==null)
		{
			rv[0]=1;
			return rv;
		}
		
		if(ub.getUserPassword().equals(pwd))
		{
			rv[0]=0;
			rv[1]=ub;
			return rv;
		}
		else
		{
			rv[0]=2;
			return rv;
		}
	}
	
	public static int registUser(String name,String pwd,int permission,InputStream photoStream) throws IOException, SQLException
	{
		User1Bean ub=User1Dao.getUserByName(name);
		if(ub!=null)
			return 1;
		ub=new User1Bean(name,pwd,permission);
		int rv=User1Dao.addUser(ub);
		if(rv>0)
		{
			if(photoStream!=null)
			{
				User1Dao.setUserPhoto(ub, photoStream);
			}
			return 0;
		}
		return 2;
	}
	
	public static void getUserPhoto(int userId,OutputStream photoStream)
	{
		User1Dao.getUserPhoto(userId, photoStream);
	}
	
	public static Object[] getAllUsers(int pageNum,int countPerPage)
	{
		Object [] rv=new Object[2];
		int pagesCount;
		int startRow;
		
		int usersCount=User1Dao.getUsersCount();
		pagesCount=usersCount/countPerPage;
		if(usersCount%countPerPage>0)
			pagesCount++;
		rv[0]=pagesCount;
		startRow=countPerPage*(pageNum-1)+1;
		rv[1]=User1Dao.getAllUsers(startRow, countPerPage);
		
		return rv;
	}
	
	public static int deleteUser(int userId)
	{
		return User1Dao.deleteUser(userId);
	}
	
	public static int updateUser(User1Bean ub)
	{
		return User1Dao.updateUser(ub);
	}
	public static User1Bean getUserById(int userId)
	{
		return User1Dao.getUserById(userId);
	}
	
	
}
