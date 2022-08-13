package team.tnt.collectoralbum.common.init;

import net.minecraft.resources.ResourceLocation;
import team.tnt.collectoralbum.CollectorsAlbum;
import team.tnt.collectoralbum.common.CardDefinition;
import team.tnt.collectoralbum.common.ICardCategory;

import java.util.HashMap;
import java.util.Map;

public final class CardRegistry {

    private static final Map<ResourceLocation, CardDefinition> CARD_DEFINITION_MAP = new HashMap<>();

    public static final CardDefinition ACACIA_SAPLING_CARD = createAndRegisterCard("acacia_sapling_card", CardCategoryRegistry.NATURE, 95);
    public static final CardDefinition ALLIUM_CARD = createAndRegisterCard("allium_card", CardCategoryRegistry.NATURE, 101);
    public static final CardDefinition AMETHYST_SHARD_CARD = createAndRegisterCard("amethyst_shard_card", CardCategoryRegistry.ITEMS, 147);
    public static final CardDefinition APPLE_CARD = createAndRegisterCard("apple_card", CardCategoryRegistry.NATURE, 109);
    public static final CardDefinition AZURE_BLUET_CARD = createAndRegisterCard("azure_bluet_card", CardCategoryRegistry.NATURE, 102);
    public static final CardDefinition BAMBOO_CARD = createAndRegisterCard("bamboo_card", CardCategoryRegistry.NATURE, 117);
    public static final CardDefinition BAT_CARD = createAndRegisterCard("bat_card", CardCategoryRegistry.MOBS, 87);
    public static final CardDefinition BEACON_CARD = createAndRegisterCard("beacon_card", CardCategoryRegistry.ITEMS, 122);
    public static final CardDefinition BEE_CARD = createAndRegisterCard("bee_card", CardCategoryRegistry.MOBS, 76);
    public static final CardDefinition BEETROOT_CARD = createAndRegisterCard("beetroot_card", CardCategoryRegistry.NATURE, 114);
    public static final CardDefinition BELL_CARD = createAndRegisterCard("bell_card", CardCategoryRegistry.ITEMS, 143);
    public static final CardDefinition BIRCH_SAPLING_CARD = createAndRegisterCard("birch_sapling_card", CardCategoryRegistry.NATURE, 93);
    public static final CardDefinition BLAZE_CARD = createAndRegisterCard("blaze_card", CardCategoryRegistry.MOBS, 61);
    public static final CardDefinition BLAZE_POWDER_CARD = createAndRegisterCard("blaze_powder_card", CardCategoryRegistry.ITEMS, 138);
    public static final CardDefinition BLUE_ORCHID_CARD = createAndRegisterCard("blue_orchid_card", CardCategoryRegistry.NATURE, 100);
    public static final CardDefinition BREWING_STAND_CARD = createAndRegisterCard("brewing_stand_card", CardCategoryRegistry.ITEMS, 123);
    public static final CardDefinition BROWN_MUSHROOM_CARD = createAndRegisterCard("brown_mushroom_card", CardCategoryRegistry.NATURE, 105);
    public static final CardDefinition CAMPFIRE_CARD = createAndRegisterCard("campfire_card", CardCategoryRegistry.ITEMS, 144);
    public static final CardDefinition CARROT_CARD = createAndRegisterCard("carrot_card", CardCategoryRegistry.NATURE, 112);
    public static final CardDefinition CAT_CARD = createAndRegisterCard("cat_card", CardCategoryRegistry.MOBS, 86);
    public static final CardDefinition CHAINMAIL_BOOTS_CARD = createAndRegisterCard("chainmail_boots_card", CardCategoryRegistry.ARMOR, 42);
    public static final CardDefinition CHAINMAIL_CHESTPLATE_CARD = createAndRegisterCard("chainmail_chestplate_card", CardCategoryRegistry.ARMOR, 40);
    public static final CardDefinition CHAINMAIL_HELMET_CARD = createAndRegisterCard("chainmail_helmet_card", CardCategoryRegistry.ARMOR, 39);
    public static final CardDefinition CHAINMAIL_LEGGINGS_CARD = createAndRegisterCard("chainmail_leggings_card", CardCategoryRegistry.ARMOR, 41);
    public static final CardDefinition CHICKEN_CARD = createAndRegisterCard("chicken_card", CardCategoryRegistry.MOBS, 79);
    public static final CardDefinition CLOCK_CARD = createAndRegisterCard("clock_card", CardCategoryRegistry.ITEMS, 134);
    public static final CardDefinition COAL_CARD = createAndRegisterCard("coal_card", CardCategoryRegistry.ITEMS, 124);
    public static final CardDefinition COMPASS_CARD = createAndRegisterCard("compass_card", CardCategoryRegistry.ITEMS, 133);
    public static final CardDefinition COOKIE_CARD = createAndRegisterCard("cookie_card", CardCategoryRegistry.ITEMS, 135);
    public static final CardDefinition COW_CARD = createAndRegisterCard("cow_card", CardCategoryRegistry.MOBS, 78);
    public static final CardDefinition CREEPER_CARD = createAndRegisterCard("creeper_card", CardCategoryRegistry.MOBS, 64);
    public static final CardDefinition DANDELION_CARD = createAndRegisterCard("dandelion_card", CardCategoryRegistry.NATURE, 98);
    public static final CardDefinition DARK_OAK_SAPLING_CARD = createAndRegisterCard("dark_oak_sapling_card", CardCategoryRegistry.NATURE, 96);
    public static final CardDefinition DEAD_BUSH_CARD = createAndRegisterCard("dead_bush_card", CardCategoryRegistry.NATURE, 97);
    public static final CardDefinition DIAMOND_AXE_CARD = createAndRegisterCard("diamond_axe_card", CardCategoryRegistry.TOOLS, 23);
    public static final CardDefinition DIAMOND_BOOTS_CARD = createAndRegisterCard("diamond_boots_card", CardCategoryRegistry.ARMOR, 50);
    public static final CardDefinition DIAMOND_CARD = createAndRegisterCard("diamond_card", CardCategoryRegistry.ITEMS, 127);
    public static final CardDefinition DIAMOND_CHESTPLATE_CARD = createAndRegisterCard("diamond_chestplate_card", CardCategoryRegistry.ARMOR, 48);
    public static final CardDefinition DIAMOND_HELMET_CARD = createAndRegisterCard("diamond_helmet_card", CardCategoryRegistry.ARMOR, 47);
    public static final CardDefinition DIAMOND_HOE_CARD = createAndRegisterCard("diamond_hoe_card", CardCategoryRegistry.TOOLS, 25);
    public static final CardDefinition DIAMOND_HORSE_ARMOR_CARD = createAndRegisterCard("diamond_horse_armor_card", CardCategoryRegistry.ARMOR, 58);
    public static final CardDefinition DIAMOND_LEGGINGS_CARD = createAndRegisterCard("diamond_leggings_card", CardCategoryRegistry.ARMOR, 49);
    public static final CardDefinition DIAMOND_PICKAXE_CARD = createAndRegisterCard("diamond_pickaxe_card", CardCategoryRegistry.TOOLS, 22);
    public static final CardDefinition DIAMOND_SHOVEL_CARD = createAndRegisterCard("diamond_shovel_card", CardCategoryRegistry.TOOLS, 24);
    public static final CardDefinition DIAMOND_SWORD_CARD = createAndRegisterCard("diamond_sword_card", CardCategoryRegistry.TOOLS, 21);
    public static final CardDefinition EMERALD_CARD = createAndRegisterCard("emerald_card", CardCategoryRegistry.ITEMS, 128);
    public static final CardDefinition ENCHANTMENT_TABLE_CARD = createAndRegisterCard("enchantment_table_card", CardCategoryRegistry.ITEMS, 121);
    public static final CardDefinition ENDER_DRAGON_CARD = createAndRegisterCard("ender_dragon_card", CardCategoryRegistry.MOBS, 73);
    public static final CardDefinition ENDER_EYE_CARD = createAndRegisterCard("ender_eye_card", CardCategoryRegistry.ITEMS, 137);
    public static final CardDefinition ENDER_PEARL_CARD = createAndRegisterCard("ender_pearl_card", CardCategoryRegistry.ITEMS, 136);
    public static final CardDefinition ENDERMAN_CARD = createAndRegisterCard("enderman_card", CardCategoryRegistry.MOBS, 75);
    public static final CardDefinition FEATHER_CARD = createAndRegisterCard("feather_card", CardCategoryRegistry.ITEMS, 149);
    public static final CardDefinition FIREWORK_ROCKET_CARD = createAndRegisterCard("firework_rocket_card", CardCategoryRegistry.ITEMS, 139);
    public static final CardDefinition FOX_CARD = createAndRegisterCard("fox_card", CardCategoryRegistry.MOBS, 81);
    public static final CardDefinition GHAST_CARD = createAndRegisterCard("ghast_card", CardCategoryRegistry.MOBS, 72);
    public static final CardDefinition GLOW_BERRIES_CARD = createAndRegisterCard("glow_berries_card", CardCategoryRegistry.NATURE, 119);
    public static final CardDefinition GLOWSTONE_DUST_CARD = createAndRegisterCard("glowstone_dust_card", CardCategoryRegistry.ITEMS, 131);
    public static final CardDefinition GOLD_INGOT_CARD = createAndRegisterCard("gold_ingot_card", CardCategoryRegistry.ITEMS, 126);
    public static final CardDefinition GOLDEN_APPLE_CARD = createAndRegisterCard("golden_apple_card", CardCategoryRegistry.NATURE, 110);
    public static final CardDefinition GOLDEN_AXE_CARD = createAndRegisterCard("golden_axe_card", CardCategoryRegistry.TOOLS, 18);
    public static final CardDefinition GOLDEN_BOOTS_CARD = createAndRegisterCard("golden_boots_card", CardCategoryRegistry.ARMOR, 46);
    public static final CardDefinition GOLDEN_CARROT_CARD = createAndRegisterCard("golden_carrot_card", CardCategoryRegistry.NATURE, 115);
    public static final CardDefinition GOLDEN_CHESTPLATE_CARD = createAndRegisterCard("golden_chestplate_card", CardCategoryRegistry.ARMOR, 44);
    public static final CardDefinition GOLDEN_HELMET_CARD = createAndRegisterCard("golden_helmet_card", CardCategoryRegistry.ARMOR, 43);
    public static final CardDefinition GOLDEN_HOE_CARD = createAndRegisterCard("golden_hoe_card", CardCategoryRegistry.TOOLS, 20);
    public static final CardDefinition GOLDEN_HORSE_ARMOR_CARD = createAndRegisterCard("golden_horse_armor_card", CardCategoryRegistry.ARMOR, 57);
    public static final CardDefinition GOLDEN_LEGGINGS_CARD = createAndRegisterCard("golden_leggings_card", CardCategoryRegistry.ARMOR, 45);
    public static final CardDefinition GOLDEN_PICKAXE_CARD = createAndRegisterCard("golden_pickaxe_card", CardCategoryRegistry.TOOLS, 17);
    public static final CardDefinition GOLDEN_SHOVEL_CARD = createAndRegisterCard("golden_shovel_card", CardCategoryRegistry.TOOLS, 19);
    public static final CardDefinition GOLDEN_SWORD_CARD = createAndRegisterCard("golden_sword_card", CardCategoryRegistry.TOOLS, 16);
    public static final CardDefinition HEART_OF_THE_SEA_CARD = createAndRegisterCard("heart_of_the_sea_card", CardCategoryRegistry.ITEMS, 140);
    public static final CardDefinition HONEYCOMB_CARD = createAndRegisterCard("honeycomb_card", CardCategoryRegistry.ITEMS, 146);
    public static final CardDefinition HORSE_CARD = createAndRegisterCard("horse_card", CardCategoryRegistry.MOBS, 82);
    public static final CardDefinition IRON_AXE_CARD = createAndRegisterCard("iron_axe_card", CardCategoryRegistry.TOOLS, 13);
    public static final CardDefinition IRON_BOOTS_CARD = createAndRegisterCard("iron_boots_card", CardCategoryRegistry.ARMOR, 38);
    public static final CardDefinition IRON_CHESTPLATE_CARD = createAndRegisterCard("iron_chestplate_card", CardCategoryRegistry.ARMOR, 36);
    public static final CardDefinition IRON_HELMET_CARD = createAndRegisterCard("iron_helmet_card", CardCategoryRegistry.ARMOR, 35);
    public static final CardDefinition IRON_HOE_CARD = createAndRegisterCard("iron_hoe_card", CardCategoryRegistry.TOOLS, 15);
    public static final CardDefinition IRON_HORSE_ARMOR_CARD = createAndRegisterCard("iron_horse_armor_card", CardCategoryRegistry.ARMOR, 56);
    public static final CardDefinition IRON_INGOT_CARD = createAndRegisterCard("iron_ingot_card", CardCategoryRegistry.ITEMS, 125);
    public static final CardDefinition IRON_LEGGINGS_CARD = createAndRegisterCard("iron_leggings_card", CardCategoryRegistry.ARMOR, 37);
    public static final CardDefinition IRON_PICKAXE_CARD = createAndRegisterCard("iron_pickaxe_card", CardCategoryRegistry.TOOLS, 12);
    public static final CardDefinition IRON_SHOVEL_CARD = createAndRegisterCard("iron_shovel_card", CardCategoryRegistry.TOOLS, 14);
    public static final CardDefinition IRON_SWORD_CARD = createAndRegisterCard("iron_sword_card", CardCategoryRegistry.TOOLS, 11);
    public static final CardDefinition JUNGLE_SAPLING_CARD = createAndRegisterCard("jungle_sapling_card", CardCategoryRegistry.NATURE, 94);
    public static final CardDefinition KELP_CARD = createAndRegisterCard("kelp_card", CardCategoryRegistry.NATURE, 116);
    public static final CardDefinition LANTERN_CARD = createAndRegisterCard("lantern_card", CardCategoryRegistry.ITEMS, 145);
    public static final CardDefinition LAPIS_LAZULI_CARD = createAndRegisterCard("lapis_lazuli_card", CardCategoryRegistry.ITEMS, 132);
    public static final CardDefinition LEATHER_BOOTS_CARD = createAndRegisterCard("leather_boots_card", CardCategoryRegistry.ARMOR, 34);
    public static final CardDefinition LEATHER_CHESTPLATE_CARD = createAndRegisterCard("leather_chestplate_card", CardCategoryRegistry.ARMOR, 32);
    public static final CardDefinition LEATHER_HELMET_CARD = createAndRegisterCard("leather_helmet_card", CardCategoryRegistry.ARMOR, 31);
    public static final CardDefinition LEATHER_HORSE_ARMOR_CARD = createAndRegisterCard("leather_horse_armor_card", CardCategoryRegistry.ARMOR, 55);
    public static final CardDefinition LEATHER_LEGGINGS_CARD = createAndRegisterCard("leather_leggings_card", CardCategoryRegistry.ARMOR, 33);
    public static final CardDefinition MAGMA_CUBE_CARD = createAndRegisterCard("magma_cube_card", CardCategoryRegistry.MOBS, 71);
    public static final CardDefinition MELON_SLICE_CARD = createAndRegisterCard("melon_slice_card", CardCategoryRegistry.NATURE, 111);
    public static final CardDefinition MUSIC_DISC_CARD = createAndRegisterCard("music_disc_card", CardCategoryRegistry.ITEMS, 142);
    public static final CardDefinition NAUTILUS_SHELL_CARD = createAndRegisterCard("nautilus_shell_card", CardCategoryRegistry.ITEMS, 141);
    public static final CardDefinition NETHER_STAR_CARD = createAndRegisterCard("nether_star_card", CardCategoryRegistry.ITEMS, 129);
    public static final CardDefinition NETHER_WART_CARD = createAndRegisterCard("nether_wart_card", CardCategoryRegistry.NATURE, 120);
    public static final CardDefinition NETHERITE_AXE_CARD = createAndRegisterCard("netherite_axe_card", CardCategoryRegistry.TOOLS, 28);
    public static final CardDefinition NETHERITE_BOOTS_CARD = createAndRegisterCard("netherite_boots_card", CardCategoryRegistry.ARMOR, 54);
    public static final CardDefinition NETHERITE_CHESTPLATE_CARD = createAndRegisterCard("netherite_chestplate_card", CardCategoryRegistry.ARMOR, 52);
    public static final CardDefinition NETHERITE_HELMET_CARD = createAndRegisterCard("netherite_helmte_card", CardCategoryRegistry.ARMOR, 51);
    public static final CardDefinition NETHERITE_HOE_CARD = createAndRegisterCard("netherite_hoe_card", CardCategoryRegistry.TOOLS, 30);
    public static final CardDefinition NETHERITE_INGOT_CARD = createAndRegisterCard("netherite_ingot_card", CardCategoryRegistry.ITEMS, 148);
    public static final CardDefinition NETHERITE_LEGGINGS_CARD = createAndRegisterCard("netherite_leggings_card", CardCategoryRegistry.ARMOR, 53);
    public static final CardDefinition NETHERITE_PICKAXE_CARD = createAndRegisterCard("netherite_pickaxe_card", CardCategoryRegistry.TOOLS, 27);
    public static final CardDefinition NETHERITE_SHOVEL_CARD = createAndRegisterCard("netherite_shovel_card", CardCategoryRegistry.TOOLS, 29);
    public static final CardDefinition NETHERITE_SWORD_CARD = createAndRegisterCard("netherite_sword_card", CardCategoryRegistry.TOOLS, 26);
    public static final CardDefinition OAK_SAPLING_CARD = createAndRegisterCard("oak_sapling_card", CardCategoryRegistry.NATURE, 91);
    public static final CardDefinition OXEYE_DAISY_CARD = createAndRegisterCard("oxeye_daisy_card", CardCategoryRegistry.NATURE, 104);
    public static final CardDefinition PANDA_CARD = createAndRegisterCard("panda_card", CardCategoryRegistry.MOBS, 77);
    public static final CardDefinition PARROT_CARD = createAndRegisterCard("parrot_card", CardCategoryRegistry.MOBS, 83);
    public static final CardDefinition PIG_CARD = createAndRegisterCard("pig_card", CardCategoryRegistry.MOBS, 80);
    public static final CardDefinition PIGLIN_BRUTE_CARD = createAndRegisterCard("piglin_brute_card", CardCategoryRegistry.MOBS, 68);
    public static final CardDefinition PILLAGER_CARD = createAndRegisterCard("pillager_card", CardCategoryRegistry.MOBS, 69);
    public static final CardDefinition POLAR_BEAR_CARD = createAndRegisterCard("polar_bear_card", CardCategoryRegistry.MOBS, 90);
    public static final CardDefinition POPPY_CARD = createAndRegisterCard("poppy_card", CardCategoryRegistry.NATURE, 99);
    public static final CardDefinition POTATO_CARD = createAndRegisterCard("potato_card", CardCategoryRegistry.NATURE, 113);
    public static final CardDefinition RED_MUSHROOM_CARD = createAndRegisterCard("red_mushroom_card", CardCategoryRegistry.NATURE, 106);
    public static final CardDefinition RED_TULIP_CARD = createAndRegisterCard("red_tulip_card", CardCategoryRegistry.NATURE, 103);
    public static final CardDefinition REDSTONE_CARD = createAndRegisterCard("redstone_card", CardCategoryRegistry.ITEMS, 130);
    public static final CardDefinition SHEEP_CARD = createAndRegisterCard("sheep_card", CardCategoryRegistry.MOBS, 84);
    public static final CardDefinition SHIELD_CARD = createAndRegisterCard("shield_card", CardCategoryRegistry.ARMOR, 59);
    public static final CardDefinition SKELETON_CARD = createAndRegisterCard("skeleton_card", CardCategoryRegistry.MOBS, 63);
    public static final CardDefinition SLIME_CARD = createAndRegisterCard("slime_card", CardCategoryRegistry.MOBS, 66);
    public static final CardDefinition SPIDER_CARD = createAndRegisterCard("spider_card", CardCategoryRegistry.MOBS, 65);
    public static final CardDefinition SPRUCE_SAPLING_CARD = createAndRegisterCard("spruce_sapling_card", CardCategoryRegistry.NATURE, 92);
    public static final CardDefinition SPYGLASS_CARD = createAndRegisterCard("spyglass_card", CardCategoryRegistry.ITEMS, 150);
    public static final CardDefinition SQUID_CARD = createAndRegisterCard("squid_card", CardCategoryRegistry.MOBS, 85);
    public static final CardDefinition STONE_AXE_CARD = createAndRegisterCard("stone_axe_card", CardCategoryRegistry.TOOLS, 8);
    public static final CardDefinition STONE_HOE_CARD = createAndRegisterCard("stone_hoe_card", CardCategoryRegistry.TOOLS, 10);
    public static final CardDefinition STONE_PICKAXE_CARD = createAndRegisterCard("stone_pickaxe_card", CardCategoryRegistry.TOOLS, 7);
    public static final CardDefinition STONE_SHOVEL_CARD = createAndRegisterCard("stone_shovel_card", CardCategoryRegistry.TOOLS, 9);
    public static final CardDefinition STONE_SWORD_CARD = createAndRegisterCard("stone_sword_card", CardCategoryRegistry.TOOLS, 6);
    public static final CardDefinition SUGAR_CANE_CARD = createAndRegisterCard("sugar_cane_card", CardCategoryRegistry.NATURE, 108);
    public static final CardDefinition SWEET_BERRIES_CARD = createAndRegisterCard("sweet_berries_card", CardCategoryRegistry.NATURE, 118);
    public static final CardDefinition TURTLE_HELMET_CARD = createAndRegisterCard("turtle_helmet_card", CardCategoryRegistry.ARMOR, 60);
    public static final CardDefinition VILLAGER_CARD = createAndRegisterCard("villager_card", CardCategoryRegistry.MOBS, 89);
    public static final CardDefinition WHEAT_CARD = createAndRegisterCard("wheat_card", CardCategoryRegistry.NATURE, 107);
    public static final CardDefinition WITCH_CARD = createAndRegisterCard("witch_card", CardCategoryRegistry.MOBS, 67);
    public static final CardDefinition WITHER_CARD = createAndRegisterCard("wither_card", CardCategoryRegistry.MOBS, 74);
    public static final CardDefinition WITHER_SKELETON_CARD = createAndRegisterCard("wither_skeleton_card", CardCategoryRegistry.MOBS, 70);
    public static final CardDefinition WOLF_CARD = createAndRegisterCard("wolf_card", CardCategoryRegistry.MOBS, 88);
    public static final CardDefinition WOODEN_AXE_CARD = createAndRegisterCard("wooden_axe_card", CardCategoryRegistry.TOOLS, 3);
    public static final CardDefinition WOODEN_HOE_CARD = createAndRegisterCard("wooden_hoe_card", CardCategoryRegistry.TOOLS, 5);
    public static final CardDefinition WOODEN_PICKAXE_CARD = createAndRegisterCard("wooden_pickaxe_card", CardCategoryRegistry.TOOLS, 2);
    public static final CardDefinition WOODEN_SHOVEL_CARD = createAndRegisterCard("wooden_shovel_card", CardCategoryRegistry.TOOLS, 4);
    public static final CardDefinition WOODEN_SWORD_CARD = createAndRegisterCard("wooden_sword_card", CardCategoryRegistry.TOOLS, 1);
    public static final CardDefinition ZOMBIE_CARD = createAndRegisterCard("zombie_card", CardCategoryRegistry.MOBS, 62);


    private static CardDefinition createAndRegisterCard(String id, ICardCategory category, int cardNumber) {
        return createAndRegisterCard(new ResourceLocation(CollectorsAlbum.MODID, id), category, cardNumber);
    }

    public static CardDefinition createAndRegisterCard(ResourceLocation cardId, ICardCategory category, int cardNumber) {
        CardDefinition definition = new CardDefinition(cardId, category, cardNumber);
        registerCard(definition);
        return definition;
    }

    public static void registerCard(CardDefinition definition) {
        ResourceLocation cardId = definition.cardId();
        if (CARD_DEFINITION_MAP.put(cardId, definition) != null) {
            throw new IllegalStateException("Duplicate card entry found: " + cardId);
        }
    }

    public static int count() {
        return CARD_DEFINITION_MAP.size();
    }
}
