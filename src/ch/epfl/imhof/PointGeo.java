package ch.epfl.imhof;

/**
 * Un point à la surface de la Terre, en coordonnées sphériques
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public final class PointGeo {
    
    private double longitude;
    private double latitude;
    
    /**
     * Construit un point avec la latitude et la longitude
     * données.
     *
     * @param longitude
     *            la longitude du point, en radians
     * @param latitude
     *            la latitude du point, en radians
     * @throws IllegalArgumentException
     *             si la longitude est invalide, c-à-d hors
     *             de l'intervalle [-π; π]
     * @throws IllegalArgumentException
     *             si la latitude est invalide, c-à-d hors
     *             de l'intervalle [-π/2; π/2]
     */
    public PointGeo(double longitude, double latitude) throws IllegalArgumentException{
        if(longitude<-java.lang.Math.PI || longitude>java.lang.Math.PI){
            throw new IllegalArgumentException("La longitude est invalide");
        }
        else if(latitude<(-java.lang.Math.PI/2) || latitude>(java.lang.Math.PI/2)){
            throw new IllegalArgumentException("La latitude est invalide");
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }
    
    /**
     * @return La longitude, en radians
     */
    public double longitude(){
        return longitude;
    }
    
    /**
     * @return La latitude, en radians
     */
    public double latitude(){
        return latitude;
    }
}
