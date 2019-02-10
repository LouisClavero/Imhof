package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Convertit les coordonnées cartésiennes en sphériques, ou l'inverse,
 * utilisant la méthode de projection équirectangulaire
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public class EquirectangularProjection implements Projection {

    /** 
     * Convertit les coordonnées sphériques d'un point donné en coordonnées cartésiennes
     * 
     * @param point
     *          Un point en coordonnées sphériques
     * @return La position de ce point en coordonnées cartésiennes 
     */
	@Override
    public Point project(PointGeo point) {
        double x = point.longitude();
        double y = point.latitude();
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
        double longitude = point.x();
        double latitude = point.y();
        return new PointGeo(longitude,latitude);
    }

}
