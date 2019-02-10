package ch.epfl.imhof;

/**
 * Représente une entité de type T dotée d'attributs
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 * @param <T>
 *          Entité que l'on veut doter d'attributs
 */
public final class Attributed<T> {

    private final Attributes attributes;
    private final T value;
    
    /**
     * Construit une valeur attribuée dont la valeur et les attributs sont ceux donnés
     * 
     * @param value
     *          Valeur que l'on veut doter des attributs passés en argument
     *          
     * @param attributes
     *          Attributs que l'on veut donner à la valeur passée en argument
     * 
     */
    public Attributed(T value, Attributes attributes){
        this.value = value;
        this.attributes = attributes;
    }
    
    /**
     * 
     * @return la valeur de type T
     */
    public T value(){
        return value;
    }
    
    /**
     * 
     * @return les attributs de la valeur de type T
     */
    public Attributes attributes(){
        return attributes;
    }
    
    /**
     * Vérifie si les attributs incluent celui qui est passé en argument
     * 
     * @param attributeName
     *          Attribut appartenant ou pas aux attributs de la valeur
     *          
     * @return vrai si l'attribut passé en argument est inclus dans l'ensemble d'attributs et faux autrement
     */
    public boolean hasAttribute(String attributeName){
        return (attributes.contains(attributeName));
    }
    
    /**
     * 
     * @param attributeName
     *          Nom de l'attribut à partir duquel on veut obtenir la valeur
     *          
     * @return la valeur associée à l'attribut passé en argument, ou null si celle-ci n'existe pas
     */
    public String attributeValue(String attributeName){
            return attributes.get(attributeName);
    }
    
    /**
     * 
     * @param attributeName
     *          Nom de l'attribut à partir duquel on veut obtenir la valeur
     *          
     * @param defaultValue
     *          Valeur par défaut
     *          
     * @return la valeur associée à l'attribut donné, ou la valeur par défaut donnée si celui-ci n'existe pas
     */
    public String attributeValue(String attributeName, String defaultValue){       
        return attributes.get(attributeName, defaultValue);
    }
    
    /**
     * 
     * @param attributeName
     *          Nom de l'attribut à partir duquel on veut obtenir la valeur sous forme d'entier
     *          
     * @param defaultValue
     *          Valeur par défaut associée à l'attribut ci celui-ci n'en a pas
     * 
     * @return la valeur entière associée à l'attribut donné, ou la valeur par défaut si celui-ci n'existe pas
     *          ou si la valeur qui lui est associée n'est pas un entier valide
     */
    public int attributeValue(String attributeName, int defaultValue){
        return attributes.get(attributeName, defaultValue);
    }
}
