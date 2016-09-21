package whu.action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import whu.common.Context;



public class BaseAction {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	public BaseAction()
	{
		request=(HttpServletRequest)Context.get("request");
		response=(HttpServletResponse)Context.get("response");
	}
	
}
