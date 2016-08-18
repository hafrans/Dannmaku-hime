package net;

public class HSMTPException extends Exception {

	public HSMTPException() {
		super();
	}

	public HSMTPException(String message) {
		super(message);
	}

	public HSMTPException(Throwable cause) {
		super(cause);
	}

	public HSMTPException(String message, Throwable cause) {
		super(message, cause);
	}

	public HSMTPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
