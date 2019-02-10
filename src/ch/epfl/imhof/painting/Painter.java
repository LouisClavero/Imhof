package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

public abstract interface Painter {

	public abstract void drawMap(Map map, Java2DCanvas canvas);

	public static Painter polygon(Color color){
		Painter painter = 
				(map, canvas) -> {
					for(Attributed<Polygon> p : map.polygons()){
						canvas.drawPolygon(p.value(), color);
					}
				};
				return painter;
	}

	public static Painter line(LineStyle linestyle){
		return (map, canvas) -> {					
			for(Attributed<PolyLine> p : map.polyLines()){
				canvas.drawPolyLine(p.value(), linestyle);
			}
		};
	}

	public static Painter line(float width, Color color, LineCap linecap, LineJoin linejoin, float... dashingPattern){
		LineStyle linestyle = new LineStyle(width, color, linecap, linejoin, dashingPattern);
		return line(linestyle);
	}

	public static Painter line(float width, Color color){
		LineStyle linestyle = new LineStyle(width, color);
		return line(linestyle);
	}

	public static Painter outline(LineStyle linestyle){
		return	(map, canvas) -> {					
			for (Attributed<Polygon> p : map.polygons()) {
				canvas.drawPolyLine(p.value().shell(), linestyle);
				for (ClosedPolyLine c : p.value().holes()) {
					canvas.drawPolyLine(c, linestyle);
				}	
			}
		};
	}

	public static Painter outline(float width, Color color, LineCap linecap, LineJoin linejoin, float... dashingPattern){
		LineStyle linestyle = new LineStyle(width, color, linecap, linejoin, dashingPattern);
		return outline(linestyle);
	}

	public static Painter outline(float width, Color color){
		LineStyle linestyle = new LineStyle(width, color);
		return outline(linestyle);
	}

	public default Painter when(Predicate<Attributed<?>> predicate){
		return	(map, canvas) -> {
			for(Attributed<Polygon> p : map.polygons()){
				if(predicate.test(p)){
					drawMap(map, canvas);
				}
			}
			for(Attributed<PolyLine> p1 : map.polyLines()){
				if(predicate.test(p1)){
					drawMap(map, canvas);
				}
			}

		};
	}

	public default Painter above(Painter painterAbove){
		return	(map, canvas) -> {
			painterAbove.drawMap(map, canvas);
			this.drawMap(map, canvas);
		};
	}

	public default Painter layered(){
		return (map, canvas) -> {
			Painter painter = when(Filters.onLayer(-5));
			for(int i=-4; i<=5; i++){
				Predicate<Attributed<?>> predicate = Filters.onLayer(i);
				painter.above(when(predicate));
			}
		};
	}
}
