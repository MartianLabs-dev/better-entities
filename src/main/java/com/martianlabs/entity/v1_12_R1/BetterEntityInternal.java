package com.martianlabs.entity.v1_12_R1;

import com.martianlabs.entity.api.BetterEntity;
import com.martianlabs.entity.api.BetterEntityModel;
import com.martianlabs.entity.api.Resistance;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import lombok.var;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class BetterEntityInternal extends EntityMonster {
    private final BetterEntity wrapper;

    private final Set<Player> damagers;

    private int ticksToUpdateBossBar;

    public BetterEntityInternal(World world, BetterEntityModel model) {
        super(world);

        this.wrapper = new BetterEntity() {
            @Getter(AccessLevel.PRIVATE) private final BetterEntityInternal handle = BetterEntityInternal.this;

            @Getter private final String name = model.getName();
            @Getter private final BetterEntityType type = model.getType();
            @Getter private final BossBar bossBar = model.getBossBar();

            @Getter private String displayName;
            @Getter private String displayNamePattern;

            @Getter private final Set<Resistance> resistances = new HashSet<>();

            @Override
            public void setDisplayName(String value) {
                this.displayName = value;
                this.getHandle().updateDisplayName();
            }

            @Override
            public void setDisplayNamePattern(String value) {
                this.displayNamePattern = value;
                this.getHandle().updateDisplayName();
            }

            @Override
            public float getHealth() {
                return this.getHandle().getHealth();
            }

            @Override
            public void setHealth(float value) {
                this.getHandle().setHealth(value);
            }

            @Override
            public double getMaxHealth() {
                return this.getHandle().getAttributeInstance(GenericAttributes.maxHealth).getValue();
            }

            @Override
            public void setMaxHealth(double value) {
                this.getHandle().getAttributeInstance(GenericAttributes.maxHealth).setValue(value);
            }

            @Override
            public void setDefence(double value) {
                this.getHandle().getAttributeInstance(GenericAttributes.h).setValue(value);
            }

            @Override
            public void setSpeed(double value) {
                this.getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(GenericAttributes.MOVEMENT_SPEED.getDefault() * value);
            }

            @Override
            public void setBaby(boolean value) {
                // TODO
            }

            @Override
            public void setLocation(Location location) {
                this.getHandle().setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            }

            @Override
            public void spawn(org.bukkit.World world) {
                ((CraftWorld) world).getHandle().addEntity(this.getHandle(), CreatureSpawnEvent.SpawnReason.CUSTOM);
            }

            @Override
            public void setEquipment(EquipmentSlot slot, ItemStack stack)   {
                this.getHandle().setSlot(toNms(slot), CraftItemStack.asNMSCopy(stack));
            }

            @Override
            public void addResistance(Resistance value) {
                if (this.resistances.contains(value))
                    return;

                this.resistances.add(value);

                if (value == Resistance.KNOCKBACK)
                    this.getHandle().getAttributeInstance(GenericAttributes.c).setValue(1.0D);
            }

            @Override
            public void removeResistance(Resistance value) {
                if (!this.resistances.contains(value))
                    return;

                this.resistances.remove(value);

                if (value == Resistance.KNOCKBACK)
                    this.getHandle().getAttributeInstance(GenericAttributes.c).setValue(0.0D);
            }

            @Override
            public boolean hasResistance(Resistance value) {
                return this.resistances.contains(value);
            }

            @Override
            public ItemStack getEquipment(EquipmentSlot slot) {
                return CraftItemStack.asBukkitCopy(this.getHandle().getEquipment(toNms(slot)));
            }

            private EnumItemSlot toNms(EquipmentSlot slot) {
                switch (slot) {
                    case HAND:
                        return EnumItemSlot.MAINHAND;
                    case OFF_HAND:
                        return EnumItemSlot.OFFHAND;
                    case FEET:
                        return EnumItemSlot.FEET;
                    case LEGS:
                        return EnumItemSlot.LEGS;
                    case CHEST:
                        return EnumItemSlot.CHEST;
                    case HEAD:
                        return EnumItemSlot.HEAD;
                }
                throw new IllegalArgumentException();
            }
        };

        this.setCustomNameVisible(true);

        this.getWrapper().setDisplayName(model.getDisplayName());
        this.getWrapper().setDisplayNamePattern(model.getDisplayNamePattern());
        this.getWrapper().setMaxHealth(model.getMaxHealth());
        this.getWrapper().setHealth((float)model.getMaxHealth());
        this.getWrapper().setDefence(model.getDefence());
        this.getWrapper().setSpeed(model.getSpeed());
        this.getWrapper().setBaby(model.isBaby());
        for (val slot : EquipmentSlot.values())
            this.getWrapper().setEquipment(slot, model.getEquipment(slot));
        for (val resistance : model.getResistances())
            this.getWrapper().addResistance(resistance);

        this.updateDisplayName();

        this.damagers = new HashSet<>();
    }

    private void updateDisplayName() {
        var name = this.getWrapper().getDisplayNamePattern();

        if (name == null) {
            this.setCustomName(this.getWrapper().getDisplayName());

            return;
        }

        name = ChatColor.translateAlternateColorCodes('&', name);

        name = name.replaceAll("%name%", this.getWrapper().getDisplayName());

        name = name.replaceAll("%health%", String.valueOf(Math.round(this.getWrapper().getHealth())));

        // TODO: More placeholders

        this.setCustomName(name);
    }

    @Override
    public void postTick() {
        super.postTick();

        if (this.ticksToUpdateBossBar < 5) {
            this.ticksToUpdateBossBar++;
        } else {
            this.ticksToUpdateBossBar = 0;
            this.updateBossBar();
        }
    }

    private void updateBossBar() {
        val bossBar = this.getWrapper().getBossBar();

        bossBar.setProgress(this.getWrapper().getHealth() / this.getWrapper().getMaxHealth());

        val viewers = new HashSet<>(bossBar.getPlayers());

        if (this.isAlive()) {
            val nearby = getNearbyPlayers();

            for (val player : nearby)
                if (!viewers.contains(player))
                    bossBar.addPlayer(player);
                else
                    viewers.remove(player);
        }

        for (val player : viewers)
            bossBar.removePlayer(player);
    }

    private Collection<Player> getNearbyPlayers() {
        return this.getWorld()
                .getEntities(
                        this,
                        new AxisAlignedBB(
                                this.getX() - 16,
                                this.getY() - 16,
                                this.getZ() - 16,
                                this.getX() + 16,
                                this.getY() + 16,
                                this.getZ() + 16),
                        x -> x instanceof EntityPlayer)
                .stream()
                .map(
                        x -> (Player) x.getBukkitEntity()
                )
                .collect(Collectors.toList());
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (damagesource instanceof EntityDamageSourceIndirect) {
            val projectile = damagesource.i();

            if (projectile instanceof EntityArrow && this.getWrapper().hasResistance(Resistance.ARROW))
                return false;

            if (projectile instanceof EntityPotion && this.getWrapper().hasResistance(Resistance.POTION))
                return false;
        }

        val result = super.damageEntity(damagesource, f);

        if (!result) return false;

        if (damagesource instanceof EntityDamageSource) {
            val entity = damagesource.getEntity();

            if (entity instanceof EntityPlayer) {
                val player = ((EntityPlayer) entity).getBukkitEntity();

                this.damagers.add(player);

                val builder = new StringBuilder();

                builder.append("&c");

                val hearts = (int) Math.round(this.getWrapper().getHealth() / this.getWrapper().getMaxHealth() * 10);

                for (var i = 0; i < hearts; i++) {
                    builder.append("❤");
                }

                builder.append("&7");

                for (var i = hearts; i < 10; i++) {
                    builder.append("❤");
                }

                player.sendActionBar(ChatColor.translateAlternateColorCodes('&', builder.toString()));
            }
        }

        this.updateDisplayName();
        this.updateBossBar();

        return true;
    }
}
