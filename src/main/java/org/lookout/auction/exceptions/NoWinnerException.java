package org.lookout.auction.exceptions;

public class NoWinnerException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoWinnerException(String msg) {
		super(msg);
	}
}
