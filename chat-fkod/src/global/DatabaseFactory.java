package global;

public class DatabaseFactory {
	public static Database getDatabase(Vendor vendor, String id, String pass){
		String driver= "", url= "";
			switch (vendor) {
			case ORACLE: driver = Constants.ORACLE_DRIVER;
						 url = Constants.ORACLE_URL;	
				break;
			case MYSQL:	driver = Constants.MYSQL_DRIVER;
						url = Constants.MYSQL_URL;
				break;
			case MSSQL:	driver = Constants.MSSQL_DRIVER;
						url = Constants.MSSQL_URL;
				break;
			case MARIADB:driver = Constants.MARIADB_DRIVER;
						url = Constants.MARIADB_URL;	
				break;
			case MONGODB:driver = Constants.MONGODB_DRIVER;
						url = Constants.MONGODB_URL;	
				break;
			default:
				break;
			}
	return new 	DatabaseImpl(driver, url, id, pass);
	}
}
