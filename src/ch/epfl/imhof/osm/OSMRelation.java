package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * Classe qui représente une relation OSM et qui hérite de la classe des entités OSM
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */

public final class OSMRelation extends OSMEntity {

    private final List<Member> members;
    
    /**
     * Construit une relation à partir de son identifiant unique, ses membres et ses attributs
     * 
     * @param id
     *          Identifiant de la relation
     * @param members
     *          Membres de la relation
     * @param attributes
     *          Attributs de la relation
     */
    public OSMRelation(long id, List<Member> members, Attributes attributes){
        super(id, attributes);
        this.members = Collections.unmodifiableList(new ArrayList<>(members));
    }
    
    /**
     * 
     * @return la liste des membres
     */
    public List<Member> members(){
        return members;
    }
    
    /**
     * 
     *Classe qui représente un membre d'une relation OSM
     */
    public final static class Member{

        private final Type type;
        private final String role;
        private final OSMEntity member;
        
        /**
         * Construit un membre ayant le type, le rôle et la valeur donnés
         * 
         * @param type
         *          Type du membre
         * @param role
         *          Rôle du membre
         * @param member
         *          Valeur du membre
         */
        public Member(Type type, String role, OSMEntity member){
            this.type = type;
            this.role = role;
            this.member = member;
        }
        
        /**
         * 
         * @return le type du membre
         */
        public Type type(){
            return type;
        }
        
        /**
         * 
         * @return le rôle du membre
         */
        public String role(){
            return role;
        }
        
        /**
         * 
         * @return le membre
         */
        public OSMEntity member(){
            return member;
        }
        
        /**
         * 
         *Énumère les trois types de membres possibles qu'une relation peut comporter
         */
        public enum Type{
            NODE,
            WAY,
            RELATION;
        }


    }
    
    /**
     * 
     *Bâtisseur d'une relation qui hérrite du bâtisseur d'entités
     */
    public final static class Builder extends OSMEntity.Builder{

        private final List<Member> members = new ArrayList<>();
        
        /**
         * Construit un bâtisseur pour une relation ayant l'identifiant donné
         * 
         * @param id
         *          Identifiant de la relation
         */
        public Builder(long id){
            super(id);
        }
        
        /**
         * Ajoute un nouveau membre de type et de rôle donnés à la relation
         * 
         * @param type
         *          Type du membre
         * @param role
         *          Rôle du membre
         * @param newMember
         *          Valeur du membre
         */
        public void addMember(Member.Type type, String role, OSMEntity newMember){
            members.add(new Member(type, role, newMember));
        }
        
        /**
         * Construit la relation ayant l'identifiant passé au constructeur 
         * ainsi que les membres et les attributs ajoutés jusqu'à présent au bâtisseur
         * 
         * @return la relation construite
         * @throws IllegalStateException si  la relation en construction est incomplète
         */
        public OSMRelation build() throws IllegalStateException{
            if(super.isIncomplete()){
                throw new IllegalStateException("La relation en cours de construction est incomplète");
            }

            return new OSMRelation(id, members, attributes.build()); 
        }
    }
}
