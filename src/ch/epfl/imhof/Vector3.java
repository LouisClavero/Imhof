package ch.epfl.imhof;

public final class Vector3 {
	
	private double x,y,z;
	
	public Vector3(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double x(){
		return x;
	}
	
	public double y(){
		return y;
	}
	
	public double z(){
		return z;
	}
	
	public double norm(){
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}
	
	public Vector3 normalized(){
        return new Vector3(x/norm(), y/norm(), z/norm());
	}
	
	public double scalarProduct(Vector3 v, Vector3 v1){
		return v.x()*v1.x() + v.y()*v1.y() + v.z()*v1.z();
	}
}
