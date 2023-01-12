package sales.manager.common.sales.report;

import java.util.ArrayList;
import java.util.List;

public class TransactionGroup {
	private List<Record> transactions = new ArrayList<Record>();
	private String stockID;
	private String itemID;

	public TransactionGroup() {}

	public TransactionGroup(String itemID) {
		this.itemID = itemID;
		stockID = "";
	}

	public boolean addRecord(Record record) {
		transactions.add(record);
		return true;
	}

	public String getItemID() {
		return itemID;
	}

	public String getStockID() {
		return stockID;
	}

	public void setStockID(String id) {
		stockID = id;
	}

	public List<Record> getTransactions() {
		return transactions;
	}

}
