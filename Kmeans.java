package image_segmentation_kmeans;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Kmeans {
	protected int n;// 聚类处理的对象个数
	protected int k;// 簇的个数
	protected ClusterItem[] objects;// 聚类的所有数据对象
	private int imageWidth;
	private int imageHeight;
	protected ClusterItem[] centers;// 质心对象
	protected boolean isEnd = false;
	protected int times;// 迭代次数上限

	public void init(String path, int k, int times) {
		int[][] pixelValues = parseImage(path);
		System.out.println(pixelValues);
		imageWidth = pixelValues.length;
		imageHeight = pixelValues[0].length;
		n = imageWidth * imageHeight;
		this.k = k;
		objects = new ClusterItem[n];
		this.times = times;
		int num = 0;
		// initDateItemSet
		for (int i = 0; i < imageWidth; i++)
			for (int j = 0; j < imageHeight; j++) {
				num = i * imageHeight + j;
				System.out.println(num);
				System.out.println(imageWidth);
				Color c = new Color(pixelValues[i][j]);// 将像素值转换为三原色的整型值
				System.out.println(c);
				System.out.println(objects[num] == null);
				objects[num] = new ClusterItem(c.getRed(), c.getGreen(), c.getBlue(), 0);
				/*
				 * // objects[num].attributeValues[0] = c.getRed(); //
				 * objects[num].attributeValues[1] = c.getGreen(); //
				 * objects[num].attributeValues[2] = c.getBlue(); //
				 * objects[num].group = 0;
				 */ }
		// initCenters
		centers = new ClusterItem[k];
		for (int i = 0; i < k; i++) {
			int w = (int) (Math.random() * imageWidth);
			int h = (int) (Math.random() * imageHeight);
			centers[i] = objects[h * imageWidth + w - 1];
			centers[i].setCenterNum(i + 1);
		}

	}

	private int[][] parseImage(String path) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int width = bi.getWidth();
		int height = bi.getHeight();
		System.out.println(width);
		System.out.println(height);
		int[][] pixelValues = new int[width][height];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				System.out.println(bi.getRGB(i, j));
				pixelValues[i][j] = bi.getRGB(i, j);
			}
		return pixelValues;
	}

	public void kmeans(String path1, String path2, int k, int times) {
		init(path1, k, times);
		process();
		displayImage(path2);
	}

	private void setNewCenter(ClusterItem[] centersSum, int[] counts) {
		for (int i = 0; i < k; i++) {
			centers[i].attributeValues[0] = centersSum[i].attributeValues[0] / counts[i];
			centers[i].attributeValues[1] = centersSum[i].attributeValues[1] / counts[i];
			centers[i].attributeValues[2] = centersSum[i].attributeValues[2] / counts[i];
		}
	}

	public void process() {
		ClusterItem[] centersSum = new ClusterItem[k];
		for (int i = 0; i < centersSum.length; i++) {
			centersSum[i] = new ClusterItem(0, 0, 0, 0);
		}
		int[] countsEachGroup = new int[k];
		for (int t = 0; !isEnd && t < times; t++) {
			for (int m = 0; m < n; m++) {
				int groupNum = objects[m].minDistance(centers);
				objects[m].setCenterNum(groupNum);
				centersSum[groupNum - 1].attributeValues[0] += objects[m].attributeValues[0];
				centersSum[groupNum - 1].attributeValues[1] += objects[m].attributeValues[1];
				centersSum[groupNum - 1].attributeValues[2] += objects[m].attributeValues[2];
				countsEachGroup[groupNum - 1]++;
			}
			setNewCenter(centersSum, countsEachGroup);
		}
	}

	public void displayImage(String path) {
		ArrayList<Color> colors = new ArrayList<Color>();
		Color ci;
		int red;
		int green;
		int blue;
		
		for (int i = 0; i < k; i++) {
			red=(int) (Math.random() * 256);
			green=(int) (Math.random() * 256);
			blue=(int) (Math.random() * 256);
			System.out.println("the color of "+i+"is");
			System.out.println(red);
			System.out.println(green);
			System.out.println(blue);
			ci = new Color(red, green, blue);
			colors.add(i, ci);
		}
		BufferedImage bi = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
		int num = 0;
		for (int i = 0; i < imageWidth; i++)
			for (int j = 0; j < imageHeight; j++) {
				num = i * imageHeight + j;
				System.out.println(objects[num].group - 1);
				bi.setRGB(i, j, colors.get(objects[num].group - 1).getRGB());
			}
		try {
			ImageIO.write(bi, "jpg", new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
