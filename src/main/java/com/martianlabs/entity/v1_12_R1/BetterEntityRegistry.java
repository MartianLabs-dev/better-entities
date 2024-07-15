package com.martianlabs.entity.v1_12_R1;

import com.martianlabs.entity.api.BetterEntity;
import com.martianlabs.entity.api.BetterEntityModel;
import lombok.SneakyThrows;
import lombok.val;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodCall;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class BetterEntityRegistry {
    private static final Map<String, Class<? extends BetterEntityInternal>> REGISTRY = new HashMap<>();

    @SneakyThrows
    public static void register(BetterEntityModel model) {
        val key = new MinecraftKey("better-entity", model.getName());

        val type = new ByteBuddy()
                .subclass(BetterEntityInternal.class)
                .defineConstructor(Modifier.PUBLIC)
                .withParameters(World.class)
                .intercept(MethodCall.invoke(BetterEntityInternal.class.getConstructor(World.class, BetterEntityModel.class))
                        .withArgument(0)
                        .with(model))
                .make()
                .load(model.getClass().getClassLoader())
                .getLoaded();

        val id = model.getType().getBukkitType().getTypeId();

        REGISTRY.put(model.getName(), type);

        EntityTypes.b.a(id, key, type);
    }

    @SneakyThrows
    public static BetterEntity spawn(Location location, String name) {
        if (!REGISTRY.containsKey(name))
            throw new IllegalArgumentException("Entity with name \"" + name + "\" is not registered");

        val model = REGISTRY.get(name);

        val world = ((CraftWorld) location.getWorld()).getHandle();

        val entity = model.getConstructor(World.class).newInstance(world).getWrapper();

        entity.setLocation(location);

        entity.spawn(location.getWorld());

        return entity;
    }
}
