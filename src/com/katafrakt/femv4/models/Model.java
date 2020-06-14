package com.katafrakt.femv4.models;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import com.katafrakt.femv4.elements.Element;
import com.katafrakt.femv4.elements.Node;
import com.katafrakt.femv4.elements.RectangularElement;
import com.katafrakt.femv4.main.Main;
import com.katafrakt.femv4.main.ModelVisualization;
import com.katafrakt.femv4.maths.Maths;
import com.katafrakt.femv4.maths.Matrix;
import com.katafrakt.femv4.maths.Vector;
import com.katafrakt.femv4.shapes.DoublePoint;
import com.katafrakt.femv4.shapes.Triangle;
import com.katafrakt.framework.util.Yazdýr;


public abstract class Model {
	public static ArrayList<Model> modelList=new ArrayList<Model>();
	public static Model currentModel;
	
	public Matrix stiffness;
	public Vector forces;
	public Vector displacements;
	
	public double deltafactor=100;
	
	public ArrayList<Node> nodeList;
	public ArrayList<Element> elementList;
	
	public boolean singular=false;
	
	public double maxStress=0.01;
	
	public Model(){
		currentModel=this;
		modelList.add(this);
		elementList=new ArrayList<Element>();
		nodeList=new ArrayList<Node>();
		setModel();
		modifyModel();
		createStiffness();
		createForces();
		modifyMatrices();
		//Yazdýr.printArray(stiffness.array);
		//Yazdýr.printVector(forces.vector);
		solveDisplacement();
		placeDisplacement();
		//Yazdýr.printVector(displacements.vector);
		setStresses();
		afterProcess();

		
	}
	
	

	



	public abstract void setModel();
	abstract void modifyModel();
	public void createStiffness(){
		double[][] tempStiffness=new double[nodeList.size()*2][nodeList.size()*2];
		for(Element e:elementList){		
			for(int i=0;i<e.nodeList.size();i++){
				for(int j=0;j<e.nodeList.size();j++){
					tempStiffness[e.nodeList.get(i).index*2][e.nodeList.get(j).index*2]+=e.k[i*2][j*2];
					tempStiffness[e.nodeList.get(i).index*2+1][e.nodeList.get(j).index*2]+=e.k[i*2+1][j*2];
					tempStiffness[e.nodeList.get(i).index*2][e.nodeList.get(j).index*2+1]+=e.k[i*2][j*2+1];
					tempStiffness[e.nodeList.get(i).index*2+1][e.nodeList.get(j).index*2+1]+=e.k[i*2+1][j*2+1];
				}
			}
		}
		//Yazdýr.printArray(tempStiffness);
		stiffness=new Matrix(tempStiffness);
	}

	public void createForces(){
		double[] tempForces=new double[nodeList.size()*2];
		for(Node n:nodeList){
			tempForces[n.index*2]=n.forceX;
			tempForces[n.index*2+1]=n.forceY;
		}
		forces=new Vector(tempForces);
		
	}
	
	public void modifyMatrices(){
		for(Node n:nodeList){
			if(n.statX){
				//System.out.println(n.name);
				stiffness.addElim(2*n.index);
				forces.addElim(2*n.index);
			}
			if(n.statY){
				//System.out.println(n.index*2+1);
				stiffness.addElim(2*n.index+1);
				forces.addElim(2*n.index+1);
			}
		}
	}
	
	public void solveDisplacement(){
		
		RealMatrix coefficient=new Array2DRowRealMatrix(stiffness.array);
		DecompositionSolver solver;
		if(singular==false)
			solver = new LUDecomposition(coefficient).getSolver();
		else{
			SingularValueDecomposition inc= new SingularValueDecomposition(coefficient);
			solver=inc.getSolver();
		}
		RealVector constant=new ArrayRealVector(forces.vector);
		RealVector solution = solver.solve(constant);
		
		displacements=new Vector(solution.toArray());
		
	}
	
