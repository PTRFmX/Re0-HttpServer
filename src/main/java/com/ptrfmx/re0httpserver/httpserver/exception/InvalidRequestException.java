package com.ptrfmx.re0httpserver.httpserver.exception;

import com.ptrfmx.re0httpserver.httpserver.status.HttpStatus;

public class InvalidRequestException extends Exception {

	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = 1L;

	private HttpStatus status = HttpStatus.BAD_REQUEST;
	
	private static final String ERR_MSG = "Invalid request exception";
	
	public InvalidRequestException() {}
	
	public HttpStatus getStatus() {
		return status;
	}
	
	public String getErrMsg() {
		return ERR_MSG;
	}
}
