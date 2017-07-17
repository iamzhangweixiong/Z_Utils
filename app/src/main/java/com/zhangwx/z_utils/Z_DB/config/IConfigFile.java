package com.zhangwx.z_utils.Z_DB.config;

import java.util.List;

public interface IConfigFile {
	public String getConfigName();

	public long getLongValue(String key, long defValue);

	public boolean getBooleanValue(String key, boolean defValue);

	public int getIntValue(String key, int defValue);

	public String getStringValue(String key, String defValue);

	public void setBooleanValue(String key, boolean value);

	public void setLongValue(String key, long value);

	public void setIntValue(String key, int value);

	public void setStringValue(String key, String value);

    public void removes(List<String> keys);
}
