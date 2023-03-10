package sales.manager.common.sales.report;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.zip.CRC32;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sales.manager.common.Util;
import sales.manager.common.sales.report.event.Event;
import sales.manager.common.sales.report.event.HashProvider;
import sales.manager.common.sales.report.event.Order;
import sales.manager.common.sales.report.event.PostageLabel;
import sales.manager.common.sales.report.event.Refund;

public class Record {
	private LocalDate date;
	private Event eventType;
	private String orderNumber;
	private HashProvider data;
	private long hash;

	private static DateTimeFormatter dateParser = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
	public static final String EMPTY = "--";

	public Record() {}

	public Record(LocalDate date, Event eventType, String orderNumber, HashProvider data) throws Exception {
		this.setDate(date);
		this.setEventType(eventType);
		this.setOrderNumber(orderNumber);
		this.setData(data);
		CRC32 crc = new CRC32();
		HashProvider hp = data;
		crc.update(Util.longToBytes(date.getLong(ChronoField.EPOCH_DAY)));
		crc.update(eventType.name().getBytes());
		crc.update(orderNumber.getBytes());
		crc.update(Util.longToBytes(hp.getHashCode()));
		hash = crc.getValue();
	}

	public static Record fromCSVRow(String csvRow) throws Exception {
		String[] sep = csvRow.split("\",\"");
		LocalDate date = LocalDate.parse(sep[0].substring(1), dateParser);
		Event event = Event.valueOf(sep[1].replace(" ", ""));
		HashProvider data;

		switch (event) {
			case Order:
				data = Order.fromCSVArray(sep);
				break;

			case Postagelabel:
				data = PostageLabel.fromCSVArray(sep);
				break;

			case Refund:
				data = Refund.fromCSVArray(sep);
				break;

			case Transfer:
			default: return null;
		}

		return new Record(date, event, sep[2], data);
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Event getEventType() {
		return eventType;
	}

	public void setEventType(Event eventType) {
		this.eventType = eventType;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public HashProvider getData() {
		return data;
	}

	public void setData(HashProvider data) {
		this.data = data;
	}

	public long getHash() {
		return hash;
	}

	@JsonIgnore
	public String getItemID() {
		HashProvider provider = data;
		return provider.getItemID();
	}
}
