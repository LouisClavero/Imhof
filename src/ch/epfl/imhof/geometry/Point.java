package ch.epfl.imhof.geometry;

import java.util.function.Function;

/**
 * Un point à la surface de la Terre en coordonnées cartésiennes
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public final class Point {
	private double x;
	private double y;

	/**
	 * Construit un point à partir des valeurs x et y
	 * 
	 * @param x
	 *        L'abscisse du point, en degrés
	 * @param y
	 *        L'ordonnée du point, en degrés
	 */
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}

	/**
	 * @return L'abscisse du point, en degrés
	 */
	public double x(){
		return x;
	}

	/**
	 * @return L'ordonnée du point, en degrés
	 */
	public double y(){
		return y;
	}

	public static Function<Point, Point> alignedCoordinateChange(Point p, Point p1, Point p2, Point p3) throws IllegalArgumentException{
		if(p.x()==p1.x()||p.y()==p1.y()){
			throw new IllegalArgumentException("les deux points se trouvent sur une meme ligne");
		}
		double coefX = (p3.x()-p1.x()) / (p2.x()-p.x());
		double coefY = (p3.y()-p1.y()) / (p2.y()-p.y());
		double ordX = p1.x()- p.x()*coefX;
		double ordY = p1.y() - p.y()*coefY;
		Function<Point, Point> f =
				(Point point) -> {
					return new Point(coefX*point.x() + ordX, coefY*point.y() + ordY);		
				};
		return f;
	}

}
