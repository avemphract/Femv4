package com.katafrakt.femv4.elements;

import com.katafrakt.femv4.materials.Material;
import com.katafrakt.femv4.maths.Maths;
import com.katafrakt.femv4.models.Model;
import com.katafrakt.framework.util.Yazdýr;

public class RectangularElementFull extends RectangularElement {

	public RectangularElementFull(Model model, Material material, Node node1, Node node2, Node node3, Node node4) {
		super(model, material, node1, node2, node3, node4);
		// TODO Auto-generated constructor stub
	}
	protected void setStiffness() {
		double[][] d=getMaterialMatrix();
		//1
		double[][] b1=Maths.dotProduct(getA(-0.57735d,-0.57735d),getG(-0.57735d,-0.57735d));
		double[][] bt1=Maths.transpose(b1);
		double[][] o1=Maths.productWithConstat(Maths.dotProduct(Maths.dotProduct(bt1, d), b1), getJ(-0.57735d,-0.57735d)*thickness);
		//System.out.println("o1");
		//Yazdýr.printArray(o1);
		//2
		double[][] b2=Maths.dotProduct(getA(0.57735d,-0.57735d),getG(0.57735d,-0.57735d));
		double[][] bt2=Maths.transpose(b2);
		double[][] o2=Maths.productWithConstat(Maths.dotProduct(Maths.dotProduct(bt2, d), b2), getJ(0.57735d,-0.57735d)*thickness);
		//System.out.println("o2");
		//Yazdýr.printArray(o2);
		//3
		double[][] b3=Maths.dotProduct(getA(0.57735d,0.57735d),getG(0.57735d,0.57735d));
		double[][] bt3=Maths.transpose(b3);
		double[][] o3=Maths.productWithConstat(Maths.dotProduct(Maths.dotProduct(bt3, d), b3), getJ(0.57735d,0.57735d)*thickness);
		//System.out.println("o3");
		//Yazdýr.printArray(o3);
		//4
		double[][] b4=Maths.dotProduct(getA(-0.57735d,0.57735d),getG(-0.57735d,0.57735d));
		double[][] bt4=Maths.transpose(b4);
		double[][] o4=Maths.productWithConstat(Maths.dotProduct(Maths.dotProduct(bt4, d), b4), getJ(-0.57735d,0.57735d)*thickness);
		//System.out.println("o4");
		//Yazdýr.printArray(o4);
		k=Maths.matrixAdder(o1,o2,o3,o4);
		//System.out.println("full "+model);
		//Yazdýr.printArray(d);
	}
}
