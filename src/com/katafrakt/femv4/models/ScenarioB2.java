package com.katafrakt.femv4.models;

import com.katafrakt.femv4.elements.RectangularElementRedu;

public class ScenarioB2 extends ScenarioB {

	@Override
	void modifyModel() {
		singular=true;
		deltafactor=200000;
		e[0]=new RectangularElementRedu(this,m,n[2],n[3],n[1],n[0]);
		e[1]=new RectangularElementRedu(this,m,n[4],n[5],n[3],n[2]);
		e[2]=new RectangularElementRedu(this,m,n[6],n[7],n[5],n[4]);
		e[3]=new RectangularElementRedu(this,m,n[8],n[9],n[7],n[6]);
		e[4]=new RectangularElementRedu(this,m,n[10],n[11],n[9],n[8]);
		e[5]=new RectangularElementRedu(this,m,n[12],n[13],n[11],n[10]);

	}

}
