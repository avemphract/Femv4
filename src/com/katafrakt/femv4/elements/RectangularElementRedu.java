package com.katafrakt.femv4.elements;

import com.katafrakt.femv4.materials.Material;
import com.katafrakt.femv4.maths.Maths;
import com.katafrakt.femv4.models.Model;
import com.katafrakt.framework.util.Yazdýr;

public class RectangularElementRedu extends RectangularElement {

	public RectangularElementRedu(Model model, Material material, Node node1,Node node2, Node node3, Node node4) {
		super(model, material, node1, node2, node3, node4);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setStiffness() {
		// TODO Auto-generated method stub
		double[][] d=getMaterialMatrix();
		
		double[][] b1=Maths.dotProduct(getA(0,0),getG(0,0));
		double[][] bt1=Maths.transpose(b1);
		double[][] o1=Maths.productWithConstat(Maths.dotProduct(Maths.dotProduct(bt1, d), b1), getJ(0,0)*thickness);
		
		//System.out.println("getJ "+getJ(0,0));
		k=Maths.productWithConstat(o1, 4d);
		//System.out.println("redu "+model);
		//Yazdýr.printArray(d);
		//Yazdýr.printArray(k);
		
	}

}
