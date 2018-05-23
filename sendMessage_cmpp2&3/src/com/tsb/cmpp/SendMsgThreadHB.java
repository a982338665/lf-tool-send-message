package com.tsb.cmpp;

import com.tsb.cmpp2.msg.util.MsgContainerNew;
import com.tsb.ischool.msglog.model.SendMsgLog;
import com.tsb.ischool.msglog.service.impl.SendMsgSuccessQueue;
import com.tsb.ischool.sms.model.MobileMessage;
import com.tsb.ischool.sms.service.impl.SendMsgQueue;
import com.tsb.ischool.tmpmsg.model.tmpTubasicMsg;

/**
 * create by lishengbo on 2018-03-23 17:34
 */
public class SendMsgThreadHB extends Thread{


    public  boolean SendMsgThreadIswich=false;
    private CMPPSocket msgSocket;

    private String mobile;
    private String msg;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SendMsgThreadHB(CMPPSocket msgSocket){
        this.msgSocket=msgSocket;
    }

    @Override
    public void run() {
        while(SendMsgThreadIswich){
            //从队列中取出待发送短信数据=======================================================
            MobileMessage bean = null;
            try {
                //高优先级队列校验
                if (verifyQueueFirst()) {
                    bean = SendMsgQueue.arrayPIR_Queue.take();
                }
                //低优先级队列校验
                if(bean==null){//arrayPIR_Queue队列为空时，发送arrayPIR_Queue_Slow_sending队列
                    if (verifyQueueSecond()) {
                        bean = SendMsgQueue.arrayPIR_Queue_Slow_sending.take();
                    }
                }
            } catch (Exception e1) {
                bean = null;
            }
            //从队列中取出待发送短信数据=======================================================
            if (bean != null) {
                MsgContainerNew instance = MsgContainerNew.getInstance();
                instance.setMsgSocket(msgSocket);
                //开始发送短信
                boolean result = instance.sendMsg(bean.getSequenceId(),bean.getMessageContent(),bean.getMobileNo());
                try {
                    Thread.sleep(55);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //如果发送失败并且失败次数小于3
                //bean.getId()=1 则向低优先及推送消息
                //否则：高优先级推送
                //失败3次，不在往队列放
                // TODO 失败3次以上的数据未存储
                if(!result&&bean.getSendFailCount()<3){
                    bean.setType("2");//设置成2的话，后面就不再插入send_msg_log表
                    bean.setSendFailCount(bean.getSendFailCount()+1);
                    if(bean.getId()!=null&&"1".equals(bean.getId())){
                        SendMsgQueue.getInstance().pushPrioritySending(bean);
                    }else{
                        SendMsgQueue.getInstance().pushDetry(bean);
                    }
                }
                //如果发送成功
                if(result){
                    //如果是发送tmp_tubasic_msg表中的数据，并且发送成功的话
                    if(bean!=null&&bean.getId()!=null&&"1".equals(bean.getId())){
                        try {
                            success_push_tmp(bean);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //如果发送的是正常的队列数据，且成功则如下
                    if(bean!=null&&bean.getId()!=null&&!"1".equals(bean.getId())){
                        success_push_nomal(bean);
                    }
                }
            }else{
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void success_push_nomal(MobileMessage bean) {
        SendMsgLog response = new SendMsgLog();
        response.setId(bean.getId());
        response.setSendStatus(1);
        SendMsgSuccessQueue.getInstance().pushDetry(response);
    }

    private void success_push_tmp(MobileMessage bean) {
        tmpTubasicMsg ttm = new tmpTubasicMsg();
        ttm.setMobile(bean.getMobileNo());
        ttm.setSendStatus(1);
        SendMsgSuccessQueue.getInstance().pushDetry_tmp(ttm);
    }

    private boolean verifyQueueSecond() {
        return SendMsgQueue.arrayPIR_Queue_Slow_sending != null
                && !SendMsgQueue.arrayPIR_Queue_Slow_sending.isEmpty()
                &&msgSocket!=null&&msgSocket.getInputStream()!=null
                &&msgSocket.getOutStream()!=null;
    }

    private boolean verifyQueueFirst() {
        return SendMsgQueue.arrayPIR_Queue != null
                && !SendMsgQueue.arrayPIR_Queue.isEmpty()
                &&msgSocket!=null&&msgSocket.getInputStream()!=null
                &&msgSocket.getOutStream()!=null;
    }

}
