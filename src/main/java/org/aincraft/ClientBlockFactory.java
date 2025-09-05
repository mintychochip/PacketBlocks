package org.aincraft;

import java.util.Map;
import java.util.UUID;

public class ClientBlockFactory {

  private final Map<UUID, EntityIDGenerator> generators;

  public ClientBlockFactory(Map<UUID, EntityIDGenerator> generators) {
    this.generators = generators;
  }
}
