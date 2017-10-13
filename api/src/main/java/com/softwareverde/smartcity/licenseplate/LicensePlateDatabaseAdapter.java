package com.softwareverde.smartcity.licenseplate;

import com.softwareverde.database.DatabaseConnection;
import com.softwareverde.database.DatabaseException;
import com.softwareverde.database.Query;
import com.softwareverde.database.Row;

import java.sql.Connection;
import java.util.List;

public class LicensePlateDatabaseAdapter {

    private DatabaseConnection<Connection> _databaseConnection;

    public LicensePlateDatabaseAdapter(final DatabaseConnection<Connection> databaseConnection) {
        _databaseConnection = databaseConnection;
    }

    public LicensePlate inflateById(final Long licensePlateId) throws DatabaseException {
        final Query query = new Query("SELECT * FROM license_plates WHERE id = ?");
        query.setParameter(licensePlateId);

        final List<Row> rows = _databaseConnection.query(query);
        if (rows.size() > 0) {
            final Row row = rows.get(0);
            final LicensePlate licensePlate = _fromRow(row);
            return licensePlate;
        }
        return null;
    }

    private LicensePlate _fromRow(final Row row) {
        final Long id = row.getLong("id");
        final String number = row.getString("number");
        final String state = row.getString("state");

        final LicensePlate licensePlate = new LicensePlate();

        licensePlate.setId(id);
        licensePlate.setNumber(number);
        licensePlate.setState(state);

        return licensePlate;
    }
}
