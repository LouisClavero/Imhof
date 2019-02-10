package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Un polygone, avec ou sans trous, à partir d'une polyligne fermée
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public final class Polygon {
    
    private List<ClosedPolyLine> holes;
    private ClosedPolyLine shell;
    /**
     * Construit un polygone à trous à partir d'une polyligne fermée et d'une liste de polyligne fermées
     * 
     * @param shell
     *      Polyligne fermée qui délimite le polygone
     * @param holes
     *      Liste de polylignes à l'intérieur du polygone qui forment des trous
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes){
        this.shell = shell;
        this.holes = Collections.unmodifiableList(new ArrayList<ClosedPolyLine>(holes));
    }
    /**
     * Construit un polygone sans trous à partir d'une polyligne fermée
     * 
     * @param shell
     *       Polyligne fermée qui délimite le polygone
     */
    public Polygon(ClosedPolyLine shell){
        this(shell, Collections.emptyList());
    }
    /**
     * 
     * @return la polyligne fermée qui délimite le polygone
     */
    public ClosedPolyLine shell(){
        return shell;
    }
    /**
     * 
     * @return une liste non modifiable de polylignes formant les trous à l'intérieur du polygone
     */
    public List<ClosedPolyLine> holes(){
        return holes;
    }
}
