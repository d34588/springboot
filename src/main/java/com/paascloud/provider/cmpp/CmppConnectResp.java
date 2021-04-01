package com.paascloud.provider.cmpp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * 连接响应对象
 */
public class CmppConnectResp extends MessageHeader {
	// 响应状态状态 0：正确 1：消息结构错 2：非法源地址 3：认证错 4：版本太高 5~ ：其他错误
    private int status;
    // 响应状态状态 0：正确 1：消息结构错 2：非法源地址 3：认证错 4：版本太高 5~ ：其他错误
    private String statusStr;
    // ISMG认证码，用于鉴别ISMG。 其值通过单向MD5 hash计算得出，
    // 表示如下： AuthenticatorISMG =MD5（Status+AuthenticatorSource+shared secret），
    // Shared secret 由中国移动与源地址实体事先商定，AuthenticatorSource为源地址实体发送给ISMG的对应消息CMPP_Connect中的值。 认证出错时，此项为空。
    private byte[] authenticatorISMG;
    // 服务器支持的最高版本号，对于3.0的版本，高4bit为3，低4位为0
    private byte version;

    public CmppConnectResp(byte[] data) {
    	// Command_Id+Sequence_Id=8、Status=4、AuthenticatorISMG=16、Version=1
        if (data.length == 8 + 4 + 16 + 1) {
            ByteArrayInputStream bins = new ByteArrayInputStream(data);
            DataInputStream dins = new DataInputStream(bins);
            try {
                this.setTotalLength(data.length + 4);
                this.setCommandId(dins.readInt());
                this.setSequenceId(dins.readInt());
                this.setStatus(dins.readInt());
                byte[] aiByte = new byte[16];
                dins.read(aiByte);
                this.authenticatorISMG = aiByte;
                this.version = dins.readByte();
                dins.close();
                bins.close();
            } catch (IOException e) {
            }
        } else {
            System.out.println("链接至IMSP,解析数据包出错，包长度不一致。长度为:" + data.length);
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        switch (status) {
            case 0:
                statusStr = "正确";
                break;
            case 1:
                statusStr = "消息结构错";
                break;
            case 2:
                statusStr = "非法源地址";
                break;
            case 3:
                statusStr = "认证错";
                break;
            case 4:
                statusStr = "版本太高";
                break;
            case 5:
                statusStr = "其他错误";
                break;
            default:
                statusStr = status + ":未知";
                break;
        }
    }

	@Override
	public String toString() {
		return "Message_Header [totalLength=" + this.getTotalLength() + ", commandId=" + this.getCommandId() + ", sequenceId=" + this.getSequenceId() + "] " 
				+ "CMPP_CONNECT_RESP [status=" + status + ", statusStr=" + statusStr + ", authenticatorISMG="
				+ Arrays.toString(authenticatorISMG) + ", version=" + version + "]";
	}
    
}