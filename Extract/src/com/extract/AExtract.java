package com.extract;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.model.Item;
import com.model.ParsedHtml;
import com.model.policy.Element;
import com.model.policy.Element.ElementType;
import com.model.policy.Template;
import com.util.CssUtil;
import com.util.Fetcher;
import com.util.XPathUtil;

public abstract class AExtract implements Extract {

	private Fetcher fetcher = new Fetcher();

	protected String extract(String field, Item item) throws Exception {
		Template template = item.getTemplate();
		if (template == null) {
			return "";
		}

		/* 找到对应的Element */
		Element element = null;
		Set<Element> elements = template.getElements();
		for (Element e : elements) {
			if (StringUtils.equals(field, e.getName())) {
				element = e;
				break;
			}
		}
		if (element == null) {
			return "";
		}

		/* 解析XAPTH/CSS */
		String text = extract(item, element);

		/* 解析正则表达式 */
		if (!StringUtils.isEmpty(element.getRegex())) {
			text = text.replace("&nbsp;", "").replace("\r\n", "")
					.replace("&#91;", "[").replace("&#93;", "]");
			text = extract(text, element.getRegex());
		}

		/* 解析format */
		if ("javascript".equals(element.getFormat())) {
			text = getJavascript(item, text);
			text = new ParsedHtml(text).getDoc().text();
		}
		return text;
	}

	private String extract(Item item, Element element) {
		String text = "";
		try {
			text = getString(item.getParsedHtml(), element);
		} catch (Exception e) {
			return text;
		}
		return text;
	}

	private String extract(String value, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return value;
		}
	}

	private String getJavascript(Item item, String url) {
		URL jsUrl = null;
		try {
			jsUrl = new URL(new URL(item.getUrl()), url);
		} catch (MalformedURLException e1) {
			return "";
		}
		Item jsItem = new Item();
		jsItem.setUrl(jsUrl.toString());
		try {
			fetcher.fetch(jsItem);
		} catch (Exception e) {
			return "";
		}
		try {
			return new String(jsItem.getRawData(), jsItem.getEncoding());
		} catch (UnsupportedEncodingException e) {
			try {
				return new String(jsItem.getRawData(), "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				return "";
			}
		}
	}

	private String getString(ParsedHtml parsedHtml, Element e) throws Exception {
		if (ElementType.XPATH.equals(e.getType())) {
			return XPathUtil.getResult(parsedHtml.getNode(), e.getDefine());
		} else if (ElementType.CSS.equals(e.getType())) {
			return CssUtil.getResult(parsedHtml.getDoc(), e.getDefine());
		}
		return "";
	}

}
