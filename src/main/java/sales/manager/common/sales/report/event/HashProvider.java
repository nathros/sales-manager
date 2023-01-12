package sales.manager.common.sales.report.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class HashProvider {
	public abstract long getHashCode();
	public abstract String getItemID();

	public HashProvider() {}
}
