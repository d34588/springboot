package com.paascloud.provider.cmpp;

/**
 * Cmpp3.0协议所需常量及程序所需常量
 */
public class Constants {
	/** 消息头长度 12*/
	public static final int MESSAGE_HEADER_LEN = 12;
	
	/** 消息体长度183 */
	public static final int CMPP3_SUBMIT_LEN_EXPMSGLEN = 8 + 1 + 1 + 1 + 1 + 10 + 1 + 32 + 1 + 1
			+ 1 + 1 + 6 + 2 + 6 + 17 + 17 + 21 + 1 + 32 + 1 + 1 + 20;

}
