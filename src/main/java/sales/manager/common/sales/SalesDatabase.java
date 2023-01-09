package sales.manager.common.sales;

import java.util.HashMap;
import java.util.List;

import sales.manager.common.sales.report.Record;
import sales.manager.common.sales.report.ReportParser;
import sales.manager.common.sales.report.TransactionGroup;

public class SalesDatabase {
	private HashMap<String, TransactionGroup> sales = new HashMap<String, TransactionGroup>();

	boolean addToDatabase(List<Record> records) {
		for (Record record: records) {
			String id = record.getItemID();
			if (id.equals("")) continue;
			TransactionGroup group = sales.get(id);
			if (group == null) {
				group = new TransactionGroup(id);
			}
			group.addRecord(record);
			sales.put(id, group);
		}
		return true;
	}

	public boolean readDatabase() {
		ReportParser parser = new ReportParser("report.csv");
		try {
			List<Record> list = parser.parse();

			sales = new HashMap<String, TransactionGroup>();
			addToDatabase(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public HashMap<String, TransactionGroup> getSales() {
		return sales;
	}
}
