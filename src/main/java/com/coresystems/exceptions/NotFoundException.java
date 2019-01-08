package com.coresystems.exceptions;
/**
 * @author Oratile
 */
public class NotFoundException extends Exception {
	
	private static final long serialVersionUID = 5615498528420945179L;
	/**
	 * @param errorMessage
	 */
	public NotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
