package sales.manager.common.sales.report;

import java.util.zip.CRC32;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sales.manager.common.sales.report.event.HashProvider;

public class Buyer extends HashProvider {
	String username;
	String name;
	String addressCity;
	String addressRegion;
	String addressPostcode;
	String addressCountry;
	long hash;

	public Buyer() {}

	public Buyer(
		String username,
		String name,
		String addressCity,
		String addressRegion,
		String addressPostcode,
		String addressCountry,
		long hash) {
		this.username = username;
		this.name = name;
		this.addressCity = addressCity;
		this.addressRegion = addressRegion;
		this.addressPostcode = addressPostcode;
		this.addressCountry = addressCountry;
		this.hash = hash;
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
	@JsonIgnore
	public long getHashCode() {
		return hash;
	}

	@Override
	@JsonIgnore
	public String getItemID() {
		return "";
	}
}
