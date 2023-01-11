package sales.manager.common.stock;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import sales.manager.web.handlers.templates.models.BodyModel;

public class StockItem {
	public String itemName;
	public String supplier;
	public Double price;
	public Double importFee;
	public LocalDate purchaseDate;
	public LocalDate receivedDate;
	public HashMap<String, Double> itemIDsPostageMap; // item-ID to postage cost
	public Boolean sold;

	public StockItem() {}

	public StockItem(
		String itemName,
		String supplier,
		String price,
		String importFee,
		String purchaseDate,
		String receivedDate,
		List<String> itemIDs,
		List<String> postage,
		String sold) throws Exception
	{
		if ((itemName == null) | itemName.equals("")) throw new Exception("Item name is empty");
		if ((supplier == null) | supplier.equals("")) throw new Exception("Supplier is empty");
		if ((price == null) |price.equals("")) throw new Exception("Purchase price is empty");
		if ((importFee == null) | importFee.equals("")) throw new Exception("Tax price is empty");
		if ((purchaseDate == null) | purchaseDate.equals("")) throw new Exception("Purchase date is empty");
		if (itemIDs == null) {
			this.itemIDsPostageMap = new HashMap<String, Double>();
		} else {
			if (itemIDs.size() != postage.size()) new Exception("Item ID and postage count mismatch");
			this.itemIDsPostageMap = new HashMap<String, Double>();
			for (int i = 0; i < itemIDs.size(); i++) {
				this.itemIDsPostageMap.put(itemIDs.get(i), Double.valueOf(postage.get(i)));
			}
		}
		if (sold == null) throw new Exception("Sold state is empty");

		this.itemName = itemName;
		this.supplier = supplier;
		this.price = Double.valueOf(price);
		this.importFee = Double.valueOf(importFee);
		this.purchaseDate = LocalDate.parse(purchaseDate);
		if (!receivedDate.equals("")) this.receivedDate = LocalDate.parse(receivedDate);
		if (sold.equals(BodyModel.QUERY_ON)) this.sold = true;
		else if (sold.equals(BodyModel.QUERY_OFF)) this.sold = false;
		else this.sold = Boolean.parseBoolean(sold);
	}

	public void update(StockItem item) {
		this.itemName = item.itemName;
		this.supplier = item.supplier;
		this.price = item.price;
		this.importFee = item.importFee;
		this.purchaseDate = item.purchaseDate;
		this.receivedDate = item.receivedDate;
		this.itemIDsPostageMap = item.itemIDsPostageMap;
		this.sold = item.sold;
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

	public String getItemIDsStr() {
		if (itemIDsPostageMap == null) return "";
		StringBuilder sb = new StringBuilder();
		/*for (String i: itemIDs) {
			sb.append(i);
			sb.append(",");
		}*/
		return sb.toString();
	}

	public HashMap<String, Double> getitemIDsPostageMap() {
		return itemIDsPostageMap;
	}
}
