package itstep.learning.services.db;

import oracle.jdbc.datasource.impl.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class OracleDbService implements DbService {
    private Connection connection;

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            OracleDataSource ods = new OracleDataSource();
            // ods. setURL( "jdbc: oracle: thin: @localhost/XEPDB1" );
// connection = ods. getConnection( "user_213", "pass_213" );
            ods.setURL("jdbc:oracle: thin:@localhost:1521:XE");
            connection = ods.getConnection("system", "root");
        }
            return connection;
    }
}
