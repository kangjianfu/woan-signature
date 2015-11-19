package com.woaner.security.utils;

public class Approve {
	/** 
	    * 将两个ASCII字符合成一个字节； 
	    * 如："EF"--> 0xEF 
	    * @param src0 byte 
	    * @param src1 byte 
	    * @return byte 
	    */ 
	    public static byte uniteBytes(byte src0, byte src1) { 
	        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue(); 
	        _b0 = (byte)(_b0 << 4); 
	        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue(); 
	        byte ret = (byte)(_b0 ^ _b1); 
	        return ret; 
	    } 

	    /** 
	    * 将指定字符串src，以每两个字符分割转换为16进制形式 
	    * 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9} 
	    * @param src String 
	    * @return byte[] 
	    */ 
	    public static byte[] HexString2Bytes(String src){ 
	        byte[] ret = new byte[src.length()/2]; 
	        byte[] tmp = src.getBytes(); 
	        for(int i=0; i<src.length()/2; i++){ 
	            ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]); 
	        } 
	        return ret; 
	    }
	    public static String  getResponse(String password){
	    	  byte[] newpwd = Approve.HexString2Bytes(password);
	  	    	byte[] newchallenge = Approve.HexString2Bytes("");
	  	    		//将密码和挑战串转成16进制表示的字节数据
	  	    		byte[] result =new byte[newpwd.length+newchallenge.length];
	  	    		System.arraycopy(newpwd, 0, result, 0, newpwd.length);
	  	    		System.arraycopy(newchallenge, 0, result, newpwd.length, newchallenge.length);
	  	    		//合并数据
	  	    		/*String response = MdApprove.byte2MD5(result);*/
	  	    		//md5加密计算response
	  	    		return "";
	    }
	  
}
