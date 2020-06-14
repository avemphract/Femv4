package com.katafrakt.femv4.shapes;

import com.katafrakt.femv4.elements.Node;

public class DoublePoint {
	public double x,y;
	public DoublePoint(double x,double y){
		this.x=x;
		this.y=y;
	}
	public static DoublePoint toVector(Node n1,Node n2){
		return new DoublePoint(n2.x-n1.x,n2.y-n1.y);
		
	}
	public static DoublePoint toVector(Node n1,DoublePoint p2){
		return new DoublePoint(p2.x-n1.x,p2.y-n1.y);
	}
	public static double scalarProduct(DoublePoint p1,DoublePoint p2){
		return (p1.x*p2.x+p1.y*p2.y);
	}
	public static double toLenght(DoublePoint p1,DoublePoint p2){
		return Math.pow(Math.pow(p1.x-p2.x, 2)+Math.pow(p1.y-p2.y, 2),0.5d);
	}
}
