package sales.manager.common.stock;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;

import sales.manager.common.Database;
import sales.manager.common.log.Log;
import sales.manager.web.Main;

public class StockDatabase {
	private int stockIDCount = 0;
	private HashMap<Integer, StockItem> stockList = new HashMap<Integer, StockItem>();

	public StockDatabase() {}

	public boolean writeDatabase() {
		try {
			var json = Database.mapper.writeValueAsString(stockList);
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Main.stockPath), StandardCharsets.UTF_8));
		    out.write(json);
		    out.close();
		    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Main.stockPath + ".index"), StandardCharsets.UTF_8));
		    out.write(String.valueOf(stockIDCount));
		    out.close();
			Log.l.info("Successfully written stock database: " + stockList.size() + " records, " + stockIDCount + " max id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean readDatabase() throws FileNotFoundException, IOException, IllegalArgumentException, IllegalAccessException {
		stockList.clear();
		var path = Paths.get(Main.stockPath);
		if (Files.exists(path)) {
			var bytes = Files.readAllBytes(Paths.get(Main.stockPath));
			String json = new String(bytes, StandardCharsets.UTF_8);
			stockList = Database.mapper.readValue(json, new TypeReference<HashMap<Integer, StockItem>>(){});
			bytes = Files.readAllBytes(Paths.get(Main.stockPath + ".index"));
			String index = new String(bytes, StandardCharsets.UTF_8).replaceAll("[\\r\\n]+", "");
			stockIDCount = Integer.valueOf(index);
		    Log.l.info("Successfully read stock database: " + stockList.size() + " records, " + stockIDCount + " max id");
		} else {
			Log.l.info("Stock database does not exist");
		}
		return true;
	}

	public boolean backupDatabase() {
		Path src = Paths.get(Main.stockPath);
		Path dest = Paths.get(Main.stockPath + ".bak");
		try {
			Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean restoreDatabase() {
		Path src = Paths.get(Main.stockPath + ".bak");
		Path dest = Paths.get(Main.stockPath);
		try {
			Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean addStock(String id, StockItem item) throws Exception {
		if ((id != null) && !id.equals("")) {
			var idInt = Integer.valueOf(id);
			if (stockList.containsKey(idInt)) throw new Exception("ID " + id + " already exists int stock database");
			stockList.put(idInt, item);
		} else {
			stockList.put(stockIDCount, item);
		}
		stockIDCount++;
		return writeDatabase();
	}

	public HashMap<Integer, StockItem> getItems() {
		return stockList;
	}

	public int getStockIDCount() {
		return stockIDCount;
	}

	public void setStockIDCount(int id) {
		stockIDCount = id;
	}


	public StockItem findByID(Integer id) {
		return stockList.get(id);
	}

	public StockItem findByID(String id) {
		if (id == null || id.equals("")) return null;
		return findByID(Integer.valueOf(id));
	}

	public boolean remove(String id) {
		return stockList.remove(Integer.valueOf(id)) != null;
	}

}
