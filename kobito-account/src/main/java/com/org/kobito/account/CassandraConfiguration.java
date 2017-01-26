package com.org.kobito.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

/**
 * Created by v_nam on 2017/01/26.
 */
@EnableReactiveCassandraRepositories
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Autowired
    private Environment env;

    @Override
    protected String getKeyspaceName() {
        return env.getProperty("spring.data.cassandra.keyspace-name");
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.RECREATE;
    }
}
