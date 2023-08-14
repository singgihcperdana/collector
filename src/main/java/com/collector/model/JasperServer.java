package com.collector.model;

public class JasperServer {

  public JasperServer() {
  }

  private String scheme;
  private String host;
  private int port;
  private String username;
  private String password;

  private final String serverHandle = "/jasperserver";
  private final String baseRestUrl = serverHandle+"/rest";
  private final String baseRestUrlV2 = serverHandle+"/rest_v2";
  private final String baseReport = "/reports";

  // login parameters
  private final String parameterUsername = "j_username";
  private final String parameterPassword = "j_password";
  public final String serviceLogin = "/login";

  public String getScheme() {
    return scheme;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getServerHandle() {
    return serverHandle;
  }

  public String getBaseRestUrl() {
    return baseRestUrl;
  }

  public String getBaseRestUrlV2() {
    return baseRestUrlV2;
  }

  public String getBaseReport() {
    return baseReport;
  }

  public String getParameterUsername() {
    return parameterUsername;
  }

  public String getParameterPassword() {
    return parameterPassword;
  }

  public String getServiceLogin() {
    return serviceLogin;
  }
}
