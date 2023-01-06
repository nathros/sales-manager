package sales.manager.common.sales.report;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ReportParser {
	private String file;

	public ReportParser(String file) {
		this.file = file;
	}

	public List<Record> parse() throws Exception {
		try (Stream<String> lines = Files.lines(Path.of(file), StandardCharsets.UTF_8))
		{
			boolean start = false;
			List<Record> list = new ArrayList<Record>();
		    for (String line : (Iterable<String>) lines::iterator) {
				if (!start) {
					if (line.contains("Transaction creation date")) {
						start = true;
					}
					continue;
				}
				Record item = Record.fromCSVRow(line);
				if (item != null) {
					list.add(item);
				} else {
					System.out.print("IGNORE: ");
				}

				System.out.println(line);
		    }
		    return list;
		} catch (Exception e) {
			return null;
		}
	}
}
