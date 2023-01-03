package sales.manager.web.handlers;

import java.util.HashMap;

import com.sun.net.httpserver.HttpHandler;

import sales.manager.web.handlers.analytics.AnalyticsHandler;
import sales.manager.web.handlers.sales.SalesHandler;
import sales.manager.web.handlers.sandpit.ParseTestHandler;
import sales.manager.web.handlers.sandpit.SandpitHandler;
import sales.manager.web.handlers.stock.AddStockHandler;
import sales.manager.web.handlers.stock.StockHandler;

public class Handlers {
	public static HashMap<String, HttpHandler> handlers = getHandlers();

	private static HashMap<String, HttpHandler> getHandlers() {
		HashMap<String, HttpHandler> ret = new HashMap<String, HttpHandler>();
		ret.put(RootHandler.PATH, new RootHandler());
		ret.put(AssetHandler.PATH, new AssetHandler());
		ret.put(AdminHandler.PATH, new AdminHandler());

		ret.put(AnalyticsHandler.PATH, new AnalyticsHandler());
		ret.put(StockHandler.PATH, new StockHandler());
		ret.put(AddStockHandler.PATH, new AddStockHandler());
		ret.put(SalesHandler.PATH, new SalesHandler());

		ret.put(SandpitHandler.PATH, new SandpitHandler());
		ret.put(ParseTestHandler.PATH, new ParseTestHandler());
		return ret;
	}

}
