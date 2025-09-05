package org.aincraft;

import java.util.UUID;

public interface EntityIDGenerator {

  UUID getNext() throws IllegalArgumentException;

  void free(UUID id) throws IllegalArgumentException;

  boolean hasNext();

  int getCapacity();

  int getSize();
}
