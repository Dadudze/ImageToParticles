package me.Dadudze.ImageToParticles;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import sun.awt.image.ToolkitImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ITP extends JavaPlugin {

    /**
     * Draws rotated image
     * @param downMiddle Lower center of expected image
     * @param angle Rotation angle
     * @param radius Radius of image (blocks)
     * @param height Height of image (blocks)
     * @param image Image to be drawn
     * @param ignored Ignored color
     */
    public static void drawRotatedImage(Location downMiddle, double angle, double radius, double height, BufferedImage image, Color ignored) {
        Location loc1 = downMiddle.clone().add(Math.cos(Math.toRadians(angle))*radius, 0, Math.sin(Math.toRadians(angle))*radius);
        Location loc2 = downMiddle.clone().add(-Math.cos(Math.toRadians(angle))*radius, height, -Math.sin(Math.toRadians(angle))*radius);
        drawImage(image, loc1, loc2, ignored);
    }


    /**
     * Draws rotated image
     * @param downMiddle Lower center of expected image
     * @param angle Rotation angle
     * @param radius Radius of image (blocks)
     * @param height Height of image (blocks)
     * @param file Image to be drawn
     * @param ignored Ignored color
     */
    public static void drawRotatedImage(Location downMiddle, double angle, double radius, double height, File file, Color ignored) {
        Location loc1 = downMiddle.clone().add(Math.cos(Math.toRadians(angle))*radius, 0, Math.sin(Math.toRadians(angle))*radius);
        Location loc2 = downMiddle.clone().add(-Math.cos(Math.toRadians(angle))*radius, height, -Math.sin(Math.toRadians(angle))*radius);
        drawImage(file, loc1, loc2, ignored);
    }

    /**
     * Draws image with particles
     * @param file Image to be drawn
     * @param loc1 First point of image
     * @param loc2 Second point of image
     * @param ignored Color that will not be drawn (For transparency)
     */
    public static void drawImage(File file, Location loc1, Location loc2, Color ignored) {
        try {
            drawImage(ImageIO.read(file), loc1, loc2, ignored);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws image with particles
     * @param image Image to be drawn
     * @param loc1 First point of image
     * @param loc2 Second point of image
     * @param ignored Color that will not be drawn (For transparency)
     */
    public static void drawImage(BufferedImage image, Location loc1, Location loc2, Color ignored) {
        Vector xChange = loc2.toVector().subtract(loc1.toVector()).multiply(new Vector(1, 0, 1)).multiply(1d/image.getWidth());
        Vector yChange = loc2.toVector().subtract(loc1.toVector()).multiply(new Vector(0, 1, 0)).multiply(1d/image.getWidth());
        Location location = loc1.clone();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int i1 = 0; i1 < image.getHeight(); i1++) {
                Color color = new Color(image.getRGB(i, i1));
                if(!color.equals(ignored)) {
                    spawnParticle(EnumParticle.REDSTONE, location, color);
                }
                location.add(yChange);
            }
            location.setY(loc1.getY());
            location.add(xChange);
        }
    }

    /**
     * @param image image for resize
     * @param width new wight for {@param image}
     * @param height new height for {@param image}
     * @return resized {@param image}
     */
    public static BufferedImage resize(BufferedImage image, int width, int height) {
        return ((ToolkitImage) image.getScaledInstance(width, height, 1)).getBufferedImage();
    }

    /**
     * Spawns particle for nearby players
     * @param particle Particle type
     * @param pos Particle's position
     * @param color Particle's color (Works with potion and redstone particles)
     */
    public static void spawnParticle(EnumParticle particle, Location pos, Color color) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true,
                (float) pos.getX(), (float) pos.getY(), (float) pos.getZ(),
                Math.max(1/255f, color.getRed()/255f), color.getGreen()/255f, color.getBlue()/255f,
                1f, 0, null);
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getWorld().equals(pos.getWorld()))
                .filter(p -> p.getLocation().distance(pos) < 30)
                .forEach(p -> ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet));
    }
}
