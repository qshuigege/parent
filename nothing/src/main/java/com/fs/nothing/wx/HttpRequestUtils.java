package com.fs.nothing.wx;

public class HttpRequestUtils {/*
	*//**
	 * 
	 * @param url 请求资源的路径
	 * @param postData 请求参数
	 * @return	正常时返回byte数组,异常时返回null
	 *//*
	public static byte[] requestByteArrayPost(String url, String postData) throws Exception{
		//try {
			URL urlObj = new URL(url);//MalformedURLException
			HttpURLConnection conn = (HttpURLConnection)urlObj.openConnection();//IOException
			//设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false; 
			conn.setDoOutput(true);
			//设置是否从httpUrlConnection读入，默认情况下是true
			conn.setDoInput(true);
			//post请求不能使用缓存
			conn.setUseCaches(false);
			//设定传送的内容类型是可序列化的java对象 
			//(如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException) 
			//conn.setRequestProperty("Content-type", "application/x-java-serialized-object");
			//设定请求的方法为"POST"，默认是GET
			conn.setRequestMethod("POST");
			
			OutputStream outStream = conn.getOutputStream();
			outStream.write(postData.getBytes("utf-8"));
			
			conn.connect();//此句可以省略，因为conn.getInputStream()会隐式调用connect()
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inStream = conn.getInputStream();
				BufferedInputStream bin = new BufferedInputStream(inStream);
				byte[] buf = new byte[4096];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int rc = 0;
				while ((rc = bin.read(buf))!=-1) {
					baos.write(buf,0,rc);
				}
				inStream.close();
				bin.close();
				baos.close();
				return baos.toByteArray();
				//baos.write(b, off, len);
			}else{
				throw new Exception("HttpRequestUtils.requestByteArrayPost() HttpURLConnection.HTTP_OK is not 200");
			}
			
			
			
			
		//} catch (MalformedURLException e) {
		//	e.printStackTrace();
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
		//return null;
	}
	
	public static String requestStringPost(String url, String postData) throws Exception{
		//try {
			URL urlObj = new URL(url);//MalformedURLException
			HttpURLConnection conn = (HttpURLConnection)urlObj.openConnection();//IOException
			//设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false; 
			conn.setDoOutput(true);
			//设置是否从httpUrlConnection读入，默认情况下是true
			conn.setDoInput(true);
			//post请求不能使用缓存
			conn.setUseCaches(false);
			//设定传送的内容类型是可序列化的java对象 
			//(如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException) 
			//conn.setRequestProperty("Content-type", "application/x-java-serialized-object");
			//设定请求的方法为"POST"，默认是GET
			conn.setRequestMethod("POST");
			
			OutputStream outStream = conn.getOutputStream();
			outStream.write(postData.getBytes("utf-8"));
			
			conn.connect();//此句可以省略，因为conn.getInputStream()会隐式调用connect()
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuffer content = new StringBuffer();
				String tempStr = "";
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
				while ((tempStr = in.readLine()) != null) {
					content.append(tempStr);
				}
				in.close();
				return content.toString();
			}else{
				throw new Exception("HttpRequestUtils.requestStringPost() HttpURLConnection.HTTP_OK is not 200");
			}
			
			
			
			
		//} catch (MalformedURLException e) {
		//	e.printStackTrace();
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
		//return null;
	}
	
	public static byte[] requestByteArrayGet(String url) throws Exception{
		//try {
			URL urlObj = new URL(url);//MalformedURLException
			HttpURLConnection conn = (HttpURLConnection)urlObj.openConnection();//IOException
			conn.connect();//此句可以省略，因为conn.getInputStream()会隐式调用connect()
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inStream = conn.getInputStream();
				BufferedInputStream bin = new BufferedInputStream(inStream);
				byte[] buf = new byte[4096];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int rc = 0;
				while ((rc = bin.read(buf))!=-1) {
					baos.write(buf,0,rc);
				}
				inStream.close();
				bin.close();
				baos.close();
				return baos.toByteArray();
				//baos.write(b, off, len);
			}else{
				throw new Exception("HttpRequestUtils.requestByteArrayGet() HttpURLConnection.HTTP_OK is not null");
			}
			
			
			
			
		//} catch (MalformedURLException e) {
		//	e.printStackTrace();
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
		//return null;
	
		*//*
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod("https://www.baidu.com/img/2016_6_1logo_8d0030f576acdcf660d470e225368007.gif");
		try {
			client.executeMethod(method);
			InputStream is = method.getResponseBodyAsStream();
			BufferedInputStream bin = new BufferedInputStream(is); 

			OutputStream out = new FileOutputStream(new File("D:\\baidu_logoaaa.png")); 
			int size = 0;//实际读到的内容 
			byte[] buf = new byte[1024];//把每次读到的内容放到buf中，最多读1KB 
			while ((size = bin.read(buf)) != -1) { 
			out.write(buf, 0, size); 
			}
			is.close();
			bin.close(); 
			out.close(); 
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*//*
		
		
	
	
	}
	
	public static String requestStringGet(String url) throws Exception{
		//try {
			URL urlObj = new URL(url);//MalformedURLException
			HttpURLConnection conn = (HttpURLConnection)urlObj.openConnection();//IOException
			conn.connect();//此句可以省略，因为conn.getInputStream()会隐式调用connect()
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuffer content = new StringBuffer();
				String tempStr = "";
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
				while ((tempStr = in.readLine()) != null) {
					content.append(tempStr);
				}
				return content.toString();
			}else{
				throw new Exception("HttpRequestUtils.requestStringGet() HttpURLConnection.HTTP_OK is not 200");
			}
			
			
			
			
		//} catch (MalformedURLException e) {
		//	e.printStackTrace();
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
		//return "";
	
	}*/
}
