package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe générique représentant un graphe non orienté
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 * @param <N>
 *          Type des noeuds du graphe
 */
public final class Graph<N> {

    private final Map<N, Set<N>> neighbors;

    /**
     * Construit un graphe non orienté à partir de la table d'adjacence
     * 
     * @param neighbors
     *          Table d'adjacence qui représente les noeuds voisins de chaque noeud
     */
    public Graph(Map<N, Set<N>> neighbors){
        Map<N, Set<N>> neighborsCopy = new HashMap<N, Set<N>>();
        for(N node: neighbors.keySet()){
            neighborsCopy.put(node, new HashSet<N>(neighbors.get(node)));
        }
        this.neighbors = Collections.unmodifiableMap(neighborsCopy);
    }
    
    /**
     * 
     * @return l'ensemble des noeuds du graphe
     */
    public Set<N> nodes(){
        Set<N> set = new HashSet<N>();
        for(N entite:neighbors.keySet()){
            set.add(entite);
            for(N ent:neighbors.get(entite)){
                if(!set.contains(ent)){
                    set.add(ent);
                }
            }
        }
        return Collections.unmodifiableSet(set);
        
    }
    
    /**
     * Donne l'ensemble des noeuds voisins du noeud passé en argument
     * 
     * @param node
     *          Noeud dont on veut obtenir les voisins
     * 
     * @return l'ensemble des voisins du noeud
     * 
     * @throws IllegalArgumentException si le noeud ne fait pas partie du graphe
     */
    public Set<N> neighborsOf(N node) throws IllegalArgumentException{
        if(!nodes().contains(node)){
            throw new IllegalArgumentException("Le noeud ne fait pas partie du graphe");
        }
        else return Collections.unmodifiableSet(neighbors.get(node));
    }
    
    /**
     * Bâtisseur de graphe non orienté
     *
     * @param <N>
     *          Type des noeuds du graphe
     */
    public final static class Builder<N>{
        
        private Map<N, Set<N>> neighbors = new HashMap<N, Set<N>>();
        
        
        /**
         * Ajoute le noeud passé en argument à l'ensemble des noeuds du graphe
         * 
         * @param n
         *          Noeud que l'on veut ajouter à l'ensemble des noeuds du graphe
         */
        public void addNode(N n){
            if(!neighbors.containsKey(n)){
                neighbors.put(n, new HashSet<N>());
            }
        }
        
        /**
         * Ajoute le premier noeud à l'ensemble des voisins du deuxième noeud, créant ainsi une arête entre les deux
         * 
         * @param n1
         *          Premier noeud que l'on veut relier
         * 
         * @param n2
         *          Deuxiême noeud que l'on veut relier
         *          
         * @throws IllegalArgumentException si un des noeuds ne fait pas partie partie du graphe
         */
        public void addEdge(N n1, N n2) throws IllegalArgumentException{
            if(!neighbors.containsKey(n1) || !neighbors.containsKey(n2)){
                throw new IllegalArgumentException("Un des deux noeuds ne fait pas partie du graphe");
            }
            neighbors.get(n2).add(n1);
            neighbors.get(n1).add(n2);
        }
        
        /**
         * 
         * @return un graphe non orienté avec les noeuds et les voisins ajoutés jusqu'à présent dans le bâtisseur
         */
        public Graph<N> build(){
            return new Graph<N>(this.neighbors);
        }
    }
}
