package sales.manager.common.sales;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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




			ObjectMapper mapper = new ObjectMapper();
			mapper = mapper.findAndRegisterModules();
			var a = mapper.writeValueAsString(sales);

			HashMap<String, TransactionGroup> map
			  = mapper.readValue(a, new TypeReference<HashMap<String, TransactionGroup>>(){});

			map.size();
			sales = map;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public HashMap<String, TransactionGroup> getSales() {
		return sales;
	}
}
