package com.doxa.core.particles;

import java.util.Arrays;

import org.bukkit.util.Vector;

public class ParticleMath {
    /**
     * Gets all points in a specified line to be drawn of particles
     *
     * @param start     The point in which the line should start
     * @param direction The direction which the line is going in
     * @param length    The length of the line
     * @param gap       The gap between each particle
     * @return points   An array of type {@link Point} describing the locations of each particle to be drawn
     */
    public static Point[] getLine(Point start, Vector direction, double length, double gap) {
        Point[] points = new Point[(int) (length / gap) - 1];
        int counter = 0;
        for (double i = gap; i < length; i += gap) {
            points[counter] = start.add(direction.multiply(i));
            direction.normalize(); // normalize
            counter++;
        }
        return points;
    }
 
 
    public static Point[] getVerticalCircle(Point center, double radius, double gap, double yaw) {
        Point[] points = new Point[(int) Math.ceil(360 / gap)];
        int counter = 0;
 
        double yangle = Math.toRadians(yaw + 90); // note that here we do have to convert to radians.
        double cos = Math.cos(-yangle); // getting the cos value for the yaw.
        double sin = Math.sin(-yangle); // getting the sin value for the yaw.
 
 
        for (double i = 0; i < 360; i += gap) {
            double radians = Math.toRadians(i);
            double x = Math.cos(radians) * radius;
            double z = Math.sin(radians) * radius;
 
            Vector vector = new Vector(x, z, 0);
            rotateAroundAxisY(vector, cos, sin);
 
 
            points[counter] = center.add(vector);
            counter++;
        }
        return points;
    }
 
 
    public static double[][] getVerticalCircleCalculations(double radius, double gap) {
        double[][] calculations = new double[(int) (360 / gap)][2];
        int counter = 0;
        for (double i = 0; i < 360; i += gap) {
            double radians = Math.toRadians(i);
            double x = Math.cos(radians) * radius;
            double z = Math.sin(radians) * radius;
            calculations[counter][0] = x;
            calculations[counter][1] = z;
            counter++;
        }
        return calculations;
    }
 
    public static Point[] calculateVerticalCircle(double[][] calculations, Point center, double yaw, double gap) {
        Point[] points = new Point[(int) Math.ceil(360 / gap)];
        int counter = 0;
 
        double yangle = Math.toRadians(yaw + 90); // note that here we do have to convert to radians.
        double yAxisCos = Math.cos(-yangle); // getting the cos value for the yaw.
        double yAxisSin = Math.sin(-yangle); // getting the sin value for the yaw.
 
        for (double i = 0; i < 360; i += gap) {
            Vector v = new Vector(calculations[counter][0], calculations[counter][1], 0);
            rotateAroundAxisY(v, yAxisCos, yAxisSin);
            points[counter] = center.add(v);
            counter++;
        }
        return points;
    }
 
    public static Point[] getHalfCircle(double radius, double gap, double yaw, Point center) {
        Point[] points = new Point[(int) Math.ceil(180 / gap)];
        int counter = 0;
 
        double yangle = Math.toRadians(yaw); // note that here we do have to convert to radians.
        double cos = Math.cos(-yangle); // getting the cos value for the yaw.
        double sin = Math.sin(-yangle); // getting the sin value for the yaw.
 
        double xangle = Math.toRadians(-30); // note that here we do have to convert to radians.
        double xAxisCos = Math.cos(xangle); // getting the cos value for the pitch.
        double xAxisSin = Math.sin(xangle); // getting the sin value for the pitch.
 
        double zangle = Math.toRadians(-30); // note that here we do have to convert to radians.
        double zAxisCos = Math.cos(zangle); // getting the cos value for the roll.
        double zAxisSin = Math.sin(zangle); // getting the sin value for the roll.
 
 
        for (double i = 0; i < 180; i += gap) {
            double radians = Math.toRadians(i);
            double x = Math.cos(radians) * radius;
            double z = Math.sin(radians) * radius;
 
            Vector vector = new Vector(x, 0, z);
            rotateAroundAxisZ(vector, zAxisCos, zAxisSin);
            rotateAroundAxisX(vector, xAxisCos, xAxisSin);
            rotateAroundAxisY(vector, cos, sin);
 
 
            points[counter] = center.add(vector);
            counter++;
        }
        return points;
    }
    
