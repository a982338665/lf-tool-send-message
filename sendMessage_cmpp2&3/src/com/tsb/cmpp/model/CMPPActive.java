package com.tsb.cmpp.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsb.cmpp.Common;

/**
 * 链路检测包,没有包体
 * 
 * @author David Yin
 * @version 1.0 (2013-11-21 14:38:35)
 */
public class CMPPActive extends CMPPResponsePacket implements CMPPRequestBody {
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(CMPPActive.class);

    public byte[] getRequestBody() {
        return new byte[0];
    }

    /**
     * 解析从输入流得到的包体字节流
     */
    public void parseResponseBody(byte[] packet) {

        byte[] length = new byte[4];
        System.arraycopy(packet, 0, length, 0, 4);
        this.totalLength = Common.bytes4ToInt(length);
        if (log.isDebugEnabled()) {
            log.debug("返回包长度解析后为:" + totalLength);
        }

        byte[] commandid = new byte[4];
        System.arraycopy(packet, 4, commandid, 0, 4);
        this.commandID = Common.bytes4ToInt(commandid);
        if (log.isDebugEnabled()) {
            log.debug("返回包命令字解析后=" + commandID + "，实际=" + CommandID.CMPP_ACTIVE_TEST);
        }

        byte[] seqid = new byte[4];
        System.arraycopy(packet, 8, seqid, 0, 4);
        this.sequenceID = Common.bytes4ToInt(seqid);
        if (log.isDebugEnabled()) {
            log.debug("返回包序列号解析后为:" + sequenceID);
        }
        log.info("CMPPActive消息解析成功,sequenceID=" + sequenceID);
    }
}