package com.collector.tool.jasperserver.sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.collector.constant.ConstantJasperServer;
import com.collector.utils.JasperRestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
//import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Thomas Zimmer - mail@ThomasZimmer.net - http://www.thomaszimmer.net
 *
 */
public class GetCityTest {

	static JasperRestUtils restUtils = new JasperRestUtils();
	protected HttpRequestBase httpReq;
	protected HttpResponse httpRes;
	private final Log log = LogFactory.getLog(getClass());
	
	/**
	 * @param args
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {

		GetCityTest test = new GetCityTest();
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
		InputStream is = httpRes.getEntity().getContent();
		byte[] buffer = new byte[8 * 1024];
		File file = new File("D:\\tmp\\report\\get_city.csv");
		new File(file.getParent()).mkdirs();
		OutputStream output = new FileOutputStream(file);
		try {
		    int bytesRead;
		    while ((bytesRead = is.read(buffer)) != -1) {
		      output.write(buffer, 0, bytesRead);
		    }
		  } finally {
		    output.close();
				is.close();
		  }
	}

	@AfterEach
	public void tearDown() throws Exception{
		restUtils.releaseConnection(httpRes);
	}
}
