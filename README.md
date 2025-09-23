# PacketBlocks

An alternative to “baked blocks” (e.g., retexturing Note/Mushroom/Chorus Fruit blocks). PacketBlocks creates item-displays **client-side**, letting you create near-unlimited variants without consuming server block IDs.

## Why PacketBlocks?

- **Server impact:** *minimal.* Tick time and memory stay almost untouched compared to using server-side display entities (which add ticking, pathing/visibility checks, and steady packet churn).
- **Client impact:** *higher.* Rendering and VRAM cost shifts to the player’s machine, so FPS and frame time scale with how many PacketBlocks are visible.

### Installation

1. Download the latest release from the [releases page](https://github.com/mintychochip/PacketBlocks/releases).
2. Place the JAR file into your server's plugin directory
3. Restart the server

### Developer API

```groovy
repositories {
    mavenCentral()
    // If PacketBlocks is published to a custom or GitHub Packages repository, add that repository here:
    // maven { url 'https://repo.yourdomain.com/releases' }
    // or for GitHub Packages:
    // maven { url = uri("https://maven.pkg.github.com/mintychochip/PacketBlocks") }
}

dependencies {
    // Replace 'VERSION' with the latest release or snapshot version of PacketBlocks
    implementation 'com.mintychochip:PacketBlocks:VERSION'
}
```

> **Note:**  
> Replace `VERSION` with the latest version tag or commit hash, for example: `1.0.0` or `master-SNAPSHOT`.>

Listen to block-related actions by listening to `PacketBlockBreakEvent`,`PacketBlockPlaceEvent` etc...

Example:
```java
import com.mintychochip.packetblocks.event.PacketBlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MyBlockListener implements Listener {
    @EventHandler
    public void onPacketBlockBreak(PacketBlockBreakEvent event) {
        // Your custom logic here
        System.out.println("A block was broken by PacketBlocks: " + event.getBlock().getType());
    }
}
```

### License

This project is licensed under the GNU General Public License (GPL). See the [LICENSE](LICENSE) file for details.

### Support

Need help? Open an issue or join the discussion on the [discord](https://discord.gg/Uh4NMurJ).

---

Crafted with ❤️ by [mintychochip](https://github.com/mintychochip)

