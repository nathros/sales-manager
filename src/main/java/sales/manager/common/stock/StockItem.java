package sales.manager.common.stock;

import java.time.LocalDate;

public class StockItem {
	public String itemName;
	public String supplier;
	public Double price;
	public Double importFee;
	public LocalDate purchaseDate;
	public LocalDate receivedDate;

	public StockItem() {}

	public StockItem(
		String itemName,
		String supplier,
		String price,
		String importFee,
		String purchaseDate,
		String receivedDate) throws Exception
	{
		if ((itemName == null) | itemName.equals("")) throw new Exception("Item name is empty");
		if ((supplier == null) | supplier.equals("")) throw new Exception("Supplier is empty");
		if ((price == null) |price.equals("")) throw new Exception("Purchase price is empty");
		if ((importFee == null) | importFee.equals("")) throw new Exception("Tax price is empty");
		if ((purchaseDate == null) | purchaseDate.equals("")) throw new Exception("Purchase date is empty");

		this.itemName = itemName;
		this.supplier = supplier;
		this.price = Double.valueOf(price);
		this.importFee = Double.valueOf(importFee);
		this.purchaseDate = LocalDate.parse(purchaseDate);
		if (!receivedDate.equals("")) this.receivedDate = LocalDate.parse(receivedDate);
	}

	public void update(StockItem item) {
		this.itemName = item.itemName;
		this.supplier = item.supplier;
		this.price = item.price;
		this.importFee = item.importFee;
		this.purchaseDate = item.purchaseDate;
		this.receivedDate = item.receivedDate;
	}

	public String getItemNameStr() {
		return itemName;
	}

	public String getSupplierStr() {
		return supplier;
	}

	public String getPriceStr() {
		return String.format("%.2f", price);
	}

	public String getImportFeeStr() {
		return String.format("%.2f", importFee);
	}

	public String getPurchaseDateStr() {
		return purchaseDate.toString();
	}

	public String getReceivedDateStr() {
		if (receivedDate == null) return "";
		return receivedDate.toString();
	}
}
