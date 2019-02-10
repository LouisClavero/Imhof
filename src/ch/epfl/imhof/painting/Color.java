package ch.epfl.imhof.painting;

public final class Color {

	private double r, g, b;

	public static Color RED = new Color(1, 0, 0);
	public static Color GREEN = new Color(0, 1, 0);
	public static Color BLUE = new Color(0, 0, 1);
	public static Color BLACK = new Color(0, 0, 0);
	public static Color WHITE = new Color(1, 1, 1);

	private Color(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public double r(){ return r;}
	public double g(){ return g;}
	public double b(){ return b;}

	public static Color gray(double i) throws IllegalArgumentException{
		if(i<0||i>1){
			throw new IllegalArgumentException("valeur invalide");
		}
		return new Color(i, i, i);
	}

	public static Color rgb(double r, double g, double b) throws IllegalArgumentException{
		if(r<0||r>1||g<0||g>1||b<0||b>1){
			throw new IllegalArgumentException("valeurs invalides");
		}
		return new Color(r, g, b);
	}

	public static Color rgb(int i) throws IllegalArgumentException{
		return new Color(gammaDecode(((i >> 16) & 0xFF) / 255d),
				gammaDecode(((i >>  8) & 0xFF) / 255d),
				gammaDecode(((i >>  0) & 0xFF) / 255d));
	}

	private static double gammaDecode(double x) {
		if (x <= 0.04045)
			return x / 12.92;
		else
			return Math.pow((x + 0.055) / (1 + 0.055), 2.4);
	}
	
    private static float gammaEncode(double x) {
        if (x <= 0.0031308)
            return (float)(12.92 * x);
        else
            return (float)((1 + 0.055) * Math.pow(x, 1.0 / 2.4) - 0.055);
    }
    
    public java.awt.Color toAWTColor() {
        return new java.awt.Color(gammaEncode(r), gammaEncode(g), gammaEncode(b));
    }
    
    public Color mixColors(Color c1, Color c2){
    	return new Color(c1.r()*c2.r(), c1.g()*c2.g(), c1.b()*c2.b());
    }

}
