package com.paascloud.provider.cmpp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 发送短信对象
 */
public class CmppSubmit extends MessageHeader {
	private long msgId;
	private byte pkTotal = 1;
	private byte pkNumber = 1;
	// 是否要求返回状态确认报告： 0：不需要； 1：需要。
	private byte registeredDelivery = 1;
	// 信息级别。
	private byte msgLevel = 0;
	private String serviceId;
	// 计费用户类型字段：0：对目的终端MSISDN计费；1：对源终端MSISDN计费； 2：对SP计费；3：表示本字段无效，对谁计费参见Fee_terminal_Id字段。
	private byte feeUserType = 3;
	// 被计费用户的号码，当Fee_UserType为3时该值有效，当Fee_UserType为0、1、2时该值无意义。
	private String feeTerminalId;
	// 被计费用户的号码类型，0：真实号码；1：伪码。
	private byte feeTerminalType = 0;
	private byte tp_pid = 0;
	private byte tp_udhi = 0;
	// 信息格式：0：ASCII串；3：短信写卡操作；4：二进制信息；8：UCS2编码；15：含GB汉字
	private byte msgFmt = 15;
	private String msgSrc;
	// 资费类别：01：对“计费用户号码”免费；02：对“计费用户号码”按条计信息费；03：对“计费用户号码”按包月收取信息费。
	private String feeType;
	private String feeCode;
	private String vaildTime = "";
	private String atTime = "";
	private String srcId;
	private byte destUsrTl = 1;
	private String destTerminalId;
	// 接收短信的用户的号码类型，0：真实号码；1：伪码。
	private byte destTerminalType = 0;
	private byte msgLength;
	private byte[] msgContent;
	private String linkId = "";
	
	int result = -1; // 默认无应答
	private int terminalIdLen = 32;
	private int linkIdLen = 20;
	private int submitExpMsgLen = Constants.CMPP3_SUBMIT_LEN_EXPMSGLEN;
	
	public byte[] toByteArray() {
		ByteArrayOutputStream bous = new ByteArrayOutputStream();
		DataOutputStream dous = new DataOutputStream(bous);
		try {
			// 请求头
			dous.writeInt(this.getTotalLength());
			dous.writeInt(this.getCommandId());
			dous.writeInt(this.getSequenceId());
			// 请求体
			dous.writeLong(msgId);
			dous.writeByte(pkTotal);
			dous.writeByte(pkNumber);
			dous.writeByte(registeredDelivery);
			dous.writeByte(msgLevel);
			MsgUtils.writeString(dous, serviceId, 10);
			dous.writeByte(feeUserType);
			MsgUtils.writeString(dous, feeTerminalId, terminalIdLen);
			dous.writeByte(feeTerminalType);
			dous.writeByte(tp_pid);
			dous.writeByte(tp_udhi);
			dous.writeByte(msgFmt);
			MsgUtils.writeString(dous, msgSrc, 6);
			MsgUtils.writeString(dous, feeType, 2);
			MsgUtils.writeString(dous, feeCode, 6);
			MsgUtils.writeString(dous, vaildTime, 17);
			MsgUtils.writeString(dous, atTime, 17);
			MsgUtils.writeString(dous, srcId, 21);
			dous.writeByte(destUsrTl);
			MsgUtils.writeString(dous, destTerminalId, terminalIdLen);
			// 接收短信的用户的号码类型，0：真实号码；1：伪码。
			dous.writeByte(destTerminalType);
			dous.writeByte(msgLength);
			// 短信内容
			dous.write(msgContent);
			MsgUtils.writeString(dous, linkId, linkIdLen);
			dous.close();
		} catch (IOException e) {
			System.out.print("封装链接二进制数组失败。");
		}
		return bous.toByteArray();
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public byte getPkTotal() {
		return pkTotal;
	}

	public void setPkTotal(byte pkTotal) {
		this.pkTotal = pkTotal;
	}

	public byte getPkNumber() {
		return pkNumber;
	}

	public void setPkNumber(byte pkNumber) {
		this.pkNumber = pkNumber;
	}

	public byte getRegisteredDelivery() {
		return registeredDelivery;
	}

	public void setRegisteredDelivery(byte registeredDelivery) {
		this.registeredDelivery = registeredDelivery;
	}

	public byte getMsgLevel() {
		return msgLevel;
	}

	public void setMsgLevel(byte msgLevel) {
		this.msgLevel = msgLevel;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public byte getFeeUserType() {
		return feeUserType;
	}

	public void setFeeUserType(byte feeUserType) {
		this.feeUserType = feeUserType;
	}

	public String getFeeTerminalId() {
		return feeTerminalId;
	}

	public void setFeeTerminalId(String feeTerminalId) {
		this.feeTerminalId = feeTerminalId;
	}

	public byte getFeeTerminalType() {
		return feeTerminalType;
	}

	public void setFeeTerminalType(byte feeTerminalType) {
		this.feeTerminalType = feeTerminalType;
	}

	public byte getTp_pid() {
		return tp_pid;
	}

	public void setTp_pid(byte tp_pid) {
		this.tp_pid = tp_pid;
	}

	public byte getTp_udhi() {
		return tp_udhi;
	}

	public void setTp_udhi(byte tp_udhi) {
		this.tp_udhi = tp_udhi;
	}

	public byte getMsgFmt() {
		return msgFmt;
	}

	public void setMsgFmt(byte msgFmt) {
		this.msgFmt = msgFmt;
	}

	public String getMsgSrc() {
		return msgSrc;
	}

	public void setMsgSrc(String msgSrc) {
		this.msgSrc = msgSrc;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getVaildTime() {
		return vaildTime;
	}

	public void setVaildTime(String vaildTime) {
		this.vaildTime = vaildTime;
	}

	public String getAtTime() {
		return atTime;
	}

	public void setAtTime(String atTime) {
		this.atTime = atTime;
	}

	public String getSrcId() {
		return srcId;
	}

	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	public byte getDestUsrTl() {
		return destUsrTl;
	}

	public void setDestUsrTl(byte destUsrTl) {
		this.destUsrTl = destUsrTl;
	}

	public String getDestTerminalId() {
		return destTerminalId;
	}

	public void setDestTerminalId(String destTerminalId) {
		this.destTerminalId = destTerminalId;
	}

	public byte getDestTerminalType() {
		return destTerminalType;
	}

	public void setDestTerminalType(byte destTerminalType) {
		this.destTerminalType = destTerminalType;
	}

	public byte getMsgLength() {
		return msgLength;
	}

	public void setMsgLength(byte msgLength) {
		this.msgLength = msgLength;
	}

	public byte[] getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(byte[] msgContent) {
		this.msgContent = msgContent;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getTerminalIdLen() {
		return terminalIdLen;
	}

	public void setTerminalIdLen(int terminalIdLen) {
		this.terminalIdLen = terminalIdLen;
	}

	public int getLinkIdLen() {
		return linkIdLen;
	}

	public void setLinkIdLen(int linkIdLen) {
		this.linkIdLen = linkIdLen;
	}

	public int getSubmitExpMsgLen() {
		return submitExpMsgLen;
	}

	public void setSubmitExpMsgLen(int submitExpMsgLen) {
		this.submitExpMsgLen = submitExpMsgLen;
	}

}
