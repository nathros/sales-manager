package sales.manager.web.handlers.files;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.xmlet.htmlapifaster.EnumBorderType;
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

public class FilesHandler extends BaseHandler {
	public static final String PATH = "/files";

	public static DynamicHtml<BodyModel> view = DynamicHtml.view(FilesHandler::body);

	public static final String TAPE_ID = "tape_id";

	static void body(DynamicHtml<BodyModel> view, BodyModel model) {
		final String tapeId = model.getQueryNoNull(TAPE_ID);

		final List<File> files = new ArrayList<File>();
		if (!tapeId.equals("")) {
			try {
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		view
			.div()
				.a().attrHref(FilesAddHandler.PATH).text("Add files").__()
				.p().text("files list from tape ID").__()
				.form().of(form -> {
					form.input().attrType(EnumTypeInputType.TEXT).attrName(TAPE_ID).dynamic(input -> input.attrValue(tapeId)).__();
					})
					.button().attrType(EnumTypeButtonType.SUBMIT).text("Submit").__()
				.__()


				.table().dynamic(table -> {
					table.attrBorder(EnumBorderType._1).tr()
						.th().text("Path").__()
					.__();
					for (File f: files) {
						table.tr()
							.td().text(f.getAbsolutePath()).__()
						.__();
					}
					if (files.size() == 0) table.tr().td().text("Empty").__().__();
				}).__()
			.__(); // div

	}

	@Override
	public void requestHandle(HttpExchange he) throws IOException {
		try {
			TemplateHeadModel thm = TemplateHeadModel.of("Files");
			TemplatePageModel tepm = TemplatePageModel.of(view, thm, SelectedPage.Files, BodyModel.of(he, null));
			String response = TemplatePage.view.render(tepm);

			he.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (Exception e) {
			view = DynamicHtml.view(FilesHandler::body);
			throw e;
		}
	}
}
