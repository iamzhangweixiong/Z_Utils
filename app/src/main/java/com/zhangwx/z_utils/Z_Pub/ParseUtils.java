package com.zhangwx.z_utils.Z_Pub;

import android.text.TextUtils;

/**
 * 转换的utils，String转成int、long、double时需要try...catch
 * @author cby
 *
 */
public class ParseUtils {
	
    public static int parseInt(String str) {
    	int ret = 0;
    	if (TextUtils.isEmpty(str)) {
    		return ret;
    	} 
    	
        try {
        	ret = Integer.parseInt(str.trim());
        } catch(NumberFormatException e) {
        }
        
        return ret;
    }
    
    public static int parseInt(String str, String tag) {
    	int ret = 0;
    	if (TextUtils.isEmpty(str)) {
    		return ret;
    	} 
    	
        try {
        	ret = Integer.parseInt(str);
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }
        
        return ret;
    }
    
    public static long parseLong(String str) {
    	long ret = 0L;
    	if (TextUtils.isEmpty(str)) {
    		return ret;
    	} 
    	
        try {
        	ret = Long.parseLong(str);
        } catch(NumberFormatException e) {
        }
        
        return ret;
    }
    
    public static long parseLong(String str, String tag) {
    	long ret = 0L;
    	if (TextUtils.isEmpty(str)) {
    		return ret;
    	} 
    	
        try {
        	ret = Long.parseLong(str);
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }
        
        return ret;
    }
    
    public static float parseFloat(String str) {
    	float ret = 0.0f;
    	if (TextUtils.isEmpty(str)) {
    		return ret;
    	} 
    	
        try {
        	ret = Float.parseFloat(str);
        } catch(NumberFormatException e) {
        }
        
        return ret;
    }
    
    public static float parseFloat(String str, String tag) {
    	float ret = 0.0f;
    	if (TextUtils.isEmpty(str)) {
    		return ret;
    	} 
    	
        try {
        	ret = Float.parseFloat(str);
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }
        
        return ret;
    }
    
    public static double parseDouble(String str) {
    	double ret = 0;
    	if (TextUtils.isEmpty(str)) {
    		return ret;
    	} 
    	
        try {
        	ret = Double.parseDouble(str);
        } catch(NumberFormatException e) {
        }
        
        return ret;
    }
    
    public static double parseDouble(String str, String tag) {
    	double ret = 0;
    	if (TextUtils.isEmpty(str)) {
    		return ret;
    	} 
    	
        try {
        	ret = Double.parseDouble(str);
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }
        
        return ret;
    }
}
