package sales.manager.web.handlers;

import java.util.HashMap;

import com.sun.net.httpserver.HttpHandler;

import sales.manager.web.handlers.files.FilesAddHandler;
import sales.manager.web.handlers.files.FilesHandler;
import sales.manager.web.handlers.sandpit.ParseTestHandler;
import sales.manager.web.handlers.sandpit.SandpitHandler;

public class Handlers {
	public static HashMap<String, HttpHandler> handlers = getHandlers();

	private static HashMap<String, HttpHandler> getHandlers() {
		HashMap<String, HttpHandler> ret = new HashMap<String, HttpHandler>();
		ret.put(RootHandler.PATH, new RootHandler());
		ret.put(AssetHandler.PATH, new AssetHandler());
		ret.put(AdminHandler.PATH, new AdminHandler());

		ret.put(FilesHandler.PATH, new FilesHandler());
		ret.put(FilesAddHandler.PATH, new FilesAddHandler());

		ret.put(SandpitHandler.PATH, new SandpitHandler());
		ret.put(ParseTestHandler.PATH, new ParseTestHandler());
		return ret;
	}

}
