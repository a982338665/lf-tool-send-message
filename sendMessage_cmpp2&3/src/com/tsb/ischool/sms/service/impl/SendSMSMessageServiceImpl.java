package com.tsb.ischool.sms.service.impl;


import com.tsb.ischool.framework.common.ISchoolConstants;
import com.tsb.ischool.msglog.dao.ISendMsgServiceDao;
import com.tsb.ischool.msglog.model.SendMsgServiceBean;
import com.tsb.ischool.sms.model.C2SMobile;
import com.tsb.ischool.sms.model.MobileMessage;
import com.tsb.ischool.sms.service.ISendSMSMessageService;
import com.tsb.ischool.utils.UuidUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 类 编 号： <br>
 * 类 名 称：SendSMSMessageServiceImpl<br>
 * <b>内容摘要：发送手机短消息的实现类</b><br>
 * 完成日期：<br>
 * 编码作者：Dongrisheng<br>
 */
@Service("sendSMSMessageService")
public class SendSMSMessageServiceImpl implements ISendSMSMessageService {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Resource
    private ISendMsgServiceDao sendMsgServiceDao;

    @Override
    public int toSendBatchMessage(C2SMobile c2sMobile) {

        String xkSendUserId = c2sMobile.getXkSendUserId().toString();
        SendMsgServiceBean sendRequestParam = sendMsgServiceDao.getUserTokenByXkId(xkSendUserId);
        if (sendRequestParam == null) {
            logger.debug("没有token，暂时无法发短信,此处仅记录第一条list>>>" + c2sMobile.toString());
            MobileMessage mm = c2sMobile.getList().get(0);
            if (c2sMobile.getList().get(0).getReceiveId() != null) {
                mm.setReceiveId(c2sMobile.getList().get(0).getReceiveId());
            }
            mm.setAccessToken(null);
            mm.setOpenId(null);
            mm.setId(UuidUtil.generateUUID());
            mm.setType("1");
            SendMsgQueue.getInstance().pushFail(mm);
            return 0;
        }

        String accessToken = sendRequestParam.getAccess_token();
        String openId = sendRequestParam.getOpen_id();
        logger.debug("获取token/openID:--->accessToken:" + accessToken + "|--->openId:" + openId);
        int iSendOk = 0;
        String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
                + Thread.currentThread().getId() + ".|发送手机短信.|list="
                + c2sMobile.toString() + ".|";
        logger.debug(operation + "开始.|");
        c2sMobile.verify();//验证传入的手机短信列表是否为空
        boolean result = false;
        String lcid = sendMsgServiceDao.getLcIdByXkIdStu(c2sMobile.getSid());
        MobileMessage mm = c2sMobile.getList().get(0);
        if (lcid == null) {
            mm.setAccessToken(null);
            mm.setOpenId(null);
            mm.setId(UuidUtil.generateUUID());
            mm.setType("2");
            mm.setMessageContent(c2sMobile.getSid() + "|" + mm.getMessageContent());
            SendMsgQueue.getInstance().pushFail(mm);
            return 0;
        }

        mm.setReceiveId(lcid);
        mm.setAccessToken(accessToken);
        mm.setOpenId(openId);
        mm.setId(UuidUtil.generateUUID());
        mm.setType("1");
        result = SendMsgQueue.getInstance().pushDetry(mm);
        if (result) {
            iSendOk++;
        }


      /*   for(int i=0;i<c2sMobile.getList().size();i++){
             MobileMessage mm = c2sMobile.getList().get(i);
        	 //根据家长学酷id获取孩子联创id
        	 List<Map<String,Object>> listMap = sendMsgServiceDao.getLcIdByXkId(c2sMobile.getSid());//目前集合中始終包含一個對象，直接獲取學生ID即可
        	 String stuId = "";
        	 if(listMap!=null){
				 for(Map<String ,Object> map : listMap) {
					 stuId += map.get("lc_id")+",";
				 }
				 if(!"".equals(stuId.trim())){
					 System.out.println("stuId:"+stuId);
					 mm.setReceiveId(stuId);

					 mm.setAccessToken(accessToken);
					 mm.setOpenId(openId);
					 mm.setId(UuidUtil.generateUUID());
					 mm.setType("1");
					 result = SendMsgQueue.getInstance().pushDetry(mm);
					 if (result){
						 iSendOk ++;
					 }
				 }
			 }else{
				 logger.debug("接收短信的用户："+c2sMobile.getSid()+"|没有对应的孩子或联创id================》");
			 }

         }*/

        logger.debug(operation + "开始.|");
        return iSendOk;


    }

    @Override
    public int toSendBatchMessageHB(C2SMobile c2sMobile) {
        int iSendOk = 0;
        String operation = ISchoolConstants.LOGGER_PREFIX_DEBUG + "THREADID = "
                + Thread.currentThread().getId() + ".|发送手机短信.|list="
                + c2sMobile.toString() + ".|";
        logger.debug(operation + "开始.|");
        c2sMobile.verify();//验证传入的手机短信列表是否为空
        boolean result = false;
        for (int i = 0; i < c2sMobile.getList().size(); i++) {
            MobileMessage mm = c2sMobile.getList().get(i);
            mm.setId(UuidUtil.generateUUID());
            mm.setType("1");
            result = SendMsgQueue.getInstance().pushDetry(mm);
            if (result) {
                iSendOk++;
            }
        }
        logger.debug(operation + "开始.|");
        return iSendOk;

    }


}



