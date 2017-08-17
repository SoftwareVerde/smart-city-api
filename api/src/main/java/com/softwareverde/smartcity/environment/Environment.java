package com.softwareverde.smartcity.environment;

import com.softwareverde.database.Database;

import java.sql.Connection;

public class Environment {
    public final Database<Connection> database;

    public Environment(final Database<Connection> database) {
        this.database = database;
    }
}
