package com.ptrfmx.re0httpserver.httpserver.status;

public enum HttpStatus {
	
	OK(200),
	BAD_REQUEST(400),
	NOT_FOUND(404),
	UNPROCESSABLE_ENTITY(422),
	INTERNAL_SERVER_ERROR(500),
	BAD_GATEWAY(502);
	
	private int code;
	
	HttpStatus(int code){
        this.code = code;
    }
	
	public int getCode(){
        return code;
    }
}
