package com.weibo.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.http.HttpParameters;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.model.Item;
import com.sohu.t.open.util.ApiClient;
import com.sohu.t.open.util.TwUtils;

public class SohuWeiboClient extends AbstractWeiboClient<Map<String, Object>> {

	private static Logger LOG = Logger.getLogger(SohuWeiboClient.class);

	private static Set<Serializable> cache = new HashSet<Serializable>(200);
	private static Lock lock = new ReentrantLock();

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"E MMM dd hh:mm:ss yyyy", Locale.US);

	private int interval = 0;

	public static int typeId = 100003;

	public SohuWeiboClient(String[] params) {
		super(params);
		interval = Integer.parseInt(params[4]);
		LOG.info("Initialize SohuWeiboClient");
		LOG.info("SohuWeiboClient Params:" + Arrays.toString(params));
		LOG.info("Fetch Interval: " + interval + " seconds");
	}

	private String accessToken;
	private String accessTokenSecret;

	@Override
	public Item wrapItem(Map<String, Object> weibo) {
		Item item = new Item();
		item.setUrl(weibo.get("id").toString());
		item.setSourceId(String.valueOf(typeId));
		item.setContent(weibo.get("text").toString());
		try {
			item.setPubTime(sdf.parse(weibo.get("created_at").toString()
					.replace("+0800 ", "")));
		} catch (ParseException e) {
			e.printStackTrace();
			item.setPubTime(new Date());
		}
		return item;
	}

	public List<Map<String, Object>> getWeibos() throws Exception {
		LOG.info("Send Request to Sohuweibo");
		String jsonObjs = ApiClient.doGetMethod(
				"http://api.t.sohu.com/statuses/public_timeline.json", "UTF-8");
		if (StringUtils.isBlank(jsonObjs)
				|| StringUtils.startsWith(jsonObjs, "<html>")) {
			return null;
		}
		LOG.info("Get Response to Sohuweibo");
		List<Map<String, Object>> models = trans(jsonObjs);
		LOG.info("Retrieve " + models.size() + " weibo from Sohuweibo");
		return models;
	}

	@Override
	public int getInterval() {
		return interval;
	}

	@SuppressWarnings("unused")
	private String http(String url, String method, boolean isAuthorize)
			throws Exception {
		URL u = new URL(url);
		HttpURLConnection request = (HttpURLConnection) u.openConnection();
		request.setDoOutput(true);
		request.setRequestMethod(method);
		if (isAuthorize) {
			consumer.setTokenWithSecret(accessToken, accessTokenSecret);
			consumer.sign(request);
		}
		request.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				request.getInputStream(), "UTF-8"));
		String result = "";
		String b = null;
		while ((b = reader.readLine()) != null) {
			result += b;
		}
		request.disconnect();
		return result;
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> trans(String jsonObjs) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> models = mapper.readValue(jsonObjs,
				List.class);
		return models;
	}

	private OAuthConsumer consumer = new DefaultOAuthConsumer(params[2],
			params[3]);

	// xauth sign parameter
	public HttpURLConnection signRequestForXauth(HttpURLConnection request,
			String username, String password) throws Exception {
		HttpParameters para = new HttpParameters();
		para.put("x_auth_username", TwUtils.encode(username));
		para.put("x_auth_password", TwUtils.encode(password));
		para.put("x_auth_mode", "client_auth");
		consumer.setAdditionalParameters(para);
		consumer.sign(request);
		return request;
	}

	private synchronized String XAuthAuthorize(String username, String password)
			throws Exception {
		LOG.info("XAuthAuthorize SohuWeibo");
		URL url = new URL("http://api.t.sohu.com/oauth/access_token");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.setDoOutput(true);
		request.setRequestMethod("POST");
		request = signRequestForXauth(request, username, password);
		OutputStream ot = request.getOutputStream();
		ot.write(("x_auth_username=" + TwUtils.encode(username)
				+ "&x_auth_password=" + TwUtils.encode(password) + "&x_auth_mode=client_auth")
				.getBytes());
		ot.flush();
		ot.close();
		request.connect();
		LOG.info("Response: " + request.getResponseCode() + " "
				+ request.getResponseMessage());
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String token = "";
		String b = null;
		while ((b = reader.readLine()) != null) {
			token += b;
		}
		request.disconnect();
		return token;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}

	private static Lock loginLock = new ReentrantLock();

	@Override
	public void login() throws Exception {
		LOG.info("Try to login SohuWeibo");
		try {
			loginLock.lock();
			String accessTokenStr = XAuthAuthorize(params[0], params[1]);
			if (StringUtils.isBlank(accessTokenStr)) {
				return;
			}
			setAccessToken(accessTokenStr.split("&")[0].split("=")[1]);
			setAccessTokenSecret(accessTokenStr.split("&")[1].split("=")[1]);

			consumer = new DefaultOAuthConsumer(params[2], params[3]);
			LOG.info("Successfully login SohuWeibo");
		} catch (Exception e) {
			LOG.error("Login SohuWeibo error", e);
		} finally {
			loginLock.unlock();
		}
	}

	@Override
	public Set<Serializable> getCache() {
		return cache;
	}

	@Override
	public Lock getLock() {
		return lock;
	}

	@Override
	public Serializable getIdentifier(Map<String, Object> weibo) {
		return weibo.get("id").toString();
	}

	@Override
	public Item wrapHotItem(Map<String, Object> weibo) {
		return null;
	}

	@Override
	public List<Map<String, Object>> getHotWeibos() throws Exception {
		return null;
	}
}
