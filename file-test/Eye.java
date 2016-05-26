import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Created by nathanielwarner on 5/24/16.
 *
 * This class will monitor the file created in objecttracker-cpp for the location of the object
 */
public class Eye {

	private static int camWidth = -1;
	    private static int camHeight = -1;

	    private static void determineCamSize() {
	        try {
	            String path = "cam_size";
	            FileReader fr = new FileReader(path);
	            BufferedReader textReader = new BufferedReader(fr);
	            String data = textReader.readLine();
	            textReader.close();

	            int i = 0;
	            int max;
	            try {
	                max = data.length();
	            } catch (NullPointerException e) {
	                System.out.println("race condition?");
	                return;
	            }

	            while (Character.isDigit(data.charAt(i))) {
	                i++;
	            }
	            camWidth = Integer.parseInt(data.substring(0, i));

	            i++;
	            int j = i;
	            while (Character.isDigit(data.charAt(j))) {
	                j++;
	                if (j == max)
	                    break;
	            }
	            camHeight = Integer.parseInt(data.substring(i, j));

	        } catch (IOException exc) {
	            // ignore
	        }
	    }

	    public static Point getXYLocation(int objectNum) {
	        try {
	            String path = "obj" + objectNum + "/loc";
	            FileReader fr = new FileReader(path);
	            BufferedReader textReader = new BufferedReader(fr);
	            String data = textReader.readLine();
	            textReader.close();

	            int i = 0;
	            int max;
	            try {
	                max = data.length();
	            } catch (NullPointerException e) {
	                return null;
	            }

	            while (Character.isDigit(data.charAt(i))) {
	                i++;
	            }
	            int x = Integer.parseInt(data.substring(0, i));

	            i++;
	            int j = i;
	            while (Character.isDigit(data.charAt(j))) {
	                j++;
	                if (j == max)
	                    break;
	            }
	            int y = Integer.parseInt(data.substring(i, j));

	            return new Point(x, y);
	        } catch (IOException exc) {
	            return null;
	        }
	    }

	    public static double getPercentAcrossCam(int objectNum) {
	        if (camWidth == -1) {
	            determineCamSize();
			}
	        if (camWidth == -1) {
	            return -1;
			}
	        Point loc = getXYLocation(objectNum);
			if (loc == null) {
				return -1;
			}
	        return ((double) loc.getX() / camWidth);
	    }
	
	public static void main(String[] args) {
		try {
			Process tracker = new ProcessBuilder("./ObjectTracker").start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			System.out.println(getPercentAcrossCam(0));
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
