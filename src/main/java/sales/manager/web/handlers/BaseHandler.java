package sales.manager.web.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import sales.manager.common.log.Log;
import sales.manager.web.handlers.templates.TemplateInternalError;
import sales.manager.web.handlers.templates.TemplateInternalError.TemplateInternalErrorModel;

public abstract class BaseHandler implements HttpHandler {
	public static final String LANG_VALUE = "en";

	public static final String ICON_KEY = "rel";
	public static final String ICON_VALUE = "shortcut icon";

	public static final String CHARSET_KEY = "charset";
	public static final String CHARSET_VALUE = "UTF-8";

	public static final String TYPE_KEY = "type";
	public static final String TYPE_SVG = "image/svg+xml";

	public static final String VIEWPORT_KEY = "viewport";
	public static final String VIEWPORT_VALUE = "width=device-width, initial-scale=1";

	public static final String MEDIA_KEY = "media";
	public static final String CSS_MOBILE_MEDIA = "screen and (max-width: 400px)";

	private static int count = 0;

	@Override
	public void handle(HttpExchange he) throws IOException {
		Log.l.finest("Request (" + String.format("%04d", count) + "): " + he.getRequestHeaders().getFirst("Host")
				+ he.getRequestURI());

		try {
			this.requestHandle(he);
		} catch (Exception e) {
			e.printStackTrace();
			errorHandle(he, e);
		} catch (Throwable e) {
			e.printStackTrace();
			errorHandle(he, new Exception(e));
		}
	}

	public abstract void requestHandle(HttpExchange he) throws Exception;

	protected void errorHandle(HttpExchange he, Exception exception) {
		try {
			String response = TemplateInternalError.view.render(TemplateInternalErrorModel.of(exception, he));
			he.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void parseQuery(String query, Map<String, Object> parameters) {
		if (query != null) {
			String pairs[] = query.split("[&]");

			for (String pair : pairs) {
				String param[] = pair.split("[=]");

				String key = null;
				String value = null;
				if (param.length > 1) {
					try {
						key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
						value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						continue;
					}
				}

				if (!parameters.containsKey(key)) {
					parameters.put(key, value);
				} else if (key != null) {
					Object o = parameters.get(key);
					if (o instanceof String) {
						List<String> list = new ArrayList<String>();
						list.add((String) o);
						list.add(value);
						parameters.put(key, list);
					} else {
						@SuppressWarnings("unchecked")
						List<String> list = (List<String>) o;
						list.add(value);

					}
				}
			}
		}
	}
}
