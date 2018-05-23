package com.tsb.ischool.framework.common;


/**
 * 
 * @author zkl
 * @date 2014-7-17 下午4:23:23
 * @version 1.0
 */
public class ReadWriteDataSourceDecision {
    
    public enum DataSourceType {
        write, read;
    }
    
    
    private static final ThreadLocal<DataSourceType> holder = new ThreadLocal<DataSourceType>();

    public static void markWrite() {
        holder.set(DataSourceType.write);
    }
    
    public static void markRead() {
        holder.set(DataSourceType.read);
    }
    
    public static void reset() {
        holder.set(null);
    }
    
    public static boolean isChoiceNone() {
        return null == holder.get(); 
    }
    
    public static boolean isChoiceWrite() {
        return DataSourceType.write == holder.get();
    }
    
    public static boolean isChoiceRead() {
        return DataSourceType.read == holder.get();
    }

}
