package com.movementinsome.kernel.util;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.json.JSONObject;

public class JsonAnalysisUtil {

	public static Object setJsonObjectData(JSONObject data,Object object) {
		Class c = object.getClass();
		Method[] m = c.getMethods();
		for(int i=0;i<m.length;i++){
			Iterator<String> keys=data.keys();
			String name = m[i].getName();
			if(name.contains("set")){
				while(keys.hasNext()){
					String key=keys.next();
					if(name.toLowerCase().equals(("set"+key).toLowerCase())){
						Class[] t = m[i].getParameterTypes();
						String type = t[0].getName();
						try {
							if("java.lang.Long".equals(type)){
								m[i].invoke(object, new Object[]{data.getLong(key)});
							}else if("java.lang.Integer".equals(type)){
								m[i].invoke(object, new Object[]{data.getInt(key)});
							}else if("java.lang.Double".equals(type)){
								m[i].invoke(object, new Object[]{data.getDouble(key)});
							}else if("java.lang.String".equals(type)){
								m[i].invoke(object, new Object[]{data.getString(key)});
							}
							break;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}
				}
			}
		}
		
		return object;
	}
}
