package net.bzk.infrastructure.ex;


@SuppressWarnings("serial")
public class BzkRuntimeException extends RuntimeException {

	public BzkRuntimeException() {
		super();
	}

	public BzkRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BzkRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BzkRuntimeException(String message) {
		super(message);
	}

	public BzkRuntimeException(Throwable cause) {
		super(cause);
	}

}
