package com.tsb.cmpp.model;

/**
 * 返回来的状态报告包体
 * @author David Yin
 * @version 1.0 (2013-11-21 13:17:21)
 */
public class CMPPReport {
    /**
     * 8位,信息标识,SP提交短信（CMPP_SUBMIT）操作时，与SP相连的ISMG产生的Msg_Id。
     */
    public byte[] msg_Id= new byte[8];
    
    //public long msgID;
    /**
     * 7位,发送短信的应答结果，含义与SMPP协议要求中stat字段定义相同，详见cmpp3.0协议文档。SP根据该字段确定CMPP_SUBMIT消息的处理状态。
     */
    public String stat;

    /**
     * 10位,短信发送时间,格式为YYMMDDHHMM（YY为年的后两位00-99，MM：01-12，DD：01-31，HH：00-23，MM：00-59）
     */
    public String submit_time;

    /**
     * 10位,短信处理时间,格式为YYMMDDHHMM（解释同上）
     */
    public String done_time;

    /**
     * 32位,目的终端MSISDN号码(SP发送CMPP_SUBMIT消息的目标终端)
     */
    public String dest_terminal_Id;

    /**
     * 4位,取自SMSC发送状态报告的消息体中的消息标识。
     *  
     */
    public int smsc_sequence;
}