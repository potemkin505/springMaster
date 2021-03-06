package org.feargus.springmaster.db;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.feargus.springmaster.utils.SystemVars;

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

    public DataSource getDefaultDataSource() {
	URI dbUri;
	try {
	    dbUri = new URI(SystemVars.getInstance().getDB_URL());
	    dbUsername = dbUri.getUserInfo().split(":")[0];
	    dbPassword = dbUri.getUserInfo().split(":")[1];
	    dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

	    basicDataSource.setUrl(dbUrl);
	    basicDataSource.setUsername(dbUsername);
	    basicDataSource.setPassword(dbPassword);

	} catch (URISyntaxException e) {
	    e.printStackTrace();
	    // TODO: If exception, we're returning the DS anyway. Fix this.
	}

	return basicDataSource;
    }
}
