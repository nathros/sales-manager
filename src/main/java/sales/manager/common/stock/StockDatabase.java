package sales.manager.common.stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import sales.manager.common.log.Log;
import sales.manager.web.Main;

public class StockDatabase {
	private int stockIDCount = 0;
	private static final String ID = "id";
	private HashMap<Integer, StockItem> stockList = new HashMap<Integer, StockItem>();

	public StockDatabase() {}

	public boolean writeDatabase() throws FileNotFoundException, IOException, IllegalArgumentException, IllegalAccessException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(Main.stockPath, false));
		writer.write(String.valueOf(stockIDCount));
		writer.newLine();

		Field[] fields = StockItem.class.getFields();
		int count = 0;
		for (Integer key : stockList.keySet()) {
			StockItem item = stockList.get(key);
			for (Field field : fields) {
				Object object = field.get(item);
				if (object == null) object = "";
				writer.write(field.getName() + "=" + object);
				writer.newLine();
			}
			writer.write(ID + "=" + key);
			writer.newLine();
			count++;
		}
		writer.close();
		Log.l.info("Successfully written stock database: " + count + " records, " + stockIDCount + " max id");
		return true;
	}

	public boolean readDatabase() throws FileNotFoundException, IOException, IllegalArgumentException, IllegalAccessException {
		stockList.clear();
		BufferedReader br = new BufferedReader(new FileReader(Main.stockPath));
		String line = br.readLine();
		stockIDCount = Integer.valueOf(line);

		var fieldList = Arrays.asList(StockItem.class.getFields());
		StockItem newItem = new StockItem();
		int count = 0;

		while (true) {
			line = br.readLine();
			if (line == null) break;

			String[] keyValue = line.split("=");
			if (keyValue[0].equals(ID)) {
				stockList.put(Integer.valueOf(keyValue[1]), newItem);
				count++;
				newItem = new StockItem();
				continue;
	    	} else {
				if (keyValue.length > 1) {
					for (Field field: fieldList) {
						if (field.getName().equals(keyValue[0])) {
							Class<?> type = field.getType();
							if (type == Double.class) {
								field.set(newItem, Double.valueOf(keyValue[1]));
							}  else if (type == LocalDate.class) {
								field.set(newItem, LocalDate.parse(keyValue[1]));
							} else if (type == HashMap.class) {
								int first = line.indexOf("{");
								int second = line.indexOf("}");
								var data = line.substring(first + 1, second);
								String split[] = data.split(", ");
								HashMap<String, Double> map = new HashMap<String, Double>();
								for (String i: split) {
									String mapKV[] = i.split("=");
									map.put(mapKV[0], Double.valueOf(mapKV[1]));
								}
								field.set(newItem, map);
							} else if (type == List.class) {
								String listStr = keyValue[1].substring(1, keyValue[1].length() - 1);
								List<String> list = Arrays.asList(listStr.split(","));
								field.set(newItem, list);
							} else if (type == Boolean.class) {
								Boolean b = Boolean.valueOf(keyValue[1]);
								field.set(newItem, b);
							} else {
								field.set(newItem, keyValue[1]);
							}
							break;
						}
					}
				}
	    	}
	    }
	    br.close();
	    Log.l.info("Successfully read stock database: " + count + " records, " + stockIDCount + " max id");
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
