package br.com.robot.downloader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 
 * @author tnferrei
 *
 */
public class Downloader {
	
	private static final int MAX_THREADS = 4;

	//spatialReference wkid:29193 	SAD_1969_UTM_Zone_23S
	//http://pgeo2.rio.rj.gov.br/ArcGIS/rest/services/imagens/Mosaico_2011_UTM/MapServer?f=json&pretty=true
	//http://sampleserver1.arcgisonline.com/ArcGIS/SDK/REST/index.html
	
	public static final String url = "http://pgeo2.rio.rj.gov.br/ArcGIS/rest/services/imagens/Mosaico_2011_UTM/MapServer/export";
	public static final String OUTPUT_DIR = "C:/arcgis/rio/dow";
	public static final boolean DEBUG = true;
	public static final String filename ="image_6%02d_%02d";
	
	
	
	//http://pgeo2.rio.rj.gov.br/ArcGIS/rest/services/imagens/Mosaico_2011_UTM/MapServer/export
	//?bbox=625241.895556869%2C7462829.85829842%2C695432.246443131%2C7485107.43359869&size=&format=png&transparent=false&f=image
	
	
	public static void main(String[] args) throws IOException {
		
		MyExtent bounds = new MyExtent(); 
		
		//Max bounds
		bounds.xmin = 625241.895556869;
		bounds.ymin = 7462829.85829842;
		bounds.xmax = 695432.246443131;
		bounds.ymax = 7485107.43359869;
		
		ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
		
		int w = 1600;
		int h = 1200;
		double pixel = 0.529167725002117;
		//double pixel = 0.132291931250529;
		
		double dx = bounds.xmax - bounds.xmin;
		double dy = bounds.ymax - bounds.ymin;
		//System.out.println("x:" + dx + " | y:" + dy + " => "  + (dx * dy));
		
		double cw = w * pixel;
		double ch = h * pixel;
		
		long nW = Math.round(Math.abs(dx) / cw);
		long nH = Math.round(Math.abs(dy) / ch);
		System.out.println("col:" + Math.abs(dx) / cw + " | line:" + Math.abs(dy) / ch + " => "  + (nH * nW) + " images");
		System.out.println("col:" + nW + " | line:" + nH + " => "  + (nH * nW) + " images");
		
		File wf = new File(OUTPUT_DIR, System.nanoTime() + ".log");
		PrintWriter writer = new PrintWriter(wf, "UTF-8");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		
		System.out.println(sdf.format(new Date()) + " Starting downloader");
		writer.println(sdf.format(new Date()) +  " Starting downloader");
		double x1 = bounds.xmin;
		for(int i=0;i<nW;i++){
			double x2 = x1 + (w * pixel);
			
			double y1 = bounds.ymin;
			for(int j=0;j<nH;j++){
				double y2 = y1 - (h * pixel);
				
				String name = String.format(filename, i , j);
				
	            executor.execute(new GetImage(writer, url, name, x1, y1, x2, y2, w, h, pixel));				
				y1 = y2;
			}
			x1 = x2;
			writer.flush();
		}
		executor.shutdown();
        while (!executor.isTerminated()) {
        }
        writer.flush();
        
        System.out.println(sdf.format(new Date()) + " Finished all threads");
        writer.println(sdf.format(new Date()) + " Finished all threads"); 
        writer.close();
	}
	
	
}
