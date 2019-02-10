package ch.epfl.imhof.osm;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.OpenPolyLine;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;
import ch.epfl.imhof.projection.Projection;

/** Représente un convertisseur de données OSM en carte
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public final class OSMToGeoTransformer {

	private final Projection projection;
	private final Set<String> valuesArea = new HashSet<String>(
			Arrays.asList("yes", "1", "true")
			);
	private final Set<String> attributesArea = new HashSet<String>(
			Arrays.asList(
					"aeroway", "amenity", "building", "harbour", "historic",
					"landuse", "leisure", "man_made", "military", "natural",
					"office", "place", "power", "public_transport", "shop",
					"sport", "tourism", "water", "waterway", "wetland"
					)
			);
	private final Set<String> polyLineAttributes = new HashSet<String>(
			Arrays.asList("bridge", "highway", "layer", "man_made", "railway", "tunnel", "waterway")
			);
	private final Set<String> polygonAttributes = new HashSet<String>(
			Arrays.asList("building", "landuse", "layer", "leisure", "natural", "waterway")
			);

	public OSMToGeoTransformer(Projection projection){
		this.projection = projection;
	}

	/**
	 * Convertit une carte OSM en une carte géométrique projetée
	 * @param map
	 *          Carte OSM donnée
	 * @return la carte OSM passée en argument projetée
	 */
	public Map transform(OSMMap map){
		
		Map.Builder mB = new Map.Builder();
		
		for(OSMWay way : map.ways()){
			if(wayIsArea(way)){
				
				ClosedPolyLine.Builder cPB = new ClosedPolyLine.Builder();
				for(OSMNode node : way.nonRepeatingNodes()){
					cPB.addPoint(projection.project(node.position()));
				}
				if(!way.attributes().keepOnlyKeys(polygonAttributes).isEmpty()){
					mB.addPolygon(new Attributed<Polygon>(new Polygon(cPB.buildClosed()), way.attributes().keepOnlyKeys(polygonAttributes)));
				}
			}
			else{
				PolyLine.Builder pB = new PolyLine.Builder();
				if(way.isClosed()){
					pB = new ClosedPolyLine.Builder();
				}else {pB = new OpenPolyLine.Builder();}
				for(OSMNode node : way.nonRepeatingNodes()){
					pB.addPoint(projection.project(node.position()));
				}
				if(!way.attributes().keepOnlyKeys(polyLineAttributes).isEmpty()){
					mB.addPolyLine(new Attributed<PolyLine>((way.isClosed()? pB.buildClosed() : pB.buildOpen()), way.attributes().keepOnlyKeys(polyLineAttributes)));
				}
			}
		}
		for(OSMRelation relation : map.relations()){
			if(isMultipolygon(relation)){
				if(!relation.attributes().keepOnlyKeys(polygonAttributes).isEmpty()){
					for(Attributed<Polygon> polygon : assemblePolygon(relation, relation.attributes().keepOnlyKeys(polygonAttributes))){
						mB.addPolygon(polygon);
					}
				}
			}
		}
		return mB.build();
	}

	/**
	 * Retourne l'ensemble des anneaux de la relation donnée ayant le rôle spécifié ou une liste vide 
	 * si le calcul des anneaux échoue
	 * @param relation
	 *          relation à partir de laquelle on obtient les anneaux
	 * @param role
	 *           Role du chemin qui détermine si l'anneaux est intérieur ou extérieur
	 * @return la liste des polylignes qui forment les anneaux
	 */
	private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role){
		Graph.Builder<OSMNode> gB = new Graph.Builder<>();
		List<ClosedPolyLine> list = new ArrayList<>();

		for(Member member: relation.members()){

			if(member.role().equals(role) && member.type().equals(Type.WAY)){
				OSMWay way = (OSMWay) member.member();

				for(int i = 0; i < way.nodesCount(); i++){
					gB.addNode(way.nodes().get(i));
					if(i!=0){
						gB.addEdge(way.nodes().get(i), way.nodes().get(i-1));
					}
				}
			}
		}
		Graph<OSMNode> graph = gB.build();

		for (OSMNode node : graph.nodes()) {
			if(graph.neighborsOf(node).size()!=2){		
				return Collections.emptyList();
			}
		}
		Set<OSMNode> visitedNodes = new HashSet<>();

		for(OSMNode node : graph.nodes()){
			if(!visitedNodes.contains(node)){
				boolean noUnvisitedNeighbors = false;
				ClosedPolyLine.Builder cPB = new ClosedPolyLine.Builder();
				cPB.addPoint(projection.project(node.position()));
				visitedNodes.add(node);
				while(!noUnvisitedNeighbors){
					List<OSMNode> neighbors = new ArrayList<>(graph.neighborsOf(node));
					int nghbrs = 0;
					for(OSMNode node2 : neighbors){
						if(visitedNodes.contains(node2)){
							nghbrs++;
						}
					}
					switch(nghbrs){
					case 0 : node = neighbors.get(0);
					cPB.addPoint(projection.project(node.position()));
					visitedNodes.add(node);
					break;
					case 1 : node = neighbors.get(visitedNodes.contains(neighbors.get(0)) ? 1 : 0);
					cPB.addPoint(projection.project(node.position()));
					visitedNodes.add(node);
					break;
					case 2 : noUnvisitedNeighbors = true;
					break;
					}
				}
				list.add(cPB.buildClosed());
			}
		}
		return list;

	}


	/**
	 * Retourne la liste des polygones attribués de la relation donnée, en leur attachant les attributs donnés
	 * @param relation
	 *          Relation à partir de laquelle on obtient les anneaux
	 * @param attributes
	 *          Attributs donnés au polygone
	 * @return une liste de polygones attribués avec les attributs donnés
	 */
	private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes){

		List<ClosedPolyLine> outerPolyLines = ringsForRole(relation, "outer");
		List<ClosedPolyLine> innerPolyLines = ringsForRole(relation, "inner");
		List<Attributed<Polygon>> list = new ArrayList<>();
		java.util.Map<ClosedPolyLine, Set<ClosedPolyLine>> map = new HashMap<>();

		for (ClosedPolyLine outer : outerPolyLines)
			map.put(outer, new HashSet<ClosedPolyLine>());

		Collections.sort(outerPolyLines, comparator);

		Set<ClosedPolyLine> usedHoles = new HashSet<>();
		for (ClosedPolyLine outerPolyLine : outerPolyLines) {
			List<ClosedPolyLine> holes = new ArrayList<>();
			for (ClosedPolyLine innerPolyLine : innerPolyLines) {
				if (outerPolyLine.containsPoint(innerPolyLine.firstPoint()) && !usedHoles.contains(innerPolyLine)) {
					holes.add(innerPolyLine);
					usedHoles.add(innerPolyLine);
				}
			}
			list.add(new Attributed<Polygon>(new Polygon(outerPolyLine, holes), attributes));
			holes.clear();
		}
		return list;
	}

	private Comparator<ClosedPolyLine> comparator = new Comparator<ClosedPolyLine>(){
		@Override
		public int compare(ClosedPolyLine cP, ClosedPolyLine cP1) {
			return (int) java.lang.Math.signum(cP.area() - cP1.area());
		}
	};

	private boolean isMultipolygon(OSMRelation relation){
		if(relation.attributes().get("type", "nonsense").equals("multipolygon")){
			return true;
		}
		else return false;
	}

	private boolean wayIsArea(OSMWay w){
		return w.isClosed() && ((w.hasAttribute("area")? valuesArea.contains(w.attributeValue("area")) : false)  || !w.attributes().keepOnlyKeys(attributesArea).isEmpty() );
	}

}