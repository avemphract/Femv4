package com.katafrakt.femv4.shapes;

public abstract class Shape {
	public double area;
	public double inertia;
	public double ySize;
	public DoublePoint center;
	public abstract double[] interpolationAreas(DoublePoint c);
}
