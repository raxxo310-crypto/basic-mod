# Basic Mod - NeoForge 1.21.1

A basic NeoForge mod template for Minecraft 1.21.1.

## Setup Instructions

### Prerequisites
- **Java 21** or higher
- **Gradle** (included via gradlew)
- **IDE**: IntelliJ IDEA recommended

### Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/raxxo310-crypto/basic-mod.git
   cd basic-mod
   ```

2. **Open in IntelliJ IDEA**
   - `File > Open > build.gradle`
   - Wait for Gradle to sync and download dependencies

3. **Run the mod**
   - Right panel: `Gradle > Tasks > fg_runs > runClient`
   - Or use the Gradle toolbar to run `runClient` task

4. **Build the mod**
   ```bash
   ./gradlew build
   ```
   - Output JAR: `build/libs/basicmod-1.0.0.jar`

## Project Structure

```
src/
├── main/
│   ├── java/com/raxxo310/basicmod/
│   │   └── BasicMod.java              # Main mod class
│   └── resources/
│       ├── META-INF/
│       │   └── mods.toml              # Mod metadata
│       └���─ assets/basicmod/           # Textures, models, sounds
│           ├── textures/
│           ├── models/
│           └── lang/
└── test/
```

## Next Steps

To expand your mod:

### Add Custom Items
```java
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(BuiltInRegistries.ITEM, BasicMod.MOD_ID);
    
    public static final Supplier<Item> CUSTOM_ITEM = 
        ITEMS.register("custom_item", () -> new Item(new Item.Properties()));
}
```

### Add Custom Blocks
```java
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(BuiltInRegistries.BLOCK, BasicMod.MOD_ID);
    
    public static final Supplier<Block> CUSTOM_BLOCK = 
        BLOCKS.register("custom_block", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
        ));
}
```

## Resources

- [NeoForge Documentation](https://docs.neoforged.net/)
- [NeoForge GitHub](https://github.com/neoforged/NeoForge)
- [Minecraft Wiki](https://minecraft.wiki/)

## License

MIT License - Feel free to use this as a template!