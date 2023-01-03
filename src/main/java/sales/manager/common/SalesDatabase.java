package sales.manager.common;

import java.util.HashMap;
import java.util.List;

import sales.manager.common.report.Record;
import sales.manager.common.report.TransactionGroup;

public class SalesDatabase {
	HashMap<String, TransactionGroup> sales = new HashMap<String, TransactionGroup>();

	boolean addToDatabase(List<Record> records) {
		for (Record rec : records) {
			var group = sales.get(rec.getOrderNumber());
			if (group == null) {
				TransactionGroup tg = new TransactionGroup();

				sales.put(rec.getOrderNumber(), tg);
			} else {

			}
		}
		return true;
	}
}
