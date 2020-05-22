package com.ptrfmx.re0httpserver.httpserver.servlet;

import java.io.IOException;

import com.ptrfmx.re0httpserver.httpserver.request.HttpRequest;
import com.ptrfmx.re0httpserver.httpserver.response.HttpResponse;

public class DispatchServlet {
	
	public void init() {
        
    }
	
	public void destroy() {
		
	}

	public void doDispatch(HttpRequest request, HttpResponse response) throws IOException {
		HttpRequest.RequestMethod method = request.getMethod();
        if (request.getMethod() == HttpRequest.RequestMethod.GET) {
            // TODO: Perform GET operation
        } else if (method == HttpRequest.RequestMethod.POST) {
        	// TODO: Perform POST operation
        } else if (method == HttpRequest.RequestMethod.PUT) {
        	// TODO: Perform PUT operation
        } else if (method == HttpRequest.RequestMethod.DELETE) {
        	// TODO: Perform DELETE operation
        }
    }
}
