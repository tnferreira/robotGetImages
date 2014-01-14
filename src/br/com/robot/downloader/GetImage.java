package br.com.robot.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Locale;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

public class GetImage implements Runnable {

	private String url;
	private String filename;
	double xmin;
	double ymin;
	double xmax;
	double ymax;
	double pixel;
	int h;
	int w;
	PrintWriter writer;

	public GetImage(PrintWriter writer,String url, String filename, 
			double xmin, double ymin,
			double xmax, double ymax,
			int w, int h, double pixel) {
		this.url = url;
		this.filename = filename;
		this.xmax = xmax;
		this.xmin = xmin;
		this.ymax = ymax;
		this.ymin = ymin;
		this.pixel = pixel;
		this.h = h;
		this.w = w;
		this.writer = writer;

	}

	@Override
	public void run() {
		String size = String.format("&size=%d,%d", w, h);
		String format = "&format=jpg&f=image"; //png
		String bbox = "bbox=" + String.format(Locale.US, "%.8f,%.8f,%.8f,%.8f", xmin, ymin, xmax, +ymax);
		
		String query = bbox + size + format;
		
		try {
			writer.println("Downloading: " + filename + " === " + bbox);
			System.out.println("Downloading: " + filename + " === " + bbox);
			getImage(filename, query);
			WordFile.write(filename, xmin, ymin, pixel);
		} catch (IOException e) {
			writer.println("ERROR at " + filename + " : " + e.toString());
		}
		writer.flush();
	}

	public void getImage(String filename, String query) throws IOException {
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		httpClient.getParams().setParameter("User-Agent", "Mozilla/5.0 (Windows NT 5.2; WOW64)");
		httpClient.getParams().setParameter("Cache-Control", "max-age=0");

		//PROXY
		HttpHost proxy = new HttpHost("10.20.15.22", 3128);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

		HttpGet httpGet = new HttpGet(url + "?" + query);
		HttpResponse response = httpClient.execute(httpGet);
		
		if(response.getStatusLine().getStatusCode()!=200) {
	        throw new IOException("Could not download: " + response.getStatusLine());
		}
		
		InputStream in = response.getEntity().getContent();
		FileOutputStream fos = new FileOutputStream(new File(Downloader.OUTPUT_DIR, filename + ".jpg"));

		byte[] buffer = new byte[4096];
		int length;
		while ((length = in.read(buffer)) > 0) {
			fos.write(buffer, 0, length);
		}
		fos.flush();
		fos.close();
		fos = null;
	}
	
	

}
