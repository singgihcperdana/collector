package com.collector.tool.jasperserver.sample;

import java.io.File;
import java.io.FileInputStream;

import com.collector.constant.ConstantJasperServer;
import com.collector.utils.JasperRestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
//import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Chaim arbiv - carbiv@jaspersoft.com
 *
 */
public class BasicResourceCRUDTest{
	
	static JasperRestUtils restUtils = new JasperRestUtils();
	protected HttpRequestBase httpReq;
	protected HttpResponse httpResponse;
	private final Log log = LogFactory.getLog(getClass());
	
	@BeforeAll
	public static void setUp() throws Exception {
		restUtils.loginToServer();
//		PropertyConfigurator.configure(LOG4J_PATH);
		
		// creating a folder in the server
		restUtils.releaseConnection(restUtils.sendAndAssert(new HttpPut(),
				ConstantJasperServer.RESOURCE, ConstantJasperServer.RESOURCES_LOCAL_PATH + ConstantJasperServer.SAMPLE_FOLDER_RD, HttpStatus.SC_CREATED));
	}

	@Test
	public void Resource_IMG_PUT_201() throws Exception{
		log.info("creating an image resource on in /SAMPLE_REST_FOLDER folder");
		
		final String parentUri = "/SAMPLE_REST_FOLDER";
		httpResponse = resourceCreateUpdate(	new HttpPut(),
				ConstantJasperServer.RESOURCES_LOCAL_PATH+ ConstantJasperServer.SAMPLE_IMAGE_RD,
											parentUri+"/JUNIT_IMAGE_FILE",
				ConstantJasperServer.RESOURCES_LOCAL_PATH+ ConstantJasperServer.SAMPLE_IMAGE_BIN,
				ConstantJasperServer.RESOURCE+parentUri); // since the resource is not created yet the put request is sent to the parent resource
		Assertions.assertEquals("basic response check did not pass", httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_CREATED);
	}
	
	@Test
	public void Resource_IMG_POST_200() throws Exception
	{
		log.info("updating the image content /SAMPLE_REST_FOLDER folder");
		final String resourceUri = "/SAMPLE_REST_FOLDER/JUNIT_IMAGE_FILE";		
		httpResponse = resourceCreateUpdate(	new HttpPost(),
				ConstantJasperServer.RESOURCES_LOCAL_PATH+ ConstantJasperServer.SAMPLE_IMAGE_RD,
											resourceUri,
				ConstantJasperServer.RESOURCES_LOCAL_PATH+ ConstantJasperServer.NEW_SAMPLE_IMAGE_BIN,
				ConstantJasperServer.RESOURCE+resourceUri); // since the resource exists we can send the request to the resource
	Assertions.assertEquals("basic response check did not pass", httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK);
	}
		
	@AfterEach
	public void after() throws Exception{
		restUtils.releaseConnection(httpResponse);
	}
	
	@AfterAll
	public static void tearDown() throws Exception{
		restUtils.sendAndAssert(new HttpDelete(), ConstantJasperServer.RESOURCE+"/SAMPLE_REST_FOLDER", HttpStatus.SC_OK);
	}
	
	// creates of updates a resource with a binary content like image, jrxl, properties ... 
	private HttpResponse resourceCreateUpdate(HttpRequestBase req, String fileRd, String fileUri, String fileBinPath, String resourceUri) throws Exception
	{
		// resource descriptor
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		String str = IOUtils.toString(new FileInputStream(fileRd));
		reqEntity.addPart(ConstantJasperServer.REQUEST_PARAMENTER_RD, new StringBody(str));
		
		//appending the binaries to the request body
		FileBody bin = new FileBody(new File(fileBinPath));
		reqEntity.addPart(fileUri, bin );
		
		//setting the entity in the request
		((HttpEntityEnclosingRequestBase)req).setEntity(reqEntity);

		return restUtils.sendRequest(req, resourceUri, null, false);
	}
}
