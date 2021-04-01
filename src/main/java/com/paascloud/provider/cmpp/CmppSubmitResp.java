package com.paascloud.provider.cmpp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * 发送短信响应对象
 */
public class CmppSubmitResp extends MessageHeader {
	// 信息标识
    private long msgId;
    // 结果
    private int result;

    public CmppSubmitResp(byte[] data) {
    	// Command_Id+Sequence_Id=8、msgId=8、result=4
        if (data.length == 8 + 8 + 4) {
            ByteArrayInputStream bins = new ByteArrayInputStream(data);
            DataInputStream dins = new DataInputStream(bins);
            try {
                this.setTotalLength(data.length + 4);
                this.setCommandId(dins.readInt());
                this.setSequenceId(dins.readInt());
                this.msgId = dins.readLong();
                this.result = dins.readInt();
                dins.close();
                bins.close();
            } catch (IOException e) {
            }
        } else {
            System.out.println("发送短信至IMSP,解析数据包出错，包长度不一致。长度为:" + data.length);
        }
    }

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Message_Header [totalLength=" + this.getTotalLength() + ", commandId=" + this.getCommandId() + ", sequenceId=" + this.getSequenceId() + "] " 
				+ "CmppSubmitResp [msgId=" + msgId + ", result=" + result + "]";
	}
    
}