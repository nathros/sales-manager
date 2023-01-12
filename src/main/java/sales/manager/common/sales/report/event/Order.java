package sales.manager.common.sales.report.event;

import java.util.zip.CRC32;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sales.manager.common.Util;
import sales.manager.common.sales.report.Buyer;
import sales.manager.common.sales.report.Record;

public class Order extends HashProvider {
	public Buyer buyer;
	public double netAmount;			// 10
	public String itemID;				// 17
	public String transactionID;		// 18
	public String itemTitle;			// 19
	public int quantity;				// 21
	public double itemSubtotal;		// 22
	public double postageCost; 		// 23
	public double collectedTax;		// 24
	public double fixedFee;			// 27
	public double valueFee;			// 28
	public double highNADFee;			// 29
	public double belowStandardFee;	// 30
	public double internationalFee;	// 31
	public double grossTransaction;	// 32
	public long hash;

	public Order() {}

	public Order(
		Buyer buyer,
		double netAmount,			// 10
		String itemID,				// 17
		String transactionID,		// 18
		String itemTitle,			// 19
		int quantity,				// 21
		double itemSubtotal,		// 22
		double postageCost, 		// 23
		double collectedTax,		// 24
		double fixedFee,			// 27
		double valueFee,			// 28
		double highNADFee,			// 29
		double belowStandardFee,	// 30
		double internationalFee,	// 31
		double grossTransaction,	// 32
		long hash ) {
		this.buyer = buyer;
		this.netAmount = netAmount;					// 10
		this.itemID = itemID;						// 17
		this.transactionID = transactionID;			// 18
		this.itemTitle = itemTitle;					// 19
		this.quantity = quantity;					// 21
		this.itemSubtotal = itemSubtotal;			// 22
		this.postageCost = postageCost; 			// 23
		this.collectedTax = collectedTax;			// 24
		this.fixedFee = fixedFee;					// 27
		this.valueFee = valueFee;					// 28
		this.highNADFee = highNADFee;				// 29
		this.belowStandardFee = belowStandardFee;	// 30
		this.internationalFee = internationalFee;	// 31
		this.grossTransaction = grossTransaction;	// 32
		this.hash = hash;
	}

	public static Order of(Buyer buyer,
		double netAmount,
		String itemID,
		String transactionID,
		String itemTitle,
		int quantity,
		double itemSubtotal,
		double postageCost,
		double collectedTax,
		double fixedFee,
		double valueFee,
		double highNADFee,
		double belowStandardFee,
		double internationalFee,
		double grossTransaction) {
		CRC32 crc = new CRC32();
		crc.update(Util.longToBytes(buyer.getHashCode()));
		crc.update(itemID.getBytes());
		crc.update(transactionID.getBytes());
		crc.update(itemTitle.getBytes());
		crc.update(Util.intToBytes(quantity));
		crc.update(Util.doubleToBytes(itemSubtotal));
		crc.update(Util.doubleToBytes(postageCost));
		crc.update(Util.doubleToBytes(collectedTax));
		crc.update(Util.doubleToBytes(fixedFee));
		crc.update(Util.doubleToBytes(valueFee));
		crc.update(Util.doubleToBytes(highNADFee));
		crc.update(Util.doubleToBytes(belowStandardFee));
		crc.update(Util.doubleToBytes(internationalFee));
		crc.update(Util.doubleToBytes(grossTransaction));
		long hash = crc.getValue();
		return new Order(
			buyer,
			netAmount,
			itemID,
			transactionID,
			itemTitle,
			quantity,
			itemSubtotal,
			postageCost,
			collectedTax,
			fixedFee,
			valueFee,
			highNADFee,
			belowStandardFee,
			internationalFee,
			grossTransaction,
			hash
		);
	}

	public static Order fromCSVArray(String[] sep) {
		var buyer = Buyer.of(
			sep[4],		// username
			sep[5], 	// name
			sep[6],		// city
			sep[7], 	// region
			sep[8], 	// postcode
			sep[9]);	// country

		var obj = Order.of(
			buyer,
			Double.valueOf(sep[10].equals(Record.EMPTY) ? "0" : sep[10]), 	// net amount
			sep[17],					// item ID
			sep[18],					// transaction ID
			sep[19],					// title
			Integer.valueOf(sep[21].equals(Record.EMPTY) ? "0" : sep[21]),	// quantity
			Double.valueOf(sep[22].equals(Record.EMPTY) ? "0" : sep[22]), 	// item subtotal
			Double.valueOf(sep[23].equals(Record.EMPTY) ? "0" : sep[23]), 	// postage cost
			Double.valueOf(sep[24].equals(Record.EMPTY) ? "0" : sep[24]), 	// collected tax
			Double.valueOf(sep[27].equals(Record.EMPTY) ? "0" : sep[27]), 	// fixed fee
			Double.valueOf(sep[28].equals(Record.EMPTY) ? "0" : sep[28]), 	// valueFee
			Double.valueOf(sep[29].equals(Record.EMPTY) ? "0" : sep[29]), 	// high NAD Fee
			Double.valueOf(sep[30].equals(Record.EMPTY) ? "0" : sep[30]), 	// belowStandard fee
			Double.valueOf(sep[31].equals(Record.EMPTY) ? "0" : sep[31]), 	// international fee
			Double.valueOf(sep[32].equals(Record.EMPTY) ? "0" : sep[32])	// gross transaction
		);
		return obj;
	}

	@Override
	@JsonIgnore
	public long getHashCode() {
		return hash;
	}

	@Override
	@JsonIgnore
	public String getItemID() {
		return itemID;
	}
}
