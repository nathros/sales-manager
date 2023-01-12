package sales.manager.web.handlers.analytics;

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
import sales.manager.web.resource.Asset;

public class AnalyticsHandler extends BaseHandler {
	public static final String PATH = "/analytics";

	public static DynamicHtml<BodyModel> view = DynamicHtml.view(AnalyticsHandler::body);

	static void body(DynamicHtml<BodyModel> view, BodyModel model) {
		final Runtime runtime = Runtime.getRuntime();
		final long maxMemory = runtime.maxMemory();
		final long totalMemory = runtime.totalMemory();
		final long freeMemory = runtime.freeMemory();

		final int maxMemoryMB = (int) (maxMemory / 1024 / 1024);
		final int allocatedMB = (int) (totalMemory / 1024 / 1024);
		final int usedMemMB = (int) ((totalMemory - freeMemory) / 1024 / 1024);

		int p = (int) (((double)usedMemMB / (double)maxMemoryMB) * 100);
		view
			.div()
				.p().text("AnalyticsHandler").__()
				.p().text("JVM max memory: " + maxMemoryMB + "MB").__()
				.p().text("JVM allocated memory: " + allocatedMB + "MB").__()
				.p().text("JVM used memory: " + usedMemMB + "MB").__()
				.p().text("percent used: " + p + "%").__()
				.div().attrClass("pie animate no-round").attrStyle("--p:" + p + ";--c:orange;").text(p + "%").__()
				.div().attrClass("pie animate").attrStyle("--p:66;--c:red;").text("66%-test").__()
			.__(); // div

	}

	@Override
	public void requestHandle(HttpExchange he) throws IOException {
		try {
			TemplateHeadModel thm = TemplateHeadModel.of("Analytics");
			thm.AddCSS(Asset.CSS_PIE);
			TemplatePageModel tepm = TemplatePageModel.of(view, thm, SelectedPage.Analytics, BodyModel.of(he, null));
			String response = TemplatePage.view.render(tepm);

			he.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (Exception e) {
			view = DynamicHtml.view(AnalyticsHandler::body);
			throw e;
		}
	}
}
