package com.collector.tool.jasperserver.sample;

import com.collector.constant.ConstantJasperServer;
import com.collector.utils.JasperRestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

/**
 * 
 * @author Thomas Zimmer - mail@ThomasZimmer.net - http://www.thomaszimmer.net
 *
 */
public class GetCityToListOfStringTest {

	static JasperRestUtils restUtils = new JasperRestUtils();
	protected HttpRequestBase httpReq;
	protected HttpResponse httpRes;
	private final Log log = LogFactory.getLog(getClass());
	
	/**
	 * @param args
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {

		GetCityToListOfStringTest test = new GetCityToListOfStringTest();
		try {
			test.setUp();
			test.test();
			test.tearDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@BeforeAll
	public static void setUp() throws Exception {
		restUtils.loginToServer();
	}
	
	@Test
	public void test() throws Exception {
		// GET request - download report by id from session
		final String resourceUri = "/Reports/Samples/get_city.csv";
		
		httpRes = restUtils.sendRequest(
				new HttpGet(), ConstantJasperServer.BASE_REPORT + resourceUri, null, true);
		
		// Write binary content to output file
		InputStream inputStream = httpRes.getEntity().getContent();
		List<String> lines = IOUtils.readLines(inputStream, "UTF-8");
		System.out.println("lines = " + lines.size());
	}

	@AfterEach
	public void tearDown() throws Exception{
		restUtils.releaseConnection(httpRes);
	}
}
