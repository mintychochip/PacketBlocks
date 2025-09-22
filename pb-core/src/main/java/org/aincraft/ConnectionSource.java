package org.aincraft;

import java.sql.Connection;

public interface ConnectionSource {
  Connection getConnection();
}
