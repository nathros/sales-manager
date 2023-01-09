package sales.manager.common.sales.report.event;

public enum Event {
	Order,
	Transfer,
	Refund,
	Postagelabel,
	Paymentdispute,
	Hold,
	Claim,
	Payout;

	public String getPrettyStr() {
		switch (this) {
		case Postagelabel: return "Postage Label";
		case Paymentdispute: return "Payment Dispute";
		default: return this.name();
		}
	}
}