    public static Point[] getHalfCircleR(double radius, double gap, double yaw, Point center) {
    	double ran = Math.random();
    	ran = ran*100;
    	double xradian;
    	double zradian;
    	double yradian;
    	if (ran < 10) {
    		xradian = 60;
    		yradian = -yaw;
    		zradian = 40;
    	} else if (ran > 15 && ran < 30) {
    		xradian = 40;
    		yradian = yaw;
    		zradian = 20;
    	} else if (ran > 30 && ran < 45) {
    		xradian = 30;
    		yradian = yaw;
    		zradian = 40;
    	} else if (ran > 60 && ran < 75) {
    		xradian = 40;
    		yradian = yaw;
    		zradian = 30;
    	} else if (ran > 75) {
    		xradian = -60;
    		yradian = yaw;
    		zradian = 40;
    	} else {
    		xradian = 50;
    		yradian = yaw;
    		zradian = -40;
    	}
        Point[] points = new Point[(int) Math.ceil(180 / gap)];
        int counter = 0;
 
        double yangle = Math.toRadians(yradian); // note that here we do have to convert to radians.
        double cos = Math.cos(-yangle); // getting the cos value for the yaw.
        double sin = Math.sin(-yangle); // getting the sin value for the yaw.
 
        double xangle = Math.toRadians(xradian); // note that here we do have to convert to radians.
        double xAxisCos = Math.cos(xangle); // getting the cos value for the pitch.
        double xAxisSin = Math.sin(xangle); // getting the sin value for the pitch.
 
        double zangle = Math.toRadians(zradian); // note that here we do have to convert to radians.
        double zAxisCos = Math.cos(zangle); // getting the cos value for the roll.
        double zAxisSin = Math.sin(zangle); // getting the sin value for the roll.
 
 
        for (double i = 0; i < 180; i += gap) {
            double radians = Math.toRadians(i);
            double x = Math.cos(radians) * radius;
            double z = Math.sin(radians) * radius;
 
            Vector vector = new Vector(x, 0, z);
            rotateAroundAxisZ(vector, zAxisCos, zAxisSin);
            rotateAroundAxisX(vector, xAxisCos, xAxisSin);
            rotateAroundAxisY(vector, cos, sin);
 
 
            points[counter] = center.add(vector);
            counter++;
        }
        return points;
    }
    
    public static Point[] getHalfCircle2(double radius, double gap, double yaw, Point center) {
        Point[] points = new Point[(int) Math.ceil(180/gap)];
        int counter = 0;
 
        double yangle = Math.toRadians(yaw); // note that here we do have to convert to radians.
        double cos = Math.cos(-yangle); // getting the cos value for the yaw.
        double sin = Math.sin(-yangle); // getting the sin value for the yaw.
 
        double xangle = Math.toRadians(-30); // note that here we do have to convert to radians.
        double xAxisCos = Math.cos(xangle); // getting the cos value for the pitch.
        double xAxisSin = Math.sin(xangle); // getting the sin value for the pitch.
 
        double zangle = Math.toRadians(-30); // note that here we do have to convert to radians.
        double zAxisCos = Math.cos(-zangle); // getting the cos value for the roll.
        double zAxisSin = Math.sin(-zangle); // getting the sin value for the roll.
 
 
        for (double i = 0; i < 180; i += gap) {
            double radians = Math.toRadians(i);
            double x = Math.cos(radians) * radius;
            double z = Math.sin(radians) * radius;
 
            Vector vector = new Vector(x, 0, z);
            rotateAroundAxisZ(vector, zAxisCos, zAxisSin);
            rotateAroundAxisX(vector, xAxisCos, xAxisSin);
            rotateAroundAxisY(vector, cos, sin);
 
 
            points[counter] = center.add(vector);
            counter++;
        }
        return points;
    }
    
