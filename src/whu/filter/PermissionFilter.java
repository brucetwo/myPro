package whu.filter;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import whu.entity.User1Bean;
import whu.entity.UserBean;

/**
 * Servlet Filter implementation class PermissionFilter
 */
public class PermissionFilter implements Filter {

    /**
     * Default constructor. 
     */
    public PermissionFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		
		HttpServletRequest rst=(HttpServletRequest)request;
		HttpSession session=rst.getSession();
		UserBean user=(UserBean)session.getAttribute("user");
		
		
		String actionUrl=rst.getServletPath();
		
		if(actionUrl.endsWith(".jpg") || actionUrl.endsWith(".png") || actionUrl.endsWith(".css") ||actionUrl.endsWith(".js"))
			;//静态资源
		else if(actionUrl.startsWith("/common"))
			;//公共页面
		else//特殊页面需要访问权限
		{
//			if(user==null)
//			{
//				rst.setAttribute("errMsg", "你没登录");
//				rst.setAttribute("errLink", "/common/login.html");
//				rst.getRequestDispatcher("/common/errMsg.jsp").forward(request, response);
//				return;
//			}
			if(actionUrl.startsWith("/admin"))
			{
				if(user.getIdentity()!=0)//admin:0,customer:1,seller:2
				{
					rst.setAttribute("errMsg", "你的权限不足");
					rst.setAttribute("errLink", "javascript:history.back()");
					rst.getRequestDispatcher("/common/errMsg.jsp").forward(request, response);
					return;
				}
			}
			if(actionUrl.startsWith("/seller"))
			{
				if(user.getIdentity()==1)//admin:0,customer:1,seller:2
				{
					rst.setAttribute("errMsg", "你的权限不足");
					rst.setAttribute("errLink", "javascript:history.back()");
					rst.getRequestDispatcher("/common/errMsg.jsp").forward(request, response);
					return;
				}
			}
			if(actionUrl.startsWith("/customer"))
			{
				if(user.getIdentity()==2)//admin:0,customer:1,seller:2
				{
					rst.setAttribute("errMsg", "你的权限不足");
					rst.setAttribute("errLink", "javascript:history.back()");
					rst.getRequestDispatcher("/common/errMsg.jsp").forward(request, response);
					return;
				}
			}
			
		}
		
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
