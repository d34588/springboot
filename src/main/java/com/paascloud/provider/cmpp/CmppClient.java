package com.paascloud.provider.cmpp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmppClient {
	public static Socket socket;
	// 183.230.55.1 183.230.96.94
	public static String IP = "183.230.96.94";
	public static int port = 17890;
	/** 业务网关用户名  */
	public static String spId = "195038";
	/** 业务网关密码  */
	public static String secret = "195038";
	/** 短信接入码  */
	public static String srcId = "1064899195038";
	public static DataInputStream in;
	public static DataOutputStream out;

	public static void main(String[] args) throws IOException {
		Socket socket = getSocketInstance();
		// 建立连接
		connectISMG();
		// 发送短信
		sendShortMsg("IP 27.aika168.com 8185", "1440801424815");
		// 查询
//		query(MsgUtils.getStamp(), (byte) 0x0, "MZS0000007");
		// 关闭连接
		socket.close();
	}

	// 创建指定地址的Socket连接
	public static Socket getSocketInstance() {
		try {
			socket = new Socket(IP, port);
			socket.setKeepAlive(true);
			socket.setSoTimeout(30000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return socket;
	}

	/**
	 * SP请求连接到ISMG（CMPP_CONNECT）操作
	 * 
	 * @throws IOException
	 */
	public static void connectISMG() throws IOException {
		System.out.println("正在连接到服务器：" + IP);

		// 连接对象
		CmppConnect connect = new CmppConnect();
		// 消息总长度，4字节
		connect.setTotalLength(Constants.MESSAGE_HEADER_LEN + 6 + 16 + 1 + 4);
		// 命令或响应类型，4字节
		connect.setCommandId(0x00000001);
		// 消息流水号，4字节
		connect.setSequenceId(MsgUtils.getSequence());
		// SP的企业代码，6字节
		connect.setSourceAddr(spId);
		// 鉴别源地址，16字节
		String timestamp = MsgUtils.getTimestamp();
		connect.setAuthenticatorSource(MsgUtils.getAuthenticatorSource(spId, secret, timestamp));
		// 版本号，1字节
		connect.setVersion((byte) 0x48);
		// 时间戳，4字节
		connect.setTimestamp(Integer.parseInt(timestamp));

		// 转为字节数组
		List<byte[]> dataList = new ArrayList<byte[]>();
		dataList.add(connect.toByteArray());
		out = new DataOutputStream(socket.getOutputStream());
		if (out != null && null != dataList) {
			for (byte[] data : dataList) {
				out.write(data);
				out.flush();
				System.out.println("connect数据发送完成, " + Arrays.toString(data));
			}
		}

		in = new DataInputStream(socket.getInputStream());
		int len = in.readInt();
		System.out.println("resp len: " + len);

		List<byte[]> getData = new ArrayList<byte[]>();
		if (null != in && 0 != len) {
			byte[] data = new byte[len - 4];
			in.read(data);
			getData.add(data);
			for (byte[] returnData : getData) {
				System.out.println("connect返回字节数组：" + Arrays.toString(returnData));
				MessageHeader header = new MessageHeader(returnData);
				System.out.println("connect返回消息头：" + header.toString());
				switch (header.getCommandId()) {
				case 0x80000001:
					CmppConnectResp connectResp = new CmppConnectResp(returnData);
					System.out.println("CMPP初始化链接返回值：" + connectResp.toString());
				}
			}
		}
		System.out.println("");
	}

	/**
	 * SP请求ISMG（CMPP_SUBMIT）操作，发送短信
	 * 
	 * @throws IOException
	 */
	public static void sendShortMsg(String msg, String phoneNumber) throws IOException {
		try {
			byte[] msgByte = msg.getBytes("gb2312");
			System.out.println("发送短信信息：" + msg + " 长度：" + msgByte.length);

			CmppSubmit submit = new CmppSubmit();
			// 请求头
			submit.setTotalLength(Constants.MESSAGE_HEADER_LEN + Constants.CMPP3_SUBMIT_LEN_EXPMSGLEN + msgByte.length);
			submit.setCommandId(0x00000004);
			submit.setSequenceId(MsgUtils.getSequence());
			// 请求体（注释的表示使用默认值）
			submit.setMsgId(10000000);
			// submit.setPkTotal();
			// submit.setPkNumber();
			// submit.setRegisteredDelivery();
			// submit.setMsgLevel();
			submit.setServiceId("MZS0000007");
			// ?计费用户类型字段：0：对目的终端MSISDN计费；1：对源终端MSISDN计费；2：对SP计费；3：表示本字段无效，对谁计费参见Fee_terminal_Id字段。
			submit.setFeeUserType((byte) 3);
			// ?被计费用户的号码，当Fee_UserType为3时该值有效，当Fee_UserType为0、1、2时该值无意义。
			submit.setFeeTerminalId(phoneNumber);
			// submit.setFeeTerminalType();
			// submit.setTp_pid();
			// submit.setTp_udhi();
			// ?信息格式：0：ASCII串；3：短信写卡操作；4：二进制信息；8：UCS2编码；15：含GB汉字
			submit.setMsgFmt((byte) 0);
			submit.setMsgSrc(spId);
			submit.setFeeType("01");
			submit.setFeeCode("000000");
			// submit.setVaildTime();
			// submit.setAtTime();
			submit.setSrcId(srcId);
			// submit.setDestUsrTl();
			submit.setDestTerminalId(phoneNumber);
			// submit.setDestTerminalType();
			submit.setMsgLength((byte) msgByte.length);
			submit.setMsgContent(msgByte);
			// submit.setLinkId("");

			// 发送数据
			List<byte[]> dataList = new ArrayList<byte[]>();
			dataList.add(submit.toByteArray());
			if (out != null && null != dataList) {
				for (byte[] data : dataList) {
					System.out.println("submit数据长度：" + data.length);
					out.write(data);
					out.flush();
					System.out.println("submit数据发送完成, " + Arrays.toString(data));
				}
			}

			in = new DataInputStream(socket.getInputStream());
			int len = in.readInt();
			System.out.println("resp len: " + len);

			List<byte[]> getData = new ArrayList<byte[]>();
			if (null != in && 0 != len) {
				byte[] data = new byte[len - 4];
				in.read(data);
				getData.add(data);
				for (byte[] returnData : getData) {
					System.out.println("submit返回字节数组：" + Arrays.toString(returnData));
					MessageHeader header = new MessageHeader(returnData);
					System.out.println("submit返回消息头：" + header.toString());
					switch (header.getCommandId()) {
					case 0x80000004:
						CmppSubmitResp submitResp = new CmppSubmitResp(returnData);
						System.out.println("submitresp状态值：" + submitResp.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("");
	}

	/**
	 * SP请求ISMG（CMPP_QUERY）操作，查询某时间的业务统计情况
	 * 
	 * @throws IOException
	 */
	public static void query(String time, byte queryType, String queryCode) throws IOException {
		try {
			CmppQuery query = new CmppQuery();
			// 请求头
			query.setTotalLength(Constants.MESSAGE_HEADER_LEN + 27);
			query.setCommandId(0x00000006);
			query.setSequenceId(MsgUtils.getSequence());
			query.setTime(time);
			query.setQueryType(queryType);
			query.setQueryCode(queryCode);

			// 发送数据
			List<byte[]> dataList = new ArrayList<byte[]>();
			dataList.add(query.toByteArray());
			if (out != null && null != dataList) {
				for (byte[] data : dataList) {
					System.out.println("query数据长度：" + data.length);
					out.write(data);
					out.flush();
					System.out.println("query数据发送完成, " + Arrays.toString(data));
				}
			}

			in = new DataInputStream(socket.getInputStream());
			int len = in.readInt();
			System.out.println("resp len: " + len);

			List<byte[]> getData = new ArrayList<byte[]>();
			if (null != in && 0 != len) {
				byte[] data = new byte[len - 4];
				in.read(data);
				getData.add(data);
				for (byte[] returnData : getData) {
					System.out.println("query返回字节数组：" + Arrays.toString(returnData));
					MessageHeader header = new MessageHeader(returnData);
					System.out.println("query返回消息头：" + header.toString());
					switch (header.getCommandId()) {
					case 0x80000006:
						CmppQueryResp submitResp = new CmppQueryResp(returnData);
						System.out.println("query返回：" + submitResp.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
