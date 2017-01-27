package com.org.kobito.account.model;

import com.datastax.driver.core.utils.UUIDs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

/**
 * Created by v_nam on 2017/01/26.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Account {

    private String firstname;

    private String lastname;

    @PrimaryKey
    private UUID id;

}
