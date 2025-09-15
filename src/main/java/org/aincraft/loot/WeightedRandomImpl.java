package org.aincraft.loot;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class WeightedRandomImpl<T> implements WeightedRandom<T> {

  private final List<Double> prefixWeights = new ArrayList<>();

  private final List<T> items = new ArrayList<>();

  private double cumulative = 0.0;

  @Override
  public T get() throws IllegalStateException {
    Preconditions.checkState(items.size() == prefixWeights.size());
    Preconditions.checkState(!items.isEmpty());
    Preconditions.checkState(cumulative > 0.0);
    double randomValue = ThreadLocalRandom.current().nextDouble(0, cumulative);
    int index = Collections.binarySearch(prefixWeights, randomValue);
    if (index < 0) {
      index = -(index + 1);
    }
    return items.get(index);
  }

  @Override
  public void add(T item, double weight) {
    Preconditions.checkArgument(!Double.isNaN(weight) && weight >= 0.0);
    items.add(item);
    prefixWeights.add(cumulative += weight);
  }
}

