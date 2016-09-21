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
import whu.dao.UserDao;
import whu.entity.User1Bean;
import whu.entity.UserBean;


public class TestAction extends BaseAction{
	public String add()
	{
    System.out.println(
    		UserDao.addUser(new UserBean(1,1,1,"a","a","a","a"))
    		);
    return "CUSTOM";
	}
	
}
