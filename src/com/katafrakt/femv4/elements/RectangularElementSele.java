package com.katafrakt.femv4.elements;

import com.katafrakt.femv4.materials.Material;
import com.katafrakt.femv4.maths.Maths;
import com.katafrakt.femv4.models.Model;
import com.katafrakt.framework.util.Yazdýr;

public class RectangularElementSele extends RectangularElement {

	public RectangularElementSele(Model model, Material material, Node node1, Node node2, Node node3, Node node4) {
		super(model, material, node1, node2, node3, node4);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setStiffness() {
		// TODO Auto-generated method stub
		double[][] dStrain=getStrainMaterialMatrix();
		
		//1
		double[][] b1=Maths.dotProduct(getA(-0.57735d,-0.57735d),getG(-0.57735d,-0.57735d));
		double[][] bt1=Maths.transpose(b1);
		double[][] o1=Maths.productWithConstat(Maths.dotProduct(Maths.dotProduct(bt1, dStrain), b1), getJ(-0.57735d,-0.57735d)*thickness);
		//System.out.println("o1");
		//Yazdýr.printArray(o1);
		//2
		double[][] b2=Maths.dotProduct(getA(0.57735d,-0.57735d),getG(0.57735d,-0.57735d));
		double[][] bt2=Maths.transpose(b2);
		double[][] o2=Maths.productWithConstat(Maths.dotProduct(Maths.dotProduct(bt2, dStrain), b2), getJ(0.57735d,-0.57735d)*thickness);
		//System.out.println("o2");
		//Yazdýr.printArray(o2);
		//3
		double[][] b3=Maths.dotProduct(getA(0.57735d,0.57735d),getG(0.57735d,0.57735d));
		double[][] bt3=Maths.transpose(b3);
		double[][] o3=Maths.productWithConstat(Maths.dotProduct(Maths.dotProduct(bt3, dStrain), b3), getJ(0.57735d,0.57735d)*thickness);
		//System.out.println("o3");
		//Yazdýr.printArray(o3);
		//4
		double[][] b4=Maths.dotProduct(getA(-0.57735d,0.57735d),getG(-0.57735d,0.57735d));
		double[][] bt4=Maths.transpose(b4);
		double[][] o4=Maths.productWithConstat(Maths.dotProduct(Maths.dotProduct(bt4, dStrain), b4), getJ(-0.57735d,0.57735d)*thickness);
		//System.out.println("o4");
		//Yazdýr.printArray(o4);
		//5
		double dShear[][]=getShearMaterialMatrix();
		
		double[][] b5=Maths.dotProduct(getA(0,0),getG(0,0));
		double[][] bt5=Maths.transpose(b5);
		double[][] o5=Maths.productWithConstat(Maths.dotProduct(Maths.dotProduct(bt5, dShear), b5), getJ(0,0)*thickness);
		//System.out.println("o5");
		//Yazdýr.printArray(o5);
		k= Maths.matrixAdder(o1,o2,o3,o4,Maths.productWithConstat(o5, 4));
		
	}

}
