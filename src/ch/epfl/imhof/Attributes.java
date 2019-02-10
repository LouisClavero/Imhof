package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Un ensemble d'arttributs d'un polygone avec la valeur qui leur est associée
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public final class Attributes {

	private final Map<String, String> attributes;

	/**
	 * Construit un ensemble d'attributs avec les paires clef/valeur présentes dans la table associative donnée
	 * 
	 * @param attributes
	 *          L'attribut donné à un polygone
	 */
	public Attributes(Map<String, String> attributes){

		this.attributes = Collections.unmodifiableMap(new HashMap<>(attributes));

	}

	/**
	 * 
	 * @return vrai si l'ensemble d'attributs en vide et faux autrement
	 */
	public boolean isEmpty(){

		return attributes.isEmpty();

	}

	/**
	 * Vérifie si l'ensemble d'attributs contient la clef donnée
	 * 
	 * @param key
	 *          Clef appartenant ou pas à l'ensemble des attributs
	 *          
	 * @return vrai si la clef est dans l'ensemble des attributs et faux autrement
	 */
	public boolean contains(String key){

		return attributes.containsKey(key);

	}

	/**
	 * Retourne la valeur associée à la clef donnée, ou null si celle-ci n'exite pas
	 * 
	 * @param key
	 *          Clef à partir de laquelle on obtient la valeur
	 *          
	 * @return la valeur associée à la clef donnée, ou null si la clef n'existe pas.
	 */
	public String get(String key){
		return attributes.get(key);
	}

	/**
	 * Retourne valeur associée à la clef donnée, ou la valeur par défaut passée en argument
	 * la valeur n'existe pas
	 * 
	 * @param key
	 *          Clef à partir de laquelle on obtient la valeur
	 *          
	 * @param defaultValue
	 *          Valeur passée en argument et associée à la clef si celle-ci n'en a pas déjà une
	 *          
	 * @return la valeur associée à la clef donnée, ou la valeur par défaut donnée si aucune valeur ne lui est associée
	 */
	public String get(String key, String defaultValue){
		if(attributes.get(key) == null){
			return defaultValue;
		}
		else return attributes.get(key);
	}

	/**
	 * Retourne la valeur associée à la clef sous forme d'entier, ou la valeur par défaut passée en argument
	 * si la valeur n'existe pas ou n'est pas un entier valide
	 * 
	 * @param key
	 *          Clef à partir de laquelle on obtient la valeur
	 *          
	 * @param defaultValue
	 *          Valeur passée en argument et associée à la clef si celle-ci n'en a pas déjà une
	 *          
	 * @return la valeur sous forme d'entier associée à la clef donnée, ou la valeur par défaut donnée si aucune valeur ne lui est associée,
	 *           ou si cette valeur n'est pas un entier valide
	 */
	public int get(String key, int defaultValue){

		String s = attributes.get(key);
		if(s == null || !stringParsable(s)){
			return defaultValue;
		}
		return Integer.parseInt(s);
	}

	/**
	 * Retourne une version filtrée des attributs ne contenant que ceux dont le nom figure dans l'ensemble passé.
	 * 
	 * @param keysToKeep
	 *             Ensemble contenant les attributs que l'on veut garder
	 *             
	 * @return un ensemble d'attributs à partir d'une table associative avec les attributs de l'ensemble passé en argument;
	 */
	public Attributes keepOnlyKeys(Set<String> keysToKeep){
		Map<String, String> map = new HashMap<>();
		for(String s : keysToKeep){
			if(attributes.containsKey(s) ){
				map.put(s, attributes.get(s));
			}
		}

		return new Attributes(map);

	}

	/**
	 * Bâtisseur de la classe d'attributs
	 */
	public final static class Builder{

		private final Map<String, String> attributes = new HashMap<String, String>();

		/**
		 * Ajoute l'association clef/valeur donnée à l'ensemble d'attributs en cours de construction. 
		 * Si un attribut de même nom avait déjà été ajouté précédemment à l'ensemble, sa valeur est remplacée par celle donnée.
		 * 
		 * @param key
		 *          Clef que l'on veut ajouter à la table associative
		 *          
		 * @param value
		 *          Valeur associée à la clef que l'on veut ajouter
		 */
		public void put(String key, String value){
			attributes.put(key, value);
		}

		/**
		 * 
		 * @return un ensemble d'attributs contenant les associations clef/valeur ajoutées jusqu'à présent
		 */
		public Attributes build(){
			return new Attributes(attributes);
		}

	}

	/**
	 * Vérifie si une suite de caractères peut être transformée en entier
	 * 
	 * @param string
	 *          Suite de caractères que l'on veut passer en entier
	 *          
	 * @return vrai si la suite de caractères peut être passée en entier et faux autrement
	 */
	private static boolean stringParsable(String string){

		boolean parsable = true;
		try{
			Integer.parseInt(string);
		}catch(NumberFormatException e){
			parsable = false;
		}
		return parsable;
	}



}
