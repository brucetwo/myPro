package whu.filter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import whu.common.Context;
import whu.common.DbSessionFactory;

/**
 * Servlet Filter implementation class ContextFilter
 */

//绑定request,response到threadlocal
public class ContextFilter implements Filter {

    /**
     * Default constructor. 
     */
    public ContextFilter() {
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
		//request.setCharacterEncoding("UTF-8");
		
		//DbSession dbSession=DbSessionFactory.openDbSession();
		//request.setAttribute("dbSession", dbSession);
		
		Map <String,Object> map=new HashMap<String,Object>();
		
		Context.setThreadLocalMap(map);
		
		Context.set("request",request );
		Context.set("response",response );
		
		chain.doFilter(request, response);
		
		DbSessionFactory.closeCurrentDbSession();
		
		Context.removeThreadLocalMap();
		//dbSession.close();
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
