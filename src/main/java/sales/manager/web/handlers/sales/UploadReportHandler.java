package sales.manager.web.handlers.sales;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.xmlet.htmlapifaster.EnumEnctypeType;
import org.xmlet.htmlapifaster.EnumMethodType;
import org.xmlet.htmlapifaster.EnumTypeButtonType;
import org.xmlet.htmlapifaster.EnumTypeInputType;

import com.sun.net.httpserver.HttpExchange;

import htmlflow.DynamicHtml;
import sales.manager.web.handlers.BaseHandler;
import sales.manager.web.handlers.templates.TemplatePage;
import sales.manager.web.handlers.templates.TemplatePage.SelectedPage;
import sales.manager.web.handlers.templates.TemplatePage.TemplatePageModel;
import sales.manager.web.handlers.templates.models.BodyModel;
import sales.manager.web.handlers.templates.models.TemplateHeadModel;
import sales.manager.web.resource.CSS;

public class UploadReportHandler extends BaseHandler {
	public static final String PATH = "/sales/uploadreport";

	public static final String FILE = "file";

	public static DynamicHtml<BodyModel> view = DynamicHtml.view(UploadReportHandler::body);

	static void body(DynamicHtml<BodyModel> view, BodyModel model) {

		view
			.div()
				.form().attrEnctype(EnumEnctypeType.MULTIPART_FORM_DATA).attrMethod(EnumMethodType.POST)
					.input().attrType(EnumTypeInputType.FILE).attrName(FILE).__()
					.br().__()
					.button().attrClass(CSS.BUTTON).attrStyle("float:right").attrType(EnumTypeButtonType.SUBMIT).text("Submit").__()
				.__()
			.__(); // div

	}

	@Override
	public void requestHandle(HttpExchange he) throws Exception {
		try {
			TemplateHeadModel thm = TemplateHeadModel.of("Upload Report");
			TemplatePageModel tepm = TemplatePageModel.of(view, thm, SelectedPage.Sales, BodyModel.of(he, null));
			String response = TemplatePage.view.render(tepm);

			he.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (Exception e) {
			view = DynamicHtml.view(UploadReportHandler::body);
			throw e;
		}
	}
}
