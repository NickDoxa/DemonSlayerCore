package com.doxa.core.particles;

import org.bukkit.Particle;
import org.bukkit.World;

public class ParticleData<T> {
	
    private final World world;
    private final Particle particle;
    private final int count;
    private Double offsetX, offsetY, offsetZ;
    private Double extra;
    private T data;
 
 
    public ParticleData(Particle particle, World world, int count) {
        this.particle = particle;
        this.world = world;
        this.count = count;
    }
 
    public ParticleData(Particle particle, World world, int count, double offsetX, double offsetY, double offsetZ) {
        this.particle = particle;
        this.world = world;
        this.count = count;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }
 
    public ParticleData(Particle particle, World world, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.particle = particle;
        this.world = world;
        this.count = count;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.extra = extra;
    }
 
    public ParticleData(Particle particle, World world, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        this.particle = particle;
        this.world = world;
        this.count = count;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.extra = extra;
        this.data = data;
    }
 
    public ParticleData(Particle particle, World world, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.particle = particle;
        this.world = world;
        this.count = count;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.data = data;
 
    }
 
    public ParticleData(Particle particle, World world, int count, T data) {
        this.particle = particle;
        this.world = world;
        this.count = count;
        this.data = data;
 
    }
 
 
 
    public void spawnParticles(Point[] points) {
        if (offsetX != null) {
            if (extra != null) {
                if (data != null) {
                    for (Point point : points) {
                        world.spawnParticle(particle, point.x, point.y, point.z, count, offsetX, offsetY, offsetZ, extra, data);
                    }
                } else {
                    for (Point point : points) {
                        world.spawnParticle(particle, point.x, point.y, point.z, count, offsetX, offsetY, offsetZ, java.util.Optional.ofNullable(extra));
                    }
                }
 
 
            } else if (data != null) {
                for (Point point : points) {
                    world.spawnParticle(particle, point.x, point.y, point.z, count, offsetX, offsetY, offsetZ, data);
                }
            } else {
                for (Point point : points) {
                    world.spawnParticle(particle, point.x, point.y, point.z, count, offsetX, offsetY, offsetZ);
                }
            }
 
 
        } else if (data != null) {
            for (Point point : points) {
                world.spawnParticle(particle, point.x, point.y, point.z, count, data);
            }
        } else {
            for (Point point : points) {
                world.spawnParticle(particle, point.x, point.y, point.z, count);
            }
        }
    }
}
