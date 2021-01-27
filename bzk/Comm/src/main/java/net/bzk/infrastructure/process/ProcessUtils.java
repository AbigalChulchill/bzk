package net.bzk.infrastructure.process;

import java.io.BufferedReader;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

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

	public static ExecResult exec(File dir, String cmd, Consumer<String> i) {
		Process p = exec(dir, cmd);
		return printExec(dir, p, i);
	}

	public static ExecResult exec(File dir, Consumer<String> i, String... cmd) {
		Process p = exec(dir, cmd);
		return printExec(dir, p, i);

	}

	public static ExecResult printExec(File dir, Process p, Consumer<String> i) {
		try {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader ereader = new BufferedReader(new InputStreamReader(p.getErrorStream()));) {
				String line;
				while ((line = reader.readLine()) != null) {
					i.accept(line);
					System.out.println(line);
				}
				int exitVal = p.waitFor();
				return new ExecResult(exitVal, IOUtils.toString(ereader));
			}
		} catch (Exception e) {
			throw new BzkRuntimeException(e);
		}

	}

	@Data
	@AllArgsConstructor(access = AccessLevel.PUBLIC)
	public static class ExecResult {
		private int exit;
		private String errorMsg;

	}
}
