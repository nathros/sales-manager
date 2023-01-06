package sales.manager.web.handlers.templates;

import org.xmlet.htmlapifaster.EnumTypeInputType;

import htmlflow.DynamicHtml;
import sales.manager.web.Main;
import sales.manager.web.handlers.AdminHandler;
import sales.manager.web.handlers.BaseHandler;
import sales.manager.web.handlers.analytics.AnalyticsHandler;
import sales.manager.web.handlers.sales.SalesHandler;
import sales.manager.web.handlers.sandpit.SandpitHandler;
import sales.manager.web.handlers.stock.StockHandler;
import sales.manager.web.handlers.templates.TemplateHead.TemplateHeadModel;
import sales.manager.web.handlers.templates.models.BodyModel;

public class TemplatePage {
	public static enum SelectedPage {
		Admin,
		Analytics,
		Sales,
		Stock,
		Sandpit,
		Missing
	}

	public static class TemplatePageModel {
		final DynamicHtml<BodyModel> dynamicHtml;
		final TemplateHeadModel head;
		final SelectedPage page;
		final BodyModel body;

		private TemplatePageModel(DynamicHtml<BodyModel> dynamicHtml, TemplateHeadModel head, SelectedPage page, BodyModel body) {
			this.dynamicHtml = dynamicHtml;
			this.head = head;
			this.page = page;
			this.body = body;
		}

		public static TemplatePageModel of(DynamicHtml<BodyModel> dynamicHtml, TemplateHeadModel head, SelectedPage page, BodyModel body) {
			return new TemplatePageModel(dynamicHtml, head, page, body);
		}

		public BodyModel getBodyModel() { return body; }
	}

	public static DynamicHtml<TemplatePageModel> view = DynamicHtml.view(TemplatePage::template);

	static void template(DynamicHtml<TemplatePageModel> view, TemplatePageModel model) {
		final String selected = " selected";
		final DynamicHtml<TemplatePageModel> viewTmp = view;

		try {
			view
				.html().attrLang(BaseHandler.LANG_VALUE)
					.dynamic(head -> viewTmp.addPartial(TemplateHead.view, TemplateHead.TemplateHeadModel.of(model.head.title)))
					.body()
						.header().attrClass("header-root")
						.div().attrClass("header-root-container")
							.div().attrId("nav-toggle")
								.input().attrType(EnumTypeInputType.CHECKBOX).__()
								.div() // Menu Icon
									.span().__()
									.span().__()
									.span().__()
								.__() // div
								.ul().attrId("nav-menu")
									.li()
										.a().dynamic(a -> a.attrHref(AdminHandler.PATH)
											.attrClass("icon-admin" + (model.page == SelectedPage.Admin ? selected : ""))
											.text("Admin"))
										.__()
									.__()
									.li()
										.a().dynamic(a -> a.attrHref(AnalyticsHandler.PATH)
											.attrClass("icon-bar-chart" + (model.page == SelectedPage.Analytics ? selected : ""))
											.text("Analytics"))
										.__()
									.__()
									.li()
										.a().dynamic(a -> a.attrHref(SalesHandler.PATH)
											.attrClass("icon-sales" + (model.page == SelectedPage.Sales ? selected : ""))
											.text("Sales"))
										.__()
									.__()
									.li()
										.a().dynamic(a -> a.attrHref(StockHandler.PATH)
											.attrClass("icon-stock" + (model.page == SelectedPage.Stock ? selected : ""))
											.text("Stock"))
										.__()
									.__()
									.li().dynamic(li -> {
										if (Main.DEBUG_MODE) {
											li.a()
											.attrHref(SandpitHandler.PATH).attrClass("icon-sandpit" + (model.page == SelectedPage.Sandpit ? selected : ""))
											.text("Sandpit")
											.__();
										}
									})
								.__()
							.__() // div nav-toggle
						.__() // div header-root-container
					.__() // header
					.div().attrClass("main-content")
						.div().attrClass("nav-area").__()
						.div().attrClass("main-content-wrapper")
							.dynamic(div -> viewTmp.addPartial(model.dynamicHtml, model.getBodyModel()))
						.__() // div
					.__() //div
				.__() // body
			.__(); // html

		} catch (Exception e) {
			TemplatePage.view = DynamicHtml.view(TemplatePage::template);
			throw e;
		}
	}
}
