package sales.manager.web.handlers.stock;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.xmlet.htmlapifaster.EnumTypeButtonType;
import org.xmlet.htmlapifaster.EnumTypeInputType;

import com.sun.net.httpserver.HttpExchange;

import htmlflow.DynamicHtml;
import sales.manager.web.handlers.BaseHandler;
import sales.manager.web.handlers.templates.TemplateHead.TemplateHeadModel;
import sales.manager.web.handlers.templates.TemplatePage;
import sales.manager.web.handlers.templates.TemplatePage.SelectedPage;
import sales.manager.web.handlers.templates.TemplatePage.TemplatePageModel;
import sales.manager.web.handlers.templates.models.BodyModel;

public class AddStockHandler extends BaseHandler {
	public static final String PATH = "/stock/add";
	private final static String ID = "id";
	private final static String NAME = "name";
	private final static String SUPPLIER = "supplier";
	private final static String PRICE = "price";
	private final static String TAX = "tax";
	private final static String DATE = "date";
	private final static String RX = "rx";

	public static DynamicHtml<BodyModel> view = DynamicHtml.view(AddStockHandler::body);

	static void body(DynamicHtml<BodyModel> view, BodyModel model) {
		final String id = model.getQueryNoNull(ID);
		final String name = model.getQueryNoNull(NAME);
		final String supplier = model.getQueryNoNull(SUPPLIER);
		final String price = model.getQueryNoNull(PRICE);
		final String tax = model.getQueryNoNull(TAX);
		final String date = model.getQueryNoNull(DATE);
		final String rx = model.getQueryNoNull(RX);

		view
			.div()
				.p().text("AddStockHandler").__()
				.div().dynamic(div -> {
					div.p().text("Current Database ID").__();
					div.form()
						.label().attrStyle("display:inline-block;width:150px").text("ID:").__()
						.input().attrType(EnumTypeInputType.TEXT).attrName(ID).attrValue(id).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text("Name:").__()
						.input().attrType(EnumTypeInputType.TEXT).attrName(NAME).attrValue(name).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text("Supplier:").__()
						.input().attrType(EnumTypeInputType.TEXT).attrName(SUPPLIER).attrValue(supplier).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text("Cost: (£)").__()
						.input().attrType(EnumTypeInputType.NUMBER).attrName(PRICE).attrValue(price).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text("Tax: (£)").__()
						.input().attrType(EnumTypeInputType.NUMBER).attrName(TAX).attrValue(tax).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text("Order Date:").__()
						.input().attrType(EnumTypeInputType.DATE).attrName(DATE).attrValue(date).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text("Received Date:").__()
						.input().attrType(EnumTypeInputType.DATE).attrName(RX).attrValue(rx).__()
						.br().__()

						.button().attrType(EnumTypeButtonType.SUBMIT).text("Add").__()
					.__();
				})
			.__(); // div

	}

	@Override
	public void requestHandle(HttpExchange he) throws IOException {
		try {
			TemplateHeadModel thm = TemplateHeadModel.of("Add New Stock Item");
			TemplatePageModel tepm = TemplatePageModel.of(view, thm, SelectedPage.Stock, BodyModel.of(he, null));
			String response = TemplatePage.view.render(tepm);

			he.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (Exception e) {
			view = DynamicHtml.view(AddStockHandler::body);
			throw e;
		}
	}
}
