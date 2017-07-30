package sasa.synapse.parser;

import java.sql.SQLException;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

import sasa.authorization.jersey.AuthorizationException;
import sasa.authorization.jersey.AuthorizationSQLLite;
import sasa.synapse.parser.entities.SynapseElement;
import sasa.synapse.parser.sqllite.DBConnector;
import sasa.synapse.parser.sqllite.StorageSQLLite;
import sasa.synapse.parser.storage.ISynapseStorage;

public class BinderOnlySqlLite extends AbstractBinder {

	private JdbcConnectionSource connetcorSqlLite;

	private boolean init = false;
	
	public BinderOnlySqlLite() throws SQLException {
		connetcorSqlLite = new JdbcConnectionSource(DBConnector.url);
	}

	public BinderOnlySqlLite(boolean fisrt) throws SQLException {
		connetcorSqlLite = new JdbcConnectionSource(DBConnector.url);
		init = fisrt;
	}
	
	@Override
	protected void configure() {
		try {
			StorageSQLLite storage = new StorageSQLLite(new DBConnector<SynapseElement>(new SynapseElement(), connetcorSqlLite));
			bind(storage).to(ISynapseStorage.class);
			
			AuthorizationSQLLite auth = new AuthorizationSQLLite(connetcorSqlLite);
			if(init){
				auth.init();
			}
			bind(auth).to(AuthorizationSQLLite.class);
			
		} catch (SQLException | AuthorizationException e) {
			e.printStackTrace();
		}
	}

}
