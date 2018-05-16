package rxtx.exceptions;

public class PortInUseFailure extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PortInUseFailure() {}

	@Override
	public String toString() {
		return "Port is already in use.";
	}
	
}
