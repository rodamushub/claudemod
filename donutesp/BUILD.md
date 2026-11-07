# DonutESP — Build Instructions

## Requirements
- Java 17+
- Internet connection (Gradle downloads Fabric/Minecraft on first run)

## Steps

```bash
# 1. Clone / extract this folder
cd donutesp

# 2. Make Gradle wrapper executable (Linux/Mac)
chmod +x gradlew

# 3. Build
./gradlew build          # Windows: gradlew.bat build

# 4. Output jar
#    build/libs/donutesp-1.0.0.jar
```

## Install
1. Copy `donutesp-1.0.0.jar` → `.minecraft/mods/`
2. Requires: Fabric Loader 0.15.6+, Fabric API 0.95.4+1.20.4
3. Minecraft version: 1.20.4

## Keybinds
| Key | Action |
|---|---|
| Right Shift | Open GUI |
| G | Toggle ESP |
| F | Toggle FreeCam |
| B | Toggle BaseFinder |

## Notes
- The mod presents as "Continuity 3.0.0" to the server
- Brand packet spoofed to "vanilla"
- Mod-sync channels blocked

## If gradlew fails (no wrapper jar)
Install Gradle 8.5 system-wide and run:
```bash
gradle wrapper --gradle-version=8.5
./gradlew build
```
Or open in IntelliJ IDEA — it will download everything automatically.
