package sales.manager.web.handlers.stock;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;

import htmlflow.DynamicHtml;
import sales.manager.common.Database;
import sales.manager.common.stock.StockItem;
import sales.manager.web.handlers.BaseHandler;
import sales.manager.web.handlers.HTMLEntity;
import sales.manager.web.handlers.templates.TemplateHead.TemplateHeadModel;
import sales.manager.web.handlers.templates.TemplatePage;
import sales.manager.web.handlers.templates.TemplatePage.SelectedPage;
import sales.manager.web.handlers.templates.TemplatePage.TemplatePageModel;
import sales.manager.web.handlers.templates.models.BodyModel;
import sales.manager.web.resource.CSS;

public class StockHandler extends BaseHandler {
	public static final String PATH = "/stock";

	public static DynamicHtml<BodyModel> view = DynamicHtml.view(StockHandler::body);

	static void body(DynamicHtml<BodyModel> view, BodyModel model) {
		final var stock = Database.stock.getItems();
		view
			.div()
				.a().attrClass(CSS.BUTTON).attrHref(AddStockHandler.PATH).text("Add New Stock Item").__()
				.table().dynamic(table -> {
					table
						.tr()
							.th().text("ID").__()
							.th().text("Name").__()
							.th().text("Supplier").__()
							.th().text("Price").__()
							.th().text("Import Fee").__()
							.th().text("Purchase Date").__()
							.th().text("Received Date").__()
							.th().text("Item IDs").__()
							.th().text("Action").__()
						.__();
					for (Integer key: stock.keySet()) {
						StockItem item = stock.get(key);
						var last = table
							.tr()
								.td().text(key).__()
								.td().text(item.getItemNameStr()).__()
								.td().text(item.getSupplierStr()).__()
								.td().text(HTMLEntity.POUND + item.getPriceStr()).__()
								.td().text(HTMLEntity.POUND + item.getImportFeeStr()).__()
								.td().text(item.getPurchaseDateStr()).__()
								.td().text(item.getReceivedDateStr()).__()
								.td();
								for (String i: item.getItemIDs()) {
									last = last.a().text(i).__().br().__();
								}
								last.__().td()
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
							.__();

					}
				})
			.__(); // div

	}

	@Override
	public void requestHandle(HttpExchange he) throws IOException {
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
