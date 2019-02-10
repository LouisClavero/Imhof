package ch.epfl.imhof.painting;

public final class LineStyle {

	private float width;
	private float[] dashingPattern;
	private Color color;
	public enum LineCap{
		BUTT,
		ROUND, 
		SQUARE;
	}
	private LineCap lineCap;
	public enum LineJoin{
		BEVEL,
		MITER,
		ROUND;
	}
	private LineJoin lineJoin;
	
	public LineStyle(float width, Color color, LineCap lineCap, LineJoin lineJoin, float... dashingPattern) throws IllegalArgumentException{
		if(width<0){
			throw new IllegalArgumentException("largeur invalide");
		}
		for (int i = 0; i < dashingPattern.length; i++) {
			if(dashingPattern[i]<=0){
				throw new IllegalArgumentException("valeurs des segments invalides");
			}
		}
		this.width = width;
		this.color = color;
		this.dashingPattern = dashingPattern;
		this.lineCap = lineCap;
		this.lineJoin = lineJoin;
	}
	
	public LineStyle(float width, Color color){
		this(width, color, LineCap.BUTT, LineJoin.MITER, new float[0]);
	}
	
	public float width(){
		return width;
	}
	
	public float[] dashingPattern(){
		return dashingPattern;
	}
	
	public Color color(){
		return color;
	}
	
	public LineCap lineCap(){
		return lineCap;
	}
	
	public LineJoin lineJoin(){
		return lineJoin;
	}
	
	public LineStyle withWidth(float l){
		return new LineStyle(l, color, lineCap, lineJoin, dashingPattern);
	}
	
	public LineStyle withDashes(float[] dash){
		return new LineStyle(width, color, lineCap, lineJoin, dash);
	}
	
	public LineStyle withColor(Color c){
		return new LineStyle(width, c, lineCap, lineJoin, dashingPattern);
	}
	
	public LineStyle withLineCap(LineCap l){
		return new LineStyle(width, color, l, lineJoin, dashingPattern);
	}
	
	public LineStyle withLineJoin(LineJoin l){
		return new LineStyle(width, color, lineCap, l, dashingPattern);
	}
}
