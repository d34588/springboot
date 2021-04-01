package com.paascloud.provider.cmpp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 连接对象
 */
public class CmppConnect extends MessageHeader {
	private String sourceAddr;
	private byte[] authenticatorSource;
	private byte version;
	private int timestamp;

	public byte[] toByteArray() {
		ByteArrayOutputStream bous = new ByteArrayOutputStream();
		DataOutputStream dous = new DataOutputStream(bous);
		try {
			dous.writeInt(this.getTotalLength());
			dous.writeInt(this.getCommandId());
			dous.writeInt(this.getSequenceId());
			MsgUtils.writeString(dous, this.sourceAddr, 6);
			dous.write(authenticatorSource);
			dous.writeByte(version);
			dous.writeInt(timestamp);
			dous.close();
		} catch (IOException e) {
			System.out.print("封装链接二进制数组失败。");
		}
		return bous.toByteArray();
	}

	public String getSourceAddr() {
		return sourceAddr;
	}

	public void setSourceAddr(String sourceAddr) {
		this.sourceAddr = sourceAddr;
	}

	public byte[] getAuthenticatorSource() {
		return authenticatorSource;
	}

	public void setAuthenticatorSource(byte[] authenticatorSource) {
		this.authenticatorSource = authenticatorSource;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
}