    public static Point[] getHalfCircle3(double radius, double gap, double yaw, Point center) {
        Point[] points = new Point[(int) Math.ceil(180/gap)];
        int counter = 0;
 
        double yangle = Math.toRadians(yaw); // note that here we do have to convert to radians.
        double cos = Math.cos(-yangle); // getting the cos value for the yaw.
        double sin = Math.sin(-yangle); // getting the sin value for the yaw.
 
        double xangle = Math.toRadians(-30); // note that here we do have to convert to radians.
        double xAxisCos = Math.cos(xangle); // getting the cos value for the pitch.
        double xAxisSin = Math.sin(xangle); // getting the sin value for the pitch.
 
        double zangle = Math.toRadians(-180); // note that here we do have to convert to radians.
        double zAxisCos = Math.cos(-zangle); // getting the cos value for the roll.
        double zAxisSin = Math.sin(-zangle); // getting the sin value for the roll.
 
 
        for (double i = 0; i < 180; i += gap) {
            double radians = Math.toRadians(i);
            double x = Math.cos(radians) * radius;
            double z = Math.sin(radians) * radius;
 
            Vector vector = new Vector(x, 0, z);
            rotateAroundAxisZ(vector, zAxisCos, zAxisSin);
            rotateAroundAxisX(vector, xAxisCos, xAxisSin);
            rotateAroundAxisY(vector, cos, sin);
 
 
            points[counter] = center.add(vector);
            counter++;
        }
        return points;
    }
 
    public static Point[] getHorizontalHelix(Point location, int radius, int height, int loops, double yaw, double gap) {
        double y = 0.0;
        double limit = 2 * Math.PI * loops;
        Point[] points = new Point[((int) Math.ceil(limit / gap)) * 2];
        int counter = 0;
 
        double yangle = Math.toRadians(yaw); // note that here we do have to convert to radians.
        double cos = Math.cos(-yangle); // getting the cos value for the yaw.
        double sin = Math.sin(-yangle); // getting the sin value for the yaw.
 
        double zangle = Math.toRadians(180); // note that here we do have to convert to radians.
        double zAxisCos = Math.cos(zangle); // getting the cos value for the roll.
        double zAxisSin = Math.sin(zangle); // getting the sin value for the roll.
 
        for (double theta = 0; theta < limit; theta += gap) {
            double x = Math.cos(theta) * radius;
            y += height / (limit / gap);
            double z = Math.sin(theta) * radius;
 
            Vector vector = new Vector(x, z, y);
 
            Vector vector2 = vector.clone();
 
            rotateAroundAxisZ(vector2, zAxisCos, zAxisSin);
            rotateAroundAxisY(vector2, cos, sin);
            rotateAroundAxisY(vector, cos, sin);
 
 
            points[counter] = location.add(vector);
            points[counter + 1] = location.add(vector2);
 
 
            counter += 2;
        }
        return points;
    }
 
 
    public static Point[] getWaterfall(double[][][] calculated, int time) {
        Point[] points = new Point[calculated[time].length];
        for (int i = 0; i < calculated[time].length; i++) {
            points[i] = new Point(calculated[time][i][0], calculated[time][i][1], calculated[time][i][2]);
        }
        return points;
    }
 
    public static double[][][] drawWaterfall(Point point, double yaw, double height, double gap, int duration, double width) {
        // todo: make this return something
        return null;
    }
 
    public static Point[] getZigZag(Point start, double totalLength, double gap, Vector direction) {
        Point[] points = new Point[1592];
 
        int counter = 0;
 
        for (int zigs = 1; zigs < 5; zigs++) {
            Vector point = direction.clone().setX(direction.getX() * zigs).setZ(direction.getZ() * 2).setY(0);
            Vector back = direction.clone().setX(direction.getX() * -zigs).setZ(direction.getZ() * -2).setY(0);
 
            Vector starting = new Vector(start.x, 0, start.z);
 
            Vector current = point.clone().subtract(starting);
 
            for (double i = gap; i < totalLength; i += gap) {
                points[counter] = start.add(direction.multiply(i));
                current.normalize(); // normalize
                counter++;
            }
            current = back.clone().subtract(point);
            for (double i = gap; i < totalLength; i += gap) {
                points[counter] = start.add(direction.multiply(i));
                current.normalize(); // normalize
                counter++;
            }
 
 
        }
        System.out.println(counter);
        System.out.println(Arrays.toString(points));
        return points;
    }
 
 
    private static Vector rotateAroundAxisX(Vector v, double cos, double sin) {
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }
 
    private static Vector rotateAroundAxisY(Vector v, double cos, double sin) {
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }
 
    private static Vector rotateAroundAxisZ(Vector v, double cos, double sin) {
        double x = v.getX() * cos - v.getY() * sin;
        double y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }
}
