package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public interface Projection {
    
    public Point project(PointGeo point);
    public PointGeo inverse(Point point);

}
