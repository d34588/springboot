package com.paascloud.provider.cmpp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 查询发送短信状态对象
 */
public class CmppQuery extends MessageHeader {
	private String time;
	private byte queryType = 1;
	private String queryCode;
	private String reserve = "";

	public byte[] toByteArray() {
		ByteArrayOutputStream bous = new ByteArrayOutputStream();
		DataOutputStream dous = new DataOutputStream(bous);
		try {
			dous.writeInt(this.getTotalLength());
			dous.writeInt(this.getCommandId());
			dous.writeInt(this.getSequenceId());
			MsgUtils.writeString(dous, this.time, 8);
			dous.writeByte(queryType);
			MsgUtils.writeString(dous, this.queryCode, 10);
			MsgUtils.writeString(dous, this.reserve, 8);
			dous.close();
		} catch (IOException e) {
			System.out.print("封装链接二进制数组失败。");
		}
		return bous.toByteArray();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public byte getQueryType() {
		return queryType;
	}

	public void setQueryType(byte queryType) {
		this.queryType = queryType;
	}

	public String getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

}
