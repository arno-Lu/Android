package com.ckt.io.wifidirect.utils;

import java.io.File;
import java.lang.reflect.Method;

import android.content.Context;
import android.os.storage.StorageManager;

/**
 * @author Administrator
 */
public class SdcardUtils {
	
	/**
	 * @param isExternalSdcardFirst
	 * @return
	 */
	public static File getUseableSdcardFile(Context context, boolean isExternalSdcardFirst) {
		File f = null;
		if(isExternalSdcardFirst) {
			f = getExternalSDcardFile(context);
			if(f == null) {
				f = getInnerSDcardFile(context);
			}
		}else {
			f = getInnerSDcardFile(context);
			if(f == null) {
				f = getExternalSDcardFile(context);
			}
		}
		
		return f;
	}

    /**
     * get the external sdcard File
     * @return return the sdcard if exist or return null
     */
    public static File getExternalSDcardFile(Context context) {
        String sdPath = getSDCards(context)[1];
        if(!"".equals(sdPath) && canRead(sdPath)) {
            return new File(sdPath);
        }
        return null;
    }

    /**
     * get the inner sdcard File
     * @return
     */
    public static File getInnerSDcardFile(Context context) {
        String innerPath = getSDCards(context)[0];
        if(!"".equals(innerPath) && canRead(innerPath)) {
            return new File(innerPath);
        }
        return null;
    }

    /**
     * 
     * @param path
     * @return
     */
    private static boolean canRead(String path) {
        File f = new File(path);
        return f.canRead();
    }

    /**API14
     *
     * @param context
     * @return sdlist 0-->inner 1--->external
     */
    private static String[] getSDCards(Context context) {
        String [] ret = new String [2];
        String exteranl_sd="", inner_sd="";
        try {
            Class StorageVolume = Class.forName("android.os.storage.StorageVolume");
            StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Method getVolumeList = storageManager.getClass().getMethod("getVolumeList");
            Method isRemovable = StorageVolume.getMethod("isRemovable");
            Method getPath = StorageVolume.getMethod("getPath");
            Object[] volumes = (Object[]) getVolumeList.invoke(storageManager);
            if(volumes == null) return ret;
            for(int i=0; i<volumes.length; i++) {
                if((Boolean)(isRemovable.invoke(volumes[i]))) {
                    final String temp = (String)getPath.invoke(volumes[i]);
                    if(!temp.contains("usb")) {
                        exteranl_sd = temp;
                    }
                }else {
                    inner_sd = (String)getPath.invoke(volumes[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ret[0] = inner_sd;
        ret[1] = exteranl_sd;
        return ret;
    }
}
