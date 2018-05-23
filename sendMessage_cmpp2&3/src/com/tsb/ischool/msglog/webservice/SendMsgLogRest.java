/**
 * 版权所有：版权所有(C) 2016，学酷网络 
 * 文件名称: SendMsgLogRest.java 
 * 设计作者: 
 * 完成日期: 
 * 内容摘要:
 *
 * 修改记录: 
 * 修改日期:
 * 修 改 人:
 * 修改内容:
 */
package com.tsb.ischool.msglog.webservice;

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
import com.tsb.ischool.msglog.model.SendMsgLog;
import com.tsb.ischool.msglog.service.ISendMsgLogService;
import com.tsb.ischool.msglog.webservice.c2sbean.C2SSendMsgLog;
import com.tsb.ischool.framework.bean.comm.PageBean;
import com.tsb.ischool.framework.bean.comm.ResultBean;
import com.tsb.ischool.framework.common.ErrorCode;
import com.tsb.ischool.framework.common.ISchoolConstants;
import com.tsb.ischool.framework.exception.ISchoolException;
import com.tsb.ischool.sms.model.MobileMessage;
import com.tsb.ischool.sms.service.impl.SendMsgQueue;
import com.tsb.ischool.tmpmsg.model.tmpTubasicMsg;
import com.tsb.ischool.tmpmsg.webservice.c2sbean.C2StmpTubasicMsg;
import com.tsb.ischool.utils.UuidUtil;

/**
 * 类 编 号： 
 * 类 名 称：SendMsgLogRest
 * 内容摘要：
 * 完成日期：
 * 编码作者：
 */
@Controller
@Path("/sendmsglog")
public class SendMsgLogRest
{
 
    private Logger logger = Logger.getLogger(SendMsgLogRest.class);
	@Resource
	private ISendMsgLogService sendMsgLogService;

	private void log(String message) {
		String operation = ISchoolConstants.LOGGER_PREFIX_INFO + "THREADID = "
				+ Thread.currentThread().getId() + ".|"+this.getClass().getName()+"." + message
				+ ".|";
		logger.info(operation);
	}
	
	
	@GET
	@Path("/query")
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	public ResultBean query()
			throws ISchoolException {
		try {
			int count = 1;
			if(SendMsgQueue.arrayPIR_Queue.size()==0){
				while(true){
					C2SSendMsgLog bean = new C2SSendMsgLog();
					bean.setCurpage(count);
					bean.setPagesize(1000);
					bean.setSendStatus(0);
					PageBean<SendMsgLog> list = sendMsgLogService
							.query(bean);
					List<SendMsgLog> listTmp = list.getDataList();
					if(listTmp.size()==0){
						break;
					}
					for (SendMsgLog tmpTubasicMsg : listTmp) {
						MobileMessage mm = new MobileMessage();
						mm.setId(tmpTubasicMsg.getId());
						mm.setMobileNo(tmpTubasicMsg.getMobile());
						mm.setMessageContent(tmpTubasicMsg.getContextWs());
						SendMsgQueue.getInstance().pushDetry(mm);
					}
					count++;
					Thread.sleep(100);
				}
			}
			return new ResultBean(ResultBean.CODE_SUCCESS, 0,"共发送"+count+"条", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultBean(ResultBean.CODE_ERROR,
				ErrorCode.ISCHOOL_REQJSON_PARSEMSG_EXCEPTION, 1, "");
	}

	
 	
}
