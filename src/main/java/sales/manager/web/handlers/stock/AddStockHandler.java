package sales.manager.web.handlers.stock;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.xmlet.htmlapifaster.EnumTypeButtonType;
import org.xmlet.htmlapifaster.EnumTypeInputType;

import com.sun.net.httpserver.HttpExchange;

import htmlflow.DynamicHtml;
import sales.manager.common.Database;
import sales.manager.common.stock.StockItem;
import sales.manager.web.handlers.BaseHandler;
import sales.manager.web.handlers.HTMLEntity;
import sales.manager.web.handlers.templates.TemplatePage;
import sales.manager.web.handlers.templates.TemplatePage.SelectedPage;
import sales.manager.web.handlers.templates.TemplatePage.TemplatePageModel;
import sales.manager.web.handlers.templates.models.BodyModel;
import sales.manager.web.handlers.templates.models.TemplateHeadModel;
import sales.manager.web.resource.Asset;
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
	private final static String POSTAGE = "postage";
	private final static String SOLD = "sold";

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
		final List<String> itemIDsQ = model.getQueryArray(ITEMIDS);
		final List<String> postageQ = model.getQueryArray(POSTAGE);
		final String soldQ = model.getQueryNoNull(SOLD);

		String tmpErrorMsg = null;
		String tmpSuccessMsg = null;
		final StockItem editItem = Database.stock.findByID(idQ);
		if (addQ.equals(ADD_ADD)) {
			try {
				var stock = new StockItem(nameQ, supplierQ, priceQ, taxQ, dateQ, rxQ, itemIDsQ, postageQ, soldQ);
				Database.stock.addStock(idQ, stock);
				tmpSuccessMsg = "Add success";
			} catch (Exception e) {
				tmpErrorMsg = e.getMessage();
			}
		} else if (addQ.equals(ADD_UPDATE)) {
			try {
				var item = Database.stock.findByID(idQ);
				var stock = new StockItem(nameQ, supplierQ, priceQ, taxQ, dateQ, rxQ, itemIDsQ, postageQ, soldQ);
				if (item == null) {
					Database.stock.addStock(idQ, stock);
				} else {
					item.update(stock);
					Database.stock.writeDatabase();
				}
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
		Supplier<Boolean> displaySolds = () -> {
			if (editItem != null) {
				return editItem.sold;
		    }
		    return soldQ.equals(BodyModel.QUERY_ON);
		};

		Supplier<List<String>> displayItemIDs = () -> {
			if (editItem != null) {
				List<String> list = new ArrayList<String>();
				list.addAll(editItem.getItemIDsPostageMap().keySet());
				return list;
		    }
		    return itemIDsQ;
		};

		Supplier<List<String>> displayPostages = () -> {
			if (editItem != null) {
				List<Double> tmp = new ArrayList<Double>();
				tmp.addAll(editItem.getItemIDsPostageMap().values());
				List<String> list = new ArrayList<String>();
				for (Double cost: tmp) {
					list.add(String.format("%.2f", cost));
				}
				return list;
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
		final Boolean soldD = displaySolds.get();
		final List<String> displayItemIDsD = displayItemIDs.get();
		final List<String> postageD = displayPostages.get();
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

						.label().attrStyle("display:inline-block;width:150px").text("Sold:").__()
						.input().of(input -> {
							if (soldD) input.attrType(EnumTypeInputType.CHECKBOX).attrName(SOLD).attrChecked(true).__();
							else input.attrType(EnumTypeInputType.CHECKBOX).attrName(SOLD).__();
						}).__()
						.br().__()

						.label().attrStyle("display:inline-block;width:150px").text("Sale Item IDs:").__()
						.div().attrStyle("display:inline-block").attrId("list-container")
						.of(o -> {
							o.div().attrStyle("padding-bottom:2px")
								.button().attrStyle("float:right").attrType(EnumTypeButtonType.BUTTON).attrOnclick("AddSaleRow()").text("+").__()
								.label().attrStyle("float:left").text("Item ID").__()
								.label().attrStyle("float:right;padding-right:5px").text("Postage Cost").__()
							.__()
							.br().__();

							if (displayItemIDsD != null && displayItemIDsD.size() > 0) {
								for (int count = 0; count < displayItemIDsD.size(); count++) {
									String id = displayItemIDsD.get(count);
									String cost = postageD.get(count);
									o.div().attrId("list-" + count);
										o.input().attrType(EnumTypeInputType.TEXT).attrName(ITEMIDS).attrValue(id).__();
										o.input().attrType(EnumTypeInputType.TEXT).attrName(POSTAGE).attrValue(String.valueOf(cost)).__();
										if (count == 0) o.button().attrType(EnumTypeButtonType.BUTTON).attrOnclick("removeSaleRow(this)").attrDisabled(true).text("X").__();
										else o.button().attrType(EnumTypeButtonType.BUTTON).attrOnclick("removeSaleRow(this)").text("X").__();
										o.br().__();
									o.__();
								}
							} else {
								o.div().attrId("list-0");
									o.input().attrType(EnumTypeInputType.TEXT).attrName(ITEMIDS).attrValue("").__();
									o.input().attrType(EnumTypeInputType.TEXT).attrName(POSTAGE).attrValue("").__();
									//o.button().attrType(EnumTypeButtonType.BUTTON).attrOnclick("AddSaleRow()").text("+").__();
									o.button().attrType(EnumTypeButtonType.BUTTON).attrOnclick("removeSaleRow(this)").attrDisabled(true).text("X").__();
								o.__();
							}
						}).__()
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
			thm.AddScript(Asset.JS_ADD_STOCK);
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
