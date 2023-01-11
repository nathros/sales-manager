package sales.manager.web.handlers.stock;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import org.xmlet.htmlapifaster.EnumTypeInputType;

import com.sun.net.httpserver.HttpExchange;

import htmlflow.DynamicHtml;
import sales.manager.common.Database;
import sales.manager.common.stock.StockItem;
import sales.manager.web.handlers.BaseHandler;
import sales.manager.web.handlers.HTMLEntity;
import sales.manager.web.handlers.sales.SalesHandler;
import sales.manager.web.handlers.templates.TemplatePage;
import sales.manager.web.handlers.templates.TemplatePage.SelectedPage;
import sales.manager.web.handlers.templates.TemplatePage.TemplatePageModel;
import sales.manager.web.handlers.templates.models.BodyModel;
import sales.manager.web.handlers.templates.models.TemplateHeadModel;
import sales.manager.web.resource.CSS;

public class StockHandler extends BaseHandler {
	public static final String PATH = "/stock";

	public static final String FILTER_ONLY_IN_STOCK = "instock";
	public static final String FILTER_START_PURCHASED = "spurchase";

	public static DynamicHtml<BodyModel> view = DynamicHtml.view(StockHandler::body);

	static void body(DynamicHtml<BodyModel> view, BodyModel model) {
		final HashMap<Integer, StockItem> stock = new HashMap<Integer, StockItem>();
		final HashMap<Integer, StockItem> search = Database.stock.getItems();

		final String inStock = model.getQuery(FILTER_ONLY_IN_STOCK);
		if (inStock != null) {
			for (Integer i: search.keySet()) {
				var item = search.get(i);
				if (!item.sold) stock.put(i, item);
			}
		}
		stock.putAll(search);

		view
			.div()
				.a().attrClass(CSS.BUTTON).attrHref(AddStockHandler.PATH).text("Add New Stock Item").__()
				.a().attrClass(CSS.BUTTON)
					.attrHref(AddStockHandler.PATH + "?" + FILTER_ONLY_IN_STOCK + "=" + BodyModel.QUERY_ON).text("In Stock only").__()
				.table().dynamic(table -> {
					table
						.tr()
							.th().text("ID").__()
							.th().text("Sold").__()
							.th().text("Purchase Date").__()
							.th().text("Name").__()
							.th().text("Supplier").__()
							.th().text("Price").__()
							.th().text("Import Fee").__()
							.th().text("Received Date").__()
							.th().text("Postage Cost").__()
							.th().text("Action").__()
						.__()
						.tr().of(tr -> {
							for (Integer key: stock.keySet()) {
								StockItem item = stock.get(key);
									tr
										.td().text(key).__()
										.td().of(td -> {
											if (item.sold) td.input().attrType(EnumTypeInputType.CHECKBOX).attrChecked(item.sold).attrDisabled(true).__();
											else td.input().attrType(EnumTypeInputType.CHECKBOX).attrDisabled(true).__();
										}).__()
										.td().text(item.getPurchaseDateStr()).__()
										.td().text(item.getItemNameStr()).__()
										.td().text(item.getSupplierStr()).__()
										.td().text(HTMLEntity.POUND + item.getPriceStr()).__()
										.td().text(HTMLEntity.POUND + item.getImportFeeStr()).__()
										.td().text(item.getReceivedDateStr()).__()
										.td().of(td -> {
											for (String i: item.getitemIDsPostageMap().keySet()) {
												var cost = item.getitemIDsPostageMap().get(i);
												td.a().attrHref(SalesHandler.PATH + "?" + SalesHandler.FILTER_ID + "=" + i)
													.text(i + " [" + HTMLEntity.POUND + cost + "]").__().br().__();
											}
										}).__()
										.td()
											.a().attrClass(CSS.BUTTON).attrHref(AddStockHandler.PATH + "?" +
												AddStockHandler.EDIT + "=" + AddStockHandler.EDIT_EDIT + "&" +
												AddStockHandler.ID + "=" + key)
												.text("Edit")
											.__()
											.a().attrClass(CSS.BUTTON + CSS.BACKGROUND_CAUTION).attrHref(AddStockHandler.PATH + "?" +
												AddStockHandler.EDIT + "=" + AddStockHandler.EDIT_DEL + "&" +
												AddStockHandler.ID + "=" + key)
												.text("Delete")
											.__()
										.__() // td
									.__(); // tr
							}
						}).__(); // tr
				})
			.__(); // div

	}

	@Override
	public void requestHandle(HttpExchange he) throws Exception {
		try {
			TemplateHeadModel thm = TemplateHeadModel.of("Files");
			TemplatePageModel tepm = TemplatePageModel.of(view, thm, SelectedPage.Stock, BodyModel.of(he, null));
			String response = TemplatePage.view.render(tepm);
			he.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (Exception e) {
			view = DynamicHtml.view(StockHandler::body);
			throw e;
		}
	}
}
