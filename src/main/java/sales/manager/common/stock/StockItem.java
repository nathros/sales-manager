package sales.manager.common.stock;

import java.time.LocalDate;

public class StockItem {
	public String itemName;
	public String supplier;
	public Double price;
	public Double importFee;
	public LocalDate purchaseDate;
	public LocalDate receivedDate;

	public StockItem() {
		itemName = "";
		supplier = "";
		price = Double.valueOf("0");
		importFee = Double.valueOf("0");;
		purchaseDate = LocalDate.now();
		receivedDate = LocalDate.now();
	}
}
