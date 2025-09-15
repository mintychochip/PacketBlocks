package org.aincraft.loot;

public interface WeightedRandom<T> {

  static <T> WeightedRandom<T> create() {
    return new WeightedRandomImpl<>();
  }

  T get() throws IllegalStateException;

  void add(T item, double weight);
}
