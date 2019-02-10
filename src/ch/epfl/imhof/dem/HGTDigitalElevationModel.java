package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

public class HGTDigitalElevationModel implements DigitalElevationModel{
	
	private char latitude, longitude;
	private int latValue, longValue;
	private String extension;
	private FileInputStream stream;
	private double resolution;
	private ShortBuffer buffer;
	
	public HGTDigitalElevationModel(File file) throws IllegalArgumentException, IOException{
		if(file.getName().length()<11 || file.getName().length()>11){
			throw new IllegalArgumentException("the file name does not have 11 characters");
		}
		else{
			char latitude = file.getName().charAt(0);
			int latValue = Integer.parseInt(file.getName().substring(1, 3));
			char longitude = file.getName().charAt(3);
			int longValue = Integer.parseInt(file.getName().substring(4, 8));
			String extension = file.getName().substring(8, 11);
			boolean validSize = (Math.sqrt(file.length()/2) == Math.floor(Math.sqrt(file.length()/2)));
			
			if(latitude != 'N' || latitude != 'S' || longitude != 'W' || longitude != 'E' || extension!= ".hgt" || !validSize){
				throw new IllegalArgumentException("the file name is invalid");
			}
			
			if(latitude == 'S'){
				latValue = -latValue;
			}
			
			if(longitude == 'W'){
				longValue = -longValue;
			}
			
			this.latitude = latitude;
			this.latValue = latValue;
			this.longitude = longitude;
			this.longValue = longValue;
			this.extension = extension;
			this.stream = new FileInputStream(file);
			this.resolution = Math.sqrt(file.length()/2);
			this.buffer = stream.getChannel().map(MapMode.READ_ONLY, 0, file.length()).asShortBuffer();
		}
	}
	
	@Override
	public void close() throws Exception {	
		buffer.clear();
		stream.close();
	}

	@Override
	public Vector3 normalAt(PointGeo point) {
		double s = Earth.RADIUS*resolution;
		double i = (int) (point.longitude()/s);
		int j = (int) (point.latitude()/s);
		int pointByLine = (int)(1/Math.toDegrees(resolution) + 1);
		double zij = buffer.get((int) (i + j*pointByLine));
		double zi1j = buffer.get((int) ((i+1) + j*pointByLine));
		double zij1 = buffer.get((int) (i+(j+1)*pointByLine));
		double zi1j1 = buffer.get((int) ((i+1) + (j+1)*pointByLine));
		double v1 = 0.5*s*(zij - zi1j + zij1 - zi1j1);
		double v2 = 0.5*s*(zij + zi1j - zij1 - zi1j1);
		return new Vector3(v1, v2, s*s);
	}

}
