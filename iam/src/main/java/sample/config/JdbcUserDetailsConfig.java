package sample.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

@Slf4j
@Configuration
public class JdbcUserDetailsConfig {

    @SneakyThrows
    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() {
        EmbeddedDatabase embeddedDatabase = embeddedDatabase();

        DatabaseMetaData metaData = embeddedDatabase.getConnection().getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"});

        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            log.info("tableName: {}", tableName);
        }
        return new JdbcUserDetailsManager(embeddedDatabase);
    }

    @Bean
    public EmbeddedDatabase embeddedDatabase() {
        // @formatter:off
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql")
                .addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql")
                .addScript("org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql")
                .addScript("org/springframework/security/core/userdetails/jdbc/users.ddl")
                .build();
        // @formatter:on
    }
}
