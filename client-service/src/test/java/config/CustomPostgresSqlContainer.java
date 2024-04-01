package config;

import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPostgresSqlContainer extends PostgreSQLContainer<CustomPostgresSqlContainer> {
    private static final String IMAGE_VERSION = "postgres:11.1";
    private static CustomPostgresSqlContainer container;

    private CustomPostgresSqlContainer() {
        super(IMAGE_VERSION);
    }

    public static CustomPostgresSqlContainer getInstance() {
        if (container == null) {
            container = new CustomPostgresSqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}