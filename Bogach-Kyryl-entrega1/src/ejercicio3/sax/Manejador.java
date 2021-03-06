package ejercicio3.sax;

import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import servicio.model.CiudadGeoNames;

public class Manejador extends DefaultHandler {

	private LinkedList<String> pila;
	private LinkedList<CiudadGeoNames> ciudades;
	private CiudadGeoNames ciudad;
	private boolean inGeoname;
	private int totalResultCount;

	@Override
	public void startDocument() throws SAXException {
		this.pila = new LinkedList<>();
		this.ciudades = new LinkedList<>();
		this.ciudad = null;
		this.inGeoname = false;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		pila.push(qName);

		if (qName.equals("geoname")) {
			this.inGeoname = true;
			this.ciudad = new CiudadGeoNames();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) {
		String texto = new String(ch, start, length).trim();
		String elemento = pila.peek();
		
		if (elemento.equals("totalResultsCount")) {
			this.totalResultCount = Integer.parseInt(texto);
		}
		
		if (!inGeoname) return;
		
		switch (elemento) {
		case "name":
			this.ciudad.setNombre(texto);
			break;
		case "countryName":
			this.ciudad.setPais(texto);
			break;
		case "geonameId":
			long idGeonames = Long.parseLong(texto);
			this.ciudad.setIdGeonames(idGeonames);
			break;
		case "lng":
			double longitud = Double.parseDouble(texto);
			this.ciudad.setLongitud(longitud);
			break;
		case "lat":
			double latitud = Double.parseDouble(texto);
			this.ciudad.setLatitud(latitud);
			break;
		default:
			return;
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		pila.pop();

		if (qName.equals("geoname")) {
			this.inGeoname = false;
			this.ciudades.add(ciudad);
			this.ciudad = null;
		}
	}
	
	public LinkedList<CiudadGeoNames> getCiudades() {
		return ciudades;
	}
	
	public int getTotalResultCount() {
		return totalResultCount;
	}

}
