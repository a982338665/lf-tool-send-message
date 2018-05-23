package com.tsb.ischool.framework.bean.comm;

import java.io.Serializable;
import java.util.List;


/**
 * 
 * @author shenziming
 * @date 2014-7-31
 * @version 1.0
 * @param <T>
 */
public final class PageBean<T> implements Serializable{    
    private int currentPage;// 当前页   
    private int nextPage;//下一页
    private int lastPage;//上一页
    private int pageSize;// 每页显示条数    
    private int totalPage;// 总页数    
    private int totalRecord;// 总记录数    
    private List<T> dataList;// 分页数据   
    private boolean hashLastPage;//是否有上一页
    private boolean hashNextPage;//是否有下一页
    
    private PageBean() {    
    }    
    
    /*  
     * 初始化PageBean实例  
     */    
    private PageBean(final int pageSize, final int page, final int totalRecord) {    
        // 初始化每页显示条数    
        this.pageSize = pageSize;    
        // 设置总记录数    
        this.totalRecord = totalRecord;    
        // 初始化总页数    
        setTotalPage();    
        // 初始化当前页    
        setCurrentPage(page);    
    
    }    
    
    /*  
     * 外界获得PageBean实例  
     */    
    public static PageBean newPageBean(final int pageSize, final int page, final int totalRecord) {    
    
        return new PageBean(pageSize, page, totalRecord);    
    }    
    
    // 设置当前请求页    
    private void setCurrentPage(int page) {    
//        try {    
//            currentPage = Integer.parseInt(page);    
//    
//        } catch (java.lang.NumberFormatException e) {    
//            // 这里异常不做处理，当前页默认为1    
//            currentPage = 1;    
//        }  
    	currentPage = page;
        // 如果当前页小于第一页时，当前页指定到首页    
        if (currentPage < 1) {     
            currentPage = 1;
            lastPage = currentPage;            
            hashLastPage = false;
        }
        else{
        	hashLastPage = true;
        	lastPage = currentPage - 1;    
        }
    
        if (currentPage > totalPage) {     
            currentPage = totalPage;    
            hashNextPage = false;
            nextPage = currentPage;
        }
        else{
        	hashNextPage = true;
        	nextPage = currentPage + 1;
        }
    
    }    
    
    private void setTotalPage() {    
        if (totalRecord % pageSize == 0) {    
    
            totalPage = totalRecord / pageSize;    
        } else {    
            totalPage = totalRecord / pageSize + 1;    
        }    
    }    
    
    /*  
     * 获得当前页  
     */    
    public int getCurrentPage() {    
        return currentPage;    
    }    
    
    /*  
     * 获得总页数  
     */    
    public int getTotalPage() {    
        return totalPage;    
    
    }    
    
    /*  
     * 获得开始行数  
     */    
    public int getStartRow() {    
        return (currentPage - 1) * pageSize;    
    }    
    
    /*  
     * 获得结束行  
     */    
    public int getEndRow() {    
        return currentPage * pageSize;    
    }    
    
    /*  
     * 获得翻页数据  
     */    
    public List<T> getDataList() {    
        return dataList;    
    }    
    
    /*  
     * 设置翻页数据  
     */    
    public void setDataList(List<T> dataList) {    
        this.dataList = dataList;    
    }    
    
    //首页    
     public int getFirst() {    
        
    	 return 1;    
     }   
     
     // // 尾页    
     //    
      public int getLast() {    
         
     	 return totalPage;    
      }
    //    
    //上一页    
//        
//     public int getPrevious() {    
//        
//    	 return currentPage - 1;    
//     } 
      
//  	//    
//    // // 下一页    
//     public int getNext() {    
//        
//    	 return currentPage + 1;    
//     }    
    //   
     
     
    public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public boolean isHashLastPage() {
		return hashLastPage;
	}

	public void setHashLastPage(boolean hashLastPage) {
		this.hashLastPage = hashLastPage;
	}

	public boolean isHashNextPage() {
		return hashNextPage;
	}

	public void setHashNextPage(boolean hashNextPage) {
		this.hashNextPage = hashNextPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	@Override
	public String toString() {
		return "PageBean [currentPage=" + currentPage + ", nextPage="
				+ nextPage + ", lastPage=" + lastPage + ", pageSize="
				+ pageSize + ", totalPage=" + totalPage + ", totalRecord="
				+ totalRecord + ", dataList=" + dataList + ", hashLastPage="
				+ hashLastPage + ", hashNextPage=" + hashNextPage + "]";
	}

 
	
     
     
}   