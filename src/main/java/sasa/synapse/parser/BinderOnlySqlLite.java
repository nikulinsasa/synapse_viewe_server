package sasa.synapse.parser;

import java.sql.SQLException;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

import sasa.authorization.jersey.AuthorizationException;
import sasa.authorization.jersey.AuthorizationSQLLite;
import sasa.synapse.parser.entities.SynapseElement;
import sasa.synapse.parser.sqllite.DBConnector;
import sasa.synapse.parser.sqllite.ElementDescriptionStorage;
import sasa.synapse.parser.sqllite.ElementIOExampleStorage;
import sasa.synapse.parser.sqllite.StorageSQLLite;
import sasa.synapse.parser.storage.ISynapseStorage;
import sasa.synapse.parser.storage.StorageException;

public class BinderOnlySqlLite extends AbstractBinder {

	private JdbcConnectionSource connetcorSqlLite;

	private boolean init = false;
	
	public BinderOnlySqlLite() throws SQLException {
		connetcorSqlLite = new JdbcConnectionSource(DBConnector.url);
	}

	public BinderOnlySqlLite(boolean first) throws SQLException {
		connetcorSqlLite = new JdbcConnectionSource(DBConnector.url);
		init = first;
	}
	
	@Override
	protected void configure() {
		try {
			StorageSQLLite storage = new StorageSQLLite(new DBConnector<SynapseElement>(new SynapseElement(), connetcorSqlLite));
			bind(storage).to(ISynapseStorage.class);
			
			try {
				bind(new ElementDescriptionStorage(connetcorSqlLite)).to(ElementDescriptionStorage.class);
				bind(new ElementIOExampleStorage(connetcorSqlLite)).to(ElementIOExampleStorage.class);
			} catch (StorageException e) {
				e.printStackTrace();
			}
			
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
