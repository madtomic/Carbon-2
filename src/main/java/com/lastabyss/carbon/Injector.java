package com.lastabyss.carbon;


import com.lastabyss.carbon.utils.Utilities;

import net.minecraft.server.v1_8_R1.Block;
import net.minecraft.server.v1_8_R1.Blocks;
import net.minecraft.server.v1_8_R1.DataWatcher;
import net.minecraft.server.v1_8_R1.Enchantment;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityTypes;
import net.minecraft.server.v1_8_R1.EnumProtocol;
import net.minecraft.server.v1_8_R1.EnumProtocolDirection;
import net.minecraft.server.v1_8_R1.IBlockData;
import net.minecraft.server.v1_8_R1.Item;
import net.minecraft.server.v1_8_R1.Items;
import net.minecraft.server.v1_8_R1.MinecraftKey;
import net.minecraft.server.v1_8_R1.MonsterEggInfo;
import net.minecraft.server.v1_8_R1.Packet;
import net.minecraft.server.v1_8_R1.PotionBrewer;
import net.minecraft.server.v1_8_R1.TileEntity;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Recipe;

import gnu.trove.map.TObjectIntMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

/**
 * The injector class is the driver behind Carbon.
 * 
 * @author Navid
 */
public class Injector {

	private Carbon plugin;

