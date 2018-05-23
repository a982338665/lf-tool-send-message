/**
 * 版权所有：版权所有(C) 2016，学酷网络 
 * 文件名称: tmpTubasicMsgRest.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */
package com.tsb.ischool.tmpmsg.webservice;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import com.tsb.ischool.framework.bean.comm.PageBean;
import com.tsb.ischool.framework.bean.comm.ResultBean;
import com.tsb.ischool.framework.common.ErrorCode;
import com.tsb.ischool.framework.common.ISchoolConstants;
import com.tsb.ischool.framework.exception.ISchoolException;
import com.tsb.ischool.sms.model.MobileMessage;
import com.tsb.ischool.sms.service.impl.SendMsgQueue;
import com.tsb.ischool.tmpmsg.model.tmpTubasicMsg;
import com.tsb.ischool.tmpmsg.service.ItmpTubasicMsgService;
import com.tsb.ischool.tmpmsg.webservice.c2sbean.C2StmpTubasicMsg;
import com.tsb.ischool.utils.UuidUtil;

/**
 * 类 编 号： 
 * 类 名 称：tmpTubasicMsgRest
 * 内容摘要：
 * 完成日期：
 * 编码作者：
 */
@Controller
@Path("/tmptubasicmsg")
public class tmpTubasicMsgRest
{
 
    private Logger logger = Logger.getLogger(tmpTubasicMsgRest.class);
	@Resource
	private ItmpTubasicMsgService tmpTubasicMsgService;

	private void log(String message) {
		String operation = ISchoolConstants.LOGGER_PREFIX_INFO + "THREADID = "
				+ Thread.currentThread().getId() + ".|"+this.getClass().getName()+"." + message
				+ ".|";
		logger.info(operation);
	}
	
	/**
	 * 新增或者编辑保存tmpTubasicMsg
	 * @throws ISchoolException
	 */
	@POST
	@Path("/insertorupdate")
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	public ResultBean insertorupdate(tmpTubasicMsg bean,@Context HttpServletRequest request) throws ISchoolException {
		int i = 0;
		if(bean.getMobile()==null){
			bean.setMobile(UuidUtil.generateUUID());
			i = tmpTubasicMsgService.insert(bean);
		}else{
			i = tmpTubasicMsgService.update(bean);
		}
		return new ResultBean(ResultBean.CODE_SUCCESS, 0, i, "");
	}
	
	/**
	 * 删除tmpTubasicMsg
	 * @throws ISchoolException
	 */
	@POST
	@Path("/delete")
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	public ResultBean delete(tmpTubasicMsg bean,@Context HttpServletRequest request) throws ISchoolException {
		int i = 0;
		if(bean.getMobile()!=null){
			i = tmpTubasicMsgService.deleteById(bean.getMobile());
		}
		return new ResultBean(ResultBean.CODE_SUCCESS, 0, i, "");
	}
	
	/**
	 * 逻辑删除tmpTubasicMsg
	 * @throws ISchoolException
	 */
	@POST
	@Path("/deletelogic")
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	public ResultBean deletelogic(tmpTubasicMsg bean,@Context HttpServletRequest request) throws ISchoolException {
		int i = 0;
		if(bean.getMobile()!=null){
			i = tmpTubasicMsgService.deletelogicById(bean.getMobile());
		}
		return new ResultBean(ResultBean.CODE_SUCCESS, 0, i, "");
	}
	
	/**
	 * 通过id查询tmpTubasicMsg详细
	 * 
	 * @throws ISchoolException
	 */
	@POST
	@Path("/querybyid")
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	public ResultBean querybyid(tmpTubasicMsg bean)
			throws ISchoolException {
		String opration = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|通过id查询tmpTubasicMsg详细.";
		logger.debug(opration + "| 开始校验参数 | tmpTubasicMsg="
				+ bean.toString());
		if (bean.verify()) {
			tmpTubasicMsg  tmpTubasicMsg = tmpTubasicMsgService.queryById(bean.getMobile());
			logger.debug(opration + "| 完成查询 | =tmpTubasicMsg"
					+ tmpTubasicMsg);
			return new ResultBean(ResultBean.CODE_SUCCESS, 0,tmpTubasicMsg, "");
		}
		return new ResultBean(ResultBean.CODE_ERROR,
				ErrorCode.ISCHOOL_REQJSON_PARSEMSG_EXCEPTION, 1, "");
	}
	
	/**
	 * 查询tmpTubasicMsg
	 * 
	 * @throws ISchoolException
	 */
	@POST
	@Path("/query")
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	public ResultBean query(C2StmpTubasicMsg bean)
			throws ISchoolException {
		String opration = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
				+ Thread.currentThread().getId() + ".|获取tmpTubasicMsg列表.";
		logger.debug(opration + "| 开始校验参数 | bean="
				+ bean.toString());
		if (bean.verify()) {
			PageBean<tmpTubasicMsg> list = tmpTubasicMsgService
					.query(bean);
			logger.debug(opration + "| 完成查询 | =tmpTubasicMsg"
					+ list);
			return new ResultBean(ResultBean.CODE_SUCCESS, 0,list, "");
		}
		return new ResultBean(ResultBean.CODE_ERROR,
				ErrorCode.ISCHOOL_REQJSON_PARSEMSG_EXCEPTION, 1, "");
	}
	
	
	@GET
	@Path("/sendtmpmsg")
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	public ResultBean sendtmpmsg()
			throws ISchoolException {
		try {
			int count = 1;
			while(true){
				C2StmpTubasicMsg bean = new C2StmpTubasicMsg();
				bean.setCurpage(count);
				bean.setPagesize(1000);
				PageBean<tmpTubasicMsg> list = tmpTubasicMsgService
						.query(bean);
				List<tmpTubasicMsg> listTmp = list.getDataList();
				if(listTmp.size()==0){
					break;
				}
				for (tmpTubasicMsg tmpTubasicMsg : listTmp) {
					MobileMessage mm = new MobileMessage();
					mm.setId("1");
					mm.setMobileNo(tmpTubasicMsg.getMobile());
					mm.setMessageContent(tmpTubasicMsg.getContextWs());
					mm.setSequenceId(0);//序列号设置0
					SendMsgQueue.getInstance().pushPrioritySending(mm);
				}
				count++;
				Thread.sleep(100);
			}
			return new ResultBean(ResultBean.CODE_SUCCESS, 0, "", "");
		} catch (Exception e) {
		}
		return new ResultBean(ResultBean.CODE_ERROR,
				ErrorCode.ISCHOOL_REQJSON_PARSEMSG_EXCEPTION, 1, "");
	}
	
	@GET
	@Path("/getqueuesize")
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	public ResultBean getQueueSize()
			throws ISchoolException {
		try {
			return new ResultBean(ResultBean.CODE_SUCCESS, 0,"arrayPIR_Queue_fast to be sent:"+SendMsgQueue.arrayPIR_Queue.size()+"   ;arrayPIR_Queue_slow to be sent:"+SendMsgQueue.arrayPIR_Queue_Slow_sending.size(), "");
		} catch (Exception e) {
		}
		return new ResultBean(ResultBean.CODE_ERROR,
				ErrorCode.ISCHOOL_REQJSON_PARSEMSG_EXCEPTION, 1, "");
	}
	
 	
}
