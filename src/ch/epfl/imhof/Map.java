package ch.epfl.imhof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Une carte projetée, composée d'entités géométriques attribuées
 *  
 * @author Thomas Artiach 238868
 * @author Louis CLavero 233933
 *
 */
public final class Map {
    
    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;
    
    /**
     * Construit une carte à partir des listes de polylignes et polygones attribués donnés
     * 
     * @param polyLines
     *          Liste de polylignes attribués
     * @param polygons
     *          Liste de polygones attribués
     */
    public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons){
        this.polyLines = Collections.unmodifiableList(new ArrayList<>(polyLines));
        this.polygons = Collections.unmodifiableList(new ArrayList<>(polygons));
    }
    
    /**
     * 
     * @return une liste non modifiable de polylignes
     */
    public List<Attributed<PolyLine>> polyLines(){
        return Collections.unmodifiableList(polyLines);
    }
    
    /**
     * 
     * @return une liste non modifiable de polygones
     */
    public List<Attributed<Polygon>> polygons(){
        return Collections.unmodifiableList(polygons);
    }
    
    /**
     * 
     * Bâtisseur de la classe Map
     */
    public static final class Builder{
        
        private final List<Attributed<PolyLine>> polyLines = new ArrayList<>();
        private final List<Attributed<Polygon>> polygons = new ArrayList<>();
        
        /**
         * Ajoute une polyligne attribuée à la carte en cours de construction
         * @param newPolyLine
         *          Polyligne attribuée à la carte
         */
        public void addPolyLine(Attributed<PolyLine> newPolyLine){
            polyLines.add(newPolyLine);
        }
        
        /**
         * Ajoute un polygone attribuée à la carte en cours de construction
         * @param newPolygon
         *          Polygone attribué à la carte
         */
        public void addPolygon(Attributed<Polygon> newPolygon){
            polygons.add(newPolygon);
        }
        
        /**
         * construit une carte avec les polylignes et polygones ajoutés jusqu'à présent au bâtisseur
         * @return une carte avec les polylignes et les polygones attribués jusqu'à présent
         */
        public Map build(){
            return new Map(polyLines, polygons);
        }
    }
}
