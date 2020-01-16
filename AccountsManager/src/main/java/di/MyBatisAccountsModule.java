package di;

import com.google.inject.name.Names;
import configuration.MybatisConfiguration;
import db.mappers.AccountsMapper;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import java.util.Properties;

public class MyBatisAccountsModule extends MyBatisModule {


    private MybatisConfiguration mybatisConfiguration;


    public MyBatisAccountsModule(MybatisConfiguration mybatisConfiguration) {
        this.mybatisConfiguration = mybatisConfiguration;
    }


    @Override
    protected void initialize() {
        install(JdbcHelper.MySQL);
        bind(DefaultObjectWrapperFactory.class);
        bind(DefaultObjectFactory.class);
        bindDataSourceProviderType(PooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        Names.bindProperties(binder(), setProperties());
        addMapperClass(AccountsMapper.class);
    }

    private Properties setProperties() {
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
