package com.katafrakt.femv4.elements;

import com.katafrakt.femv4.materials.Material;
import com.katafrakt.femv4.maths.Maths;
import com.katafrakt.femv4.models.Model;
import com.katafrakt.femv4.shapes.DoublePoint;
import com.katafrakt.femv4.shapes.FilledRectangle;
import com.katafrakt.framework.util.Yazdýr;

public abstract class RectangularElement extends Element {
	
	//public TwoUnkEquation[] n=new TwoUnkEquation[4];
	//public TwoUnkEquation getX,getY;
	private double[][] displacements;
	private double[][] forces;
	
	public RectangularElement(Model model, Material material,Node node1,Node node2,Node node3,Node node4) {
		super(model, material);
		


		nodeList.add(node1);
		nodeList.add(node2);	
		nodeList.add(node3);
		nodeList.add(node4);
		
		area=((Math.abs(nodeList.get(3).x-nodeList.get(1).x)+Math.abs(nodeList.get(0).x-nodeList.get(2).x))*100);

		name="e"+node1.name+node2.name+node3.name+node4.name;
		setStiffness();
		//shape=new FilledRectangle(2500, 4000);
		//System.out.println(name+" "+(node1.index*2)+" "+(node1.index*2+1)+" "+(node2.index*2)+" "+(node2.index*2+1)+" "+(node3.index*2)+" "+(node3.index*2+1)+" "+(node4.index*2)+" "+(node4.index*2+1));
	}
	
	public double[][] getG(double a,double b){
		double[][] temp=new double[4][8];
		temp[0][0]=-1+b;
		temp[0][2]=1-b;
		temp[0][4]=1+b;
		temp[0][6]=-1-b;

		temp[1][0]=-1+a;
		temp[1][2]=-1-a;
		temp[1][4]=1+a;
		temp[1][6]=1-a;
		
		temp[2][1]=-1+b;
		temp[2][3]=1-b;
		temp[2][5]=1+b;
		temp[2][7]=-1-b;
		
		temp[3][1]=-1+a;
		temp[3][3]=-1-a;
		temp[3][5]=1+a;
		temp[3][7]=1-a;
		return Maths.productWithConstat(temp, 0.25d	);
	}
	public double[][] getA(double a,double b){
		double j11=-(1d-b)*nodeList.get(0).x +(1d-b)*nodeList.get(1).x +(1d+b)*nodeList.get(2).x -(1d+b)*nodeList.get(3).x;
		double j12=-(1d-b)*nodeList.get(0).y +(1d-b)*nodeList.get(1).y +(1d+b)*nodeList.get(2).y -(1d+b)*nodeList.get(3).y;
		

		double j21=-(1d-a)*nodeList.get(0).x -(1d+a)*nodeList.get(1).x +(1d+a)*nodeList.get(2).x +(1d-a)*nodeList.get(3).x;
		double j22=-(1d-a)*nodeList.get(0).y -(1d+a)*nodeList.get(1).y +(1d+a)*nodeList.get(2).y +(1d-a)*nodeList.get(3).y;
		
		
		j11=j11/4d;
		j12=j12/4d;
		j22=j22/4d;
		j21=j21/4d;
		double[][] J=new double[][]{{j11,j12},{j21,j22}};
		double[][] A=new double[][]{{j22,-j12,0,0},{0,0,-j21,j11},{-j21,j11,j22,-j12}};
		return Maths.productWithConstat(A, 1/getJ(a,b));
	}
	
	protected double getJ(double a,double b){
		double j11=-(1d-b)*nodeList.get(0).x +(1d-b)*nodeList.get(1).x +(1d+b)*nodeList.get(2).x -(1d+b)*nodeList.get(3).x;
		double j12=-(1d-b)*nodeList.get(0).y +(1d-b)*nodeList.get(1).y +(1d+b)*nodeList.get(2).y -(1d+b)*nodeList.get(3).y;
		
		double j21=-(1d-a)*nodeList.get(0).x -(1d+a)*nodeList.get(1).x +(1d+a)*nodeList.get(2).x +(1d-a)*nodeList.get(3).x;
		double j22=-(1d-a)*nodeList.get(0).y -(1d+a)*nodeList.get(1).y +(1d+a)*nodeList.get(2).y +(1d-a)*nodeList.get(3).y;
		
		j11=j11/4d;
		j12=j12/4d;
		j22=j22/4d;
		j21=j21/4d;
		return ((j11*j22-j21*j12));
	}
	
	protected abstract void setStiffness();
	
	
	@Override
	public void setStress() {		
		double[][] stressVector=new double[3][1];
		displacements=new double[8][1];
		for(int i=0;i<4;i++){
			displacements[i*2][0]=nodeList.get(i).dx;
			displacements[i*2+1][0]=nodeList.get(i).dy;
		}
		stressVector= Maths.dotProduct(Maths.dotProduct(getMaterialMatrix(), Maths.dotProduct(getA(0,0),getG(0,0))),displacements);
		//Yazdýr.printArray(stressVector);
		stressX=stressVector[0][0]; stressY=stressVector[1][0]; stressXY=stressVector[2][0];
		stressTotal=Math.pow(Math.pow(stressX-stressY, 2)+4*Math.pow(stressXY, 2), 0.5d);
		//System.out.println(name+"  "+stressTotal);
		/*System.out.print(index+"   "+name);
		System.out.print(String.format(("   %.4e"),stressVector[0][0] )+" ");
		System.out.print(String.format(("   %.4e"),stressVector[1][0] )+" ");
		System.out.print(String.format(("   %.4e"),stressVector[2][0] )+" ");
		System.out.println(String.format(("   %.4e"),stressTotal )+" ");*/

	}
	
	public DoublePoint pointToShape(DoublePoint p){
		double minX;
		double lenght;
		double a=0,b=0;
		
		if(p.y==200){
			minX=nodeList.get(2).x;
			lenght=nodeList.get(1).x-nodeList.get(2).x;
			a=(p.x-minX)/lenght*2-1;
			b=1;
		}
		if(p.y==100){
			minX=(nodeList.get(2).x+nodeList.get(3).x);
			lenght=2000;
			a=(p.x-minX)/lenght*2-1;
			b=0;
		}
		if(p.y==0){
			minX=nodeList.get(3).x;
			lenght=nodeList.get(0).x-nodeList.get(3).x;
			a=(p.x-minX)/lenght*2-1;
			b=-1;
		}
		return new DoublePoint(a,b);
	}

 	public void getManualStress(double a,double b){
		double[][] stressVector= Maths.dotProduct(Maths.dotProduct(getMaterialMatrix(), Maths.dotProduct(getA(a,b),getG(a,b))),displacements);
		Yazdýr.printArray(stressVector);
		System.out.print(name);
	}
 	public double[] getManualStress2(double a,double b){
		double[][] stressVector= Maths.dotProduct(Maths.dotProduct(getMaterialMatrix(), Maths.dotProduct(getA(a,b),getG(a,b))),displacements);
		return new double[]{ stressVector[0][0],stressVector[1][0],stressVector[2][0]};
	}
 	public double[] getManualStress(DoublePoint p){
		double[][] stressVector= Maths.dotProduct(Maths.dotProduct(getMaterialMatrix(), Maths.dotProduct(getA(p.x,p.y),getG(p.x,p.y))),displacements);
		return new double[]{ stressVector[0][0],stressVector[1][0],stressVector[2][0]};
	}
}