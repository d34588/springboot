package com.paascloud.provider.cmpp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * 查询发送短信状态响应对象
 */
public class CmppQueryResp extends MessageHeader {
	private byte[] time;
	private byte queryType;
	private byte[] queryCode;
    private int MT_TLMsg;
    private int MT_Tlusr;
    private int MT_Scs;
    private int MT_WT;
    private int MT_FL;
    private int MO_Scs;
    private int MO_WT;
    private int MO_FL;

    public CmppQueryResp(byte[] data) {
    	// Command_Id+Sequence_Id=8、
        if (data.length == 8 + 51) {
            ByteArrayInputStream bins = new ByteArrayInputStream(data);
            DataInputStream dins = new DataInputStream(bins);
            try {
                this.setTotalLength(data.length + 4);
                this.setCommandId(dins.readInt());
                this.setSequenceId(dins.readInt());
                byte[] timeByte = new byte[8];
                dins.read(timeByte);
                this.time = timeByte;
                
                this.setQueryType(dins.readByte());
                
                byte[] CodeByte = new byte[10];
                dins.read(CodeByte);
                this.queryCode = CodeByte;
                
                this.setMT_TLMsg(dins.readInt());
                this.setMT_Tlusr(dins.readInt());
                this.setMT_Scs(dins.readInt());
                this.setMT_WT(dins.readInt());
                this.setMT_FL(dins.readInt());
                this.setMO_Scs(dins.readInt());
                this.setMO_WT(dins.readInt());
                this.setMO_FL(dins.readInt());
                
                dins.close();
                bins.close();
            } catch (IOException e) {
            }
        } else {
            System.out.println("查询发送短信状态,解析数据包出错，包长度不一致。长度为:" + data.length);
        }
    }

	public byte[] getTime() {
		return time;
	}

	public void setTime(byte[] time) {
		this.time = time;
	}

	public byte getQueryType() {
		return queryType;
	}

	public void setQueryType(byte queryType) {
		this.queryType = queryType;
	}

	public byte[] getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(byte[] queryCode) {
		this.queryCode = queryCode;
	}

	public int getMT_TLMsg() {
		return MT_TLMsg;
	}

	public void setMT_TLMsg(int mT_TLMsg) {
		MT_TLMsg = mT_TLMsg;
	}

	public int getMT_Tlusr() {
		return MT_Tlusr;
	}

	public void setMT_Tlusr(int mT_Tlusr) {
		MT_Tlusr = mT_Tlusr;
	}

	public int getMT_Scs() {
		return MT_Scs;
	}

	public void setMT_Scs(int mT_Scs) {
		MT_Scs = mT_Scs;
	}

	public int getMT_WT() {
		return MT_WT;
	}

	public void setMT_WT(int mT_WT) {
		MT_WT = mT_WT;
	}

	public int getMT_FL() {
		return MT_FL;
	}

	public void setMT_FL(int mT_FL) {
		MT_FL = mT_FL;
	}

	public int getMO_Scs() {
		return MO_Scs;
	}

	public void setMO_Scs(int mO_Scs) {
		MO_Scs = mO_Scs;
	}

	public int getMO_WT() {
		return MO_WT;
	}

	public void setMO_WT(int mO_WT) {
		MO_WT = mO_WT;
	}

	public int getMO_FL() {
		return MO_FL;
	}

	public void setMO_FL(int mO_FL) {
		MO_FL = mO_FL;
	}

	@Override
	public String toString() {
		return "Message_Header [totalLength=" + this.getTotalLength() + ", commandId=" + this.getCommandId() + ", sequenceId=" + this.getSequenceId() + "] " 
				+ "CmppQueryResp [time=" + new String(time) + ", queryType=" + queryType + ", queryCode="
				+ new String(queryCode) + ", MT_TLMsg=" + MT_TLMsg + ", MT_Tlusr=" + MT_Tlusr + ", MT_Scs="
				+ MT_Scs + ", MT_WT=" + MT_WT + ", MT_FL=" + MT_FL + ", MO_Scs=" + MO_Scs + ", MO_WT=" + MO_WT
				+ ", MO_FL=" + MO_FL + "]";
	}
    
}