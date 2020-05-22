package com.ptrfmx.re0httpserver.httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ptrfmx.re0httpserver.httpserver.constants.Constants;
import com.ptrfmx.re0httpserver.httpserver.exception.InvalidRequestException;
import com.ptrfmx.re0httpserver.httpserver.request.HttpRequest;
import com.ptrfmx.re0httpserver.httpserver.response.HttpResponse;
import com.ptrfmx.re0httpserver.httpserver.servlet.DispatchServlet;

public class HttpServer {
	
	private static final Logger logger = Logger.getLogger(HttpServer.class.getName());;
	
	public static void main(String[] args) {
		
		logger.setLevel(Level.ALL);
		
		ServerSocket server = null;
		
		try {
			server = new ServerSocket(Constants.SOCKET_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server == null || !(server instanceof ServerSocket)) {
				logger.severe(Constants.SERVER_CRASHED);
				System.exit(0);
			}
		}
		
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Socket client = server.accept();
				logger.info(Constants.CLIENT_CONNECTED + client.getLocalAddress());
				
				DispatchServlet servlet = new DispatchServlet();
				servlet.init();
				
				HttpRequest request = new HttpRequest(client.getInputStream().readAllBytes());
				HttpResponse response = new HttpResponse();
				
				servlet.doDispatch(request, response);
				
			} catch (IOException | InvalidRequestException e) {
				e.printStackTrace();
				logger.warning(Constants.CLIENT_DISCONNECTED);
			}
			
		}
	}
	
}