	public void placeDisplacement(){
		int k=0;
		for(Node n:nodeList){
			if(!stiffness.eliminated.contains(n.index*2)){
				n.dx=displacements.vector[k];
				k++;
			}
			if(!stiffness.eliminated.contains(n.index*2+1)){
				n.dy=displacements.vector[k];
				k++;
			}
			//System.out.println(n.name+"   "+n.dx+"   "+n.dy);
		}
	}
	private void setStresses() {
		for(Element e:elementList){
			e.setStress();
		}
		for(Element e:elementList){
			if(maxStress<e.stressTotal)
				maxStress=e.stressTotal;
		}
		
	}

	public void render(Graphics g){
		g.setColor(Color.GREEN);
		g.drawLine(ModelVisualization.frameStart, ModelVisualization.frameStart, Main.width, ModelVisualization.frameStart);
		g.setColor(Color.RED);
		g.drawLine(ModelVisualization.frameStart, ModelVisualization.frameStart, ModelVisualization.frameStart,Main.height);
		for(Element e:elementList)
			e.render(g);
	}
	protected void afterProcess(){
		RectangularElement e;
		//Plot the axial stress on top of the beam
		System.out.println("%****************************"+this);
		System.out.println("%Plot the axial stress");
		System.out.print("[");
		for(int x=0;x<=6000;x+=10){
			System.out.print(x+" ");
		}
		System.out.print("];");

		System.out.println();
		
		System.out.print("[");
		for(int x=0;x<=6000;x+=10){
			e= (RectangularElement) getElement(new DoublePoint(x,200));
			DoublePoint sh=e.pointToShape(new DoublePoint(x,200));
			double[] d=e.getManualStress(sh);
			//System.out.print(String.format(("%.5e"), d[0])+",");
			System.out.print(d[0]+" ");
		}
		System.out.print("];");
		
		System.out.println();
		//Plot the shear stress at the centroid
		System.out.println("%Plot the shear stress");
		System.out.print("[");
		for(int x=0;x<=6000;x+=10){
			System.out.print(x+" ");
		}
		System.out.print("];");
		
		System.out.println();
		System.out.print("[");
		for(int x=0;x<=6000;x+=10){
			e= (RectangularElement) getElement(new DoublePoint(x,100));
			DoublePoint sh=e.pointToShape(new DoublePoint(x,100));
			double[] d=e.getManualStress(sh);
			//System.out.print(String.format(("%.5e"), d[0])+",");
			System.out.print(d[2]+" ");
		}
		System.out.print("];");
		System.out.println();
		//Plot the transverse displacement of the beam 
		System.out.println("%Plot the transverse");
		System.out.print("[");
		for(int x=0;x<14;x+=2){
			System.out.print((nodeList.get(x).dx+nodeList.get(x+1).dx)/2+" ");
		}

		System.out.println("];");
		//Calculate the reaction shear force and reaction moment
		System.out.println("%{Shear force and reaction moment");
		for(int x=0;x<6;x++){
			RectangularElement e1=(RectangularElement)elementList.get(x);
			double[] d=Maths.productWithConstat(e1.getManualStress2(1, 0),DoublePoint.toLenght(e1.nodeList.get(0).toPoint(), nodeList.get(1).toPoint()));
			System.out.print("Element"+x);
			Yazdýr.printVector(d);
			System.out.println();
		}
		System.out.println("%}");	
	}
	public abstract String toString();
	
	
	public Element getElement(DoublePoint p) {
		for(Element e:elementList){
			Triangle t1=new Triangle(e.nodeList.get(0).toPoint(),e.nodeList.get(1).toPoint(),p);
			Triangle t2=new Triangle(e.nodeList.get(1).toPoint(),e.nodeList.get(2).toPoint(),p);
			Triangle t3=new Triangle(e.nodeList.get(2).toPoint(),e.nodeList.get(3).toPoint(),p);
			Triangle t4=new Triangle(e.nodeList.get(3).toPoint(),e.nodeList.get(0).toPoint(),p);
			//System.out.print(t1.area+t2.area+t3.area+t4.area+" "+e.area+" ");
			if(t1.area+t2.area+t3.area+t4.area<=e.area+0.1d){
				//System.out.print(t1.area+t2.area+t3.area+t4.area);
				return e;
				}
		}
		return null;
		
	}
}