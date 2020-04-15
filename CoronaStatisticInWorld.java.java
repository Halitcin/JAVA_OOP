package module6;

import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

public class CoronaStatisticInWorld extends PApplet{
	UnfoldingMap map;
	HashMap<String, Integer> CoronaMap;
	List<Feature> countries;
	List<Marker> countryMarkers;
	

	public void setup() {
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500, new Microsoft.RoadProvider() {
			
			@Override
			public String[] getTileUrls(Coordinate arg0) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		MapUtils.createDefaultEventDispatcher(this, map);

		// Load lifeExpectancy data
		CoronaMap = ParseFeed.CoronaDeathsFromCSV(this,"time_series_covid19_deaths_global.csv");
		

		// Load country polygons and adds them as markers
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		
		// Country markers are shaded according to life expectancy (only once)
		shadeCountries();
		
	}

	public void draw() {
		// Draw map tiles and country markers
		map.draw();
		addKey();
	}
	

	private void shadeCountries() {
		for (Marker marker : countryMarkers) {
			// Find data for country of the current marker
			String countryId = marker.getId();
			System.out.println(CoronaMap.containsKey(countryId));
			if (CoronaMap.containsKey(countryId)) {
				int covid = CoronaMap.get(countryId);
				// Encode value as brightness (values range: 40-90)
				if (covid >= 20000) {
					marker.setColor(color(255, 0, 0));
				}
				else if (covid < 20000 && covid >= 10000 ) {
					marker.setColor(color(0, 0, 255));
					
				}
				else if (covid < 10000 && covid >= 5000 ) {
					marker.setColor(color(0, 255, 0));
					
				}
				else if (covid < 5000 && covid >= 3000 ) {
					marker.setColor(color(255, 255, 0));
					
				}
				else if (covid < 3000 && covid >= 1000 ) {
					marker.setColor(color(0, 255, 255));
					
				}
				else if (covid < 1000 ) {
					marker.setColor(color(200, 100, 100));
					
				}
				
			}
			else {
				marker.setColor(color(255,255,255));
			}
		}
	}
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		rect(xbase, ybase, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Covid-19 Death Key", xbase+25, ybase+25);
		
		fill(150, 30, 30);
		
		
		fill(color(255, 0, 0));
		ellipse(xbase+35, ybase+60, 12, 12);
		fill(color(0, 0, 255));
		ellipse(xbase+35, ybase+80, 12, 12);
		fill(color(0, 255, 0));
		ellipse(xbase+35, ybase+100, 12, 12);
		fill(color(255, 255, 0));
		ellipse(xbase+35, ybase+120, 12, 12);
		fill(color(0, 255, 255));
		ellipse(xbase+35, ybase+140, 12, 12);
		fill(color(200, 100, 100));
		ellipse(xbase+35, ybase+160, 12, 12);
		fill(color(255, 255, 255));
		ellipse(xbase+35, ybase+180, 12, 12);
		
		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text("> 20.000", xbase+50, ybase+60);
		text("> 10.000", xbase+50, ybase+80);
		text("> 5.000", xbase+50, ybase+100);
		text("> 3.000", xbase+50, ybase+120);
		text("> 1000", xbase+50, ybase+140);
		text("< 1.000", xbase+50, ybase+160);
		text("No Data", xbase+50, ybase+180);
		
	}

}
