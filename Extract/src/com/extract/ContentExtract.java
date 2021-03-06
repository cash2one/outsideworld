package com.extract;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.TagNode;

import com.model.Item;

public class ContentExtract extends AExtract {

	@Override
	public void process(Item item) throws Exception {
		if (item.getTemplate() == null
				&& !"MetaSearch".equalsIgnoreCase(item.getType())) {
			item.setStatus(false);
			return;
		}
		String content = extract("content", item);
		// 如果没有抽取，则使用默认的抽取策略
		if (StringUtils.isBlank(content)) {
			content = getDefault(item.getParsedHtml().getNode(), "//p");
		}
		item.setContent(content);
	}

	private String getDefault(TagNode node, String xpath) throws Exception {
		String result = "";
		Object[] objs = node.evaluateXPath(xpath);
		if (objs != null && objs.length > 0) {
			for (int i = 0; i < objs.length; i++) {
				if (objs[i] instanceof TagNode) {
					result += extractTxt((TagNode) objs[i]);
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private String extractTxt(TagNode node) {
		List<TagNode> children = node.getAllElementsList(false);
		int linkLength = 0;
		for (TagNode child : children) {
			if (child.getName().equalsIgnoreCase("script")) {
				child.removeAllChildren();
				node.removeChild(child);
			} else if (child.getName().equals("a")) {
				linkLength += child.getText().length();
			}
		}
		String text = node.getText().toString();
		if (text.length() > linkLength * 2) {
			return text;
		} else {
			return "";
		}
	}
}
