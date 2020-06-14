package com.katafrakt.femv4.models;

import com.katafrakt.femv4.elements.RectangularElementSele;

public class ScenarioA3 extends ScenarioA {

	@Override
	protected void modifyModel() {
		e[0]=new RectangularElementSele(this,m,n[2],n[3],n[1],n[0]);
		e[1]=new RectangularElementSele(this,m,n[4],n[5],n[3],n[2]);
		e[2]=new RectangularElementSele(this,m,n[6],n[7],n[5],n[4]);
		e[3]=new RectangularElementSele(this,m,n[8],n[9],n[7],n[6]);
		e[4]=new RectangularElementSele(this,m,n[10],n[11],n[9],n[8]);
		e[5]=new RectangularElementSele(this,m,n[12],n[13],n[11],n[10]);
		n[12].forceX=-1000;
		n[13].forceY=0;
		n[13].forceX=+1000;
		n[12].forceY=+1000;
	}

}
