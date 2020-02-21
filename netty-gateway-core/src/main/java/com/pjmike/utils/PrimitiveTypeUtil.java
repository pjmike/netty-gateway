package com.pjmike.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 定义了基本类型的工具类，
 * 可以方便的判断一个Class对象是否属于基本类型或基本类型的数组。
 * 本工具类所包含的基本类型判断包括如下一些内容：
 * </p>
 * <p>
 * String
 * boolean
 * byte
 * short
 * int
 * long
 * float
 * double
 * char
 * Boolean
 * Byte
 * Short
 * Integer
 * Long
 * Float
 * Double
 * Character
 * BigInteger
 * BigDecimal
 * </p>
 *
 * <p>
 * 摘自链接：https://github.com/all4you/redant
 * </p>
 */
public class PrimitiveTypeUtil {

	/**
	 * 私有的构造函数防止用户进行实例化。
	 */
	private PrimitiveTypeUtil() {}

	/** 基本类型  **/
	private static final Class<?>[] PRI_TYPE = { 
			String.class, 
			boolean.class,
			byte.class, 
			short.class, 
			int.class, 
			long.class, 
			float.class,
			double.class, 
			char.class, 
			Boolean.class, 
			Byte.class, 
			Short.class,
			Integer.class, 
			Long.class, 
			Float.class, 
			Double.class,
			Character.class, 
			BigInteger.class, 
			BigDecimal.class 
	};

	/** 基本数组类型  **/
	private static final Class<?>[] PRI_ARRAY_TYPE = { 
			String[].class,
			boolean[].class, 
			byte[].class, 
			short[].class, 
			int[].class,
			long[].class, 
			float[].class, 
			double[].class, 
			char[].class,
			Boolean[].class, 
			Byte[].class, 
			Short[].class, 
			Integer[].class,
			Long[].class, 
			Float[].class, 
			Double[].class, 
			Character[].class,
			BigInteger[].class, 
			BigDecimal[].class 
	};
	
	/**
	 * 基本类型默认值
	 */
	private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<Class<?>, Object>(9);
	static {
        primitiveDefaults.put(boolean.class, false);
        primitiveDefaults.put(byte.class, (byte)0);
        primitiveDefaults.put(short.class, (short)0);
        primitiveDefaults.put(char.class, (char)0);
        primitiveDefaults.put(int.class, 0);
        primitiveDefaults.put(long.class, 0L);
        primitiveDefaults.put(float.class, 0.0f);
        primitiveDefaults.put(double.class, 0.0);
	}
	
	/**
	 * 判断是否为基本类型
	 * @param cls 需要进行判断的Class对象
	 * @return 是否为基本类型
	 */
	public static boolean isPriType(Class<?> cls) {
		for (Class<?> priType : PRI_TYPE) {
			if (cls == priType){
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否为基本类型数组
	 * @param cls 需要进行判断的Class对象
	 * @return 是否为基本类型数组
	 */
	public static boolean isPriArrayType(Class<?> cls) {
		for (Class<?> priType : PRI_ARRAY_TYPE) {
			if (cls == priType){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获得基本类型的默认值
	 * @param type 基本类型的Class
	 * @return 基本类型的默认值
	 */
	public static Object getPriDefaultValue(Class<?> type) {
		return primitiveDefaults.get(type);
	}
	
}
