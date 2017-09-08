package net.protocol;
public class Protocol {
	/*** 手机端协议开始字节 ***/
	public final static byte CLIENT_BEGIN_BYTE = 0x69;
	/*** 后台协议开始字节 ***/
	public final static byte JOB_BEGIN_BYTE = 0x70;
	
	/*** 远程控制协议开始字节 ***/
	public final static byte BEGIN_BYTE = 0x68;
	/*** 远程控制协议结束字节 ***/
	public final static byte END_BYTE = 0x16;
	/*** 远程控制协议设置系统时间 ***/
	public final static byte SET_TIME_BYTE = 0x01;
	/*** 远程控制协议获取系统时间 ***/
	public final static byte GET_TIME_BYTE = 0x02;
	/*** 远程控制协议创建灌溉计划 ***/
	public final static byte CREATE_PLAN_BYTE = 0x10;
	/*** 远程控制协议查询灌溉计划***/
	public final static byte SEARCH_PLAN_BYTE = 0x11;
	/*** 远程控制协议启动灌溉计划 ***/
	public final static byte START_PLAN_BYTE = 0x12;
	/*** 远程控制协议停止灌溉计划 ***/
	public final static byte END_PLAN_BYTE = 0x13;
	/*** 远程控制协议删除灌溉计划 ***/
	public final static byte DELETE_PLAN_BYTE = 0x14;
	/*** 远程控制协议操作外接设备 ***/
	public final static byte OPERATE_DEVICE_BYTE = 0x2d;
	

	/*** 通信协议设置系统时间 ***/
	public final static short SET_TIME = 21;
	/*** 通信协议获取系统时间 ***/
	public final static short GET_TIME = 22;
	/*** 通信协议创建灌溉计划 ***/
	public final static short CREATE_PLAN = 10;
	/*** 通信协议查询灌溉计划***/
	public final static short SEARCH_PLAN = 11;
	/*** 通信协议启动灌溉计划 ***/
	public final static short START_PLAN = 12;
	/*** 通信协议停止灌溉计划 ***/
	public final static short END_PLAN = 13;
	/*** 通信协议删除灌溉计划 ***/
	public final static short DELETE_PLAN = 14;
	/*** 通信协议协操作外接设备 ***/
	public final static short OPERATE_DEVICE = 24;
	/*** 通信协议信息上报 ***/
	public final static short REPORT_MSG = 23;
	/*** 通信协议 登入 ***/
	public final static short LOGININ = 40;
	/*** 通信协议 登出 ***/
	public final static short LOGINOUT = 41;
	/*** 通信协议 心跳 ***/
	public final static short HEART_BEAT = 50;
	
	
	public final static short ERROR = 80;
	
	/*** 版本***/
	public final static String VERSION = "001";

}
