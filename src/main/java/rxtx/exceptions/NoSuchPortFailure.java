package rxtx.exceptions;

public class NoSuchPortFailure extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchPortFailure(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return "Port is not found!";
	}
	
}
