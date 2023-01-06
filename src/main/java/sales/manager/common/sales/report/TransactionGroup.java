package sales.manager.common.sales.report;

import java.util.ArrayList;
import java.util.List;

public class TransactionGroup {
	private List<Record> transactions = new ArrayList<Record>();
	private String StockID;

	public boolean addRecord(Record record) {


		return true;
	}

}
