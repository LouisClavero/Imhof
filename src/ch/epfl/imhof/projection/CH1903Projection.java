package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Convertit les coordonnées cartésiennes en sphériques, ou l'inverse,
 * utilisant la méthode de projection CH1903
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public class CH1903Projection implements Projection {

    /** 
     * Convertit les coordonnées sphériques d'un point donné en coordonnées cartésiennes
     * 
     * @param point
     *          Un point en coordonnées sphériques
     * @return La position de ce point en coordonnées cartésiennes         
     */
	@Override
    public Point project(PointGeo point) {
        double longitude = (Math.toDegrees(point.longitude())*3600 - 26782.5)/10000;
        double latitude = (Math.toDegrees(point.latitude())*3600 - 169028.66)/10000;
        double x = 600072.37 + 211455.93*longitude - 10938.51*longitude*latitude 
                - 0.36*longitude*Math.pow(latitude, 2) - 44.54*Math.pow(longitude, 3);
        double y = 200147.07 + 308807.95*latitude + 3745.25*Math.pow(longitude, 2) 
                + 76.63*Math.pow(latitude, 2) - 194.56*Math.pow(longitude, 2)*latitude + 119.79*Math.pow(latitude, 3);
        return new Point(x,y);
    }

    /** 
     * Convertit les coordonnés cartésiennes d'un point donné en coordonnées sphériques
     * 
     * @param point
     *          Un point en coordonnées cartésiennes
     * @return La position de ce point en coordonnées sphériques
     */
	@Override
    public PointGeo inverse(Point point) {
        double x = (point.x()-600000)/1000000;
        double y = (point.y()-200000)/1000000;
        double long0 = 2.6779094 + 4.728982*x + 0.791484*x*y + 0.1306*x*Math.pow(y, 2) - 0.0436*Math.pow(x, 3);
        double lat0 = 16.9023892 + 3.238272*y - 0.270978*Math.pow(x, 2) 
                - 0.002528*Math.pow(y, 2) - 0.0447*Math.pow(x, 2)*y - 0.0140*Math.pow(y, 3);
        double longitude = Math.toRadians(long0)*100/36;
        double latitude = Math.toRadians(lat0)*100/36;
        return new PointGeo(longitude, latitude);
    }

}
