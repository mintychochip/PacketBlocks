package org.aincraft;

public enum SoundType {
  BREAK("break"),
  PLACE("place");

  private final String identifier;

  SoundType(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }
}
