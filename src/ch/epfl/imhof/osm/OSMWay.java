package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * Classe qui représente un chemin et qui hérite de la classe des entités OSM
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public final class OSMWay extends OSMEntity {

    private final List<OSMNode> nodes;

    /**
     * Construit un chemin à partir de son identifiant, ses noeuds et ses attributs
     * 
     * @param id
     *          Identifiant du chemin
     * @param nodes
     *          Noeuds reliés qui forment le chemin
     * @param attributes
     *          Attributs du chemin
     * @throws IllegalArgumentException si la liste de noeuds contient moins de 2 noeuds
     */
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) throws IllegalArgumentException{
        super(id, attributes);
        if(nodes.size()<2){
            throw new IllegalArgumentException("La liste de noeuds possède moins de 2 éléments");
        }
        this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
    }

    /**
     * 
     * @return le nombre de noeuds du chemin
     */
    public int nodesCount(){
        return nodes.size();
    }

    /**
     * 
     * @return la liste de noeuds du chemin
     */
    public List<OSMNode> nodes(){
        return Collections.unmodifiableList(nodes);
    }

    /**
     * 
     * @return la liste des noeuds du chemin sans le dernier si celui-ci est identique au premier
     */
    public List<OSMNode> nonRepeatingNodes(){
        final List<OSMNode> list = new ArrayList<>();
        if(firstNode().equals(lastNode())){
            for(int i=0; i<nodes.size()-1; i++){
                list.add(nodes.get(i));
            }
        }
        else for(int i=0; i<nodes.size(); i++){
            list.add(nodes.get(i));
        }

        return Collections.unmodifiableList(list);
    }

    /**
     * 
     * @return le premier noeud du chemin
     */
    public OSMNode firstNode(){
        return nodes.get(0);
    }

    /**
     * 
     * @return le dernier noeud du chemin
     */
    public OSMNode lastNode(){
        return nodes.get(nodes.size()-1);
    }

    /**
     * Vérifie que le chemin est fermé
     * 
     * @return vrai si et seulement si le chemin est fermé
     */
    public boolean isClosed(){
        return firstNode().equals(lastNode());
    }

    /**
     * 
     *Bâtisseur d'un chemin qui hérite du bâtisseur d'entités
     */
    public final static class Builder extends OSMEntity.Builder {

        private final List<OSMNode> nodes = new ArrayList<>();

        /**
         * Construit un bâtisseur pour un chemin ayant l'identifiant donné
         * 
         * @param id
         *          Identifiant du chemin à construire
         */
        public Builder(long id){
            super(id);
        }

        /**
         * Ajoute un noeud à la fin des nœuds du chemin en cours de construction
         * 
         * @param newNode
         *          Noeud que l'on veut ajouter à la liste des noeuds
         */
        public void addNode(OSMNode newNode){
            nodes.add(newNode);
        }

        /**
         * Construit le chemin ayant les noeuds et les attributs ajoutés jusqu'à présent
         * 
         * @return le chemin construit avec les noeuds et les attribut
         * @throws IllegalStateException si le chemin en construction est incomplet
         */
        public OSMWay build() throws IllegalStateException{

            if(nodes.size()<2 || super.isIncomplete()){
                throw new IllegalStateException("Le chemin en cours de construction est incomplet");
            }

            return new OSMWay(id, nodes, attributes.build());

        }   

        /**
         * Redéfini la méthode de la super classe, et retourne vrai si la liste de noeuds contient moins de 2 noeuds
         */
        public boolean isIncomplete(){
            return (nodes.size()<2 || super.isIncomplete());
        }

    }

}
