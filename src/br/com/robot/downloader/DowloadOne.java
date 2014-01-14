package br.com.robot.downloader;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DowloadOne {

	public static final String url = "http://pgeo2.rio.rj.gov.br/ArcGIS/rest/services/imagens/Mosaico_2011_UTM/MapServer/export";

	public static void main(String[] args) throws FileNotFoundException,
			UnsupportedEncodingException {
		ExecutorService executor = Executors.newFixedThreadPool(4);
		int w = 1600;
		int h = 1200;
		double pixel = 0.529167725002117;

		PrintWriter writer = new PrintWriter("log", "UTF-8");

		executor.execute(new GetImage(writer, url, "image_510_05",
				663208.05030003, 7475044.73564999, 664054.71866004,
				7474409.73437998, w, h, pixel));

		executor.shutdown();
		while (!executor.isTerminated()) {
		}

		System.out.println("Finished all threads");

	}
}
