package com.katafrakt.femv4.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;

import com.katafrakt.femv4.main.ModelVisualization;
import com.katafrakt.femv4.materials.Material;
import com.katafrakt.femv4.maths.Maths;
import com.katafrakt.femv4.models.Model;
import com.katafrakt.femv4.shapes.Shape;

public abstract class Element implements IRenderer {
	Model model;
	public int index;
	
	public Material material;
	double thickness;
	public ArrayList<Node> nodeList;
	public String name;

	public double area;

	public double[][] k;
	
	public double stressX;
	public double stressY;
	public double stressXY;
	
	public double stressTotal;
	
	public Element(Model model,Material material){
		this.model=model;
		this.material=material;
		this.thickness=30;
		nodeList=new ArrayList<Node>();
		model.elementList.add(this);
		index=model.elementList.size();
	}
	public double[][] getMaterialMatrix(){
		double[][] array=new double[3][3];
		array[0][0]=1;
		array[0][1]=material.poisson;
		array[1][0]=material.poisson;
		array[1][1]=1;
		array[2][2]=(1-Math.pow(material.poisson,2))/2;
		array[2][2]=(1-material.poisson)/2;
		array=Maths.productWithConstat(array, material.elasticityModul/(1-Math.pow(material.poisson, 2)));
		return array;
	}
	public double[][] getStrainMaterialMatrix(){
		double[][] array=new double[3][3];
		array[0][0]=1;
		array[0][1]=material.poisson;
		array[1][0]=material.poisson;
		array[1][1]=1;
		array=Maths.productWithConstat(array, material.elasticityModul/(1-Math.pow(material.poisson, 2)));
		return array;
	}
	public double[][] getShearMaterialMatrix(){
		double[][] array=new double[3][3];
		array[2][2]=(1-material.poisson)/2;
		array=Maths.productWithConstat(array, material.elasticityModul/(1-Math.pow(material.poisson, 2)));
		return array;
	}
	
	@Override
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
	    g2.setStroke(new BasicStroke((int)(2)));
		Polygon poly=new Polygon();
		int xC = 0,yC = 0;
		for(Node n:nodeList){
			poly.addPoint(getPixel(n.x+n.dx*model.deltafactor), getPixel(n.y+n.dy*model.deltafactor));
			xC+=getPixel(n.x+n.dx*model.deltafactor);yC+=getPixel(n.y+n.dy*model.deltafactor);
		}
		xC/=poly.npoints;yC/=poly.npoints;
		Color c=new Color((int)((stressTotal/model.maxStress)*255), (int)((1-stressTotal/model.maxStress)*255), 0);
		g2.setColor(c);
		g2.fill(poly);
		g2.setColor(Color.BLACK);
		g2.draw(poly);
		g2.setColor(Color.BLUE);
		g2.setFont(new Font("TimesRoman", Font.BOLD, 15));
		//g2.drawString(String.format(("%.2e"), stressTotal), xC-20, yC+4);
		g2.drawString(index+"", xC-5, yC+1);
		for(int n=0;n<nodeList.size();n++){
			nodeList.get(n).render(g);
		}
	}
	
	public int getPixel(double lenght) {
		return (int)(lenght*ModelVisualization.scale+ModelVisualization.frameStart);
	}
	
	public String toString() {
		/*String str = new String();
		for(Node n:nodeList){
			str=str+n.name;
		}*/
		return index+"";
	}

	public abstract void setStress();
}