	public Injector(Carbon plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("unchecked")
	public void registerBlock(int id, String name, Block block) {
		if (plugin.getConfig().getBoolean("modify.blocks." + name, true)) {
			MinecraftKey stringkey = new MinecraftKey(name);
			Block.REGISTRY.a(id, stringkey, block);
			Iterator<IBlockData> blockdataiterator = block.O().a().iterator();
			while (blockdataiterator.hasNext()) {
				IBlockData blockdata = blockdataiterator.next();
				final int stateId = (id << 4) | block.toLegacyData(blockdata);
				Block.d.a(blockdata, stateId);
			}
			if (plugin.getConfig().getBoolean("debug.verbose", false)) {
				Carbon.log.log(Level.INFO, "[Carbon] Block {0} was registered into Minecraft.", name);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void registerBlock(int id, String name, Block block, Item item) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (plugin.getConfig().getBoolean("modify.blocks." + name, true)) {
			MinecraftKey stringkey = new MinecraftKey(name);
			Block.REGISTRY.a(id, stringkey, block);
			Iterator<IBlockData> blockdataiterator = block.O().a().iterator();
			while (blockdataiterator.hasNext()) {
				IBlockData blockdata = blockdataiterator.next();
				final int stateId = (id << 4) | block.toLegacyData(blockdata);
				Block.d.a(blockdata, stateId);
			}
			Item.REGISTRY.a(id, stringkey, item);
			((Map<Block, Item>)Utilities.<Field>setAccessible(Item.class.getDeclaredField("a"), true).get(null)).put(block, item);
			if (plugin.getConfig().getBoolean("debug.verbose", false)) {
				Carbon.log.log(Level.INFO, "[Carbon] Block {0} with item {1} was registered into Minecraft.", new Object[] { name + "(" + block.getName() + ")", item.getName() });
			}
		}
	}

	public void registerItem(int id, String name, Item item) {
		if (plugin.getConfig().getBoolean("modify.items." + name, true)) {
			Item.REGISTRY.a(id, name, item);
			if (plugin.getConfig().getBoolean("debug.verbose", false)) {
				Carbon.log.log(Level.INFO, "[Carbon] Item {0} was registered into Minecraft.", name);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void registerTileEntity(Class<? extends TileEntity> entityClass, String name) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (plugin.getConfig().getBoolean("modify.tileentities." + name, true)) {
			((Map<String, Class<? extends TileEntity>>) Utilities.<Field>setAccessible(TileEntity.class.getDeclaredField("f"), true).get(null)).put(name, entityClass);
			((Map<Class<? extends TileEntity>, String>) Utilities.<Field>setAccessible(TileEntity.class.getDeclaredField("g"), true).get(null)).put(entityClass, name);
			if (plugin.getConfig().getBoolean("debug.verbose", false)) {
				Carbon.log.log(Level.INFO, "[Carbon] Tile Entity {0} was registered into Minecraft.", entityClass.getCanonicalName());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void registerDataWatcherType(Class<?> type, int id) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field classToIdField = DataWatcher.class.getDeclaredField("classToId");
		classToIdField.setAccessible(true);
		((TObjectIntMap<Class<?>>) classToIdField.get(null)).put(type, id);
		if (plugin.getConfig().getBoolean("debug.verbose", false)) {
			Carbon.log.log(Level.INFO, "[Carbon] DataWatcher type {0} was registered into Minecraft.", type.getCanonicalName());
		}
	}

	@SuppressWarnings("unchecked")
	public void registerEntity(Class<? extends Entity> entityClass, String name, int id) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (plugin.getConfig().getBoolean("modify.entities." + name, true)) {
			((Map<String, Class<? extends Entity>>) Utilities.<Field>setAccessible(EntityTypes.class.getDeclaredField("c"), true).get(null)).put(name, entityClass);
			((Map<Class<? extends Entity>, String>) Utilities.<Field>setAccessible(EntityTypes.class.getDeclaredField("d"), true).get(null)).put(entityClass, name);
			((Map<Integer, Class<? extends Entity>>) Utilities.<Field>setAccessible(EntityTypes.class.getDeclaredField("e"), true).get(null)).put(id, entityClass);
			((Map<Class<? extends Entity>, Integer>) Utilities.<Field>setAccessible(EntityTypes.class.getDeclaredField("f"), true).get(null)).put(entityClass, id);
			((Map<String, Integer>) Utilities.<Field>setAccessible(EntityTypes.class.getDeclaredField("g"), true).get(null)).put(name, id);
			if (plugin.getConfig().getBoolean("debug.verbose", false)) {
				Carbon.log.log(Level.INFO, "[Carbon] Entity {0} was registered into Minecraft.", entityClass.getCanonicalName());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void registerEntity(Class<? extends Entity> entityClass, String name, int id, int monsterEgg, int monsterEggData) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (plugin.getConfig().getBoolean("modify.entities." + name, true)) {
			registerEntity(entityClass, name, id);
			EntityTypes.eggInfo.put(id, new MonsterEggInfo(id, monsterEgg, monsterEggData));
		}
	}

	@SuppressWarnings("unchecked")
	public void registerPacket(EnumProtocol protocol, Class<? extends Packet> packetClass, int packetID, boolean isClientbound) {
		if (plugin.getConfig().getBoolean("modify.packets." + packetID, true)) {
			try {
				((Map<Class<? extends Packet>, EnumProtocol>) Utilities.<Field>setAccessible(EnumProtocol.class.getDeclaredField("f"), true).get(null)).put(packetClass, protocol);
				((Map<EnumProtocolDirection, Map<Integer, Class<? extends Packet>>>) Utilities.<Field>setAccessible(
					EnumProtocol.class.getDeclaredField("h"), true).get(protocol)
				).get(isClientbound ? EnumProtocolDirection.CLIENTBOUND : EnumProtocolDirection.SERVERBOUND).put(packetID, packetClass);
				if (plugin.getConfig().getBoolean("debug.verbose", false)) {
					Carbon.log.log(Level.INFO, "[Carbon] Packet {0} was registered into Minecraft with ID: " + packetID, packetClass.getCanonicalName());
				}
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace(System.out);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void registerPotionEffect(int effectId, String durations, String amplifier) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (plugin.getConfig().getBoolean("modify.potions." + effectId, true)) {
			((Map<Integer, String>) Utilities.<Field>setAccessible(PotionBrewer.class.getDeclaredField("effectDurations"), true).get(null)).put(effectId, durations);
			((Map<Integer, String>) Utilities.<Field>setAccessible(PotionBrewer.class.getDeclaredField("effectAmplifiers"), true).get(null)).put(effectId, amplifier);
			if (plugin.getConfig().getBoolean("debug.verbose", false)) {
				Carbon.log.log(Level.INFO, "[Carbon] PoitonEffect {0} was registered into Minecraft.", effectId);
			}
		}
	}

	public void registerEnchantment(Enchantment enhcantment) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (plugin.getConfig().getBoolean("modify.enchantments." + enhcantment.id)) {
			Utilities.<Field>setAccessible(org.bukkit.enchantments.Enchantment.class.getDeclaredField("acceptingNew"), true).set(null, true);
			ArrayList<Enchantment> enchants = new ArrayList<Enchantment>(Arrays.asList(Enchantment.b));
			enchants.add(enhcantment);
			Utilities.setFinalField(Enchantment.class.getField("b"), null, enchants.toArray(new Enchantment[0]));
			Utilities.<Field>setAccessible(org.bukkit.enchantments.Enchantment.class.getDeclaredField("acceptingNew"), true).set(null, false);
			if (plugin.getConfig().getBoolean("debug.verbose", false)) {
				Carbon.log.log(Level.INFO, "[Carbon] Enchantment {0} was registered into Minecraft.", enhcantment);
			}
		}
	}

	private void fixBlocksRefs() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		for (Field field : Blocks.class.getDeclaredFields()) {
			field.setAccessible(true);
			if (Block.class.isAssignableFrom(field.getType())) {
				Block block = (Block) field.get(null);
				Block newblock = Block.getById(Block.getId(block));
				if (block != newblock) {
					Utilities.setFinalField(field, null, newblock);
				}
			}
		}
	}

	private void fixItemsRefs() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		for (Field field : Items.class.getDeclaredFields()) {
			field.setAccessible(true);
			if (Item.class.isAssignableFrom(field.getType())) {
				Item block = (Item) field.get(null);
				Item newblock = Item.getById(Item.getId(block));
				if (block != newblock) {
					Utilities.setFinalField(field, null, newblock);
				}
			}
		}
	}

	public void registerAll() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException {
		//Fix references in Blocks.class
		fixBlocksRefs();
		//Fix references in Items.class
		fixItemsRefs();
	}

	public void registerRecipes() {
		// reset recipes, this will restore original recipes, but with proper items now
		Bukkit.resetRecipes();
	}

	private void addRecipe(Recipe recipe) {
		Bukkit.getServer().addRecipe(recipe);
	}

}
