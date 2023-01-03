package sales.manager.common.report.event;

import java.util.zip.CRC32;

import sales.manager.common.Util;
import sales.manager.common.report.Buyer;
import sales.manager.common.report.Record;

public record Refund(
	Buyer buyer,
	double netAmount,			// 10
	//String itemID,				// 17
	//String transactionID,		// 18
	String itemTitle,			// 19
	//int quantity,				// 21
	//double itemSubtotal,		// 22
	//double postageCost, 		// 23
	double collectedTax,		// 24
	double fixedFee,			// 27
	double valueFee,			// 28
	double highNADFee,			// 29
	double belowStandardFee,	// 30
	double internationalFee,	// 31
	double grossTransaction,	// 32
	long hash) implements HashProvider {

	public static Refund of(Buyer buyer,
		double netAmount,
		String itemTitle,
		double collectedTax,
		double fixedFee,
		double valueFee,
		double highNADFee,
		double belowStandardFee,
		double internationalFee,
		double grossTransaction) {
		CRC32 crc = new CRC32();
		crc.update(Util.longToBytes(buyer.hash()));
		crc.update(itemTitle.getBytes());
		crc.update(Util.doubleToBytes(collectedTax));
		crc.update(Util.doubleToBytes(fixedFee));
		crc.update(Util.doubleToBytes(valueFee));
		crc.update(Util.doubleToBytes(highNADFee));
		crc.update(Util.doubleToBytes(belowStandardFee));
		crc.update(Util.doubleToBytes(internationalFee));
		crc.update(Util.doubleToBytes(grossTransaction));
		long hash = crc.getValue();
		return new Refund(
			buyer,
			netAmount,
			itemTitle,
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

	public static Refund fromCSVArray(String[] sep) {
		var buyer = Buyer.of(
			sep[4],		// username
			sep[5], 	// name
			sep[6],		// city
			sep[7], 	// region
			sep[8], 	// postcode
			sep[9]);	// country

		var obj = Refund.of(
			buyer,
			Double.valueOf(sep[10].equals(Record.EMPTY) ? sep[10] : "0"), 	// net amount
			sep[19],					// title
			Double.valueOf(sep[24].equals(Record.EMPTY) ? "0" : sep[25]), 	// collected tax
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
	public long getHashCode() {
		return hash;
	}
}
