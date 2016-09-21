package whu.common;
import java.util.Map;

public class Context {
	public static ThreadLocal< Map<String,Object> > threadLocal=new ThreadLocal< Map<String,Object>>();
	
	public static void setThreadLocalMap(Map<String,Object> map)
	{
		threadLocal.set(map);
	}
	
	public static void removeThreadLocalMap()
	{
		threadLocal.remove();
	}
	
	public static Object get(String key)
	{
		Map<String,Object> map=threadLocal.get();
		return map.get(key);
	}
	
	public static void set(String key,Object value)
	{
		Map<String,Object> map=threadLocal.get();
		map.put(key, value);
		
	}
	
	public static void remove(String key)
	{
		Map<String,Object> map=threadLocal.get();
		map.remove(key);
	}
}
