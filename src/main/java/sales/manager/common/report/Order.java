package sales.manager.common.report;

public record Order(
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
	double grossTransaction) {

	public static Order fromCSVArray(String[] sep) {
		var buyer = new Buyer(
			sep[4],		// username
			sep[5], 	// name
			sep[6],		// city
			sep[7], 	// region
			sep[8], 	// postcode
			sep[9]);	// country

		var order = new Order(
			buyer,
			Double.valueOf(sep[10].equals(Record.EMPTY) ? sep[10] : "0"), 	// net amount
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
		return order;
	}
}
