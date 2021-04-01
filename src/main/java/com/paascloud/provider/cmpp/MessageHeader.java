package com.paascloud.provider.cmpp;

import java.io.*;

public class MessageHeader {
	// Unsigned Integer 
    private int totalLength;
    // Unsigned Integer 
    private int commandId;
    // Unsigned Integer 
    private int sequenceId;

    public MessageHeader(byte[] data) {
        ByteArrayInputStream bins = new ByteArrayInputStream(data);
        DataInputStream dins = new DataInputStream(bins);
        try {
            this.setTotalLength(data.length + 4);
            this.setCommandId(dins.readInt());
            this.setSequenceId(dins.readInt());
            dins.close();
            bins.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

    public MessageHeader() {
        super();
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        DataOutputStream dous = new DataOutputStream(bous);

        try {
            dous.writeInt(this.getTotalLength());
            dous.writeInt(this.getCommandId());
            dous.writeInt(this.getSequenceId());
            dous.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("封装CMPP消息头二进制数组失败。");
        }

        return bous.toByteArray();
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

	@Override
	public String toString() {
		return "Message_Header [totalLength=" + totalLength + ", commandId=" + commandId + ", sequenceId=" + sequenceId + "]";
	}
    
}
