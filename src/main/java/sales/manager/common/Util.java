package sales.manager.common;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Util {

	public static List<File> getFilesListInDir(String path) {
		List<File> ret  = new ArrayList<File>();
		if ((null == path) || (path.equals(""))) {
			Path currentRelativePath = Paths.get("");
			path = currentRelativePath.toAbsolutePath().toString();
		}

		File dir = new File(path);
		for (File f: dir.listFiles()) {
			ret.add(f);
		}
		return ret;

	}

	public static List<File> getFilesListInDirRecursive(String path) {
		List<File> ret  = new ArrayList<File>();
		if ((null == path) || (path.equals(""))) {
			path = getWorkingDir().getAbsolutePath();
		}

		File dir = new File(path);
		for (File f: dir.listFiles()) {
			ret.add(f);
			if (f.isDirectory()) {
				List<File> list = getFilesListInDirRecursive(f.getAbsolutePath());
				ret.addAll(list);
			}
		}
		return ret;
	}

	public static File getWorkingDir() {
		Path currentRelativePath = Paths.get("");
		String wdir = currentRelativePath.toAbsolutePath().toString();
		File f = new File(wdir);
		return f;
	}


	public static String encodeUrl(String input) {
		try {
			return URLEncoder.encode(input, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String decodeUrl(String input) {
		try {
			return URLDecoder.decode(input, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static byte[] longToBytes(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.putLong(x);
	    return buffer.array();
	}

	public static byte[] intToBytes(int x) {
		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
	    buffer.putInt(x);
	    return buffer.array();
	}

	public static byte[] doubleToBytes(double x) {
		ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES);
	    buffer.putDouble(x);
	    return buffer.array();
	}

}
