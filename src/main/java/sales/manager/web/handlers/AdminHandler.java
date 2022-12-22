package sales.manager.web.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.xmlet.htmlapifaster.EnumTypeButtonType;
import org.xmlet.htmlapifaster.EnumTypeInputType;

import com.sun.net.httpserver.HttpExchange;

import htmlflow.DynamicHtml;
import sales.manager.web.handlers.templates.TemplateHead.TemplateHeadModel;
import sales.manager.web.handlers.templates.TemplatePage;
import sales.manager.web.handlers.templates.TemplatePage.SelectedPage;
import sales.manager.web.handlers.templates.TemplatePage.TemplatePageModel;
import sales.manager.web.handlers.templates.models.BodyModel;

public class AdminHandler extends BaseHandler {
	public static final String PATH = "/admin";
	public static DynamicHtml<BodyModel> view = DynamicHtml.view(AdminHandler::body);

	private final static String LIST_DRIVES = "list-drives";

	private final static String CHANGE_OPTIONS = "change-options";
	private final static String ENABLE_LOG_REQUESTS = "enable-requests";
	private final static String ENABLE_LOG_EXTERNAL_PROCESS = "enable-ext-log";

	static void body(DynamicHtml<BodyModel> view, BodyModel model) {
		final boolean showDrives = model.getQueryNoNull(LIST_DRIVES).equals(BodyModel.QUERY_ON);

		if (showDrives) {
		}

		final String writeOptions = model.getQueryNoNull(CHANGE_OPTIONS);
		if (writeOptions.equals(BodyModel.QUERY_ON)) {
		}

		view
		.div().dynamic(div -> {
			div
				.form()
					.input().attrType(EnumTypeInputType.HIDDEN).attrName(LIST_DRIVES).attrValue(BodyModel.QUERY_ON).__()
					.button().attrType(EnumTypeButtonType.SUBMIT).text("list Drives").__()
				.__();
			div
				.form()
					.button().attrType(EnumTypeButtonType.SUBMIT).text("Refresh").__()
				.__();

			div.hr().__();
			div
				.form()
					.fieldset()
						.legend().text("Options").__()
						.input().attrType(EnumTypeInputType.HIDDEN).attrName(CHANGE_OPTIONS).attrValue(BodyModel.QUERY_ON).__()

						.label().attrFor(ENABLE_LOG_REQUESTS).text("Enable request logging").__()
						.br().__()

						.label().attrFor(ENABLE_LOG_EXTERNAL_PROCESS).text("Enable external process logging").__()
						.br().__()
						.button().attrType(EnumTypeButtonType.SUBMIT).text("Submit").__()
					.__()
				.__();
		}).__(); //  div

	}

	@Override
	public void requestHandle(HttpExchange he) throws IOException {
		try {
			TemplateHeadModel thm = TemplateHeadModel.of("Admin");
			TemplatePageModel tepm = TemplatePageModel.of(view, thm, SelectedPage.Admin, BodyModel.of(he, null));
			String response = TemplatePage.view.render(tepm);

			he.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (Exception e) {
			view = DynamicHtml.view(AdminHandler::body);
			throw e;
		}
	}
}
