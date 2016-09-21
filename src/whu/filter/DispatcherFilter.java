package whu.filter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Filter implementation class DispatcherFilter
 */
public class DispatcherFilter implements Filter {

	private MvcCfg mvcCfg = new MvcCfg();

	/**
	 * Default constructor.
	 */
	public DispatcherFilter() {
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
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		// chain.doFilter(request, response);

		HttpServletRequest req = (HttpServletRequest) request;

		String url = req.getServletPath();
		System.out.println(url);
		// MvcCfgItem cfgItem=mvcCfg.getMvcCfgItem(url);
		try {
			while (true) {
				Object actionObject = mvcCfg.getClazz(url).newInstance();

				String viewName = (String) (mvcCfg.getMethod(url)
						.invoke(actionObject));// 返回处理类别

				if ("CUSTOM".equals(viewName))
					break;

				String viewUrl = mvcCfg.getViewMap(url).get(viewName);// 返回处理对象
				if (viewUrl.endsWith(".action")) {
					url = viewUrl;
				} else {
					req.getRequestDispatcher(viewUrl).forward(req, response);
					break;
				}
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		try {
			this.getMvcCfg();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void getMvcCfg() throws IOException, ClassNotFoundException,
			NoSuchMethodException, SecurityException {
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream("whu.properties");
		java.util.Properties props = new java.util.Properties();
		props.load(in);

		Iterator it = props.keySet().iterator();
		while (it.hasNext()) {
			MvcCfgItem cfgItem = new MvcCfgItem();

			String url = (String) it.next();
			String propValue = props.getProperty(url);

			cfgItem.setActionUrl(url);

			JSONTokener jsonToken = new JSONTokener(propValue);
			JSONObject jsonObj = (JSONObject) jsonToken.nextValue();

			String className = jsonObj.getString("class");

			Class clazz = Class.forName(className);
			mvcCfg.putClazzToClassMap(className, clazz);
			cfgItem.setActionClass(className);

			String methodName = jsonObj.getString("method");
			cfgItem.setActionMethod(methodName);

			JSONObject jsonObjView = null;
			try {
				jsonObjView = jsonObj.getJSONObject("view");
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if (jsonObjView != null) {
				String[] viewArray = JSONObject.getNames(jsonObjView);

				Map<String, String> viewMap = new HashMap<String, String>();
				for (int i = 0; i < viewArray.length; i++) {
					viewMap.put(viewArray[i],
							jsonObjView.getString(viewArray[i]));
					cfgItem.setView(viewMap);
				}
			}

			mvcCfg.putItemToCfgMap(cfgItem);

		}
	}

	public static class MvcCfgItem {
		private String actionUrl;
		private String actionClass;
		private String actionMethod;
		private Map<String, String> view;

		public String getActionUrl() {
			return actionUrl;
		}

		public void setActionUrl(String actionUrl) {
			this.actionUrl = actionUrl;
		}

		public String getActionClass() {
			return actionClass;
		}

		public void setActionClass(String actionClass) {
			this.actionClass = actionClass;
		}

		public String getActionMethod() {
			return actionMethod;
		}

		public void setActionMethod(String actionMethod) {
			this.actionMethod = actionMethod;
		}

		public Map<String, String> getView() {
			return view;
		}

		public void setView(Map<String, String> view) {
			this.view = view;
		}
	}

	public static class MvcCfg {
		private Map<String, Class> classMap = new HashMap<String, Class>();
		private Map<String, MvcCfgItem> mvcCfgMap = new HashMap<String, MvcCfgItem>();

		public Map<String, Class> getClassMap() {
			return classMap;
		}

		public void setClassMap(Map<String, Class> classMap) {
			this.classMap = classMap;
		}

		public Map<String, MvcCfgItem> getMvcCfgMap() {
			return mvcCfgMap;
		}

		public void setMvcCfgMap(Map<String, MvcCfgItem> mvcCfgMap) {
			this.mvcCfgMap = mvcCfgMap;
		}

		public void putItemToCfgMap(MvcCfgItem item) {
			mvcCfgMap.put(item.getActionUrl(), item);
		}

		public void putClazzToClassMap(String className, Class clazz) {
			classMap.put(className, clazz);
		}

		public MvcCfgItem getMvcCfgItem(String actionUrl) {
			return mvcCfgMap.get(actionUrl);
		}

		public Class getClazz(String actionUrl) {
			return classMap.get(mvcCfgMap.get(actionUrl).getActionClass());
		}

		public Method getMethod(String actionUrl) throws NoSuchMethodException,
				SecurityException {
			Class clazz = this.getClazz(actionUrl);
			return clazz.getMethod(mvcCfgMap.get(actionUrl).getActionMethod());
		}

		public Map<String, String> getViewMap(String actionUrl) {
			return mvcCfgMap.get(actionUrl).getView();
		}
	}

}
