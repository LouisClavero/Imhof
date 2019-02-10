package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

/**
 * Classe mère de toutes les classes représentant les entités OSM
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public abstract class OSMEntity {

    private final long id;
    private final Attributes attributes;

    /**
     * Construit une entité OSM dotée de l'identifiant unique et des attributs donnés.
     * 
     * @param id
     *          Identifiant donné à l'entité
     * @param attributes
     *          Attribut donné à l'entité
     */
    public OSMEntity(long id, Attributes attributes){
        this.id = id;
        this.attributes = attributes;
    }
    
    /**
     * 
     * @return l'identifiant de l'entité
     */
    public long id(){
        return id;
    }
    
    /**
     * 
     * @return l'attribut de l'entité
     */
    public Attributes attributes(){
        return attributes;
    }
    
    /**
     * 
     * @param key
     *          Clef de l'attribut appartenant ou pas à l'entité
     * @return vrai si et seulement si l'entité possède l'attribut passé en argument
     */
    public boolean hasAttribute(String key){
        return attributes.contains(key);
    }
    
     /**
      * 
      * @param key
      *         Clef à partir de laquelle on obtient la valeur de l'attribut
      * @return la valeur de l'attribut donné, ou null si celui-ci n'existe pas
      */
    public String attributeValue(String key){
        if(attributes.contains(key)){
            return attributes.get(key);
        }
        else return null;       
    }
    
    /**
     * 
     *Bâtisseur de la classe des entités, et classe mèrre de tous les bâtisseurs d'entités
     */
    public static abstract class Builder {

        protected final long id;
        private boolean incomplete;
        protected final Attributes.Builder attributes = new Attributes.Builder();
        
        /**
         * Construit un bâtisseur pour une entité OSM identifiée par l'entier donné
         * 
         * @param id
         *          Identifiant de l'entité
         */
        public Builder(long id){
            this.id=id;
            this.incomplete = false;
        }
        
        /**
         * Ajoute l'association clef/valeur donnée à l'ensemble d'attributs de l'entité en cours de construction. 
         * Si un attribut de même nom avait déjà été ajouté précédemment, sa valeur est remplacée par celle donnée
         * 
         * @param key
         *          Clef de l'attribut
         * @param value
         *          Valeur de l'attribut
         */
        public void setAttribute(String key, String value){
            attributes.put(key, value);
        }
        
        /**
         * Déclare que l'entité en cours de construction est incomplète.
         */
        public void setIncomplete(){
            this.incomplete = true;
        }
        
        /**
         * 
         * @return vrai si et seulement si l'entité en cours de construction est incomplète, 
         * c-à-d si la méthode setIncomplete a été appelée au moins une fois sur ce bâtisseur depuis sa création
         */
        public boolean isIncomplete(){
            return incomplete;
        }

    }

}
