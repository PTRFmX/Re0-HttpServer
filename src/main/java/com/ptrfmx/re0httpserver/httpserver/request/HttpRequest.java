package com.ptrfmx.re0httpserver.httpserver.request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.ptrfmx.re0httpserver.httpserver.constants.Constants;
import com.ptrfmx.re0httpserver.httpserver.exception.InvalidRequestException;

public class HttpRequest {
	
	public static enum RequestMethod {
		GET,
		POST,
		PUT,
		DELETE
	}
	
	private RequestMethod method;
	private Map<String, List<String>> params;
    private Map<String, List<String>> headers;
    private Map<String, Object> attributes;
	private String url;
	private Cookie[] cookies;
	
	public HttpRequest(byte[] data) throws IOException, InvalidRequestException {
		String[] lines = null;
        try {
            lines = URLDecoder.decode(new String(data, Charset.forName(Constants.UTF_8)), 
            		Constants.UTF_8).split(Constants.CRLF);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (lines.length <= 0 ) throw new InvalidRequestException();
        try {
            parseHeaders(lines);
            if (headers.containsKey("Content-Length") && !headers.get("Content-Length").get(0).equals("0")) {
                parseBody(lines[lines.length - 1]);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
	}
	
	public Socket getClient(Socket client) {
		return client;
	}
	
	public Map<String, List<String>> getParams() {
		return params;
	}
	
	public Map<String, List<String>> getHeaders() {
		return headers;
	}
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public String getUrl() {
		return url;
	}
	
	public RequestMethod getMethod() {
		return method;
	}

	private void parseHeaders(String[] lines) {
		
        String firstLine = lines[0];
        
        String[] firstLineSlices = firstLine.split(Constants.BLANK);
        this.method = RequestMethod.valueOf(firstLineSlices[0]);

        String rawURL = firstLineSlices[1];
        String[] urlSlices = rawURL.split("\\?");
        this.url = urlSlices[0];

        if (urlSlices.length > 1) parseParams(urlSlices[1]);

        String header;
        this.headers = new HashMap<>();
        for (int i = 1; i < lines.length; i++) {
            header = lines[i];
            if (header.equals("")) {
                break;
            }
            int colonIndex = header.indexOf(':');
            String key = header.substring(0, colonIndex);
            String[] values = header.substring(colonIndex + 2).split(",");
            headers.put(key, Arrays.asList(values));
        }

        if (headers.containsKey("Cookie")) {
            String[] rawCookies = headers.get("Cookie").get(0).split("; ");
            this.cookies = new Cookie[rawCookies.length];
            for (int i = 0; i < rawCookies.length; i++) {
                String[] kv = rawCookies[i].split("=");
                this.cookies[i] = new Cookie(kv[0], kv[1]);
            }
            headers.remove("Cookie");
        } else {
            this.cookies = new Cookie[0];
        }
    }

    private void parseBody(String body) {
        byte[] bytes = body.getBytes(Charset.forName(Constants.UTF_8));
        List<String> lengths = this.headers.get("Content-Length");
        if (lengths != null) {
            int length = Integer.parseInt(lengths.get(0));
            parseParams(new String(bytes, 0, Math.min(length,bytes.length), 
            		Charset.forName(Constants.UTF_8)).trim());
        } else {
            parseParams(body.trim());
        }
        if (this.params == null) {
            this.params = new HashMap<>();
        }
    }

    private void parseParams(String params) {
        String[] urlParams = params.split("&");
        if (this.params == null) {
            this.params = new HashMap<>();
        }
        for (String param : urlParams) {
            String[] kv = param.split("=");
            String key = kv[0];
            String[] values = kv[1].split(",");

            this.params.put(key, Arrays.asList(values));
        }
    }
}
