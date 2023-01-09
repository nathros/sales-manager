package sales.manager.web.handlers;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;

import htmlflow.DynamicHtml;
import sales.manager.web.handlers.templates.TemplateHead.TemplateHeadModel;
import sales.manager.web.handlers.templates.TemplatePage;
import sales.manager.web.handlers.templates.TemplatePage.SelectedPage;
import sales.manager.web.handlers.templates.TemplatePage.TemplatePageModel;
import sales.manager.web.handlers.templates.models.BodyModel;

public class RootHandler extends BaseHandler {
	public static final String PATH = "/";

	public static DynamicHtml<BodyModel> view = DynamicHtml.view(RootHandler::body);

	static void body(DynamicHtml<BodyModel> view, BodyModel model) {
		view
			.div()
			.__(); // div
	}

	@Override
	public void requestHandle(HttpExchange he) throws Exception {
		TemplateHeadModel thm = TemplateHeadModel.of("Root");
		TemplatePageModel tepm = TemplatePageModel.of(view, thm, SelectedPage.Admin, BodyModel.of(he, null));
		String response = TemplatePage.view.render(tepm);

		he.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
		OutputStream os = he.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
