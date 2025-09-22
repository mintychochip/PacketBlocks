package org.aincraft;

import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class BridgeAccessor {

  private static final AtomicReference<Bridge> BRIDGE = new AtomicReference<>();

  static Bridge bridgeAccess() throws IllegalStateException {
    Bridge current = BRIDGE.get();
    if (current != null) {
      return current;
    }
    RegisteredServiceProvider<Bridge> bridgeRegistration = Bukkit.getServicesManager()
        .getRegistration(Bridge.class);
    if (bridgeRegistration == null) {
      throw new IllegalStateException("failed to locate bridge");
    }
    Bridge created = bridgeRegistration.getProvider();
    BRIDGE.compareAndSet(null, created);
    return created;
  }
}
