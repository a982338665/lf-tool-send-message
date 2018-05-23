package com.tsb.ischool.framework.bean.comm;


import org.codehaus.jackson.annotate.JsonIgnore;
import com.tsb.ischool.framework.exception.ISchoolException;


/**
 * 
 * @author wl
 * @date 2016-5-13
 * @version 1.0
 */
public abstract class BaseBean{
	
	private Integer ordernum;//排序号
	private Integer isused;//状态（0=不可用；1=可用）
	private String createid;//创建人id
	private String createtime;//创建时间
	private String updatorid; //修改人id
	private String updatortime;//修改时间
	private String optip;//操作ip
	private String ext1;//扩展字段1
	private String ext2;//扩展字段2
	private String ext3;//扩展字段3
	

	public Integer getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}

	public Integer getIsused() {
		return isused;
	}

	public void setIsused(Integer isused) {
		this.isused = isused;
	}

	public String getCreateid() {
		return createid;
	}

	public void setCreateid(String createid) {
		this.createid = createid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdatorid() {
		return updatorid;
	}

	public void setUpdatorid(String updatorid) {
		this.updatorid = updatorid;
	}

	public String getUpdatortime() {
		return updatortime;
	}

	public void setUpdatortime(String updatortime) {
		this.updatortime = updatortime;
	}

	public String getOptip() {
		return optip;
	}

	public void setOptip(String optip) {
		this.optip = optip;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	
	
}
