package org.feargus.springmaster.model;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.dbcp.BasicDataSource;

public class PostgresqlDataSource {
	private BasicDataSource basicDataSource;
	private String dbUrl;
	private String dbUsername;
	private String dbPassword;
	
	public PostgresqlDataSource() {
		basicDataSource = new BasicDataSource();
		dbUrl = null;
		dbUsername = null;
		dbPassword = null;
	}
	
	public BasicDataSource getDefaultDataSource(){
		URI dbUri;
		try {
			dbUri = new URI(System.getenv("DATABASE_URL"));
			//dbUri = new URI(System.getenv("DATABASE_URL_TEST"));
			
			dbUsername = dbUri.getUserInfo().split(":")[0];
			dbPassword = dbUri.getUserInfo().split(":")[1];
	        dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
	        
	        basicDataSource.setUrl(dbUrl);
	        basicDataSource.setUsername(dbUsername);
	        basicDataSource.setPassword(dbPassword);
	        
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return basicDataSource;
	}
	
}