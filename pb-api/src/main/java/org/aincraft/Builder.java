package org.aincraft;

public interface Builder<T> {

  T build();

  interface Buildable<B extends Builder<T>, T> {

    B toBuilder();
  }
}
