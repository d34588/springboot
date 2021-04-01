package com.paascloud.provider.cmpp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 短信接口辅助工具类 
 */
public class MsgUtils {
	private static int sequenceId = 1;// 序列编号

	// 序列自增
	public synchronized static int getSequence() {
		++sequenceId;
		if (sequenceId > 255) {
			sequenceId = 0;
		}
		return sequenceId;
	}

	// 用于鉴别源地址，MD5
	public static byte[] getAuthenticatorSource(String spId, String secret, String timestamp) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] data = (spId + "\0\0\0\0\0\0\0\0\0" + secret + timestamp).getBytes();
			md5.update(data);
			return md5.digest();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("SP链接到ISMG拼接AuthenticatorSource失败：" + e.getMessage());
			return null;
		}
	}

	// 向流中写入指定长度的字符串，不足补零
	public static void writeString(DataOutputStream dous, String s, int len) {
		try {
			byte[] data = s.getBytes();
			if (data.length > len) {
				System.out.println("向流中写入的字符串超长！要写" + len + " 字符串是:" + s);
			}
			int srcLen = data.length;
			dous.write(data);
			while (srcLen < len) {
				dous.write('\0');
				srcLen++;
			}
		} catch (IOException e) {
			System.out.println("向流中写入指定字节长度的字符串失败：" + e.getMessage());
		}
	}

	// 从流中读取字符串
	public static String readString(java.io.DataInputStream ins, int len) {
		byte[] b = new byte[len];
		try {
			ins.read(b);
			String s = new String(b);
			s = s.trim();
			return s;
		} catch (IOException e) {
			return "";
		}
	}

	// 截取字段
	public static byte[] getMsgBytes(byte[] msg, int start, int end) {
		byte[] msgByte = new byte[end - start];
		int j = 0;
		for (int i = start; i < end; i++) {
			msgByte[j] = msg[i];
			j++;
		}
		return msgByte;
	}

	// 时间戳明文
	public static String getTimestamp() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmmss");
		String time = formatter.format(new Date());
		return time;
	}
	
	// 时间戳明文
	public static String getStamp() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String time = formatter.format(new Date());
		return time;
	}

	/**
	 * UCS2解码      
	 * 
	 * @param src UCS2 源串  
	 * @return 解码后的UTF-16BE字符串  
	 */
	public static String DecodeUCS2(String src) {
		byte[] bytes = new byte[src.length() / 2];
		for (int i = 0; i < src.length(); i += 2) {
			bytes[i / 2] = (byte) (Integer.parseInt(src.substring(i, i + 2), 16));
		}
		String reValue = "";
		try {
			reValue = new String(bytes, "UTF-16BE");
		} catch (UnsupportedEncodingException e) {
			reValue = "";
		}
		return reValue;
	}

	/**
	 * UCS2编码      
	 * 
	 * @param src UTF-16BE编码的源串  
	 * @return 编码后的UCS2串  
	 */
	public static String EncodeUCS2(String src) {
		byte[] bytes;
		try {
			bytes = src.getBytes("UTF-16BE");
		} catch (UnsupportedEncodingException e) {
			bytes = new byte[0];
		}
		StringBuffer reValue = new StringBuffer();
		StringBuffer tem = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			tem.delete(0, tem.length());
			tem.append(Integer.toHexString(bytes[i] & 0xFF));
			if (tem.length() == 1) {
				tem.insert(0, '0');
			}
			reValue.append(tem);
		}
		return reValue.toString().toUpperCase();
	}

	// 获取高四位
	public static int getHeight4(byte data) {
		int height;
		height = ((data & 0xf0) >> 4);
		return height;
	}

	// 获取低四位
	public static int getLow4(byte data) {
		int low;
		low = (data & 0x0f);
		return low;
	}

}
