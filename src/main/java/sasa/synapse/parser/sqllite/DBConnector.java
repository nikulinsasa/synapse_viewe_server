package sasa.synapse.parser.sqllite;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import sasa.synapse.parser.entities.DependencySynapseElements;

public class DBConnector<T> {

	public static final String url = "jdbc:sqlite:main.sqlite";
	
	private JdbcConnectionSource source;
    
	private Dao<T, String> dao;
	
	private T object;
 
    @SuppressWarnings("unchecked")
	public DBConnector(T _class) throws SQLException {
    	object = _class;
        source = new JdbcConnectionSource(url);
        dao = (Dao<T, String>) DaoManager.createDao(source,object.getClass());
    }
    
    @SuppressWarnings("unchecked")
	public DBConnector(T _class, JdbcConnectionSource source) throws SQLException {
    	object = _class;
        this.source = source;
        dao = (Dao<T, String>) DaoManager.createDao(this.source,object.getClass());
    }
 
	public JdbcConnectionSource getSource() {
		return source;
	}

	public void createTable() throws SQLException {
    	TableUtils.createTableIfNotExists(source, object.getClass());
    }
    
    public void insert(T object) throws SQLException{
    	this.object = object;
    	insert();
    }
    
    public void insert() throws SQLException{
    	dao.createOrUpdate(object);
    }
    
    public void update(T object, String id) throws SQLException {
    	dao.createOrUpdate(object);
    	dao.clearObjectCache();
    }
	
    public List<T> find(Map<String, Object> map) throws SQLException {
    	dao.clearObjectCache();
    	return createQuery(map).query();
    }
    
    public T findOne(Map<String, Object> map) throws SQLException {
    	dao.clearObjectCache();
    	return createQuery(map).queryForFirst();
    }
    
    private QueryBuilder<T, String> createQuery(Map<String, Object> map) throws SQLException {
    	QueryBuilder<T, String> queryBuilder = dao.queryBuilder();
    	for(Entry<String, Object> _entry: map.entrySet()){
    		SelectArg arg = new SelectArg();
    		Where<T, String> _where = queryBuilder.where();
        	_where.eq(_entry.getKey(), arg);
        	arg.setValue(_entry.getValue());
    	}
    	return queryBuilder;
    }
    
    public QueryBuilder<T, String> createQueryBuilder() {
    	return dao.queryBuilder();
    }
    
    public void delete(Map<String, Object> map) throws SQLException {
    	dao.delete(findOne(map));
    }
    
    public void deleteAll() throws SQLException{
    	dao.deleteBuilder().delete();
    }
    
}
