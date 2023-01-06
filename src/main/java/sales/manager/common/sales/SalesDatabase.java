package sales.manager.common.sales;

import java.util.HashMap;
import java.util.List;

import sales.manager.common.sales.report.Record;
import sales.manager.common.sales.report.ReportParser;
import sales.manager.common.sales.report.TransactionGroup;

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

	public boolean readDatabase() {
		ReportParser parser = new ReportParser("report.csv");
		try {
			List<Record> list = parser.parse();

			sales = new HashMap<String, TransactionGroup>();
			for (Record record: list) {
				String id = record.getItemID();
				TransactionGroup group = sales.get(id);
				if (group == null) {
					TransactionGroup newGroup = new TransactionGroup();
					//todo
					sales.put(id, newGroup);
				} else {
					//todo
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
