package sales.manager.web.handlers.sandpit;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;

import htmlflow.DynamicHtml;
import sales.manager.web.handlers.BaseHandler;
import sales.manager.web.handlers.templates.TemplatePage;
import sales.manager.web.handlers.templates.TemplatePage.SelectedPage;
import sales.manager.web.handlers.templates.TemplatePage.TemplatePageModel;
import sales.manager.web.handlers.templates.models.BodyModel;
import sales.manager.web.handlers.templates.models.TemplateHeadModel;

public class ParseTestHandler extends BaseHandler {
	public static final String PATH = "/sandpit/parsetest";
	public static DynamicHtml<BodyModel> view = DynamicHtml.view(ParseTestHandler::body);

	static void body(DynamicHtml<BodyModel> view, BodyModel model) {
		view
			.div().attrStyle("text-align:center")
				.p().a().attrHref("P").text("test").__().__()
			.__(); //  div
	}

	@Override
	public void requestHandle(HttpExchange he) throws IOException {
		try {
			TemplateHeadModel thm = TemplateHeadModel.of("ParseTest");
			TemplatePageModel tepm = TemplatePageModel.of(view, thm, SelectedPage.Sandpit, BodyModel.of(he, null));
			String response = TemplatePage.view.render(tepm);

			he.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (Exception e) {
			view = DynamicHtml.view(ParseTestHandler::body);
			throw e;
		}
	}
}
