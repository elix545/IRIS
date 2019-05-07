package com.temenos.useragent.example.swagger;

/*******************************************************************************
 * Copyright © Temenos Headquarters SA 1993-2019.  All rights reserved.
 *******************************************************************************/

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

import com.temenos.useragent.example.swagger.servlet.SwaggerServlet;

/**
 * This test ensures that the SwaggerServlet adds the basePath to the api-docs.json generated by the interaction sdk plugin
 * 
 */
public class SwaggerTest {
	
	private static final String SWAGGER_SERVLET_NAME = "SwaggerServlet";
	
	private static final String SWAGGER_SERVLET_INIT_PARAM_VALUE = "api";
	
	private static final String SWAGGER_SERVLET_APIKEY_PARAM_VALUE = "api_key_new";
	
	private static final String REQUEST_METHOD = "GET";
	
	private static final String REQUEST_URI = "swagger";
	
	private static final String REQUEST_CONTEXT_PATH = "/example";
	
	private static final String REQUEST_SERVER_NAME = "localhost";
	
	private static final int REQUEST_SERVER_PORT = 8080;
	
	private static final int EXPECTED_SWAGGER_SERVLET_RESPONSE_STATUS = 200;
	
	private static final String EXPECTED_SWAGGER_SERVLET_SIMPLE_STATES_SWAGGER = "{\"apiVersion\":\"0.2\",\"swaggerVersion\":\"1.2\",\"resourcePath\":\"/A\",\"apis\":[{\"path\":\"/A\",\"operations\":[{\"method\":\"GET\",\"nickname\":\"A\"}]},{\"path\":\"/B\",\"operations\":[{\"method\":\"POST\",\"nickname\":\"B\"},{\"method\":\"GET\",\"nickname\":\"B\"}]}],\"basePath\":\""+ REQUEST_CONTEXT_PATH + "/" + SWAGGER_SERVLET_INIT_PARAM_VALUE + "\",\"host\":\""+REQUEST_SERVER_NAME + ":" + REQUEST_SERVER_PORT +"\",\"securityDefinitions\":{\"api_key\":{\"type\":\"apiKey\",\"name\":\"api_key_new\",\"in\":\"header\"}}}";

	private static final DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
	
	private MockServletConfig servletConfig;

	private SwaggerServlet swaggerServlet;
	
	private MockServletContext servletContext;

	@Before
	public void setUp() throws ServletException {
		// Set up servletContext, servletConfig and swaggerServlet
		servletContext = new MockServletContext(defaultResourceLoader);
		servletConfig = new MockServletConfig(servletContext, SWAGGER_SERVLET_NAME);
		servletConfig.addInitParameter(SwaggerServlet.SWAGGER_SERVLET_INIT_PARAM, SWAGGER_SERVLET_INIT_PARAM_VALUE);
		servletConfig.addInitParameter(SwaggerServlet.SWAGGER_SERVLET_APIKEY_PARAM, SWAGGER_SERVLET_APIKEY_PARAM_VALUE);
		swaggerServlet = new SwaggerServlet();
		swaggerServlet.init(servletConfig);
	}

	@Test
	public void testSwaggerServlet() throws Exception {
		// Define the stub data used to define the servlet and the basePath built by the servlet and added to the response 
		MockHttpServletRequest request = new MockHttpServletRequest(servletContext, REQUEST_METHOD, REQUEST_URI);
		request.setContextPath(REQUEST_CONTEXT_PATH);
		request.setServerName(REQUEST_SERVER_NAME);
		request.setServerPort(REQUEST_SERVER_PORT);
		MockHttpServletResponse response = new MockHttpServletResponse();
		// Perform the call of the doGet of SwaggerServlet
		swaggerServlet.service(request, response);
		Assert.assertEquals(EXPECTED_SWAGGER_SERVLET_RESPONSE_STATUS, response.getStatus());
		Assert.assertEquals(EXPECTED_SWAGGER_SERVLET_SIMPLE_STATES_SWAGGER, response.getContentAsString());
	}	

}