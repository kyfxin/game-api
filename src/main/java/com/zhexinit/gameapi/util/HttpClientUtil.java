package com.zhexinit.gameapi.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	/**
	 * 发送GET请求
	 * @param url
	 * @return
	 */
	public static String sendGetReq(String url) {
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		logger.info("HttpClientUtil::sendGetReq, 发送请求的url={}", url);
		// 创建Get请求
		HttpGet httpGet = new HttpGet(url);
		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 配置信息
			RequestConfig requestConfig = RequestConfig.custom()
					// 设置连接超时时间(单位毫秒)
					.setConnectTimeout(5000)
					// 设置请求超时时间(单位毫秒)
					.setConnectionRequestTimeout(5000)
					// socket读写超时时间(单位毫秒)
					.setSocketTimeout(5000)
					// 设置是否允许重定向(默认为true)
					.setRedirectsEnabled(true).build();
 
			// 将上面的配置信息 运用到这个Get请求里
			httpGet.setConfig(requestConfig);
 
			// 由客户端执行(发送)Get请求
			response = httpClient.execute(httpGet);
 
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			logger.info("HttpClientUtil::sendGetReq，请求的url={}， 响应状态={}",url, response.getStatusLine());
			if (responseEntity != null) {
				String respContent = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
				logger.info("HttpClientUtil::sendGetReq， 请求的url={}， 响应内容长度={}， 响应内容 ={}", 
						url, responseEntity.getContentLength(), respContent);
				return respContent;
			} else {
				logger.error("HttpClientUrl::sendGetReq, 请求的url={}， responseEntry 为空！！！！！！！！");
			}
		} catch (ParseException | IOException  e) {
			logger.info("HttpClientUtil::sendGetReq，请求的url={}， 发送请求出异常:", url, e);
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.info("HttpClientUtil::sendGetReq，请求的url={}， 释放资源出异常:", url, e);;
			}
		}
		
		return "";
	}
	
	
	/**
	 * 发送GET请求
	 * @param url
	 * @return
	 */
	public static InputStream sendGetReqWithStreamResp(String url) {
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		logger.info("HttpClientUtil::sendGetReq, 发送请求的url={}", url);
		// 创建Get请求
		HttpGet httpGet = new HttpGet(url);
		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 配置信息
			RequestConfig requestConfig = RequestConfig.custom()
					// 设置连接超时时间(单位毫秒)
					.setConnectTimeout(5000)
					// 设置请求超时时间(单位毫秒)
					.setConnectionRequestTimeout(5000)
					// socket读写超时时间(单位毫秒)
					.setSocketTimeout(5000)
					// 设置是否允许重定向(默认为true)
					.setRedirectsEnabled(true).build();
 
			// 将上面的配置信息 运用到这个Get请求里
			httpGet.setConfig(requestConfig);
 
			// 由客户端执行(发送)Get请求
			response = httpClient.execute(httpGet);
 
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			logger.info("HttpClientUtil::sendGetReq，请求的url={}， 响应状态={}",url, response.getStatusLine());
			if (responseEntity != null) {
				logger.info("HttpClientUtil::sendGetReq， 请求的url={}， 响应内容长度={}", 
						url, responseEntity.getContentLength());
				return responseEntity.getContent();
			} else {
				logger.error("HttpClientUrl::sendGetReq, 请求的url={}， responseEntry 为空！！！！！！！！");
			}
		} catch (ParseException | IOException  e) {
			logger.info("HttpClientUtil::sendGetReq，请求的url={}， 发送请求出异常:", url, e);
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.info("HttpClientUtil::sendGetReq，请求的url={}， 释放资源出异常:", url, e);;
			}
		}
		
		return null;
	}
	
	/**
	 * 只根据url发送请求
	 * @param url
	 * @return
	 */
	public static String sendPostReqByUrl(String url) {
		logger.info("HttpClientUtil::sendPostReqByUrl， 只根据url发送post请求，url={}", url);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(url);
		RequestConfig config = RequestConfig.custom()
				// 设置连接超时时间(单位毫秒)
				.setConnectTimeout(5000)
				// 设置请求超时时间(单位毫秒)
				.setConnectionRequestTimeout(5000)
				// socket读写超时时间(单位毫秒)
				.setSocketTimeout(5000)
				// 设置是否允许重定向(默认为true)
				.setRedirectsEnabled(true).build();
		httpPost.setConfig(config);
		
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
			logger.info("HttpClientUtil::sendPostReqByUrl，请求的url={}， 响应状态={}",url, httpResponse.getStatusLine());
			HttpEntity responseEntity = httpResponse.getEntity();
			if (null != responseEntity) {
				String respContent = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
				logger.info("HttpClientUtil::sendPostReqByUrl， 请求的url={}， 响应内容长度={}， 响应内容 ={}", 
						url, responseEntity.getContentLength(), respContent);
				return respContent;
			} else {
				logger.error("HttpClientUtil::sendPostReqByUrl，发送请求地址：{},responseEntry为空", url);
			}
		} catch (IOException e) {
			logger.info("HttpClientUtil::sendPostReqByUrl，发送请求地址：{}，请求出现异常:", url, e);
		} finally {
			try {
				if (null != httpResponse) {
					httpResponse.close();
				}
				
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.info("HttpClientUtil::sendPostReqByUrl，发送请求地址：{}，释放资源出现异常:", url, e);
			}
		}
		return "";
	}
	
	/**
	 * 发送post请求，请求内容为json
	 * @param url
	 * @param json
	 * @return
	 */
	public static String sendPostReqWithJson(String url, String json) {
		logger.info("HttpClientUtil::sendPostReqWithJson，发送请求地址={}, 请求json={}", url, json);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		RequestConfig config = RequestConfig.custom()
				// 设置连接超时时间(单位毫秒)
				.setConnectTimeout(5000)
				// 设置请求超时时间(单位毫秒)
				.setConnectionRequestTimeout(5000)
				// socket读写超时时间(单位毫秒)
				.setSocketTimeout(5000)
				// 设置是否允许重定向(默认为true)
				.setRedirectsEnabled(true).build();
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setConfig(config);
		httpPost.setHeader("Content-Type", "application/json;charset=utf8");
		httpPost.setHeader("Accept", "application/json;charset=utf-8");
		httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
		
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
			logger.info("HttpClientUtil::sendPostReqWithJson，请求的url={}， 响应状态={}",url, httpResponse.getStatusLine());
			HttpEntity responseEntity = httpResponse.getEntity();
			if (null != responseEntity) {
				String respContent = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
				logger.info("HttpClientUtil::sendPostReqWithJson， 请求的url={}， 响应内容长度={}， 响应内容 ={}", 
						url, responseEntity.getContentLength(), respContent);
				return respContent;
			} else {
				logger.error("HttpClientUtil::sendPostReqWithJson，发送请求地址：{},responseEntry为空", url);
			}
		} catch (IOException e) {
			logger.info("HttpClientUtil::sendPostReqWithJson，发送请求地址：{}，请求出现异常:", url, e);
		} finally {
			try {
				if (null != httpResponse) {
					httpResponse.close();
				}
				
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.info("HttpClientUtil::sendPostReqWithJson，发送请求地址：{}，释放资源出现异常:", url, e);
			}
		}
		return "";
	}
	
	/**
	 * 自己组织请求头，发送参数为String的post请求，
	 * @param url
	 * @param body
	 * 
	 * @return
	 */
	public static String sendPostReqWithHeader(String url, String body, Map<String, String> headers) {
		logger.info("HttpClientUtil::sendPostReqWithHeader，发送请求地址={}, 请求body={}, headers={}", url, body, headers);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		RequestConfig config = RequestConfig.custom()
				// 设置连接超时时间(单位毫秒)
				.setConnectTimeout(5000)
				// 设置请求超时时间(单位毫秒)
				.setConnectionRequestTimeout(5000)
				// socket读写超时时间(单位毫秒)
				.setSocketTimeout(5000)
				// 设置是否允许重定向(默认为true)
				.setRedirectsEnabled(true).build();
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setConfig(config);
		//头部信息不为空时，设置头部参数
		for (Map.Entry<String, String> e : headers.entrySet()) {
			httpPost.addHeader(e.getKey(), e.getValue());
        }
		
		httpPost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
		
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
			logger.info("HttpClientUtil::sendPostReqWithJson，请求的url={}， 响应状态={}",url, httpResponse.getStatusLine());
			HttpEntity responseEntity = httpResponse.getEntity();
			if (null != responseEntity) {
				String respContent = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
				logger.info("HttpClientUtil::sendPostReqWithJson， 请求的url={}， 响应内容长度={}， 响应内容 ={}", 
						url, responseEntity.getContentLength(), respContent);
				return respContent;
			} else {
				logger.error("HttpClientUtil::sendPostReqWithJson，发送请求地址：{},responseEntry为空", url);
			}
		} catch (IOException e) {
			logger.info("HttpClientUtil::sendPostReqWithJson，发送请求地址：{}，请求出现异常:", url, e);
		} finally {
			try {
				if (null != httpResponse) {
					httpResponse.close();
				}
				
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.info("HttpClientUtil::sendPostReqWithJson，发送请求地址：{}，释放资源出现异常:", url, e);
			}
		}
		return "";
	}
	
	/**
	 * 发送post请求模拟提交表单
	 * @param url
	 * @param params
	 * @return
	 */
	public static String sendPostReqByForm(String url, Map<String, String> params) {
		logger.info("HttpClientUtil::sendPostReqByForm，发送请求地址={}, 请求参数={}", url, params);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		RequestConfig config = RequestConfig.custom()
				// 设置连接超时时间(单位毫秒)
				.setConnectTimeout(5000)
				// 设置请求超时时间(单位毫秒)
				.setConnectionRequestTimeout(5000)
				// socket读写超时时间(单位毫秒)
				.setSocketTimeout(5000)
				// 设置是否允许重定向(默认为true)
				.setRedirectsEnabled(true).build();
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setConfig(config);
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
	
		if (null != params && !params.isEmpty()) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			Set<Entry<String, String>> entrySet = params.entrySet();
			entrySet.forEach(entry -> {
				nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			});
			
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8); 
			httpPost.setEntity(formEntity);
		}
		
		
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
			logger.info("HttpClientUtil::sendPostReqByForm，请求的url={}， 响应状态={}",url, httpResponse.getStatusLine());
			HttpEntity responseEntity = httpResponse.getEntity();
			if (null != responseEntity) {
				String respContent = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
				logger.info("HttpClientUtil::sendPostReqByForm， 请求的url={}， 响应内容长度={}， 响应内容 ={}", 
						url, responseEntity.getContentLength(), respContent);
				return respContent;
			} else {
				logger.error("HttpClientUtil::sendPostReqByForm，发送请求地址：{},responseEntry为空", url);
			}
		} catch (IOException e) {
			logger.info("HttpClientUtil::sendPostReqByForm，发送请求地址：{}，请求出现异常:", url, e);
		} finally {
			try {
				if (null != httpResponse) {
					httpResponse.close();
				}
				
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.info("HttpClientUtil::sendPostReqByForm，发送请求地址：{}，释放资源出现异常:", url, e);
			}
		}
		return "";
	}
	
	
	/**
	 * 根据是否是https请求，获取HttpClient客户端
	 *
	 * TODO 本人这里没有进行完美封装。对于 校不校验校验证书的选择，本人这里是写死
	 *      在代码里面的，你们在使用时，可以灵活二次封装。
	 *
	 * 提示: 此工具类的封装、相关客户端、服务端证书的生成，可参考我的这篇博客:
	 *      <linked>https://blog.csdn.net/justry_deng/article/details/91569132</linked>
	 *
	 *
	 * @param isHttps 是否是HTTPS请求
	 *
	 * @return  HttpClient实例
	 * @date 2019/9/18 17:57
	 */
	private CloseableHttpClient getHttpClient(boolean isHttps) {
	   CloseableHttpClient httpClient;
	   if (isHttps) {
	      SSLConnectionSocketFactory sslSocketFactory;
	      try {
	         /// 如果不作证书校验的话
	         sslSocketFactory = getSocketFactory(false, null, null);
	 
	         /// 如果需要证书检验的话
	         // 证书
	         //InputStream ca = this.getClass().getClassLoader().getResourceAsStream("client/ds.crt");
	         // 证书的别名，即:key。 注:cAalias只需要保证唯一即可，不过推荐使用生成keystore时使用的别名。
	         // String cAalias = System.currentTimeMillis() + "" + new SecureRandom().nextInt(1000);
	         //sslSocketFactory = getSocketFactory(true, ca, cAalias);
	      } catch (Exception e) {
	         throw new RuntimeException(e);
	      }
	      httpClient = HttpClientBuilder.create().setSSLSocketFactory(sslSocketFactory).build();
	      return httpClient;
	   }
	   httpClient = HttpClientBuilder.create().build();
	   return httpClient;
	}
	 
	/**
	 * HTTPS辅助方法, 为HTTPS请求 创建SSLSocketFactory实例、TrustManager实例
	 *
	 * @param needVerifyCa
	 *         是否需要检验CA证书(即:是否需要检验服务器的身份)
	 * @param caInputStream
	 *         CA证书。(若不需要检验证书，那么此处传null即可)
	 * @param cAalias
	 *         别名。(若不需要检验证书，那么此处传null即可)
	 *         注意:别名应该是唯一的， 别名不要和其他的别名一样，否者会覆盖之前的相同别名的证书信息。别名即key-value中的key。
	 *
	 * @return SSLConnectionSocketFactory实例
	 * @throws NoSuchAlgorithmException
	 *         异常信息
	 * @throws CertificateException
	 *         异常信息
	 * @throws KeyStoreException
	 *         异常信息
	 * @throws IOException
	 *         异常信息
	 * @throws KeyManagementException
	 *         异常信息
	 * @date 2019/6/11 19:52
	 */
	private static SSLConnectionSocketFactory getSocketFactory(boolean needVerifyCa, InputStream caInputStream, String cAalias)
	      throws CertificateException, NoSuchAlgorithmException, KeyStoreException,
	      IOException, KeyManagementException {
	   X509TrustManager x509TrustManager;
	   // https请求，需要校验证书
	   if (needVerifyCa) {
	      KeyStore keyStore = getKeyStore(caInputStream, cAalias);
	      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	      trustManagerFactory.init(keyStore);
	      TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
	      if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
	         throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
	      }
	      x509TrustManager = (X509TrustManager) trustManagers[0];
	      // 这里传TLS或SSL其实都可以的
	      SSLContext sslContext = SSLContext.getInstance("TLS");
	      sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
	      return new SSLConnectionSocketFactory(sslContext);
	   }
	   // https请求，不作证书校验
	   x509TrustManager = new X509TrustManager() {
	      @Override
	      public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
	      }
	 
	      @Override
	      public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
	         // 不验证
	      }
	 
	      @Override
	      public X509Certificate[] getAcceptedIssuers() {
	         return new X509Certificate[0];
	      }
	   };
	   SSLContext sslContext = SSLContext.getInstance("TLS");
	   sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
	   return new SSLConnectionSocketFactory(sslContext);
	}
	 
	/**
	 * 获取(密钥及证书)仓库
	 * 注:该仓库用于存放 密钥以及证书
	 *
	 * @param caInputStream
	 *         CA证书(此证书应由要访问的服务端提供)
	 * @param cAalias
	 *         别名
	 *         注意:别名应该是唯一的， 别名不要和其他的别名一样，否者会覆盖之前的相同别名的证书信息。别名即key-value中的key。
	 * @return 密钥、证书 仓库
	 * @throws KeyStoreException 异常信息
	 * @throws CertificateException 异常信息
	 * @throws IOException 异常信息
	 * @throws NoSuchAlgorithmException 异常信息
	 * @date 2019/6/11 18:48
	 */
	private static KeyStore getKeyStore(InputStream caInputStream, String cAalias)
	      throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
	   // 证书工厂
	   CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
	   // 秘钥仓库
	   KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	   keyStore.load(null);
	   keyStore.setCertificateEntry(cAalias, certificateFactory.generateCertificate(caInputStream));
	   return keyStore;
	}
}
