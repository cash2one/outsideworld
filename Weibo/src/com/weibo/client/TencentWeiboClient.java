package com.weibo.client;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.model.Item;
import com.tencent.weibo.api.Statuses_API;
import com.tencent.weibo.api.Trends_API;
import com.tencent.weibo.beans.OAuth;

public class TencentWeiboClient extends
		AbstractWeiboClient<Map<String, Object>> {

	private static Logger LOG = Logger.getLogger(TencentWeiboClient.class);

	private static Set<Serializable> cache = new HashSet<Serializable>(100);
	private static Lock lock = new ReentrantLock();

	private OAuth oauth;
	private Statuses_API st = new Statuses_API();

	private int interval = 0;

	public static int typeId = 100002;

	public TencentWeiboClient(String[] params) {
		super(params);
		interval = Integer.parseInt(params[2]);
		LOG.info("Initialize TencentWeiboClient");
		LOG.info("TencentWeiboClient Params:" + Arrays.toString(params));
		LOG.info("Fetch Interval: " + interval + " seconds");
	}

	@Override
	public void login() throws Exception {
		LOG.info("Try to Login TencentWeibo Client");
		oauth = new OAuth(params[0], params[1], "null");
		LOG.info("Finished Login TencentWeibo Client");
	}

	@Override
	public Serializable getIdentifier(Map<String, Object> weibo) {
		return weibo.get("id").toString();
	}

	@Override
	public Item wrapItem(Map<String, Object> weibo) {
		Item item = new Item();
		item.setUrl(weibo.get("id").toString());
		item.setSourceId(String.valueOf(typeId));
		item.setContent(weibo.get("text").toString());
		item.setPubTime(new Date(1000 * Long.parseLong(weibo.get("timestamp")
				.toString())));
		return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getWeibos() throws Exception {
		List<Map<String, Object>> data = null;
		try {
			LOG.info("Send Request to Tencentweibo");
			ObjectMapper mapper = new ObjectMapper();

			String jsonStr = st.public_timeline(oauth, "json", "0", "100");
			Map<String, Object> m = (Map<String, Object>) mapper.readValue(
					jsonStr, Map.class).get("data");
			LOG.info("Get Response to Tencentweibo");
			data = (List<Map<String, Object>>) m.get("info");
			LOG.info("Retrieve " + data.size() + " weibo from Tencentweibo");
			return data;
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return data;
		}
	}

	@Override
	public int getInterval() {
		return interval;
	}

	@Override
	public Set<Serializable> getCache() {
		return cache;
	}

	@Override
	public Lock getLock() {
		return lock;
	}

	private Trends_API tr = new Trends_API();

	@Override
	public Item wrapHotItem(Map<String, Object> weibo) {
		Item item = new Item();
		item.setUrl(weibo.get("id").toString());
		item.setSourceId(String.valueOf(typeId));
		item.setContent(weibo.get("name").toString());
		item.setReplyNum(Integer.parseInt(weibo.get("tweetnum").toString()));
		item.setPubTime(new Date());
		return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getHotWeibos() throws Exception {
		List<Map<String, Object>> data = null;
		try {
			LOG.info("Send Request to Tencentweibo");
			ObjectMapper mapper = new ObjectMapper();

			String jsonStr = tr.ht(oauth, "json", "1", "20", "0");
			Map<String, Object> m = (Map<String, Object>) mapper.readValue(
					jsonStr, Map.class).get("data");
			LOG.info("Get Response to Tencentweibo");
			data = (List<Map<String, Object>>) m.get("info");
			LOG.info("Retrieve " + data.size() + " weibo from Tencentweibo");
			return data;
		} catch (Exception e) {
			LOG.error(e);
			return data;
		}
	}
}
