package sales.manager.common.report;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.stream.Stream;

public class ReportParser {
	private String file;

	public ReportParser(String file) {
		this.file = file;
	}

	public void parse() throws FileNotFoundException, IOException, ParseException {
		try (Stream<String> lines = Files.lines(Path.of(file), StandardCharsets.UTF_8))
		{
			boolean start = false;
		    for (String line : (Iterable<String>) lines::iterator)
		    {
		        if (!start) {
			    	if (line.contains("Transaction creation date")) {
			        	start = true;
			        }
			    	continue;
		        }

		        var r = Record.fromCSVRow(line);

		        r.getData();
		    	System.out.println(line);

		    }
		}
	}
}
