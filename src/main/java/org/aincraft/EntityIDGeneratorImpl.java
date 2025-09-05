package org.aincraft;

import com.google.common.base.Preconditions;

import java.util.*;

final class EntityIDGeneratorImpl implements EntityIDGenerator {

  private final int capacity;

  private final Queue<UUID> free = new ArrayDeque<>();
  private final Set<UUID> inUse = new HashSet<>();
  private final Set<UUID> allGenerated = new HashSet<>();

  EntityIDGeneratorImpl(int capacity) {
    Preconditions.checkArgument(capacity > 0, "Capacity must be positive");
    this.capacity = capacity;
  }

  @Override
  public UUID getNext() {
    Preconditions.checkArgument(hasNext(), "No more IDs can be generated");

    UUID id;
    if (!free.isEmpty()) {
      id = free.poll();
    } else {
      do {
        id = UUID.randomUUID();
      } while (allGenerated.contains(id));
      allGenerated.add(id);
    }

    inUse.add(id);
    return id;
  }

  @Override
  public void free(UUID id) {
    Preconditions.checkNotNull(id, "ID cannot be null");
    Preconditions.checkArgument(inUse.contains(id), "ID was not in use");

    inUse.remove(id);
    free.add(id);
  }

  @Override
  public boolean hasNext() {
    return getSize() < capacity;
  }

  @Override
  public int getCapacity() {
    return capacity;
  }

  @Override
  public int getSize() {
    return inUse.size();
  }
}
