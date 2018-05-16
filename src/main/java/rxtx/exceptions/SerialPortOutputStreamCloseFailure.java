package rxtx.exceptions;

public class SerialPortOutputStreamCloseFailure extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SerialPortOutputStreamCloseFailure(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return "SerialPortOutputStreamCloseFailure";
	}
}
