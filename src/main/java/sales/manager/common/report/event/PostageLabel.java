package sales.manager.common.report.event;

import java.util.zip.CRC32;

import sales.manager.common.Util;
import sales.manager.common.report.Record;

public record PostageLabel(
	String itemID,				// 17
	double grossTransaction,	// 32
	long hash) implements HashProvider {

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
	public long getHashCode() {
		return hash;
	}
}
