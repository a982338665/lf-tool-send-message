package com.tsb.ischool.sms.webservice;

import com.tsb.cmpp.CMPPMo;
import com.tsb.cmpp.SendMsgThread;
import com.tsb.cmpp.cmpp3.CMPPMo3;
import com.tsb.ischool.framework.bean.comm.ResultBean;
import com.tsb.ischool.framework.common.ErrorCode;
import com.tsb.ischool.framework.common.ISchoolConstants;
import com.tsb.ischool.framework.common.PropertyUtil;
import com.tsb.ischool.framework.exception.ISchoolException;
import com.tsb.ischool.sms.model.C2SMobile;
import com.tsb.ischool.sms.service.ISendSMSMessageService;
import com.tsb.ischool.sms.service.impl.SendMsgQueue;
import com.tsb.ischool.utils.ClientIpUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * 发送手机短信的 web service类-（供甘肃发短信）
 * @author Dongrisheng
 *
 */
@Controller
@Path("/sms")
public class SendMessageRest {
	private Logger logger = Logger.getLogger(SendMessageRest.class);
	@Resource
	private ISendSMSMessageService sendSMSMessageService;

	/**
	 * 配置平台
	 */
	private final static Integer on = Integer.parseInt(PropertyUtil.getProperty("on"));

	@POST
	@Path("/sendmessage")
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	public ResultBean sendMessage(C2SMobile c2sMobile,@Context HttpServletRequest request) throws ISchoolException {
		String opration = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = " + Thread.currentThread().getId() + ".|发送手机短信";
		logger.debug(opration + "| 开始校验参数 | List<MobileMessage>=" + c2sMobile.toString());
		int  result=-10;
		try {
			result = sendSMSMessageService.toSendBatchMessageHB(c2sMobile);
			return new ResultBean(ResultBean.CODE_SUCCESS, 0, result, "");
		} catch (Exception e) {
			return new ResultBean(ResultBean.CODE_ERROR, ErrorCode.ISCHOOL_REQJSON_PARSEMSG_EXCEPTION, 1, "");
		}
		
	}
	
	static {
		if(on==0){
			// 启动获取状态报告进程, MsgConfig.getIsmgPort()
			CMPPMo3 cmppMo = new CMPPMo3();
			cmppMo.start();
		}else if(on==1){
			// 启动获取状态报告进程, MsgConfig.getIsmgPort()
			CMPPMo cmppMo = new CMPPMo();
			cmppMo.start();
		}

	}
	
	
	@GET
	@Path("/getqueuesize")
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	public ResultBean getQueueSize()
			throws ISchoolException {
		try {
			return new ResultBean(ResultBean.CODE_SUCCESS, 0,"Queue to be sent: "+SendMsgQueue.arrayPIR_Queue.size()+" number,"+CMPPMo.getSocket().getInputStream(), "");
		} catch (Exception e) {
		}
		return new ResultBean(ResultBean.CODE_ERROR,
				ErrorCode.ISCHOOL_REQJSON_PARSEMSG_EXCEPTION, 1, "");
	}
	
	@GET
	@Path("/getip")
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	public ResultBean getip(@Context HttpServletRequest request) throws ISchoolException {
		try {
			String clientIp = ClientIpUtil.getClientIP(request);
			return new ResultBean(ResultBean.CODE_SUCCESS, 0, clientIp, "");
		} catch (Exception e) {
			return new ResultBean(ResultBean.CODE_ERROR, ErrorCode.ISCHOOL_REQJSON_PARSEMSG_EXCEPTION, 1, "");
		}
		
	}
}
