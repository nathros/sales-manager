package sales.manager.common.sales.report;

import java.util.ArrayList;
import java.util.List;

public class TransactionGroup {
	private List<Record> transactions = new ArrayList<Record>();
	private String StockID;
	private String itemID;

	public TransactionGroup(String itemID) {
		this.itemID = itemID;
		StockID = "";
	}

	public boolean addRecord(Record record) {
		transactions.add(record);

		return true;
	}

	public String getItemID() {
		return itemID;
	}

	public String getStockID() {
		return StockID;
	}

	public List<Record> getTransactions() {
		return transactions;
	}

}
