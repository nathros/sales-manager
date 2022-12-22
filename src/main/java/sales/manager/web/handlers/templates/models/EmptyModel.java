package sales.manager.web.handlers.templates.models;

public class EmptyModel {
	private EmptyModel() {}

	public static EmptyModel of() {
		return new EmptyModel();
	}
}