package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Une polyligne avec les points donnés dans la liste
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public abstract class PolyLine {
    
    protected final List<Point> points;
    /**
     * Construit une polyligne avec les points de la liste passées en argument
     * 
     * @param points
     *          Liste des points qui forment la polyligne
     * @throws IllegalArgumentException si la liste des points est vide
     */
    public PolyLine(List<Point> points) throws IllegalArgumentException{
        if(points.isEmpty()){
            throw new IllegalArgumentException("La liste des points est vide");
        }    
        this.points = Collections.unmodifiableList(new ArrayList<Point>(points));
    }
    /**
     * @return vrai ou faux, dépendant de si la ligne est ouverte ou fermée
     */
    public abstract boolean isClosed();
    /**
     * 
     * @return Une liste de points non modifiable
     */
    public List<Point> points(){
        return points;
    }
    /**
     * 
     * @return le premier point de la liste
     */
    public Point firstPoint(){
        return points.get(0);
    }
    /**
     *Permet de construire la polyligne en plusieurs étapes
     */
    public final static class Builder{
        
        private final List<Point> points = new ArrayList<>();
        /**
         * Ajoute le point passé en argument à la liste de points
         * 
         * @param newPoint
         *          Le point que l'on veut ajouter à la liste
         */
        public void addPoint(Point newPoint){
            points.add(newPoint);
        }
        /**
         * 
         * @return une polyligne ouverte avec les points de la liste
         */
        public OpenPolyLine buildOpen(){
            return new OpenPolyLine(points);
        }
        /**
         * 
         * @return une polyligne fermée avec les points de la liste
         */
        public ClosedPolyLine buildClosed(){
            return new ClosedPolyLine(points);
        }
    }

}
