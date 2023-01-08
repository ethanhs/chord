package me.andante.chord.block.helper;

import com.google.common.collect.ImmutableMap;
import me.andante.chord.block.CSignBlock;
import me.andante.chord.block.CWallSignBlock;
import me.andante.chord.block.vanilla.*;
import me.andante.chord.entity.boat.CBoatEntity;
import me.andante.chord.entity.boat.CBoatInfo;
import me.andante.chord.item.CBoatItem;
import me.andante.chord.util.CSignType;
import me.andante.chord.util.CTagUtil;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.sapling.OakSaplingGenerator;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntryList;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class WoodBlocks {
    private final String id;
    private final String modId;
    private final ItemGroup itemGroup;
    private final boolean flammable;
    private final int leafItemColor;

    public final Block PLANKS;
    public final Block SAPLING;
    public final Block POTTED_SAPLING;
    public final Block LOG;
    public final Block STRIPPED_LOG;
    public final Block STRIPPED_WOOD;
    public final Block WOOD;
    public final Block LEAVES;
    public final Block SLAB;
    public final Block PRESSURE_PLATE;
    public final Block FENCE;
    public final Block TRAPDOOR;
    public final Block FENCE_GATE;
    public final Block STAIRS;
    public final Block BUTTON;
    public final Block DOOR;
    public final Block SIGN;
    public final Block WALL_SIGN;

    public final Item SIGN_ITEM;
    public final Item BOAT_ITEM;

    public final EntityType<CBoatEntity> BOAT_ENTITY;

    public final Optional<RegistryEntryList.Named<Item>> AXES = Registry.ITEM.getEntryList(ConventionalItemTags.AXES);

    public WoodBlocks(String modId, final String id, ItemGroup itemGroup, boolean flammable, int leafItemColor, SaplingGenerator saplingGenerator, BoatEntity.Type boatType, PressurePlateBlock.ActivationRule pressurePlateActivationRule) {
        this.id = id;
        this.modId = modId;
        this.itemGroup = itemGroup;
        this.flammable = flammable;
        this.leafItemColor = leafItemColor;

        PLANKS = register(id + "_planks", new Block(FabricBlockSettings.copy(Blocks.OAK_PLANKS)));
        SAPLING = register(id + "_sapling", new PublicSaplingBlock(saplingGenerator, FabricBlockSettings.copy(Blocks.OAK_SAPLING)));
        POTTED_SAPLING = register("potted_" + id + "_sapling", new FlowerPotBlock(this.SAPLING, FabricBlockSettings.of(Material.DECORATION).breakInstantly().nonOpaque()), false);
        LOG = register(id + "_log", new PillarBlock(FabricBlockSettings.copy(Blocks.OAK_LOG)));
        STRIPPED_LOG = register("stripped_" + id + "_log", new PillarBlock(FabricBlockSettings.copy(Blocks.STRIPPED_OAK_LOG)));
        STRIPPED_WOOD = register("stripped_" + id + "_wood", new PillarBlock(FabricBlockSettings.copy(Blocks.STRIPPED_OAK_WOOD)));
        WOOD = register(id + "_wood", new PillarBlock(FabricBlockSettings.copy(Blocks.OAK_WOOD)));
        LEAVES = register(id + "_leaves", new LeavesBlock(FabricBlockSettings.copy(Blocks.OAK_LEAVES)));
        SLAB = register(id + "_slab", new SlabBlock(FabricBlockSettings.copy(Blocks.OAK_SLAB)));
        PRESSURE_PLATE = register(id + "_pressure_plate", new PublicPressurePlateBlock(pressurePlateActivationRule, FabricBlockSettings.copy(Blocks.OAK_PRESSURE_PLATE)));
        FENCE = register(id + "_fence", new FenceBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE)));
        TRAPDOOR = register(id + "_trapdoor", new PublicTrapdoorBlock(FabricBlockSettings.copy(Blocks.OAK_TRAPDOOR)));
        FENCE_GATE = register(id + "_fence_gate", new FenceGateBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE_GATE)));
        STAIRS = register(id + "_stairs", new PublicStairsBlock(PLANKS.getDefaultState(), FabricBlockSettings.copy(Blocks.OAK_STAIRS)));
        BUTTON = register(id + "_button", new PublicWoodenButtonBlock(FabricBlockSettings.copy(Blocks.OAK_BUTTON)));
        DOOR = register(id + "_door", new PublicDoorBlock(FabricBlockSettings.copy(Blocks.OAK_DOOR)));

        SignType signType = SignType.register(new CSignType(new Identifier(modId, id).toString()));
        SIGN = register(id + "_sign", new CSignBlock(FabricBlockSettings.copy(Blocks.OAK_SIGN), signType), false);
        WALL_SIGN = register(id + "_wall_sign", new CWallSignBlock(FabricBlockSettings.copy(Blocks.OAK_WALL_SIGN), signType), false);
        SIGN_ITEM = register(id + "_sign", new SignItem(new Item.Settings().maxCount(16).group(itemGroup), SIGN, WALL_SIGN));

        BOAT_ITEM = register(id + "_boat", new CBoatItem(new Supplier<>() {
            @Override
            public EntityType<CBoatEntity> get() {
                return BOAT_ENTITY;
            }
        }, new Item.Settings().maxCount(1).group(itemGroup)));
        BOAT_ENTITY = Registry.register(Registry.ENTITY_TYPE, new Identifier(modId, id + "_boat"), FabricEntityTypeBuilder.<CBoatEntity>create(SpawnGroup.MISC, (entity, world) -> new CBoatEntity(entity, world, new CBoatInfo(BOAT_ITEM, PLANKS.asItem(), new Identifier(modId, "textures/entity/boat/" + id + ".png"), boatType))).dimensions(EntityDimensions.fixed(1.375F, 0.5625F)).build());

        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(LEAVES.asItem(), 0.3F);
        if (isFlammable()) {
            // flammable blocks
            int baseBurnChance = 5;
            int largeBurnChance = baseBurnChance * 6;

            int baseSpreadChance = 20;
            int smallSpreadChance = baseSpreadChance / 4;
            int largeSpreadChance = baseSpreadChance * 3;

            FlammableBlockRegistry fbrInstance = FlammableBlockRegistry.getDefaultInstance();
            fbrInstance.add(PLANKS, baseBurnChance, baseSpreadChance);
            fbrInstance.add(SLAB, baseBurnChance, baseSpreadChance);
            fbrInstance.add(FENCE_GATE, baseBurnChance, baseSpreadChance);
            fbrInstance.add(FENCE, baseBurnChance, baseSpreadChance);
            fbrInstance.add(STAIRS, baseBurnChance, baseSpreadChance);
            fbrInstance.add(LOG, baseBurnChance, smallSpreadChance);
            fbrInstance.add(STRIPPED_LOG, baseBurnChance, smallSpreadChance);
            fbrInstance.add(STRIPPED_WOOD, baseBurnChance, smallSpreadChance);
            fbrInstance.add(WOOD, baseBurnChance, smallSpreadChance);
            fbrInstance.add(LEAVES, largeBurnChance, largeSpreadChance);

            // fuel registering
            int fenceFuelTime = 300;

            FuelRegistry frInstance = FuelRegistry.INSTANCE;
            frInstance.add(FENCE, fenceFuelTime);
            frInstance.add(FENCE_GATE, fenceFuelTime);
        }

        new ImmutableMap.Builder<Block, Block>()
            .put(LOG, STRIPPED_LOG)
            .put(WOOD, STRIPPED_WOOD)
            .build().forEach(
                (base, result) -> UseBlockCallback.EVENT.register(
                    (player, world, hand, hit) -> {
                        if (CTagUtil.tagContainsItem(ConventionalItemTags.AXES, player.getStackInHand(hand).getItem()) && world.getBlockState(hit.getBlockPos()).getBlock() == base) {
                            BlockPos blockPos = hit.getBlockPos();
                            BlockState blockState = world.getBlockState(blockPos);

                            world.playSound(player, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            if (!world.isClient) {
                                world.setBlockState(blockPos, result.getDefaultState().with(PillarBlock.AXIS, blockState.get(PillarBlock.AXIS)), 11);
                                if (!player.isCreative()) {
                                    ItemStack stack = player.getStackInHand(hand);
                                    stack.damage(1, player, ((p) -> p.sendToolBreakStatus(hand)));
                                }
                            }

                            return ActionResult.SUCCESS;
                        }

                        return ActionResult.PASS;
                    }
            )
        );

        Registry.register(Registry.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.create(SignBlockEntity::new, SIGN, WALL_SIGN).build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, id + "_sign")));
    }

    public String getId() {
        return this.id;
    }
    public String getModId() {
        return this.modId;
    }
    public boolean isFlammable() {
        return this.flammable;
    }
    public int getLeafItemColor() {
        return this.leafItemColor;
    }

    @SuppressWarnings("unused")
    public static class Builder {
        private ItemGroup itemGroup;
        private boolean flammable = true;
        private int leafItemColor = -1;
        private SaplingGenerator saplingGenerator = new OakSaplingGenerator();
        private BoatEntity.Type boatType = BoatEntity.Type.OAK;
        private PressurePlateBlock.ActivationRule pressurePlateActivationRule = PressurePlateBlock.ActivationRule.EVERYTHING;

        public Builder() {}

        /**
         * @param itemGroup The item group for all of this wood block set's items to be placed in.
         * @return Modified {@link WoodBlocks.Builder}
         */
        public WoodBlocks.Builder itemGroup(ItemGroup itemGroup) {
            this.itemGroup = itemGroup;
            return this;
        }
        /**
         * Sets the wood set to not be registered in fire/fuel registries.
         * @return Modified {@link WoodBlocks.Builder}
         */
        public WoodBlocks.Builder nonFlammable() {
            this.flammable = false;
            return this;
        }
        /**
         * Sets the leaf's item color to be registered client-side.
         * @param color A decimal/hexadecimal color.
         * @return Modified {@link WoodBlocks.Builder}
         */
        public WoodBlocks.Builder leafItemColor(int color) {
            this.leafItemColor = color;
            return this;
        }
        /**
         * A custom sapling generator for this wood set's sapling.
         * @param saplingGenerator The wood set's sapling generator.
         * @return Modified {@link WoodBlocks.Builder}
         */
        public WoodBlocks.Builder saplingGenerator(SaplingGenerator saplingGenerator) {
            this.saplingGenerator = saplingGenerator;
            return this;
        }
        /**
         * Sets a vanilla boat type to be assigned to the wood set. This is practically useless.
         * @param boatType A boat type for this wood set.
         * @return Modified {@link WoodBlocks.Builder}
         */
        public WoodBlocks.Builder boatType(BoatEntity.Type boatType) {
            this.boatType = boatType;
            return this;
        }
        /**
         * Sets under what conditions this wood set's pressure plate should be activated.
         * @param pressurePlateActivationRule A pressure plate activation rule.
         * @return Modified {@link WoodBlocks.Builder}
         */
        public WoodBlocks.Builder pressurePlateActivationRule(PressurePlateBlock.ActivationRule pressurePlateActivationRule) {
            this.pressurePlateActivationRule = pressurePlateActivationRule;
            return this;
        }

        /**
         * Creates and registers an instance of {@link WoodBlocks}.
         * @param modId The mod's identifier.
         * @param id The wood set's identifier.
         * @return New instance of {@link WoodBlocks}
         */
        public WoodBlocks build(String modId, String id) {
            return new WoodBlocks(modId, id, this.itemGroup, this.flammable, this.leafItemColor, this.saplingGenerator, this.boatType, this.pressurePlateActivationRule);
        }
    }

    // registries
    private Block register(String id, Block block, boolean registerItem) {
        if (registerItem) register(id, new BlockItem(block, new Item.Settings().group(this.itemGroup)));
        return Registry.register(Registry.BLOCK, new Identifier(this.modId, id), block);
    }
    private Block register(String id, Block block) {
        return this.register(id, block, true);
    }

    private Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(this.modId, id), item);
    }
}
