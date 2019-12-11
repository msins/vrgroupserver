package edu.vrgroup;

import edu.vrgroup.database.JpaEntityManagerFactoryProvider;
import java.util.Map;
import javax.persistence.Persistence;

public final class DatabaseUtil {

  public static void init(String name, String password) {
    JpaEntityManagerFactoryProvider.setEmf(
        Persistence.createEntityManagerFactory("vr.server.db", Map.of(
            "hibernate.connection.username", name,
            "hibernate.connection.password", password))
    );

  }

}
