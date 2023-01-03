package sales.manager.web.handlers.stock;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;

import htmlflow.DynamicHtml;
import sales.manager.common.stock.StockDatabase;
import sales.manager.web.handlers.BaseHandler;
import sales.manager.web.handlers.templates.TemplateHead.TemplateHeadModel;
import sales.manager.web.handlers.templates.TemplatePage;
import sales.manager.web.handlers.templates.TemplatePage.SelectedPage;
import sales.manager.web.handlers.templates.TemplatePage.TemplatePageModel;
import sales.manager.web.handlers.templates.models.BodyModel;

public class StockHandler extends BaseHandler {
	public static final String PATH = "/stock";

	public static DynamicHtml<BodyModel> view = DynamicHtml.view(StockHandler::body);

	static void body(DynamicHtml<BodyModel> view, BodyModel model) {
		StockDatabase sdb = new StockDatabase();
		String message = null;
		try {
			//sdb.writeDatabase();
			sdb.readDatabase();

		} catch (FileNotFoundException e) {
			message = "Database does not exist";
			try {
				sdb.writeDatabase();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (Exception e) {
			message = e.getMessage();
		}


		final String showMessage = message;

		view
			.div()
				.p().text("StockHandler").__()
				.p().dynamic(p -> p.text("Message: " + showMessage)).__()
				.a().attrHref(AddStockHandler.PATH).text("Add New Stock Item").__()
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
