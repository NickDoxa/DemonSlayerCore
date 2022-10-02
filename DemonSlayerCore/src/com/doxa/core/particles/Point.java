package com.doxa.core.particles;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Point {
	
    public final double x;
    public final double y;
    public final double z;
 
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
 
    public Point(Vector vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }
 
    public Point add(Vector vector) {
        return new Point(x + vector.getX(), y + vector.getY(), z + vector.getZ());
    }
 
    public Point add(double x, double y, double z) {
        return new Point(this.x + x, this.y + y, this.z + z);
    }
 
    public Location getPointLocation(Player player) {
    	return new Location(player.getWorld(), this.x, this.y, this.z);
    }
    
    @Override
    public String toString() {
        return "Point {" + "x:" + x + " y:" + y + " z:" + z + "}";
    }
}