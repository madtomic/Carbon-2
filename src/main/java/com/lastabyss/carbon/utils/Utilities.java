package com.lastabyss.carbon.utils;

import com.lastabyss.carbon.Carbon;
import com.lastabyss.carbon.DynamicEnumType;

import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.Packet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Navid
 */
public class Utilities {

	private static Carbon plugin = null;
	public final static SecureRandom random = new SecureRandom();

	private Utilities() {
		throw new UnsupportedOperationException("No, bad!");
	}

	/**
	 * Can only be instantiated once.
	 * 
	 * @param instance
	 */
	public static void instantiate(Carbon instance) {
		if (plugin == null) {
			plugin = instance;
		}
	}

	/**
	 * Registers a bukkit command without the need for a plugin.yml entry.
	 *
	 * Yes, I'm terrible.
	 * 
	 * @param fallbackPrefix
	 * @param cmd
	 */
	public static void registerBukkitCommand(String fallbackPrefix, Command cmd) {
		try {
			if (Bukkit.getServer() instanceof CraftServer) {
				Field f = CraftServer.class.getDeclaredField("commandMap");
				f.setAccessible(true);
				CommandMap cmap = (CommandMap) f.get(Bukkit.getServer());
				cmap.register(fallbackPrefix, cmd);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace(System.out);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Returns the BUKKIT ENTITYTYPE.
	 * 
	 * @param name
	 * @param id
	 * @param entityClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static EntityType addEntity(String name, int id, Class<? extends Entity> entityClass) {
		EntityType entityType = DynamicEnumType.addEnum(EntityType.class, name, new Class[] { String.class, entityClass.getClass(), Integer.TYPE }, new Object[] { name, entityClass.getClass(), id });
		try {
			Field field = EntityType.class.getDeclaredField("NAME_MAP");
			field.setAccessible(true);
			Object object = field.get(null);
			Map<String, EntityType> NAME_MAP = (Map<String, EntityType>) object;
			NAME_MAP.put(name, entityType);
			if (plugin.getConfig().getBoolean("debug.verbose", false))
				Carbon.log.log(Level.INFO, "[Carbon] Entity {0} with ID {1} was injected into CraftBukkit.", new Object[] { name, id });
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
		try {
			Field field = EntityType.class.getDeclaredField("ID_MAP");
			field.setAccessible(true);
			Object object = field.get(null);
			Map<Short, EntityType> ID_MAP = (Map<Short, EntityType>) object;
			ID_MAP.put((short) id, entityType);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
		return entityType;
	}

	@SuppressWarnings("unchecked")
	public static Material addMaterial(String name, int id) {
		Material material = DynamicEnumType.addEnum(Material.class, name, new Class[] { Integer.TYPE }, new Object[] { id });
		try {
			Field field = Material.class.getDeclaredField("BY_NAME");
			field.setAccessible(true);
			Object object = field.get(null);
			Map<String, Material> BY_NAME = (Map<String, Material>) object;
			BY_NAME.put(name, material);
			if (plugin.getConfig().getBoolean("debug.verbose", false))
				Carbon.log.log(Level.INFO, "[Carbon] Material {0} with ID {1} was injected into CraftBukkit.", new Object[] { name, id });
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
		try {
			Field field = Material.class.getDeclaredField("byId");
			field.setAccessible(true);
			Object object = field.get(0);
			Material[] byId = (Material[]) object;
			byId[id] = material;
			field.set(object, byId);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
		return material;
	}

	@SuppressWarnings("unchecked")
	public static Material addMaterial(String name, int id, short data) {
		Material material = DynamicEnumType.addEnum(Material.class, name, new Class[] { Integer.TYPE }, new Object[] { id });
		try {
			Field field = Material.class.getDeclaredField("BY_NAME");
			field.setAccessible(true);
			Object object = field.get(null);
			Map<String, Material> BY_NAME = (Map<String, Material>) object;
			BY_NAME.put(name, material);
			if (plugin.getConfig().getBoolean("debug.verbose", false))
				Carbon.log.log(Level.INFO, "[Carbon] Material {0} with ID {1} with data {2} was injected into CraftBukkit.", new Object[] { name, id, data });
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
		try {
			Field field = Material.class.getDeclaredField("byId");
			field.setAccessible(true);
			Object object = field.get(0);
			Material[] byId = (Material[]) object;
			byId[id] = material;
			field.set(object, byId);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
		try {
			Field field = Material.class.getDeclaredField("durability");
			field.setAccessible(true);
			Object object = field.get((short) 0);
			Material[] durability = (Material[]) object;
			durability[data] = material;
			field.set(object, durability);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
		return material;
	}

	/**
	 * Returns 0 if there's an error accessing the "strength" field in the minecraft server Blocks class, otherwise, returns the block's given strength.
	 * 
	 * @param b
	 * @return
	 */
	public static float getBlockStrength(net.minecraft.server.v1_8_R1.Block b) {
		try {
			Field field = b.getClass().getField("strength");
			field.setAccessible(true);
			return field.getFloat(b);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
			Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
		}
		return 0;
	}

	/**
	 * Returns 0 if there's an error accessing the "durability" field in the minecraft server Blocks class, otherwise, returns the block's given strength.
	 * 
	 * @param b
	 * @return
	 */
	public static float getBlockDurability(net.minecraft.server.v1_8_R1.Block b) {
		try {
			Field field = b.getClass().getField("durability");
			field.setAccessible(true);
			return field.getFloat(b);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
			Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
		}
		return 0;
	}

	/**
	 * Returns all adjacent blocks to a specified block. Returns an empty list if none were found. Air is not included.
	 * 
	 * @param source
	 * @return
	 */
	public static List<Block> getAllAdjacentBlocks(Block source) {
		List<Block> list = new ArrayList<Block>();
		for (BlockFace f : BlockFace.values()) {
			Block rel = source.getRelative(f);
			if (rel.getType() != Material.AIR) {
				list.add(rel);
			}
		}
		return list;
	}

	/**
	 * Sends packet to a player
	 * 
	 * @param player
	 * @param packet
	 */
	public static void sendPacket(Player player, Packet packet) {
		EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
		nmsPlayer.playerConnection.sendPacket(packet);
	}

	/**
	 * Sets accessibleobject accessible state an returns this object
	 * 
	 * @param object
	 * @param accessible
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends AccessibleObject> T setAccessible(AccessibleObject object, boolean accessible) {
		object.setAccessible(accessible);
		return (T) object;
	}

	public static void setFinalField(Field field, Object obj, Object newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
		Field fieldModifiers = Field.class.getDeclaredField("modifiers");
		fieldModifiers.setAccessible(true);
		fieldModifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		Field root = Field.class.getDeclaredField("root");
		root.setAccessible(true);
		root.set(field, null);
		Field accessor = Field.class.getDeclaredField("overrideFieldAccessor");
		accessor.setAccessible(true);
		accessor.set(field, null);
		field.set(obj, newValue);
	}

}
