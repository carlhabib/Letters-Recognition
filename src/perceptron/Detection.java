package perceptron;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Detection {

	private String path;
	
	public Detection(String path) throws IOException {
		this.path = path;
		BufferedImage img = ImageIO.read(new File(path));
	
		
		double height = img.getHeight();
		double width = img.getWidth();
		int amountPixel = 0;
		int amountBlackPixel = 0;

		int rgb;
		int red;
		int green;
		int blue;

		double percentPixel = 0;

		System.out.println("height: " + height + " /	width: " + " " + width + " / RGB : " + img.getRGB(30, 30));
		ArrayList<Point> points = new ArrayList();

		for (int h = 1; h < height; h++) {
			for (int w = 1; w < width; w++) {
				amountPixel++;
				
				rgb = img.getRGB(w, h);
				red = (rgb >> 16) & 0x000000FF;
				green = (rgb >> 8) & 0x000000FF;
				blue = (rgb) & 0x000000FF;

				if (red == 0 && green == 0 && blue == 0) {
					amountBlackPixel++;
					Point blackPixel = new Point(w, h);
					points.add(blackPixel);

				}
			}
		}
		percentPixel = (double) amountBlackPixel / (double) amountPixel;

		System.out.println("Total nb of pixels: " + amountPixel);
		System.out.println("Nb of black pixels: " + amountBlackPixel);
		System.out.println("Percentage of black pixels: " + percentPixel * 100.0 + " %");
		

		ArrayList<Double> arr = new ArrayList();
		
		double x=10000;
		double y=10000;
		double x2=0;
		double y2=0;
		for(int i=0; i < points.size(); i++){
			
			if(points.get(i).getX() < x){
				x = points.get(i).getX();		// leftupcornerX
			}
			if(points.get(i).getY() < y){
				y = points.get(i).getY(); 		// leftupcornerY
			}
			if(points.get(i).getX() > x2){
				x2 = points.get(i).getX(); 		// rightlimit
			}
			if(points.get(i).getY() > y2){
				y2 = points.get(i).getY(); 		// bottomlimit
			}
		}
		x--;
		y--;
		double widthT  = x2-x;
		double heightT = y2-y;
		widthT++;
		heightT++;
		
		Double leftUpCorner = new Double();
		leftUpCorner.setLocation(x, y);
		Double leftDownCorner = new Double();
		leftDownCorner.setLocation(x, y+widthT);
		Double rightUpCorner = new Double();
		rightUpCorner.setLocation(x+widthT, y);
		Double rightDownCorner = new Double();
		rightDownCorner.setLocation(x+widthT, y+widthT);
		Double[] arrP2D = new Double[4];
		arrP2D[0] =  leftUpCorner;
		arrP2D[1] = leftDownCorner;
		arrP2D[2] = rightUpCorner;
		arrP2D[3] = rightDownCorner;
		
		
		for (Double corner : arrP2D) {
			arr.add(corner);
		}
		System.out.println();

		
		int leftUpX = (int) arr.get(0).getX();
		int leftUpY = (int) arr.get(0).getY();

		BufferedImage imgMin ;
		BufferedImage imgCropped;
		imgMin = img;
		imgCropped = img;
		imgCropped = Scalr.resize(Scalr.crop(imgCropped, leftUpX, leftUpY, (int)widthT,(int)heightT),Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT, 100, 100);
		ImageIO.write(imgCropped, "png", new File("resources/croppedResizedImg.png"));

		Graphics2D g2d = imgMin.createGraphics();
		g2d.setColor(Color.RED);
		g2d.drawRect(leftUpX, leftUpY, (int)widthT, (int)heightT);
		ImageIO.write(imgMin,"png",new File("resources/minBoundImage.png"));

		System.out.println("width2:" + widthT + " height2:" + heightT + " leftUcornerX:" + leftUpX + " leftUcornerY:" + leftUpY);

		/*imgCropped = Scalr.resize(Scalr.crop(img, leftUpX, leftUpY, (int)widthT,(int)heightT,Scalr.OP_GRAYSCALE),Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT, 100, 100);
		File outputfile = new File("resources/croppedResizedImg.png");
		ImageIO.write(imgCropped, "png", outputfile);*/


	}

	public int[][] convertImageTo2D() throws IOException {
		  BufferedImage image = ImageIO.read(new File("resources/croppedResizedImg.png"));
	      int width = image.getWidth();
	      int height = image.getHeight();
	      System.out.println("width" + width);
		  System.out.println("height" + height);

		int[][] result = new int[height][width];

	      for (int row = 0; row < height; row++) {
	         for (int col = 0; col < width; col++) {
	            result[row][col] = image.getRGB(col, row);
	         }
	      }

	      return result;
	   }

	public ArrayList convert2DToBinary(int [][] array2D) throws IOException {
		int width = array2D[0].length;
		int height = array2D.length;
		ArrayList<Integer> arr = new ArrayList<>();
    int counterBlack=0;
    int counterWhite=0;
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {

				int rgb,red,blue,green;

				rgb = array2D[row][col];
				red = (rgb >> 16) & 0x000000FF;
				green = (rgb >> 8) & 0x000000FF;
				blue = (rgb) & 0x000000FF;

                //System.out.println("Red "+ red + "Green "+ green + "blue"+blue);
				if (red < 50 && green < 50  && blue < 50) {

					arr.add(1);
					counterBlack++;

				}else{ if(red >= 50 || green >= 50 || blue >= 50)
					arr.add(0);
				counterWhite++;
				}

			}

		}
        //System.out.print("BLACK CELLs number "+counterBlack);
		//System.out.println("White cells " + counterWhite);
		return arr;
	}

	public int[] convertArrTo1D(ArrayList<Integer> arr){
	    //System.out.println(arr);

		int[] oneD = new int[arr.size()];

		for(int i=0; i<arr.size();i++){

			oneD[i] = arr.get(i);
		}
		int counterBlack=0;
		int counterWhite=0;

		for(int i=0;i<10000;i++){
		    if (oneD[i]==1) counterBlack++;
		    if(oneD[i]==0)counterWhite++;

        }
        System.out.println("counter black "+ counterBlack);
        System.out.println("counter white "+ counterWhite);

        return oneD;

	}
}
