package com.fs.everything.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

public class CommonUtils {

	/**
	 * 给JavaBean字段值为NULL的字段设置默认值
	 * @param obj 标准的JavaBean
	 */
	public static void replaceNullValue(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				fields[i].setAccessible(true);
				if (fields[i].get(obj) == null) {
					if (String.class==fields[i].getType()) {
						fields[i].set(obj, "");
					} else if (Date.class==fields[i].getType()) {
						fields[i].set(obj, new Date(0));
					} else if (BigDecimal.class==fields[i].getType()) {
						fields[i].set(obj, new BigDecimal(-1));
					} else if (Integer.class==fields[i].getType()) {
						fields[i].set(obj, -1);
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public static void replaceNullValueOfMap(Map<String, Object> map){
		Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Object> en = it.next();
			if (null==en.getValue()){
				map.put(en.getKey(), "");
			}
		}
	}

	/**
	 * 将JavaBean的信息设置到Map中。Map的key为字段名的大写形式，value为字段值。
	 * @param result Map对象
	 * @param bean 标准的JavaBean
	 */
	public static void setCmdMap(Map<String, Object> result, Object bean) {
		Field[] fields = bean.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				fields[i].setAccessible(true);
				if (fields[i].getType()==Date.class) {
					result.put(fields[i].getName().toUpperCase(), DateUtils.format((Date)fields[i].get(bean)));
				}else{
					result.put(fields[i].getName().toUpperCase(), fields[i].get(bean));
				}
			} catch (Exception e) {
				System.out.println("setCmdMap-->"+e);
			}
		}
	}

	/**
	 * 将存放JavaBean的list的内容设置到Map中。Map的key为Bean字段名的大写形式，value为字段值。
	 */
	public static List<Map<String, Object>> beanListToMapList(List<Object> beanList) {
		if (beanList==null||beanList.size()<1) {
			return null;
		}
		List<Map<String, Object>> retList = new ArrayList<Map<String,Object>>();
		Field[] fields = beanList.get(0).getClass().getDeclaredFields();
		Map<String, Object> result = null;
		for (int j = 0; j < beanList.size(); j++) {
			
			result = new HashMap<String, Object>();
			for (int i = 0; i < fields.length; i++) {
				try {
					fields[i].setAccessible(true);
					if (fields[i].getType()==Date.class) {
						result.put(fields[i].getName().toUpperCase(), DateUtils.format((Date)fields[i].get(beanList.get(j))));
					}else{
						result.put(fields[i].getName().toUpperCase(), fields[i].get(beanList.get(j)));
					}
				} catch (Exception e) {
					System.out.println("com.actionsoft.apps.fs_b2b.utils.CommonUtils.beanListToMapList()-->"+e);
				}
			}
			retList.add(result);
		}
		
		return retList;
	}
	
	/**
	 * 解析如下类型字符串并保存到map中（key1:value1|key2:value2|key3:value3）
	 * map.get("KEY1")返回value1
	 * map.get("KEY2")返回value2
	 * map.get("KEY3")返回value3
	 * @param paramsStr
	 * @return
	 */
	public static Map<String, String> parseParamsStr(String paramsStr) {
		if(paramsStr==null){return Collections.emptyMap();}
		Map<String, String> params = new HashMap<String, String>();
		String[] arr = paramsStr.split("\\|");// 需要转义
		for (int i = 0; i < arr.length; i++) {
			arr[i] = arr[i].replaceFirst(":", "-->");
			String[] subArr = arr[i].split("-->");
			if (subArr.length == 1) {
				String[] tempArr = new String[2];
				tempArr[0] = subArr[0];
				tempArr[1] = "";
				subArr = tempArr;
			} else if (subArr.length == 0) {
				String[] tempArr = new String[2];
				tempArr[0] = "";
				tempArr[1] = "";
				subArr = tempArr;
			}
			params.put(subArr[0].toUpperCase(), subArr[1]);
		}
	
		return params;

	}
	
	/**
	 * 返回a到b之间范围的随机数，包含a和b
	 * @param a
	 * @param b
	 * @return
	 */
	public static int myRnd(int a, int b){
		return (int)Math.floor((b-a+1)*Math.random()+a);
	}

	/**
	 * 将map的所有key转成小写
	 * @param map
	 * @return
	 */
	/*public static <T> Map<String, T> mapKeyToLowerCase(Map<String, T> map){
		Map<String, T> resultMap = new HashMap<>();
		Iterator<Map.Entry<String, T>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, T> en = iterator.next();
			resultMap.put(en.getKey().toLowerCase(), en.getValue());
		}
		return resultMap;
	}*/

	/**
	 * @return
	 */
	/*public static Map<String, String> getAllParamsFromRequest(Map<String, String[]> map){
		Map<String, String> resultMap = new HashMap<>();
		Iterator<Map.Entry<String, String[]>> it = map.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, String[]> en = it.next();
			resultMap.put(en.getKey().toLowerCase(), en.getValue()[0]);
		}
		return resultMap;
	}*/
}
