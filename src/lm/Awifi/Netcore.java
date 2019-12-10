package lm.Awifi;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.URI;

/**
 * 定时拨号,更换IP(办公室路由的IP)
 */
public class Netcore {

	public static void main(String[] args){
		login();
		changestatus();
	}
	
	public static void dialup(){
		login();
	    changestatus();
	}

	/**
	 * 重新拨号连接
	 */
	private static void changestatus(){
		String html ="";
		long time1 = System.currentTimeMillis();
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore)
				.build();
		//通过queryId来获取对应的数据
		CloseableHttpResponse response =null;
		try {
			String txturl2= "http://192.168.2.1/wizard.cgi";
			String param="wan_proto=pppoe&wan_pppoe_username=053104651156&wan_pppoe_passwd=47231430&wan_pppoe_dialtype=0&wan_pppoe_mtu=1480&wan_pppoe_dnsen=1&wan_pppoe_dns0=114.114.114.114&wan_pppoe_dns1=114.114.115.115&wan_pppoe_service=&mac_clone_enabled=0&_pageStyle=pc";
			StringEntity stringEntity = new StringEntity((param), "application/json", "utf-8");
			HttpUriRequest login2 = RequestBuilder.post()
		    		.setUri(new URI(txturl2))
		    		.addHeader(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0")
					.addHeader(HttpHeaders.HOST, "192.168.2.1")
		    		.addHeader("X-Requested-With","XMLHttpRequest")
		    		.addHeader("refer", "http://192.168.2.1/")
					.setEntity(stringEntity)
		    		.build();
		    System.out.println("登录中"+login2);
	    	response = httpclient.execute(login2);
//           	HttpPost post = new HttpPost(txturl2); 
//			    System.out.println("Login form get: " + response.getStatusLine()+ "&"+statu+"&："+wanid);
		    //EntityUtils.consume(entity);
		    int resStatu = response.getStatusLine().getStatusCode();//返回码 
//			    System.out.println("状态码"+resStatu);
//			    System.out.println("httpclient耗时"+(System.currentTimeMillis()-time1));
		    HttpEntity entity = response.getEntity();
		    if (resStatu== HttpStatus.SC_OK) {//200正常  其他就不对
		    	if (entity!=null) {  
		    		//System.out.println("进入200内");
		    		html = EntityUtils.toString(entity,"UTF-8");
		    		html=html.replace("&nbsp;", " ");
		    		System.out.println("返回的数据"+html);
		    	}
		    }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }finally {
	        try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			System.out.println("重新连接耗时:"+(System.currentTimeMillis()-time1));
	}

	/**
	 * 登录路由
	 */
	private static void login(){
		String url="http://192.168.2.1/login.cgi";
		String param="username=admin&Pwd=YWRtaW4%3D&_pageStyle=pc";
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore)
				.build();
		//通过queryId来获取对应的数据
		CloseableHttpResponse response =null;
		try {
			StringEntity stringEntity = new StringEntity((param), "application/json", "utf-8");
			HttpUriRequest login2 = RequestBuilder.post()
					.setUri(new URI(url))
					.addHeader(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0")
					.addHeader(HttpHeaders.HOST, "192.168.2.1")
					.addHeader("X-Requested-With","XMLHttpRequest")
					.addHeader("refer", "http://192.168.2.1/")
					.setEntity(stringEntity)
					.build();
			response = httpclient.execute(login2);
			System.out.println("返回的数据"+ EntityUtils.toString(response.getEntity(),"UTF-8"));
			Thread.sleep(1000);
		} catch (Exception ee) {
			// TODO Auto-generated catch block
			ee.printStackTrace();
		}
	}
}
