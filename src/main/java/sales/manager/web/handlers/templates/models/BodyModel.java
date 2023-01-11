package sales.manager.web.handlers.templates.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import sales.manager.common.Util;
import sales.manager.web.handlers.BaseHandler;

public class BodyModel {
	private final HttpExchange he;
	private final Object model;
	private final Map<String, Object> queries;

	public static final String QUERY_ON = "on";
	public static final String QUERY_OFF = "off";

	private BodyModel(HttpExchange he, Object model) {
		this.he = he;
		this.model = model;
		queries = new HashMap<String, Object>();
		String query = he.getRequestURI().getRawQuery();
		BaseHandler.parseQuery(query, queries);
	}

	public static BodyModel of(HttpExchange he, Object model) {
		return new BodyModel(he, model);
	}

	public HttpExchange getHttpExchange() { return he; }
	public Object getModel() { return model; }
	public String getQuery(String key) { return (String) queries.get(key); }

	public String getQueryNoNull(String key) {
		String q = (String) queries.get(key);
		if (q == null) return "";
		else return Util.decodeUrl(q);
	}

	@SuppressWarnings("unchecked")
	public List<String> getQueryArray(String key) {
		Object o = queries.get(key);
		if (o instanceof String) {
			var list = new ArrayList<String>();
			list.add((String) o);
			return list;
		} else {
			return (List<String>) o;
		}
	}

	public boolean hasQuery() {
		String q = he.getRequestURI().getRawQuery();
		return q != null;
	}
}