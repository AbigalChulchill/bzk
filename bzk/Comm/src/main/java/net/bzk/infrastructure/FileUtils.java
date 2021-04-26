package net.bzk.infrastructure;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;

import net.bzk.infrastructure.ex.BzkRuntimeException;

public class FileUtils {

	public static void saveFile(String path, String content) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}

			try (var fos = new FileOutputStream(file)) {
				IOUtils.write(content, fos, "UTF-8");
			}
		} catch (Exception e) {
			throw new BzkRuntimeException(e);
		}

	}

}
