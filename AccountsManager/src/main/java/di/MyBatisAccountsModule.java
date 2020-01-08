package di;

import com.google.inject.Provides;
import com.google.inject.name.Names;
import configuration.MybatisConfiguration;
import db.dao.AccountsDao;
import db.mappers.AccountsMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import java.io.File;
import java.util.Properties;

public class MyBatisAccountsModule extends MyBatisModule {


    private MybatisConfiguration mybatisConfiguration;


    public MyBatisAccountsModule(MybatisConfiguration mybatisConfiguration){
        this.mybatisConfiguration=mybatisConfiguration;
    }

//    @Provides
//    public MybatisConfiguration mybatisConfiguration() {
//        MybatisConfiguration mybatisConfiguration;
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
//        try {
//            mybatisConfiguration = mapper.readValue(new File("/usr/myBatis.config"), MybatisConfiguration.class);
//            return mybatisConfiguration;
//        }
//        catch (Exception e ){
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    protected void initialize() {
        install(JdbcHelper.MySQL);
        bindDataSourceProviderType(PooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        Names.bindProperties(binder(), setProperties());
        addMapperClass(AccountsMapper.class);
        bind(AccountsDao.class);
    }

    private Properties setProperties(){
        Properties myBatisProperties = new Properties();
        myBatisProperties.setProperty("mybatis.environment.id", mybatisConfiguration.getMybatisEnvironmentId());
        myBatisProperties.setProperty("JDBC.schema", mybatisConfiguration.getSchema());
        myBatisProperties.setProperty("JDBC.username", mybatisConfiguration.getDataBaseUsername());
        myBatisProperties.setProperty("JDBC.password", mybatisConfiguration.getDataBaseUserPassword());
        myBatisProperties.setProperty("JDBC.autoCommit", String.valueOf(mybatisConfiguration.isAutoCommit()));
        myBatisProperties.setProperty("JDBC.host", mybatisConfiguration.getDataBaseHost());
        myBatisProperties.setProperty("JDBC.port", String.valueOf(mybatisConfiguration.getDataBasePort()));

        return myBatisProperties;
    }
}
