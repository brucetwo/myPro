package whu.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import whu.biz.User1Biz;
import whu.entity.User1Bean;


public class User1Action extends BaseAction{
	public String login()
	{
		String name=request.getParameter("username");
		String pwd=request.getParameter("userpwd");
		Object [] rv=User1Biz.validateUser(name, pwd);
		
		int errCode=(Integer)rv[0];
		if(errCode==0)
		{
			HttpSession session=request.getSession();
			session.setAttribute("user", rv[1]);
			return "success";
		}
		if(errCode==1)
		{
			request.setAttribute("errMsg", "无此用户");
			request.setAttribute("errLink", "login.html");
			return "err";
		}
		if(errCode==2)
		{
			request.setAttribute("errMsg", "密码错误");
			request.setAttribute("errLink", "login.html");
			return "err";
		}
		return "err";
	}

	public String regist() throws FileUploadException, IOException, SQLException
	{
		String name=null;
		String pwd=null;
		InputStream photoStream=null;
		
		if( ServletFileUpload.isMultipartContent(request) )
		{
			DiskFileItemFactory factory=new DiskFileItemFactory();
			ServletFileUpload upload=new ServletFileUpload(factory);
			List<DiskFileItem> items= upload.parseRequest(request);
			Iterator<DiskFileItem> it=items.iterator();
			while(it.hasNext())
			{
				DiskFileItem fileItem=it.next();
				String itemName=fileItem.getFieldName();
				if(fileItem.isFormField())
				{
					String itemValue=fileItem.getString("UTF-8");
					if("username".equals(itemName))
						name=itemValue;
					else if("userpwd".equals(itemName))
						pwd=itemValue;
				}
				else
				{
					if("userphoto".equals(itemName))
						photoStream=fileItem.getInputStream();
				}
			}
			
			int rv=User1Biz.registUser(name, pwd, 1,photoStream);
			
			if(rv==0)
			{
				return "success";
			}
			else if(rv==1)
			{
				request.setAttribute("errMsg", "该用户已注册");
				request.setAttribute("errLink", "regist.jsp");
				return "err";
			}
			else
			{
				request.setAttribute("errMsg", "未知错误");
				request.setAttribute("errLink", "regist.jsp");
				return "err";
			}
			
		}
		else
		{
			request.setAttribute("errMsg", "非法访问");
			request.setAttribute("errLink", "regist.jsp");
			return "err";
		}

	}
	
	public String getPhoto() throws IOException
	{
		String strUserId=request.getParameter("userid");
		int userId=Integer.parseInt(strUserId);
		User1Biz.getUserPhoto(userId, response.getOutputStream());
		
		return "CUSTOM";
	}
	
	public String getAllUsers()
	{
		int pageNum=1;
		
		String strPageNum=request.getParameter("pagenum");
		if(strPageNum!=null && !("".equals(strPageNum)) )
			pageNum=Integer.parseInt(strPageNum);
		Object []rv=User1Biz.getAllUsers(pageNum, 2);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("pageCount", rv[0]);
		request.setAttribute("users", rv[1]);
		return "success";
		
	}
	
	public String deleteUser() throws IOException
	{
		String[] userIds=request.getParameterValues("usersel");
		
		if(userIds!=null)
		{
			for(int i=0;i<userIds.length;i++)
			{
				int userId=Integer.parseInt(userIds[i]);
				User1Biz.deleteUser(userId);
			}
		}
		
		return "success";
	}
	
	public String getEditUserView()
	{
		String strUserId=request.getParameter("userid");
		int userId=Integer.parseInt(strUserId);
		User1Bean user=User1Biz.getUserById(userId);
		request.setAttribute("user", user);
		return "success";
	}
	
	public String updateUser()
	{
		String strUserId=request.getParameter("userid");
		String userName=request.getParameter("username");
		String userPwd=request.getParameter("userpwd");
		String strUserPermission=request.getParameter("userpermission");
		int userId=Integer.parseInt(strUserId);
		int userPermission=Integer.parseInt(strUserPermission);
		User1Bean user=new User1Bean(userId,userName,userPwd,userPermission);
		User1Biz.updateUser(user);
		return "success";
		
	}
}
