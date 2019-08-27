package com.drzk.parkingguide.camera;

import com.sun.jna.Platform;

import java.io.File;

public class Utils {
	public Utils() {

	}

	public static String getOsPlatform(){
		String osPrefix = getOsPrefix();
		if(osPrefix.toLowerCase().startsWith("win32-x86")) {
			return "win32";
		} else if(osPrefix.toLowerCase().startsWith("win32-amd64") ) {
			return "win64";
		} else if(osPrefix.toLowerCase().startsWith("linux-i386")) {
			return "linux32";
		}else if(osPrefix.toLowerCase().startsWith("linux-amd64")) {
			return "linux64";
		}
		return "linux64";
	}
	
	// 获取操作平台信息
	public static String getOsPrefix() {
		String arch = System.getProperty("os.arch").toLowerCase();
		final String name = System.getProperty("os.name");
		String osPrefix;
		switch(Platform.getOSType()) {
			case Platform.WINDOWS: {
				if ("i386".equals(arch))
	                arch = "x86";
	            osPrefix = "win32-" + arch;
			}
            break;
			case Platform.LINUX: {
				if ("x86".equals(arch)) {
	                arch = "i386";
	            }
	            else if ("x86_64".equals(arch)) {
	                arch = "amd64";
	            }
	            osPrefix = "linux-" + arch;
			}			       
	        break;
			default: {
	            osPrefix = name.toLowerCase();
	            if ("x86".equals(arch)) {
	                arch = "i386";
	            }
	            if ("x86_64".equals(arch)) {
	                arch = "amd64";
	            }
	            int space = osPrefix.indexOf(" ");
	            if (space != -1) {
	                osPrefix = osPrefix.substring(0, space);
	            }
	            osPrefix += "-" + arch;
			}
	        break;
	       
		}

		return osPrefix;
	}	
	
    public static String getOsName() {
    	String osName = "";
		String osPrefix = getOsPrefix();
		if(osPrefix.toLowerCase().startsWith("win32-x86")
				||osPrefix.toLowerCase().startsWith("win32-amd64") ) {
			osName = "win";
		} else if(osPrefix.toLowerCase().startsWith("linux-i386")
				|| osPrefix.toLowerCase().startsWith("linux-amd64")) {
			osName = "linux";
		}
		
		return osName;
    }
    
    // 获取加载库
	public static String getLoadLibrary(String library) {
		if (isChecking()) {
			return null;
		}
		
		String loadLibrary = "";
		String osPrefix = getOsPrefix();
		if(osPrefix.toLowerCase().startsWith("win32-x86")) {
			loadLibrary = "src/main/resources/libs/win32/";
		} else if(osPrefix.toLowerCase().startsWith("win32-amd64") ) {
			//loadLibrary = "src/main/resources/libs/win64/";
			loadLibrary = new File("dahuaSDKlibs"+File.separator+"win64").getAbsolutePath()+File.separator+library;
		} else if(osPrefix.toLowerCase().startsWith("linux-i386")) {
			loadLibrary = "";
		}else if(osPrefix.toLowerCase().startsWith("linux-amd64")) {
			//loadLibrary = "";
			loadLibrary = new File("dahuaSDKlibs"+File.separator+"linux64").getAbsolutePath()+File.separator+library+".so";
		}
		
		System.out.printf("[Load %s Path : %s]\n", library, loadLibrary);
		return loadLibrary;
	}
	
	private static boolean checking = false;
	public static void setChecking() {
		checking = true;
	}
	public static void clearChecking() {
		checking = false;
	}
	public static boolean isChecking() {
		return checking;
	}
}
