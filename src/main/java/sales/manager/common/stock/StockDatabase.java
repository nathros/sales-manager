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

public class StockDatabase {
	private int stockIDCount = 0;
	private static final String DB_FILE = "config/stock.db";
	private static final String ID = "id";
	private HashMap<Integer, StockItem> stockList = new HashMap<Integer, StockItem>();

	public StockDatabase() {}

	public boolean writeDatabase() throws FileNotFoundException, IOException, IllegalArgumentException, IllegalAccessException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(DB_FILE, false));
		writer.write(String.valueOf(stockIDCount));
		writer.newLine();

		Field[] fields = StockItem.class.getFields();
		for (Integer key : stockList.keySet()) {
			StockItem item = stockList.get(key);
			for (Field field : fields) {
				writer.write(field.getName() + "=" + field.get(item));
				writer.newLine();
			}
			writer.write(ID + "=" + key);
			writer.newLine();
		}
		writer.close();
		return true;
	}

	public boolean readDatabase() throws FileNotFoundException, IOException, IllegalArgumentException, IllegalAccessException {
		stockList.clear();
		BufferedReader br = new BufferedReader(new FileReader(DB_FILE));
	    String line = br.readLine();
	    stockIDCount = Integer.valueOf(line);

	    var fieldList = Arrays.asList(StockItem.class.getFields());
	    StockItem newItem = new StockItem();

	    while (true) {
	    	line = br.readLine();
	    	if (line == null) break;

	    	String[] keyValue = line.split("=");
	    	if (keyValue[0].equals(ID)) {
	    		stockList.put(Integer.valueOf(keyValue[1]), newItem);
	    		newItem = new StockItem();
	    		continue;
	    	} else {
	    		for (Field field: fieldList) {
	    			if (field.getName().equals(keyValue[0])) {
	    				if (field.getType() == Double.class) {
	    					field.set(newItem, Double.valueOf(keyValue[1]));
	    				}  else if (field.getType() == LocalDate.class) {
	    					field.set(newItem, LocalDate.parse(keyValue[1]));
	    				} else {
	    					if (keyValue.length > 1)
	    						field.set(newItem, keyValue[1]);
	    				}
	    				break;
	    			}
	    		}

	    	}
	    }
	    br.close();
		return true;
	}

	public boolean backupDatabase() {
		Path src = Paths.get(DB_FILE);
		Path dest = Paths.get(DB_FILE + ".bak");
		try {
			Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean restoreDatabase() {
		Path src = Paths.get(DB_FILE + ".bak");
		Path dest = Paths.get(DB_FILE);
		try {
			Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
