package ch.epfl.imhof.osm;

import java.io.BufferedInputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;

/**
 * Lecteur de données de fichiers format OSM, qui permet ensuite de construire une carte OSM avec ces données
 * 
 * @author Thomas Artiach 238868
 * @author Louis Clavero 233933
 *
 */
public final class OSMMapReader {

	private OSMMapReader(){};

	/**
	 * Lit la carte OSM contenue dans le fichier de nom donné, 
	 * en le décompressant avec gzip si et seulement si le second argument est vrai
	 * @param FileName
	 *          Nom du fichier
	 * @param unGZip
	 *          boolean qui détermine s'il faut décompresser le fichier avec gzip
	 * @return une map contenant les informations du fichier
	 * @throws IOException en cas d'autre erreur d'entrée/sortie
	 * @throws SAXException en cas d'erreur dans le format du fichier XML contenant la carte
	 */
	public static OSMMap readOSMFile(String FileName, boolean unGZip) throws IOException, SAXException{

		OSMMap.Builder mB = new OSMMap.Builder();

		try(InputStream i = (unGZip) ? 
				new GZIPInputStream(new BufferedInputStream(new FileInputStream(FileName))) : new BufferedInputStream(new FileInputStream(FileName))){

			XMLReader r = XMLReaderFactory.createXMLReader();

			r.setContentHandler(new DefaultHandler(){

				OSMNode.Builder nB;
				OSMWay.Builder wB;
				OSMRelation.Builder rB;

				@Override
				public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException{
					switch(qName){

					case "node" : 
						if(atts.getValue("id")==null || atts.getValue("lon")==null || atts.getValue("lat") == null){
							nB.setIncomplete();
						}
						long id = Long.parseLong(atts.getValue("id"));
						double lat = Math.toRadians(Double.parseDouble(atts.getValue("lat")));
						double lon = Math.toRadians(Double.parseDouble(atts.getValue("lon")));
						try{
							PointGeo point = new PointGeo(lon,lat);
							nB = new OSMNode.Builder(id, point);
						} catch (IllegalArgumentException iae){ 
							nB = null;
						}
						break;

					case "way" : 
						if(atts.getValue("id")==null){
							wB.setIncomplete();
						}
						long id2 = Long.parseLong(atts.getValue("id"));
						wB = new OSMWay.Builder(id2);
						break;

					case "relation" :

						if(atts.getValue("id")==null){
							rB.setIncomplete();
						}
						long id3 = Long.parseLong(atts.getValue("id"));
						rB = new OSMRelation.Builder(id3);
						break;

					case "nd" :
						long ref = Long.parseLong(atts.getValue("ref"));
						if(mB.nodeForId(ref) == null){
							wB.setIncomplete();
						}
						wB.addNode(mB.nodeForId(ref));
						break;

					case "tag" : 
						String key = atts.getValue("k");
						String value = atts.getValue("v");
						if(nB!=null){
							nB.setAttribute(key, value);
						}
						else if(wB!=null){
							wB.setAttribute(key, value);
						}
						else if(rB!=null){
							rB.setAttribute(key, value);
						}
						break;

					case "member" :
						if(atts.getValue("ref")==null || atts.getValue("role")==null || atts.getValue("type")==null){
							rB.setIncomplete();                    
						}
						long refMember =  Long.parseLong(atts.getValue("ref"));
						String role = atts.getValue("role");
						String type = atts.getValue("type");
						OSMEntity osm = null;
						Type type1 = null;

						switch(type){

						case "node":

							osm = mB.nodeForId(refMember);
							type1 = Type.NODE;
							break;

						case "way":

							osm = mB.wayForId(refMember);
							type1 = Type.WAY;
							break;

						case "relation":

							osm = mB.relationForId(refMember);
							type1 = Type.RELATION;
							break;
						}
						if(osm==null){
							rB.setIncomplete();
						}
						rB.addMember(type1, role, osm);
						break;

					default:
						break;
					}
				}

				@Override
				public void endElement(String uri, String localName, String qName){
					switch(qName){

					case "node" : 
						if(nB != null){
							if(!nB.isIncomplete()){
								mB.addNode(nB.build());
							}
						}
						nB = null;
						break;

					case "way":
						if(!wB.isIncomplete()){
							mB.addWay(wB.build());
						}
						wB = null;
						break;

					case "relation":
						if(!rB.isIncomplete()){
							mB.addRelation(rB.build());}
						rB = null;
						break;


					default:
						break;
					}
				}

			});

			r.parse(new InputSource(i));
		}
		return mB.build();
	}
}

