package sales.manager.common.sales.report.event;

import java.util.zip.CRC32;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sales.manager.common.Util;
import sales.manager.common.sales.report.Record;

public class PostageLabel extends HashProvider {
	public String itemID;				// 17
	public double grossTransaction;		// 32
	public long hash;

	public PostageLabel() {}

	public PostageLabel(
		String itemID,				// 17
		double grossTransaction,	// 32
		long hash) {
		this.itemID = itemID;						// 17
		this.grossTransaction = grossTransaction;	// 32
		this.hash = hash;
	}

	public static PostageLabel of(
		String itemID,
		double grossTransaction) {
		CRC32 crc = new CRC32();
		crc.update(itemID.getBytes());
		crc.update(Util.doubleToBytes(grossTransaction));
		long hash = crc.getValue();
		return new PostageLabel(
			itemID,
			grossTransaction,
			hash
		);
	}

	public static PostageLabel fromCSVArray(String[] sep) {
		var obj = PostageLabel.of(
			sep[17],					// item ID
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
