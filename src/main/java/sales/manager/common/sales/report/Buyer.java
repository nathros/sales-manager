package sales.manager.common.sales.report;

import java.util.Objects;
import java.util.zip.CRC32;

import sales.manager.common.sales.report.event.HashProvider;

public record Buyer(
	String username,
	String name,
	String addressCity,
	String addressRegion,
	String addressPostcode,
	String addressCountry,
	long hash) implements HashProvider {

	public Buyer {
		Objects.requireNonNull(username);
		Objects.requireNonNull(name);
		Objects.requireNonNull(addressCity);
		Objects.requireNonNull(addressRegion);
		Objects.requireNonNull(addressPostcode);
		Objects.requireNonNull(addressCountry);
		Objects.requireNonNull(hash);
	}

	public static Buyer of(
		String username,
		String name,
		String addressCity,
		String addressRegion,
		String addressPostcode,
		String addressCountry)
	{
		CRC32 crc = new CRC32();
		crc.update(username.getBytes());
		crc.update(name.getBytes());
		crc.update(addressCity.getBytes());
		crc.update(addressRegion.getBytes());
		crc.update(addressPostcode.getBytes());
		crc.update(addressCountry.getBytes());
		long hash = crc.getValue();
		return new Buyer(username, name, addressCity, addressRegion, addressPostcode, addressCountry, hash);
	}

	@Override
	public long getHashCode() {
		return hash;
	}

	@Override
	public String getItemID() {
		return "";
	}
}
