package com.katafrakt.femv4.elements;

import java.awt.Graphics;

public interface IRenderer {
	public abstract void render(Graphics g);
	public int getPixel(double lenght);
	
}
