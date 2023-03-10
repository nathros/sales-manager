package sales.manager.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import sales.manager.common.log.Log;
import sales.manager.common.sales.SalesDatabase;
import sales.manager.common.stock.StockDatabase;

public class Database {
	public static SalesDatabase sales = new SalesDatabase();
	public static StockDatabase stock = new StockDatabase();
	public static ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

	public static void init() {
		try {
			stock.readDatabase();
			sales.readDatabase();
		} catch (Exception e) {
			Log.l.severe(e.getMessage());
		}
	}
}
