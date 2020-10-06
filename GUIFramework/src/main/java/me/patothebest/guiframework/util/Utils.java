package me.patothebest.guiframework.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.*;

public class Utils {

    public final static String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    /**
     * Gets field reflectively
     *
     * @param clazz
     * @param fieldName
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Class<?> clazz, String fieldName, Object obj) {
        try {
            return (T) setAccessible(clazz.getDeclaredField(fieldName)).get(obj);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Gets field reflectively
     *
     * @param clazz
     * @param fieldName
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValueNotDeclared(Class<?> clazz, String fieldName, Object obj) {
        try {
            return (T) setAccessible(clazz.getField(fieldName)).get(obj);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Gets Method reflectively
     *
     * @param clazz
     * @param methodName
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Method getMethodValue(Class<?> clazz, String methodName, Class<?>... obj) {
        try {
            return clazz.getDeclaredMethod(methodName, obj);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Gets Method reflectively
     *
     * @param clazz
     * @param methodName
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Method getMethodNotDeclaredValue(Class<?> clazz, String methodName, Class<?>... obj) {
        try {
            return clazz.getMethod(methodName, obj);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Sets accessibleobject accessible state an returns this object
     *
     * @param <T>
     * @param object
     * @return
     */
    public static <T extends AccessibleObject> T setAccessible(T object) {
        object.setAccessible(true);
        return object;
    }

    /**
     * Sets field reflectively
     *
     * @param clazz
     * @param fieldName
     * @param obj
     * @param value
     */
    public static void setFieldValue(Class<?> clazz, String fieldName, Object obj, Object value) {
        try {
            setAccessible(clazz.getDeclaredField(fieldName)).set(obj, value);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Sets field reflectively
     *
     * @param clazz
     * @param fieldName
     * @param obj
     * @param value
     */
    public static void setFieldValueNotDeclared(Class<?> clazz, String fieldName, Object obj, Object value) {
        try {
            setAccessible(clazz.getField(fieldName)).set(obj, value);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     * Invoke a static method
     *
     * @param clazz
     * @param method
     * @param parameterClasses
     * @param parameters
     * @return
     */
    public static Object invokeStaticMethod(Class<?> clazz, String method, Class<?>[] parameterClasses, Object... parameters) {
        return invokeStaticMethod(getMethodValue(clazz, method, parameterClasses), parameters);
    }

    /**
     * Invoke a static method
     *
     * @param method
     * @param parameters
     * @return
     */
    public static Object invokeStaticMethod(Method method, Object... parameters) {
        try {
            return method.invoke(null, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Invoke a non static method
     *
     * @param object
     * @param method
     * @param parameterClasses
     * @param parameters
     * @return
     */
    public static Object invokeMethod(Object object, String method, Class<?>[] parameterClasses, Object... parameters) {
        return invokeMethod(object, getMethodValue(object.getClass(), method, parameterClasses), parameters);
    }

    /**
     * Invoke a non static method
     *
     * @param object
     * @param method
     * @param parameters
     * @return
     */
    public static Object invokeMethod(Object object, Method method, Object... parameters) {
        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns a craftbukkit CBS class
     *
     * @param nmsClassString the class name
     * @return the NMS class
     */
    public static Class<?> getCBSClass(String nmsClassString) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + SERVER_VERSION + "." + nmsClassString);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns a minecraft NMS class
     *
     * @param nmsClassString the class name
     * @return the NMS class
     */
    public static Class<?> getNMSClass(String nmsClassString) {
        try {
            return Class.forName("net.minecraft.server." + SERVER_VERSION + "." + nmsClassString);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns a minecraft NMS class
     *
     * @param nmsClassString the class name
     * @return the NMS class
     */
    public static Class<?> getNMSClassOrNull(String nmsClassString) {
        try {
            return Class.forName("net.minecraft.server." + SERVER_VERSION + "." + nmsClassString);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Sends a packet to the player via reflection
     *
     * @param p the bukkit player
     * @param packet the packet object
     */
    public static void sendPacket(Player p, Object packet) {
        try {
            //noinspection ConstantConditions
            getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet")).invoke(getNMSClass("EntityPlayer").getDeclaredField("playerConnection").get(getHandle(p)), packet);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get's the player handle, also known as the EntityPlayer
     *
     * @param player the bukkit player
     * @return the NMS player object
     */
    public static Object getHandle(Player player) {
        try {
            return player.getClass().getMethod("getHandle").invoke(player);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Transforms a number to a multiple of nine for the use in
     * bukkit inventories, since they require the size to be a
     * multiple of nine
     *
     * @param size the size of the list
     *
     * @return the transformed slot
     */
    public static int transformToInventorySize(long size) {
        return Math.min(((int) Math.ceil((Math.max(1, size)) / 9.0)) * 9, 54);
    }
}