package org.apache.nutch;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.util.NutchConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dao.CommonDAO;
import com.model.policy.Source;
import com.model.policy.Template;
import com.util.TemplateCache;

public class Context {

	private static Logger LOG = LoggerFactory.getLogger(Context.class);

	private Configuration nutchConfig;
	private CommonDAO commonDAO;

	private String crawlDB;
	private String crawlThread;
	private String crawlDepth;
	private String crawlTopN;
	private String crawlUrls;
	private String crawlFilter;

	private List<Source> sites;
	private List<Template> templates;

	public Context() {
		this.nutchConfig = NutchConfiguration.create();
		this.commonDAO = NutchCrawler.getBean("commonDAO");

		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader()
					.getResourceAsStream("conf/config.properties"));
		} catch (Exception e) {
			try {
				properties.load(this.getClass().getClassLoader()
						.getResourceAsStream("config.properties"));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		crawlDB = properties.getProperty("crawl.db");
		crawlUrls = properties.getProperty("crawl.urls");
		crawlThread = properties.getProperty("crawl.threads");
		crawlDepth = properties.getProperty("crawl.depth");
		crawlTopN = properties.getProperty("crawl.top");
		crawlFilter = properties.getProperty("crawl.filter");

		/* check parameters */
		if (!new File(crawlDB).exists()) {
			new File(crawlDB).mkdirs();
		}
		if (!new File(crawlUrls).exists()) {
			new File(crawlUrls).mkdirs();
		}

		/* load configuration from database */
		LOG.info("Load Configuration from Database");
		sites = commonDAO.query("from Source s where s.type = 'WEBSITE'");
		templates = commonDAO.getAll(Template.class);

		/* Resist Templates */
		LOG.info("Regist Templates");
		for (Template template : templates) {
			TemplateCache.addTemplate(template);
		}
	}

	public String getCrawlDB() {
		return crawlDB;
	}

	public Configuration getNutchConfig() {
		return nutchConfig;
	}

	public String getCrawlThread() {
		return crawlThread;
	}

	public String getCrawlDepth() {
		return crawlDepth;
	}

	public String getCrawlTopN() {
		return crawlTopN;
	}

	public String getCrawlUrls() {
		return crawlUrls;
	}

	public List<Source> getSites() {
		return sites;
	}

	public String getCrawlFilter() {
		return crawlFilter;
	}
}
