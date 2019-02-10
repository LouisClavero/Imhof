package ch.epfl.imhof;

import java.util.ArrayList; 

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Filters;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.Map;
public class test {

	public static void main(String[] args) throws IOException, SAXException{
		Function<Point, Point> f = 
				Point.alignedCoordinateChange(new Point(1, -1), new Point(5, 4), new Point(-1.5, 1), new Point(0, 0));
		Point tarace = f.apply(new Point(0, 0));
		System.out.println(tarace.x() + ", " +  tarace.y());
		
		List<Point> points = new ArrayList<Point>();
		Point p1 = new Point(0, 0);
		Point p2 = new Point(1, 0);
		Point p3 = new Point(0, 1);
		Point p4 = new Point(1, 1);
		points.add(p1);
		points.add(p2);
		points.add(p4);
		points.add(p3);
		ClosedPolyLine ligne = new ClosedPolyLine(points);
		System.out.println(ligne.area());
		
		OSMMap osmmap = OSMMapReader.readOSMFile(test.class.getResource("/OSMtestFiles/lausanne.osm.gz").getFile(), true);
		OSMToGeoTransformer osmtgt = new OSMToGeoTransformer(new CH1903Projection());
		Map map = osmtgt.transform(osmmap);
		
		Predicate<Attributed<?>> isLake =
			    Filters.tagged("natural", "water");
			Painter lakesPainter =
			    Painter.polygon(Color.BLUE).when(isLake);

			Predicate<Attributed<?>> isBuilding =
			    Filters.tagged("building");
			Painter buildingsPainter =
			    Painter.polygon(Color.BLACK).when(isBuilding);

			Painter painter = buildingsPainter.above(lakesPainter);

			Point bl = new Point(532510, 150590);
			Point tr = new Point(539570, 155260);
			Java2DCanvas canvas =
			    new Java2DCanvas(bl, tr, 800, 530, 72, Color.WHITE);

			painter.drawMap(map, canvas);
			ImageIO.write(canvas.image(), "png", new File("loz.png"));
	}
}