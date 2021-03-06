package com.woaner.security.utils;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * 
 * @author kangjianfu 本类是一个获取“signature” 的工具类 。 是专门针对访问 aws API 获取的签名用的。 对于不同密钥，获取的签名也不同
 */
public class SignatureUtils {
	/**
	 * 中国时间
	 */
	static final String TIME_STAMP_PARAM_ZG="GMT+8";
	/**
	 * 国际时间
	 */
	static final String TIME_STAMP_PARAM_GJ="GMT";
	
	
	
	/**
	 * 此方法为获取tasklist 的接口的签名方法 get方法
	 * 这个签名的时间戳为时间标准时间 =
	 * 
	 * @param secret_key
	 *            : 用户公钥
	 * @param data
	 *            : 需要访问的api的uri 例如 /api/20140928/task_list
	 * @param service_code
	 *            : 服务码
	 * @return 签名信息
	 * @throws Exception
	 */
	public static String getSignature(String secret_key, String uri, String service_code, String query_data,String timestamp) throws Exception {
		// 判断输入参数都不能为空
		if (StringUtils.verify_not_empty(secret_key) && StringUtils.verify_not_empty(uri) && StringUtils.verify_not_empty(service_code)) {
			// 获取时间戳
			//String timestamp = SignatureUtils.getTimeStamp();
			//String timestamp ="2015-01-28T15:45:55";
			StringBuffer dataBuffer = new StringBuffer();
			dataBuffer.append(uri);
			dataBuffer.append("service_code=" + service_code);
			//dataBuffer.append("service_code=TESTING");
			if (StringUtils.verify_not_empty(query_data)) {
				System.err.println("query_data+++++++++++++"+query_data);
				dataBuffer.append(query_data);
			}
			dataBuffer.append(timestamp);
			//secret_key="abcde";
			System.err.println("请求参数" + dataBuffer.toString());
			byte[] data = dataBuffer.toString().getBytes("utf-8");
			byte[] key = secret_key.getBytes();
			// 还原密钥
			SecretKey secretKey = new SecretKeySpec(key, "HmacSHA256");
			// 实例化Mac
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			// 初始化mac
			mac.init(secretKey);
			// 执行消息摘要
			byte[] digest = mac.doFinal(data);
			
			byte[] hexB = new Hex().encode(digest);
			String checksum = new String(hexB);
			return checksum;// 转为十六进制的字符串
		} else {
			throw new NullPointerException("参数为空，请重检查后在调用啊");
		}

	}
	/**
	 * 签名所需要的时间戳 
	 * @return 签名所需要的时间戳  格式:2015-03-02T12:00:00
	 */

	public static String getTimeStamp() {
	/*	TimeZone time = TimeZone.getTimeZone(TIME_STAMP_PARAM_ZG); // 设置为国际时间
		TimeZone.setDefault(time);// 设置时区
		Calendar calendar = Calendar.getInstance(time);// 获取实例
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");// 构造格式化模板
		Date date = calendar.getTime(); // 获取Date对象
		return format.format(date);// 对象进行格式化，获取字符串格式的输出
*/		return  String.valueOf(new Date().getTime());// 对象进行格式化，获取字符串格式的输出
		
	}
	/**
	 * 此方法：针对管理端接口 /api/20140928/management post 的签名方法
	 * @param uri 管理端的接口的api
	 * @param secret_key 签名的共享密钥
	 * @param postData 的数据。此数据为josn 格式:
	 * {
	    	"function": "create_servicer",
	    	"params": {
	        "name": "北京测试公司",
	        "email": "xx@x.com",
	        "contacter": "测试",
	        "address": "北京朝阳区",
	        "phone_number": "15501096071",
	        "fax_number": "010-12345678" 
    		}
		}
		@param timeStamp 当前时间戳
		
	 * @return xvs-signature 签名的值
	 * 
	 */
	 
	public static String getSignature_for_management_post(String uri,String secret_key,String queryString,String postData,String timeStamp){
		StringBuffer dataBuffer = new StringBuffer();
		dataBuffer.append(uri);
		dataBuffer.append(queryString);
		if (StringUtils.verify_not_empty(postData)) {
			dataBuffer.append(postData);
		}
		dataBuffer.append(timeStamp);
		byte[] key = secret_key.getBytes();
		// 还原密钥
		SecretKey secretKey = new SecretKeySpec(key, "HmacSHA256");
		System.err.println("签名字符串:"+dataBuffer.toString());
		byte[] data =null;
		try {
			data = dataBuffer.toString().getBytes("utf-8");
			//data = data1.getBytes("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 实例化Mac
		Mac mac;
		try {
			mac = Mac.getInstance(secretKey.getAlgorithm());
			// 初始化mac
			mac.init(secretKey);
			// 执行消息摘要
			byte[] digest = mac.doFinal(data);
			byte[] hexB = new Hex().encode(digest);
			String checksum = new String(hexB);
			/*String lowerCase = new HexBinaryAdapter().marshal(digest).toLowerCase();
			System.err.println("结果1"+checksum);
			System.err.println("结果2"+lowerCase);*/
			return checksum;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String [] args){
		String timeStamp = getTimeStamp();
		System.err.println(timeStamp);
	}
	
}

