package sales.manager.web.handlers.sales;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;

import htmlflow.DynamicHtml;
import sales.manager.common.Database;
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

		view
			.div()
				.p().text("SalesHandler").__()
			.__(); // div

	}

	@Override
	public void requestHandle(HttpExchange he) throws IOException {
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
