//package message;
//
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.security.SecureRandom;
//import java.security.cert.X509Certificate;
//
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
//import com.bcloud.msg.http.HttpSender;
//import org.apache.commons.httpclient.protocol.Protocol;
//
//public class HttpSenderTest {
//
//	public static void main(String[] args) {
//		Protocol myHttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
//		Protocol.registerProtocol("https", myHttps);
//
//
//
//		String uri = "https://send.18sms.com/msg/HttpBatchSendSM";
//
//
//
//
//		String account = "my15957199227";//账号
//		String passWord = "M78aTe23";//密码
//		String mobiles = "13916994164";//
//		String msg = "您的验证码是：168168。";//短信内容
//		//msg="张三您好，您在示远科技下单成功，我们将在1个工作日内将产品帮您增加到你的账户上，详情可24小时拨4007761818";
//		boolean needStatus = true;//是否需要状态报告，需要true，不需要false
//		String product = "";//产品ID(不用填写)
//		String extno = "";//扩展码(请登陆网站用户中心——>服务管理找到签名对应的extno并填写，线下用户请为空)
//		try {
//			String returnString = HttpSender.batchSend(uri, account, passWord, mobiles, msg, needStatus, product, extno);
//			System.out.println(returnString);
//			//TODO 处理返回值,参见HTTP协议文档
//		} catch (Exception e) {
//			//TODO 处理异常
//			e.printStackTrace();
//		}
//
//
//
//	}
//}
//
