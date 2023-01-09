package sales.manager.web.handlers.sales;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;

import htmlflow.DynamicHtml;
import sales.manager.common.Database;
import sales.manager.common.sales.report.Record;
import sales.manager.common.sales.report.TransactionGroup;
import sales.manager.common.sales.report.event.Order;
import sales.manager.common.sales.report.event.PostageLabel;
import sales.manager.web.handlers.BaseHandler;
import sales.manager.web.handlers.templates.TemplateHead.TemplateHeadModel;
import sales.manager.web.handlers.templates.TemplatePage;
import sales.manager.web.handlers.templates.TemplatePage.SelectedPage;
import sales.manager.web.handlers.templates.TemplatePage.TemplatePageModel;
import sales.manager.web.handlers.templates.models.BodyModel;

public class SalesHandler extends BaseHandler {
	public static final String PATH = "/sales";

	public static DynamicHtml<BodyModel> view = DynamicHtml.view(SalesHandler::body);

	static void body(DynamicHtml<BodyModel> view, BodyModel model) {
		Database.sales.readDatabase();
		final HashMap<String, TransactionGroup> display = Database.sales.getSales();
		view
			.div()
				.table().dynamic(table -> {
					table
						.tr()
							.th().text("Item ID").__()
							.th().text("Transactions").__()
							.th().text("Total").__()
						.__();
					for (String key: display.keySet()) {
						TransactionGroup tg = display.get(key);
						var gross = new ArrayList<Double>();
						table
							.tr()
								.td().text(key).__()
								.td()
									.table().of(transactionsTable -> {
										transactionsTable
										.tr()
											.th().text("Date").__()
											.th().text("Order Number").__()
											.th().text("Event").__()
											.th().text("Gross").__()
										.__()
										.of(next -> {
											gross.clear();
											for (Record rec: tg.getTransactions()) {
												next.tr().of(tr -> {
													tr.td().text(rec.getDate()).__();
													tr.td().text(rec.getOrderNumber()).__();
													tr.td().text(rec.getEventType().getPrettyStr()).__();
													switch (rec.getEventType()) {
													case Order: {
														Order data = (Order) rec.getData();
														tr.td().text(data.grossTransaction()).__();
														gross.add(data.grossTransaction());
														break;
													}
													case Postagelabel: {
														PostageLabel data = (PostageLabel) rec.getData();
														tr.td().text(data.grossTransaction()).__();
														gross.add(data.grossTransaction());
													}
													default:
														break;
													}
												}).__();
											}
										});
									}).__() // table
								.__() // td
								.td().of(td -> {
									Double calcGross = 0.0;
									for (Double i: gross) calcGross += i;
									td.text(calcGross);
								}).__()
							.__();
					}
				})
			.__(); // div

	}

	@Override
	public void requestHandle(HttpExchange he) throws Exception {
		try {
			TemplateHeadModel thm = TemplateHeadModel.of("Files");
			TemplatePageModel tepm = TemplatePageModel.of(view, thm, SelectedPage.Sales, BodyModel.of(he, null));
			String response = TemplatePage.view.render(tepm);

			he.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (Exception e) {
			view = DynamicHtml.view(SalesHandler::body);
			throw e;
		}
	}
}
