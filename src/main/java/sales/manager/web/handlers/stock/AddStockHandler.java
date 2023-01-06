package sales.manager.web.handlers.stock;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.function.Supplier;

import org.xmlet.htmlapifaster.EnumTypeButtonType;
import org.xmlet.htmlapifaster.EnumTypeInputType;

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

public class AddStockHandler extends BaseHandler {
	public static final String PATH = "/stock/add";
	public final static String EDIT = "edit";
	public final static String EDIT_EDIT = "edit";
	public final static String EDIT_DEL = "del";

	public final static String ID = "id";
	private final static String NAME = "name";
	private final static String SUPPLIER = "supplier";
	private final static String PRICE = "price";
	private final static String TAX = "tax";
	private final static String DATE = "date";
	private final static String RX = "rx";
	private final static String ITEMIDS = "itemids";

	private final static String ADD = "add";
	private final static String ADD_ADD = "add";
	private final static String ADD_UPDATE = "update";

	public static DynamicHtml<BodyModel> view = DynamicHtml.view(AddStockHandler::body);

	static void body(DynamicHtml<BodyModel> view, BodyModel model) {
		final String idQ = model.getQueryNoNull(ID);
		final String nameQ = model.getQueryNoNull(NAME);
		final String supplierQ = model.getQueryNoNull(SUPPLIER);
		final String priceQ = model.getQueryNoNull(PRICE);
		final String taxQ = model.getQueryNoNull(TAX);
		final String dateQ = model.getQueryNoNull(DATE);
		final String rxQ = model.getQueryNoNull(RX);
		final String addQ = model.getQueryNoNull(ADD);
		final String editQ = model.getQueryNoNull(EDIT);
		final String itemIDsQ = model.getQueryNoNull(ITEMIDS).trim().replace("\t", "");

		String tmpErrorMsg = null;
		String tmpSuccessMsg = null;
		final StockItem editItem = Database.stock.findByID(idQ);
		if (addQ.equals(ADD_ADD)) {
			try {
				var stock = new StockItem(nameQ, supplierQ, priceQ, taxQ, dateQ, rxQ, itemIDsQ);
				Database.stock.addStock(idQ, stock);
				tmpSuccessMsg = "Add success";
			} catch (Exception e) {
				tmpErrorMsg = e.getMessage();
			}
		} else if (addQ.equals(ADD_UPDATE)) {
			try {
				var item = Database.stock.findByID(idQ);
				var stock = new StockItem(nameQ, supplierQ, priceQ, taxQ, dateQ, rxQ, itemIDsQ);
				item.update(stock);
				Database.stock.writeDatabase();
				tmpSuccessMsg = "Update success";
			} catch (Exception e) {
				tmpErrorMsg = e.getMessage();
			}


		} else if (!editQ.equals("")) {
			if (editItem == null) tmpErrorMsg = "Unable to find with ID " + idQ;
			else {
				if (editQ.equals(EDIT_DEL)) {
					if (Database.stock.remove(idQ) == false) tmpErrorMsg = idQ + " is not in database";
					else {
						tmpSuccessMsg = idQ + " has been removed from database";
						try {
							Database.stock.writeDatabase();
						} catch (Exception e) {
							tmpErrorMsg = e.getMessage();
						}
					}
				}
			}
		}

		Supplier<String> displayName = () -> {
			if (editItem != null) {
				return editItem.itemName;
		    }
		    return nameQ;
		};
		Supplier<String> displaySupplier = () -> {
			if (editItem != null) {
				return editItem.getSupplierStr();
		    }
		    return supplierQ;
		};
		Supplier<String> displayPrice = () -> {
			if (editItem != null) {
				return editItem.getPriceStr();
		    }
		    return priceQ;
		};
		Supplier<String> displayTax = () -> {
			if (editItem != null) {
				return editItem.getImportFeeStr();
			}
		    return taxQ;
		};
		Supplier<String> displayDate = () -> {
			if (editItem != null) {
				return editItem.getPurchaseDateStr();
		    }
		    return dateQ;
		};
		Supplier<String> displayRX = () -> {
			if (editItem != null) {
				return editItem.getReceivedDateStr();
		    }
		    return rxQ;
		};
		Supplier<String> displayItemIDs = () -> {
			if (editItem != null) {
				return editItem.getItemIDsStr();
		    }
		    return itemIDsQ;
		};

		final String error = tmpErrorMsg == null ? "" : tmpErrorMsg;
		final String message = tmpSuccessMsg;
		final boolean isError = !error.equals("");
		final int dbID = Database.stock.getStockIDCount();

		final String nameD = displayName.get();
		final String supplierD = displaySupplier.get();
		final String priceD = displayPrice.get();
		final String taxD = displayTax.get();
		final String dateD = displayDate.get();
		final String rxD = displayRX.get();
		final String itemIDsD = displayItemIDs.get();
		view
			.div()
				.p().text("AddStockHandler").__()
				.div().dynamic(div -> {
					div.p().text("Current Database ID: " + dbID).__();
					if (isError) div.p().attrStyle("background-color:red").text("Message: " + error).__();
					if (message != null) div.p().attrStyle("background-color:lightgreen").text("Message: " + message).__();
					final String currentID = idQ.equals("") ? String.valueOf(dbID) : idQ;
					final String addValue = (editItem == null) ? ADD_ADD : ADD_UPDATE;
					div.form()
						.label().attrStyle("display:inline-block;width:150px").text("ID:").__()
						.input().attrType(EnumTypeInputType.TEXT).attrName(ID).attrValue(currentID).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text("Name:").__()
						.input().attrStyle("width:250px").attrType(EnumTypeInputType.TEXT).attrName(NAME).attrValue(nameD).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text("Supplier:").__()
						.input().attrType(EnumTypeInputType.TEXT).attrName(SUPPLIER).attrValue(supplierD).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text(String.format("Cost (%s):", HTMLEntity.POUND)).__()
						.input().attrType(EnumTypeInputType.NUMBER).attrStep("0.01").attrName(PRICE).attrValue(priceD).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text(String.format("Tax (%s):", HTMLEntity.POUND)).__()
						.input().attrType(EnumTypeInputType.NUMBER).attrStep("0.01").attrName(TAX).attrValue(taxD).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text("Order Date:").__()
						.input().attrType(EnumTypeInputType.DATE).attrName(DATE).attrValue(dateD).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text("Received Date:").__()
						.input().attrType(EnumTypeInputType.DATE).attrName(RX).attrValue(rxD).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text("Sale Item IDs:").__()
						.textarea().attrStyle("height:200px").attrName(ITEMIDS).text(itemIDsD).__()
						.br().__()

						.input().attrType(EnumTypeInputType.HIDDEN).attrName(ADD).attrValue(addValue).__()
						.button().attrClass(CSS.BUTTON).attrStyle("float:right").attrType(EnumTypeButtonType.SUBMIT).text(editItem == null ? "Add" : "Update").__()
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
