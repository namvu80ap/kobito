package com.org.kobito.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

/**
 * Created by v_nam on 2017/01/26.
 */
//@Configuration
//@EnableReactiveCassandraRepositories
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
//@ComponentScan("com.org.kobito")
public class CassandraConfiguration extends AbstractReactiveCassandraConfiguration {

    @Autowired
    private Environment env;

    @Override
    protected String getKeyspaceName() {
        return env.getProperty("spring.data.cassandra.keyspace-name");
    }

    public String getUsername() {
        return env.getProperty("spring.data.cassandra.username");
    }

    public String getPassword() {
        return env.getProperty("spring.data.cassandra.password");
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected String getContactPoints() {
        return env.getProperty("spring.data.cassandra.contact-points");
    }

    @Override
    protected int getPort() {
        return Integer.parseInt(env.getProperty("spring.data.cassandra.port"));
    }
}
