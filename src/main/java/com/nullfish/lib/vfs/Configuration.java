package com.nullfish.lib.vfs;

import java.util.HashMap;
import java.util.Map;

/**
 * 設定クラス
 * @author shunji
 *
 */
public class Configuration {
	/**
	 * 設定のマップ
	 */
	private Map configMap = new HashMap();

	/**
	 * ファイルシステム毎の設定のマップ
	 */
	private Map fileSystemConfigMap = new HashMap();
	
	/**
	 * 設定をセットする
	 * @param name
	 * @param value
	 */
	public void setDefaultConfig(String name, Object value) {
		configMap.put(name, value);
	}
	
	public Object getDefaultConfig(String name) {
		return configMap.get(name);
	}

	public void setConfig(String name, FileSystem fileSystem, Object value) {
		setConfig(name, FileFactory.interpretSecurePath(fileSystem.getRootName()), value);
	}
	
	public Object getConfig(FileSystem fileSystem, String name) {
		return getConfig(FileFactory.interpretSecurePath(fileSystem.getRootName()), name);
	}
	
	public void setConfig(String name, String fileSystem, Object value) {
		Map config = (Map)fileSystemConfigMap.get(fileSystem);
		if(config == null) {
			config = new HashMap();
			fileSystemConfigMap.put(fileSystem, config);
		}
			
		config.put(name, value);
	}
	
	public Object getConfig(String fileSystem, String name) {
		Map config = (Map)fileSystemConfigMap.get(fileSystem);
		if(config == null) {
			return getDefaultConfig(name);
		}
			
		return config.get(name);
	}
}
