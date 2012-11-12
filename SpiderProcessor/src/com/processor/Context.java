package com.processor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.processor.handler.BaseHandler;

public class Context {

	private boolean failsafe = false;
	private Map<String, Object> elements = null;
	private Map<String, Set<String>> collector = null;

	public Context(Map<String, Object> data) {
		this.elements = new HashMap<String, Object>(data);
		this.collector = new HashMap<String, Set<String>>();
	}

	public void addElement(String name, Object value) {
		elements.put(name, value);
	}

	public void addCollector(String collectorName, String elementName) {
		Set<String> collection = collector.get(collector);
		if (collection == null) {
			collection = new HashSet<String>();
		}
		collection.add(elementName);
	}

	public Set<String> getCollector(String collectorName) {
		return collector.get(collectorName);
	}

	public Object getParam(String name) {
		return elements.get(name);
	}

	public void setFailsafe(boolean failsafe) {
		this.failsafe = failsafe;
	}

	public boolean isFailsafe() {
		return failsafe;
	}

	public void error(BaseHandler handler, Exception e) {
		// TODO
	}
}
