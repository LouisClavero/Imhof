package ch.epfl.imhof.painting;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public class Java2DCanvas implements Canvas{
	
	private BufferedImage image;
	private Function<Point, Point> function;
	private Graphics2D graphics;
	
	public Java2DCanvas(Point lowLeft, Point topRight, int width, int height, int dpi, Color color){
		if(width<=0 || height<=0 || dpi<=0){
			throw new IllegalArgumentException("valeurs invalides");
		}
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.graphics = image.createGraphics(); 
		double resolution = dpi/72d;
		graphics.setColor(color.toAWTColor());
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.scale(width, height);
		graphics.fillRect(0, 0, width, height);
		Point botLeft = new Point(0, height/resolution);
		Point toppRight = new Point(width/resolution, 0);
		this.function = Point.alignedCoordinateChange(lowLeft, botLeft, topRight, toppRight);
	}
	
	public BufferedImage image(){
		return image;
	}
	
	@Override
	public void drawPolyLine(PolyLine polyline, LineStyle linestyle) {
		Graphics2D g = image.createGraphics();
		Stroke stroke = new BasicStroke(linestyle.width(), linestyle.lineCap().ordinal(), linestyle.lineJoin().ordinal(), 10.0f, linestyle.dashingPattern(), 0f);
		g.setStroke(stroke);
		Path2D path = new Path2D.Double();
		Point point = function.apply(polyline.firstPoint());
		path.moveTo(point.x(), point.y());
		for(Point p : polyline.points()){
			p = function.apply(p);
			path.lineTo(p.x(), p.y());
		}
		if(polyline.isClosed()){
			path.closePath();
		}
		g.draw(path);
	}

	@Override
	public void drawPolygon(Polygon polygon, Color color) {
		Graphics2D g = image.createGraphics();
		Path2D path = new Path2D.Double();
		Point point = function.apply(polygon.shell().firstPoint());
		path.moveTo(point.x(), point.y());
		for(Point p : polygon.shell().points()){
			p = function.apply(p);
			path.lineTo(p.x(), p.y());
		}
		path.closePath();
		Area area = new Area(path);
		for(ClosedPolyLine c : polygon.holes()){
			Path2D holesPath = new Path2D.Double();
			Point point2 = function.apply(c.firstPoint());
			holesPath.moveTo(point2.x(), point2.y());
			for(Point p : c.points()){
				p = function.apply(p);
				holesPath.lineTo(p.x(), p.y());
			}
			area.subtract(new Area(holesPath));;
		}
		g.setColor(color.toAWTColor());	
		g.draw(area);
		g.fill(area);
	}

}
