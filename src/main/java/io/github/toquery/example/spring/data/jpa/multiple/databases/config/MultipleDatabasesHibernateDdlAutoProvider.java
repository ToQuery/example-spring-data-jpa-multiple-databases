package io.github.toquery.example.spring.data.jpa.multiple.databases.config;

import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.SchemaManagement;
import org.springframework.boot.jdbc.SchemaManagementProvider;

import javax.sql.DataSource;
import java.util.stream.StreamSupport;

/**
 *
 */
public class MultipleDatabasesHibernateDdlAutoProvider implements SchemaManagementProvider {

    private final Iterable<SchemaManagementProvider> providers;

    MultipleDatabasesHibernateDdlAutoProvider(Iterable<SchemaManagementProvider> providers) {
        this.providers = providers;
    }

    String getDefaultDdlAuto(DataSource dataSource) {
        if (!EmbeddedDatabaseConnection.isEmbedded(dataSource)) {
            return "none";
        }
        SchemaManagement schemaManagement = getSchemaManagement(dataSource);
        if (SchemaManagement.MANAGED.equals(schemaManagement)) {
            return "none";
        }
        return "create-drop";
    }

    @Override
    public SchemaManagement getSchemaManagement(DataSource dataSource) {
        return StreamSupport.stream(this.providers.spliterator(), false)
                .map((provider) -> provider.getSchemaManagement(dataSource)).filter(SchemaManagement.MANAGED::equals)
                .findFirst().orElse(SchemaManagement.UNMANAGED);
    }

}
