package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Une carte OpenStreetMap, c'est-à-dire un ensemble de chemins et de relations
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public final class OSMMap {

	private final List<OSMWay> ways;
	private final List<OSMRelation> relations;

	/**
	 * Construit une carte OSM à partir des chemins et des relations donnés
	 * 
	 * @param ways
	 *          Ensemble de chemins de la carte
	 * 
	 * @param relations
	 *          Ensemble de relations de la carte
	 */
	public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations){
		this.ways = Collections.unmodifiableList(new ArrayList<OSMWay>(ways));
		this.relations = Collections.unmodifiableList(new ArrayList<OSMRelation>(relations));
	}

	/**
	 * 
	 * @return l'ensemble des chemins sous forme de liste
	 */
	public List<OSMWay> ways(){
		return ways;
	}

	/**
	 * 
	 * @return l'ensemble des relations sous forme de liste
	 */
	public List<OSMRelation> relations(){
		return relations;
	}

	/**
	 * 
	 *Bâtisseur de cartes OSM
	 */
	public static final class Builder{

		protected final Map<Long, OSMNode> nodes = new HashMap<>();
		protected final Map<Long, OSMWay> ways = new HashMap<>();
		protected final Map<Long, OSMRelation> relations = new HashMap<>();

		/**
		 * Ajoute le noeud passé en argument à la liste de noeuds de la carte
		 * 
		 * @param newNode
		 *          Noeud que l'on veut ajouter à la liste des noeuds
		 */
		public void addNode(OSMNode newNode){
			if(newNode != null){
				nodes.put(newNode.id(), newNode);
			}
		}

		/**
		 * Donne le noeud à partir de l'identifiant, ou rien si celui-ci n'a pas été ajouté au bâtisseur
		 * 
		 * @param id
		 *          Identifiant du noeud recherché
		 * 
		 * @return le noeud dont l'identifiant unique est égal à celui donné, 
		 *          ou null si ce noeud n'a pas été ajouté précédemment au bâtisseur
		 */
		public OSMNode nodeForId(long id){
			return nodes.get(id);
		}
		/**
		 * Ajoute le chemin passé en argument à la liste des chemins de la carte
		 * 
		 * @param newWay
		 *          Chemin que l'on veut ajouter à la liste des chemins de la carte
		 */
		public void addWay(OSMWay newWay){
			if(newWay == null){
				throw new NullPointerException();
			}
			else{
				ways.put(newWay.id(), newWay);
			}

		}

		/**
		 * Donne le chemin à partir de l'identifiant, ou rien si celui-ci n'a pas été ajouté au bâtisseur
		 * 
		 * @param id
		 *          identifiant du chemin recherché
		 * 
		 * @return le chemin dont l'identifiant unique est égal à celui donné, 
		 *          ou null si ce chemin n'a pas été ajouté précédemment au bâtisseur
		 */
		public OSMWay wayForId(long id){
			return ways.get(id);
		}

		/**
		 * Ajoute la relation passée en argument à la liste des relations de la carte
		 * 
		 * @param newRelation
		 *          Relation que l'on veut ajouter à la liste des relations de la carte
		 */
		public void addRelation(OSMRelation newRelation){
			if(newRelation == null){
				throw new NullPointerException();
			}
			else{ 
				relations.put(newRelation.id(), newRelation);
			}
		}

		/**
		 * Donne la relation à partir de l'identifiant, ou rien si celle-ci n'a pas été ajoutée au bâtisseur
		 * 
		 * @param id
		 *          Identifiant de la relation recherchée
		 * 
		 * @return la relation dont l'identifiant unique est égal à celui donné, 
		 *          ou null si cette relation n'a pas été ajoutée précédemment au bâtisseur
		 */
		public OSMRelation relationForId(long id){
			return relations.get(id);
		}

		/**
		 * 
		 * @return une carte OSM contenant les chemins et la relations ajoutés jusqu'à présent
		 */
		public OSMMap build(){
			return new OSMMap(ways.values(), relations.values());
		}
	}

}
