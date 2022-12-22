package sales.manager.common.report;

import java.util.Objects;

public record Buyer(
	String username,
	String name,
	String addressCity,
	String addressRegion,
	String addressPostcode,
	String addressCountry,
	String hash) {

	public Buyer {
		Objects.requireNonNull(username);
		Objects.requireNonNull(name);
		Objects.requireNonNull(addressCity);
		Objects.requireNonNull(addressRegion);
		Objects.requireNonNull(addressPostcode);
		Objects.requireNonNull(addressCountry);
	}

	public Buyer(
		String username,
		String name,
		String addressCity,
		String addressRegion,
		String addressPostcode,
		String addressCountry)
	{
		this(username,
			name,
			addressCity,
			addressRegion,
			addressPostcode,
			addressCountry,"");
	}
}
