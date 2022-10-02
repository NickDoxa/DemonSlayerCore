package com.doxa.core.particles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleAPI {
	
    private final JavaPlugin plugin;
 
    public ParticleAPI(JavaPlugin plugin) {
        this.plugin = plugin;
    }
 
    public List<Point[]> drawLine(double gap, double distance, Point location, Vector direction, ParticleData<?> data) {
        data.spawnParticles(ParticleMath.getLine(location, direction, distance, gap));
        List<Point[]> list = new ArrayList<Point[]>();
        list.add(ParticleMath.getLine(location, direction, distance, gap));
        return list;
    }
 
    public List<Point[]> drawSlash(double gap, int radius, double yaw, Point location, ParticleData<?> data) {
        location = location.add(0, (radius / 2.), 0);
        data.spawnParticles(ParticleMath.getHalfCircle(radius, gap, yaw, location));
        List<Point[]> list = new ArrayList<Point[]>();
        list.add(ParticleMath.getHalfCircle(radius, gap, yaw, location));
        return list;
    }
    
    public List<Point[]> drawRandomSlashes(double gap, int radius, double yaw, Point location, ParticleData<?> data) {
        location = location.add(0, (radius / 2.), 0);
        for (int i=0; i<8; i++) {
        	data.spawnParticles(ParticleMath.getHalfCircleR(radius, gap, yaw, location));
        }
        List<Point[]> list = new ArrayList<Point[]>();
        list.add(ParticleMath.getHalfCircleR(radius, gap, yaw, location));
        return list;
    }
    
    public List<Point[]> drawSlash2(double gap, int radius, double yaw, Point location, ParticleData<?> data) {
        location = location.add(0, (radius / 2.), 0);
        data.spawnParticles(ParticleMath.getHalfCircle2(radius, gap, yaw, location));
        List<Point[]> list = new ArrayList<Point[]>();
        list.add(ParticleMath.getHalfCircle2(radius, gap, yaw, location));
        return list;
    }
    
    public List<Point[]> drawDoubleSlash(double gap, int radius, double yaw, Point loc, ParticleData<?> data) {
        Point location = new Point(loc.x, loc.y, loc.z);
        Point location2 = new Point(loc.x, loc.y-1, loc.z);
    	location = location.add(0, (radius / 2.), 0);
    	location2= location2.add(0, (radius / 2.), 0);
        data.spawnParticles(ParticleMath.getHalfCircle3(radius, gap, yaw, location));
        data.spawnParticles(ParticleMath.getHalfCircle3(radius, gap, yaw, location2));
        List<Point[]> list = new ArrayList<Point[]>();
        list.add(ParticleMath.getHalfCircle3(radius, gap, yaw, location));
        list.add(ParticleMath.getHalfCircle3(radius, gap, yaw, location2));
        return list;
    }
 
    public void drawVerticalCircle(final Player player, final ParticleData<?> data, int duration, final int radius, double gap, int refresh) {
        new BukkitRunnable() {
            private final double[][] calculated = ParticleMath.getVerticalCircleCalculations(radius, gap);
            private Point point = new Point(player.getLocation().getX(), player.getLocation().getY() + (radius / 2.), player.getLocation().getZ());
            private double yaw = player.getLocation().getYaw();
            private int timer = 0;
 
            @Override
            public void run() {
                if (timer >= duration) {
                    this.cancel();
                    return;
                }
                if (player.isOnline()) {
                    point = new Point(player.getLocation().getX(), player.getLocation().getY() + (radius / 2.), player.getLocation().getZ());
                    yaw = player.getLocation().getYaw();
                }
                data.spawnParticles(ParticleMath.calculateVerticalCircle(calculated, point, yaw, gap));
                timer++;
            }
        }.runTaskTimer(plugin, 0, refresh);
    }
    
    public void drawVerticalCircle2(final Location loc, final ParticleData<?> data, int duration, final int radius, double gap, int refresh) {
        new BukkitRunnable() {
            private final double[][] calculated = ParticleMath.getVerticalCircleCalculations(radius, gap);
            private Point point = new Point(loc.getX(), loc.getY() + (radius / 2.), loc.getZ());
            private double yaw = loc.getYaw();
            private int timer = 0;
 
            @Override
            public void run() {
                if (timer >= duration) {
                    this.cancel();
                    return;
                }
                point = new Point(loc.getX(), loc.getY() + (radius / 2.), loc.getZ());
                yaw = loc.getYaw();
                data.spawnParticles(ParticleMath.calculateVerticalCircle(calculated, point, yaw, gap));
                timer++;
            }
        }.runTaskTimer(plugin, 0, refresh);
    }
 
    public List<Point[]> drawSuccessiveCircles(int circles, int radius, double particleGap, double yaw, double circleGap, Vector direction, Point location, ParticleData<?> data) {
        location = location.add(0, (radius / 2.), 0);
        List<Point[]> points = new ArrayList<Point[]>();
        for (double i = 0; i < circles; i++) {
            Vector gap = direction.clone().multiply((i * radius) + ((i + 1) * circleGap));
            Point center = location.add(gap);
            data.spawnParticles(ParticleMath.getVerticalCircle(center, radius, particleGap, yaw));
            points.add(ParticleMath.getVerticalCircle(center, radius, particleGap, yaw));
        }
        return points;
    }
 
 
    public List<Point[]> drawHelix(Point point, int radius, int length, int loops, double gap, double yaw, ParticleData<?> data) {
        point = point.add(0, (radius / 2.), 0);
        data.spawnParticles(ParticleMath.getHorizontalHelix(point, radius, length, loops, yaw, gap));
        List<Point[]> list = new ArrayList<Point[]>();
        list.add(ParticleMath.getHorizontalHelix(point, radius, length, loops, yaw, gap));
        return list;
    }
 
//    public void drawZigzag(Point start, int totalLength, double gap, Vector direction, ParticleData<?> data) {
//        new BukkitRunnable() {
//            private final Point[] points = ParticleMath.getZigZag(start, totalLength, gap, direction);
//            private int counter = 0;
// 
//            // todo: finish zigzag
//            @Override
//            public void run() {
//                counter++;
//                data.spawnParticles(points);
// 
//                if (counter > 20) this.cancel();
//            }
//        }.runTaskTimer(plugin, 0, 20);
//    }
 
 
    public List<Point[]> drawBurstLine(double gap, double distance, Point location, Vector direction, ParticleData<?> data, ParticleData<?> burst) {
        Point[] line = ParticleMath.getLine(location, direction, distance, gap);
        data.spawnParticles(line);
        burst.spawnParticles(line);
        List<Point[]> list = new ArrayList<Point[]>();
        list.add(line);
        return list;
    }
 
 
//    public void drawWaterfall(Point point, double yaw, double height, double gap, int duration, double width, int refresh, ParticleData<?> data) {
//        new BukkitRunnable() {
//            private final double[][][] calculated = null;
//            private int timer = 0;
//
//            @Override
//            public void run() {
//                if (timer >= duration) {
//                    this.cancel();
//                    return;
//                }
//
//                data.spawnParticles(ParticleMath.getWaterfall(calculated, timer));
//                timer++;
//            }
//        }.runTaskTimer(plugin, 0, refresh);
//    }
}
