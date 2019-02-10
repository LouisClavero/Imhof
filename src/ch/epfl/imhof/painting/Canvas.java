package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public interface Canvas {

	public abstract void drawPolyLine(PolyLine polyline, LineStyle linestyle);
	public abstract void drawPolygon(Polygon polygon, Color color);
	
}
