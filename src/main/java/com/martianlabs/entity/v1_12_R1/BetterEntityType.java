package com.martianlabs.entity.v1_12_R1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.entity.EntityType;

@Getter
@AllArgsConstructor
public enum BetterEntityType {
    ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN, EntityGuardianElder.class),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, EntitySkeletonWither.class),
    STRAY(EntityType.STRAY, EntitySkeletonStray.class),
    HUSK(EntityType.HUSK, EntityZombieHusk.class),
    SKELETON_HORSE(EntityType.SKELETON_HORSE, EntityHorseSkeleton.class),
    ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, EntityHorseZombie.class),
    DONKEY(EntityType.DONKEY, EntityHorseDonkey.class),
    MULE(EntityType.MULE, EntityHorseMule.class),
    EVOKER(EntityType.EVOKER, EntityEvoker.class),
    VEX(EntityType.VEX, EntityVex.class),
    VINDICATOR(EntityType.VINDICATOR, EntityVindicator.class),
    ILLUSIONER(EntityType.ILLUSIONER, EntityIllagerIllusioner.class),
    CREEPER(EntityType.CREEPER, EntityCreeper.class),
    SKELETON(EntityType.SKELETON, EntitySkeleton.class),
    SPIDER(EntityType.SPIDER, EntitySpider.class),
    GIANT(EntityType.GIANT, EntityGiantZombie.class),
    ZOMBIE(EntityType.ZOMBIE, EntityZombie.class),
    SLIME(EntityType.SLIME, EntitySlime.class),
    GHAST(EntityType.GHAST, EntityGhast.class),
    PIG_ZOMBIE(EntityType.PIG_ZOMBIE, EntityPigZombie.class),
    ENDERMAN(EntityType.ENDERMAN, EntityEnderman.class),
    CAVE_SPIDER(EntityType.CAVE_SPIDER, EntityCaveSpider.class),
    SILVERFISH(EntityType.SILVERFISH, EntitySilverfish.class),
    BLAZE(EntityType.BLAZE, EntityBlaze.class),
    MAGMA_CUBE(EntityType.MAGMA_CUBE, EntityMagmaCube.class),
    ENDER_DRAGON(EntityType.ENDER_DRAGON, EntityEnderDragon.class),
    WITHER(EntityType.WITHER, EntityWither.class),
    BAT(EntityType.BAT, EntityBat.class),
    WITCH(EntityType.WITCH, EntityWitch.class),
    ENDERMITE(EntityType.ENDERMITE, EntityEndermite.class),
    GUARDIAN(EntityType.GUARDIAN, EntityGuardian.class),
    SHULKER(EntityType.SHULKER, EntityShulker.class),
    PIG(EntityType.PIG, EntityPig.class),
    SHEEP(EntityType.SHEEP, EntitySheep.class),
    COW(EntityType.COW, EntityCow.class),
    CHICKEN(EntityType.CHICKEN, EntityChicken.class),
    SQUID(EntityType.SQUID, EntitySquid.class),
    WOLF(EntityType.WOLF, EntityWolf.class),
    MUSHROOM_COW(EntityType.MUSHROOM_COW, EntityMushroomCow.class),
    SNOWMAN(EntityType.SNOWMAN, EntitySnowman.class),
    OCELOT(EntityType.OCELOT, EntityOcelot.class),
    IRON_GOLEM(EntityType.IRON_GOLEM, EntityIronGolem.class),
    HORSE(EntityType.HORSE, EntityHorse.class),
    RABBIT(EntityType.RABBIT, EntityRabbit.class),
    POLAR_BEAR(EntityType.POLAR_BEAR, EntityPolarBear.class),
    LLAMA(EntityType.LLAMA, EntityLlama.class),
    PARROT(EntityType.PARROT, EntityParrot.class),
    VILLAGER(EntityType.VILLAGER, EntityVillager.class),;

    private final EntityType bukkitType;
    private final Class<? extends EntityInsentient> nmsClass;

    public boolean hasBabyModel() {
        try {
            this.nmsClass.getDeclaredMethod("setBaby", boolean.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
