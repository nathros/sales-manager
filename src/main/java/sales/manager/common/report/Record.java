package sales.manager.common.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {
	private Date date;
	private Event eventType;
	private String orderNumber;
	private Object data;

	private static SimpleDateFormat dateParser = new SimpleDateFormat("dd MMM yy");
	public static final String EMPTY = "--";

	public Record(Date date, Event eventType, String orderNumber, Object data) {
		this.setDate(date);
		this.setEventType(eventType);
		this.setOrderNumber(orderNumber);
		this.setData(data);
	}

	public static Record fromCSVRow(String csvRow) throws ParseException {
		csvRow = csvRow.replace("\"", "");
		String[] sep = csvRow.split(",");
		Date date = dateParser.parse(sep[0]);
		Event event = Event.valueOf(sep[1].replace(" ", ""));
		switch (event) {
		case Order:
			var data = Order.fromCSVArray(sep);
			return new Record(date, event, sep[3], data);

		default:
			return new Record(date, event, sep[3], null);
		}
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
