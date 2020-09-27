package net.bzk.infrastructure.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.function.Consumer;

import net.bzk.infrastructure.ex.BzkRuntimeException;

public class ProcessUtils {
	public static Process exec(File dir, String command) {
		if (command.length() == 0)
			throw new IllegalArgumentException("Empty command");

		StringTokenizer st = new StringTokenizer(command);
		String[] cmdarray = new String[st.countTokens()];
		for (int i = 0; st.hasMoreTokens(); i++)
			cmdarray[i] = st.nextToken();
		return exec(dir, cmdarray);
	}

	public static Process exec(File dir, String... cmdarray) {
		try {
			return new ProcessBuilder(cmdarray).directory(dir).start();
		} catch (IOException e) {
			throw new BzkRuntimeException(e);
		}
	}

	public static int exec(File dir, String cmd, Consumer<String> i) {
		Process p = exec(dir, cmd);
		return exec(dir, p, i);
	}

	public static int exec(File dir, Consumer<String> i, String... cmd) {
		Process p = exec(dir, cmd);
		return exec(dir, p, i);

	}

	public static int exec(File dir, Process p, Consumer<String> i) {
		try {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					i.accept(line);
					System.out.println(line);
				}
				int exitVal = p.waitFor();
				return exitVal;
			}
		} catch (Exception e) {
			throw new BzkRuntimeException(e);
		}

	}
}
