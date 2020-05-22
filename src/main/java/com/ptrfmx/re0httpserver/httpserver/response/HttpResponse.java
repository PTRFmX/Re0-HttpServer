package com.ptrfmx.re0httpserver.httpserver.response;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;

import com.ptrfmx.re0httpserver.httpserver.constants.Constants;
import com.ptrfmx.re0httpserver.httpserver.status.HttpStatus;

class Header {
	private String key;
    private String value;
    public Header() {}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}

public class HttpResponse {
	
	private StringBuilder headerAppender;
    private List<Cookie> cookies;
    private List<Header> headers;
    private HttpStatus status = HttpStatus.OK;
    private String contentType = Constants.DEFAULT_CONTENT_TYPE;
    private byte[] body = new byte[0];
    
    public HttpResponse() {
        this.headerAppender = new StringBuilder();
        this.cookies = new ArrayList<>();
        this.headers = new ArrayList<>();
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }


    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public void addHeader(Header header) {
        headers.add(header);
    }
    
    private void buildHeader() {
        headerAppender.append("HTTP/1.1").append(Constants.BLANK).append(status.getCode()).append(Constants.BLANK).append(status).append(Constants.CRLF);
        headerAppender.append("Date:").append(Constants.BLANK).append(new Date()).append(Constants.CRLF);
        headerAppender.append("Content-Type:").append(Constants.BLANK).append(contentType).append(Constants.CRLF);
        if (headers != null) {
            for (Header header : headers) {
                headerAppender.append(header.getKey()).append(":").append(Constants.BLANK).append(header.getValue()).append(Constants.CRLF);
            }
        }
        if (cookies.size() > 0) {
            for (Cookie cookie : cookies) {
                headerAppender.append("Set-Cookie:").append(Constants.BLANK).append(cookie.getName()).append("=").append(cookie.getValue()).append(Constants.CRLF);
            }
        }
        headerAppender.append("Content-Length:").append(Constants.BLANK);
    }

    private void buildBody() {
        this.headerAppender.append(body.length).append(Constants.CRLF).append(Constants.CRLF);
    }

    private void buildResponse() {
        buildHeader();
        buildBody();
    }
    
    public ByteBuffer[] getResponseByteBuffer() {
        buildResponse();
        byte[] header = null;
		try {
			header = this.headerAppender.toString().getBytes(Constants.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        ByteBuffer[] response = {ByteBuffer.wrap(header), ByteBuffer.wrap(body)};
        return response;
    }

    public byte[] getResponseBytes() {
        buildResponse();
        byte[] header = null;
		try {
			header = this.headerAppender.toString().getBytes(Constants.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        byte[] response = new byte[header.length + body.length];
        System.arraycopy(header, 0, response, 0, header.length);
        System.arraycopy(body, 0, response, header.length, body.length);
        return response;
    }
}
