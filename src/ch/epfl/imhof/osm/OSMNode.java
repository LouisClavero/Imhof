package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

/**
 * Classe qui hérite de la classe des entités OSM et qui représente un noeud OSM
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public final class OSMNode extends OSMEntity {

    private final PointGeo position;

    /**
     * Construit un noeud OSM à partir de l'identifiant, sa position et ses attributs
     * 
     * @param id
     *          Identifiant du noeud 
     * @param position
     *          Position du noeud en coordonnées sphériques
     * @param attributes
     *          Attributs du noeud
     */
    public OSMNode(long id, PointGeo position, Attributes attributes){

        super(id, attributes);
        this.position = position;

    }
    
    /**
     * 
     * @return la position du noeud
     */
    public PointGeo position(){
        return position;
    }
    
    /**
     * 
     *Bâtisseur d'un noeud OSM qui hèrite du bâtisseur d'entités
     */
    public final static class Builder extends OSMEntity.Builder {

        private final PointGeo position;

        /**
         * Construit un bâtisseur pour un nœud ayant l'identifiant et la position donnés
         * 
         * @param id
         *          Identifiant du noeud
         * @param position
         *          Position du noeud
         */
        public Builder(long id, PointGeo position){
            super(id);
            this.position = position;
        }
        
        /**
         * 
         * @return un nœud OSM avec l'identifiant et la position passés au constructeur, 
         * et les éventuels attributs ajoutés jusqu'ici au bâtisseur
         * 
         * @throws IllegalStateException si le noeud en cours de construction est incomplet
         */
        public OSMNode build() throws IllegalStateException{

            if(super.isIncomplete()){
                throw new IllegalStateException("Le noeud en cours de construction est incomplet");
            }
            return new OSMNode(id, position, attributes.build());

        }

    }

}