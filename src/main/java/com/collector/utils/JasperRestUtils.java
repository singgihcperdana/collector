package com.collector.utils;

import com.collector.config.ConfigProperties;
import com.collector.model.JasperServer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.collector.constant.ConstantJasperServer.RESOURCE;
import static com.collector.constant.ConstantJasperServer.RESOURCES_LOCAL_PATH;
import static com.collector.constant.ConstantJasperServer.SAMPLE_FOLDER_RD;

public class JasperRestUtils {

  private HttpClient httpClient;
  private	CookieStore cookieStore;
  private HttpContext httpContext;

  protected HttpRequestBase httpRequestBase;

  protected HttpRequestBase tempHttpReq;
  protected HttpResponse httpResponse;
  protected JasperServer jasperServer = ConfigProperties.getInstance().getJasperServer();

  private final Log log = LogFactory.getLog(getClass());

  protected void putSampleFolder() throws Exception{
    tempHttpReq = new HttpPut();
    sendAndAssert(tempHttpReq, RESOURCE+"/JUNIT_NEW_FOLDER", RESOURCES_LOCAL_PATH + SAMPLE_FOLDER_RD, HttpStatus.SC_CREATED);
  }

  protected void deleteSampleFolder() throws Exception{
    tempHttpReq = new HttpDelete();
    sendAndAssert(tempHttpReq, RESOURCE+"/JUNIT_NEW_FOLDER", RESOURCES_LOCAL_PATH + SAMPLE_FOLDER_RD, HttpStatus.SC_CREATED);
  }

  public JasperRestUtils(){
    httpClient = new DefaultHttpClient();
    cookieStore = new BasicCookieStore();
    httpContext = new BasicHttpContext();
    httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
  }

  public void tearDown() throws Exception{
    //releasing the related streams
    if (httpResponse.getEntity().getContent().available()!=0){
      httpResponse.getEntity().getContent();
    }
    httpClient.getConnectionManager().closeExpiredConnections();
  }

  public void loginToServer() {
    //building the request parameters
    List<NameValuePair> params = new ArrayList<>();
    params.add(new BasicNameValuePair(jasperServer.getParameterUsername(), jasperServer.getUsername()));
    params.add(new BasicNameValuePair(jasperServer.getParameterPassword(), jasperServer.getPassword()));

    try {
      httpRequestBase = new HttpPost();
      httpResponse = sendRequest(httpRequestBase, jasperServer.serviceLogin, params, true);

      //consuming the content to close the stream
      String loginResponse = IOUtils.toString(httpResponse.getEntity().getContent());
      log.info("loginResponse: " + loginResponse);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /* SERVICE FUNCTION */
  public HttpResponse sendAndAssert(HttpRequestBase req, String service, String rdPath,
      int expectedStatus) throws Exception{
    //building the body
    BasicHttpEntity reqEntity = new BasicHttpEntity();

    //appending the file descriptor from a file
    reqEntity.setContent(new FileInputStream(rdPath));

    ((HttpEntityEnclosingRequestBase)req).setEntity(reqEntity);


    //executing the request
    return sendAndAssert(req, service, expectedStatus);

  }

  public HttpResponse sendAndAssert(HttpRequestBase req, String service, int expectedStatus) throws Exception{
    httpResponse = sendRequest(req, service, null, false);
//    Assert.assertTrue("basic response check did not pass", isValidResposnse(expectedStatus));
    return httpResponse;
  }

  // send a request to the CE server
  public HttpResponse sendRequest(HttpRequestBase req, String service, List<NameValuePair> qparams,
      boolean isV2) throws Exception
  {
    if(isV2)
      req.setURI(createURI(jasperServer.getBaseRestUrlV2()+service, qparams));
    else
      req.setURI(createURI(jasperServer.getBaseRestUrl()+service, qparams));


    log.info("req method: "+req.getMethod());

    httpResponse = httpClient.execute(req, httpContext);
    log.info("response status line: "+httpResponse.getStatusLine());
    return httpResponse;
  }

  private URI createURI(String path, List<NameValuePair> qParams) throws Exception{
    URI uri;
    if (qParams!=null)
      uri = URIUtils.createURI(jasperServer.getScheme(), jasperServer.getHost(), jasperServer.getPort(), path, URLEncodedUtils.format(qParams, "UTF-8"), null);
    else
      uri = (new URL(jasperServer.getScheme(), jasperServer.getHost(), jasperServer.getPort(), path)).toURI();

    log.info("sending Request. url: " +uri.toString());
    return uri;
  }

  protected boolean isValidResposnse() throws Exception{
    return isValidResposnse(HttpStatus.SC_OK);
  }

  protected boolean isValidResposnse(int expected_respose_code) throws Exception{
    return 	httpResponse.getStatusLine().getStatusCode()==expected_respose_code;
  }

  protected boolean isValidResposnse(HttpResponse res, int expected_respose_code) throws Exception{
    return 	httpResponse.getStatusLine().getStatusCode()==expected_respose_code;
  }

  public void releaseConnection (HttpResponse res) throws Exception{
    InputStream is = res.getEntity().getContent();
    is.close();
  }
}

