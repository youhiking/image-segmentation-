package image_segmentation_kmeans;

public class ClusterItem {
	public int attributeNum=3;// 属性个数
	public double[] attributeValues;// 存储属性值的数组
	public int group;// 归属于的簇组号

	public ClusterItem(int red, int green, int blue, int i) {
		// TODO Auto-generated constructor stub
		attributeValues=new double[attributeNum];
		attributeValues[0]=red;
		attributeValues[1]=green;
		attributeValues[2]=blue;
		group=i;
	}

	public double calcuDistance(ClusterItem object) {
		double sum = 0;
		if (attributeValues.length > 0) {
			for (int i = 0; i < attributeValues.length; i++) {
				sum += (Math.pow(Math.abs(this.attributeValues[i]-object.attributeValues[i]), 2));
			}
			return Math.sqrt(sum);
		}
		return sum;
	}

	public void setCenterNum(int group) {
		this.group = group;
	}

	public int minDistance(ClusterItem[] centers) {
		int minCenter = 1;
		double minDistance = this.calcuDistance(centers[0]);
		for (int t = 1; t < centers.length; t++) {
			if (minDistance > this.calcuDistance(centers[t])) {
				minDistance = this.calcuDistance(centers[t]);
				minCenter = t + 1;
			}
		}
        return minCenter;
	}
	
}
