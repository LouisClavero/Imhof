package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

public final class RoadPainterGenerator {
	
	
	public static Painter painterForRoads(RoadSpec... roadSpecs){
		Painter emptyPainter = (map, canvas) -> {};
		return (map, canvas) -> {
			Painter tunnelPainter = emptyPainter;
			Painter innerBridgePainter = emptyPainter;
			Painter outerBridgePainter = emptyPainter;
			Painter innerWayPainter = emptyPainter;
			Painter outerWayPainter = emptyPainter;
			for(RoadSpec rS : roadSpecs){
				innerBridgePainter = innerBridgePainter.above(Painter.line(rS.wi(), rS.ci(), LineCap.ROUND, LineJoin.ROUND, null)
                        .when(rS.predicate().and(Filters.tagged("bridge"))));

                outerBridgePainter = outerBridgePainter.above(Painter.line(rS.wi()+2*rS.wc(), rS.cc(), LineCap.BUTT, LineJoin.ROUND, null)
                        .when(rS.predicate().and(Filters.tagged("bridge"))));

                innerWayPainter = innerWayPainter.above(Painter.line(rS.wi(), rS.ci(), LineCap.ROUND, LineJoin.ROUND, null)
                        .when(rS.predicate()));

                outerWayPainter = outerWayPainter.above(Painter.line(rS.wi()+2*rS.wc(), rS.cc(), LineCap.ROUND, LineJoin.ROUND, null)
                        .when(rS.predicate()));

                tunnelPainter = tunnelPainter.above(Painter.line(rS.wi()/2f, rS.cc(), LineCap.BUTT, LineJoin.ROUND, 2*rS.wi(), 2*rS.wi())
                        .when(rS.predicate().and(Filters.tagged("tunnel"))));

			}
			innerBridgePainter.above(outerBridgePainter.above(innerWayPainter.above(outerWayPainter.above(tunnelPainter))));
		};
	}
	
	public static final class RoadSpec{
		
		private float wi, wc;
		private Color ci, cc;
		private Predicate<Attributed<?>> predicate;
		
		public RoadSpec(Predicate<Attributed<?>> predicate, float wi, Color ci, float wc, Color cc){
			this.predicate = predicate;
			this.wi = wi;
			this.ci = ci;
			this.wc = wc;
			this.cc = cc;
		}
		
		public float wi(){
			return wi;
		}
		
		public Color ci(){
			return ci;
		}
		
		public float wc(){
			return wc;
		}
		
		public Color cc(){
			return cc;
		}
		
		public Predicate<Attributed<?>> predicate(){
			return predicate;
		}
	}
}
