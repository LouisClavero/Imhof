package ch.epfl.imhof.geometry;

import java.util.List;
/**
 * Une polyligne ouverte à partir d'une liste de points
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public final class OpenPolyLine extends PolyLine {
    /**
     * Construit une polyligne ouverte avec les points de la liste
     * 
     * @param points
     *          La liste des points qui forment la polyligne ouverte
     * @throws IllegalArgumentException si la liste des points est vide
     */
    public OpenPolyLine(List<Point> points) throws IllegalArgumentException {
        super(points);

    }

    public boolean isClosed() {
        return false;
    }

}
