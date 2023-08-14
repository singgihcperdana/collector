package com.collector.constant;


public class ConstantJasperServer {
	public static final boolean SHOW_SPEC_MODE = false; 
	
	// SERVER PARAMETERS

	public static final String SCHEME = "http";
	public static final String HOST = "devs.local";
	public static final int PORT = 8090;
	public static final String USER_NAME = "singgih";
	public static final String PASSWORD = "qwerty123ABC*#";

	public static final String SERVER_HANDLE = "/jasperserver";
	
	//Server paths
	public static final String BASE_REST_URL = SERVER_HANDLE+"/rest";
	public static final String BASE_REST_URL_V2 = SERVER_HANDLE+"/rest_v2";
	public static final String BASE_REPORT = "/reports";

	// login parameters
	public static final String PARAMETER_USERNAME = "j_username";
	public static final String PARAM_PASSWORD = "j_password";
	
	// SERVER ENTITIES
	public static final String SERVICE_LOGIN = "/login";
	public static final String RESOURCES_LOCAL_PATH = "resources/";
	public static final String SAMPLE_FOLDER_RD = "folder_URI.SAMPLE_REST_FOLDER.xml";
	public static final String SAMPLE_IMAGE_RD = "image_URI.JUNIT_NEW_FOLDER.JUNIT_IMAGE_FILE.xml";
	public static final String SAMPLE_IMAGE_BIN = "jasperSoftLogo.jpg";
	public static final String NEW_SAMPLE_IMAGE_BIN = "jasperSoftLogo_2.jpg";
	public static final String REQUEST_PARAMENTER_RD = "ResourceDescriptor";
	public static final String RESOURCE = "/resource";
	public static final String LOG4J_PATH = "log4j.properties";
}
