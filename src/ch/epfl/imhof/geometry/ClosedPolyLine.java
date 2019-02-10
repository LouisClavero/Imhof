package ch.epfl.imhof.geometry;

import java.util.List;
/**
 * Une polyligne fermée avec les points de la liste
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public final class ClosedPolyLine extends PolyLine {

	/**
	 * Construit une polyligne fermée avec les points de la liste passée en argument
	 * 
	 * @param points
	 *          La liste des points qui forment la polyligne
	 * @throws IllegalArgumentException si la liste des points est vide
	 */
	public ClosedPolyLine(List<Point> points) throws IllegalArgumentException {
		super(points);
	}

	public boolean isClosed() {
		return true;
	}
	/**
	 * 
	 * @return l'aire de la zone dans la polyligne
	 */
	public double area(){
		double area = 0.0;
		if(points.size() <= 2){
			area = 0.0;
		}else{
			for(int i=0; i<points.size(); i++){                    
				area += points.get(i).x()*(points.get(modIndex(i+1)).y() - points.get(modIndex(i-1)).y());
			}
			area = Math.abs(area);
		}
		return area/2;
	}
	/**
	 * Vérifie si le point passé en argument est à l'intérieur de la polyligne fermée
	 * 
	 * @param p
	 *      Le point en coordonnées cartésiennes que l'on veut situer
	 * @return vrai si le point est à l'intérieur de la polyligne et faux sinon
	 */
	public boolean containsPoint(Point p){

		int index = 0;

		for(int i=0; i < points.size(); i++){
			if(points.get(i).y() <= p.y()){
				if(points.get(modIndex(i+1)).y() > p.y() && isLeft(p, points.get(modIndex(i)), points.get(modIndex(i+1)))){
					index++;
				}
			}
			else if(points.get(modIndex(i+1)).y() <= p.y() && isLeft(p, points.get(modIndex(i+1)), points.get(i))){
				index--;
			}
		}      
		return (index != 0);
	}
	/**
	 * Vérifie si un point est à gauche du segment formé par 2 points
	 * 
	 * @param p
	 *      Le point à gauche ou à droite du segment
	 * @param p1
	 *      Le premier point du segment
	 * @param p2
	 *      Le deuxième point du segment
	 * @return vrai si le point est à gauche du segment et faux autrement
	 */
	private boolean isLeft(Point p, Point p1, Point p2){

		return ((p1.x()-p.x()) * (p2.y()-p.y()) > (p2.x()-p.x()) * (p1.y()-p.y()));
	}
	/**
	 * Transforme un indice généralisé en indice valide
	 * 
	 * @param index
	 *      L'indice que l'on veut transformer
	 * @return L'indice avec une valeur valide
	 */
	private int modIndex(int index){
		return java.lang.Math.floorMod(index, points.size());
	}
}
