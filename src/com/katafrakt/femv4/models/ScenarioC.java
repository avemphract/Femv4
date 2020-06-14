package com.katafrakt.femv4.models;

import com.katafrakt.femv4.elements.Element;
import com.katafrakt.femv4.elements.Node;
import com.katafrakt.femv4.elements.RectangularElement;
import com.katafrakt.femv4.materials.Material;
import com.katafrakt.femv4.maths.Maths;
import com.katafrakt.femv4.shapes.DoublePoint;
import com.katafrakt.framework.util.Yazdýr;

public abstract class ScenarioC extends Model {
	public Element[] e;
	public Node[] n;
	Material m;
	
	@Override
	public void setModel() {
		e=new Element[6];
		n=new Node[14];
		m=Material.soru4;
		
		n[0]=new Node(this, 0, 0, "n1");
		n[0].statX=true;
		n[0].statY=true;
		n[1]=new Node(this, 0, 200, "n2");
		n[1].statX=true;
		n[1].statY=true;

		n[2]=new Node(this, 1100, 0, "n3");
		n[3]=new Node(this, 900, 200, "n4");

		n[4]=new Node(this, 1900, 0, "n5");
		n[5]=new Node(this, 2100, 200, "n6");

		n[6]=new Node(this, 3100, 0, "n7");
		n[7]=new Node(this, 2900, 200, "n8");

		n[8]=new Node(this, 3900, 0, "n9");
		n[9]=new Node(this, 4100, 200, "n10");

		n[10]=new Node(this, 5100, 0, "n11");
		n[11]=new Node(this, 4900, 200, "n12");

		n[12]=new Node(this, 6000, 0, "n13");
		n[13]=new Node(this, 6000, 200, "n14");
		
		n[12].forceX=1000;
		n[13].forceX=-1000;
		n[13].forceY=-1000;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
