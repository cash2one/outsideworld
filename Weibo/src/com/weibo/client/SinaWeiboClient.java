package com.weibo.client;

import java.io.Serializable;
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

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.model.Item;
import com.tencent.weibo.utils.QHttpClient;

public class SinaWeiboClient extends AbstractWeiboClient<Map<String, Object>> {

	private static Logger LOG = Logger.getLogger(SinaWeiboClient.class);

	private static Set<Serializable> cache = new HashSet<Serializable>(200);
	private static Lock lock = new ReentrantLock();

	private QHttpClient httpClient = new QHttpClient();

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"E MMM dd hh:mm:ss yyyy", Locale.US);

	private int interval = 0;

	public static int typeId = 100001;

	public SinaWeiboClient(String[] params) {
		super(params);
		interval = Integer.parseInt(params[1]);
		LOG.info("Initialize SinaWeiboClient");
		LOG.info("SinaWeiboClient Params:" + Arrays.toString(params));
		LOG.info("Fetch Interval: " + interval + " seconds");
	}

	@Override
	public void login() throws Exception {

	}

	@Override
	public Item wrapItem(Map<String, Object> weibo) {
		Item item = new Item();
		item.setContent(weibo.get("text").toString());
		item.setUrl(weibo.get("id").toString());
		item.setSourceId(String.valueOf(typeId));
		try {
			item.setPubTime(sdf.parse(weibo.get("created_at").toString()
					.replace("+0800 ", "")));
		} catch (ParseException e) {
			e.printStackTrace();
			item.setPubTime(new Date());
		}
		return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getWeibos() throws Exception {
		LOG.info("Send Request to Sinaweibo");
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> data = null;
		String jsonStr = httpClient.httpGet(
				"http://api.t.sina.com.cn/statuses/public_timeline.json",
				"source=" + params[0] + "&count=200&base_app=0");
		LOG.info("Get Response to Sinaweibo");
		data = mapper.readValue(jsonStr, List.class);
		LOG.info("Retrieve " + data.size() + " weibo from Sinaweibo");
		return data;
	}

	@Override
	public int getInterval() {
		return interval;
	}

	@Override
	public Serializable getIdentifier(Map<String, Object> weibo) {
		return weibo.get("mid").toString();
	}

	@Override
	public Set<Serializable> getCache() {
		return cache;
	}

	@Override
	public Lock getLock() {
		return lock;
	}

	private Date pubTime = new Date();
	private SimpleDateFormat pubTimeSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	@Override
	public Item wrapHotItem(Map<String, Object> weibo) {
		Item item = new Item();
		item.setContent(weibo.get("query").toString());
		item.setSourceId(String.valueOf(typeId));
		item.setPubTime(pubTime);
		item.setReplyNum(Integer.parseInt(weibo.get("amount").toString()));
		return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getHotWeibos() throws Exception {
		LOG.info("Send Request to Sinaweibo");
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> data = null;
		String jsonStr = httpClient.httpGet(
				"http://api.t.sina.com.cn/trends/hourly.json", "source="
						+ params[0] + "&base_app=0");
		LOG.info("Get Response to Sinaweibo");
		Map<String, Object> trends = (Map<String, Object>) mapper.readValue(
				jsonStr, Map.class).get("trends");
		String key = (String) trends.keySet().toArray()[0];
		pubTime = pubTimeSdf.parse(key);
		data = (List<Map<String, Object>>) trends.get(key);
		LOG.info("Retrieve " + data.size() + " weibo from Sinaweibo");
		return data;
	}
}
