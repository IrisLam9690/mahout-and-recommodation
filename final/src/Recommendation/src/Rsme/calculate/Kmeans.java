package Rsme.calculate;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Kmeans {
    
    private static int K = 3; // 类数（簇） 
    private static int TOTAL = 18; // 点个数
    private int test = 0;
    private Point[] unknown = new Point[TOTAL]; // 点数组
    private int[] type = new int[TOTAL]; // 每个点暂时的类（簇）
    private Point[] z = new Point[K];// 保存新的聚类中心
    private Point[] z0 = new Point[K]; // 保存上一次的聚类中心
	File cluster = new File("cluster.txt");
	String clusterTag[] = new String[100];
	String clu_x_s[] = new String[100];
	String clu_y_s[] = new String[100];
	int clu_x[] = new int[100];
	int clu_y[] = new int[100];
	int clusterNum = 0;
	int q = 0;
    private int temp = 0;
    private int I = 0; // 迭代次数
   
    /** Creates a new instance of Kmeans */
    public Kmeans(String clusterInputAddress) {
       /** 进行聚类运算的20个点 */
    	File cluster_input = new File(clusterInputAddress);
		for (q = 0; q <= 99; q++)
		{
			clu_x_s[q] = "";
			clu_y_s[q] = "";
			clusterTag[q] = "";
		}
		
		try
		{
		    FileReader fr1 = new FileReader(cluster_input);
		    int ch1 = 0;
		    while ((ch1 = fr1.read()) != -1)
		    {
			    if ((ch1 != 32)&&(ch1 != 13)&&(ch1 != 10))//读入标签
			    {
				   clusterTag[clusterNum] = clusterTag[clusterNum]+(char)ch1;
			    }
			    if (ch1 == 32)//读入坐标
			    {
				    while (true)//读入横坐标
				    {
				   	   ch1 = fr1.read();
					   if (ch1 == 32) break;
					   if ((ch1 != 13)&&(ch1 != -1))
						   clu_x_s[clusterNum] = clu_x_s[clusterNum]+(char)ch1;
				    }
				    while ((ch1 != 13)&&(ch1 != -1))//读入纵坐标
				    {
					   ch1 = fr1.read();
					   if ((ch1 != 13)&&(ch1 != -1))
						   clu_y_s[clusterNum] = clu_y_s[clusterNum]+(char)ch1;
				    }
				    clusterNum++;//的标签数目
				    ch1 = fr1.read();//跳过换行符
			    }			   
		    }
	    }
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (q = 0; q < clusterNum; q++)
		{
			clu_x[q] = Integer.parseInt(clu_x_s[q]);
			clu_y[q] = Integer.parseInt(clu_y_s[q]);
			unknown[q] = new Point(clu_x[q],clu_y[q]);
		}
		
/*-------------------------------------------------------------------
 * 		   
 */

        for(int i = 0;i < TOTAL; i++){
            type[i] = 0;
        }
        for(int i = 0; i < K; i++){
            z[i] = unknown[i]; // 伪随机选取
            z0[i] = new Point(0.0,0.0);
        }
    }
    
    /** 计算新的聚类中心 */
    public Point newCenter(int m){
        int n = 0;
        Point sum = new Point(0.0,0.0);
        for(int i = 0;i < TOTAL; i++){
            if(type[i] == m){
                sum.setX(sum.getX() + unknown[i].getX());
                sum.setY(sum.getY() + unknown[i].getY());
                n += 1;
            }
        }
        sum.setX(sum.getX() / n);
        sum.setY(sum.getY() / n);
        return sum;
    }
    
    /** 比较两个聚类中心是否相等 */
    public boolean isEqual(Point p1,Point p2){
        if(p1.getX() * 10 == p2.getX() * 10 && p1.getY() * 10 == p2.getY() * 10)
            return true;
        else
            return false;
    }
    
    /** 计算两点之间的欧式距离 */
    public static double distance(Point p1,Point p2){
        return (p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + 
                (p1.getY() - p2.getY()) * (p1.getY() - p2.getY());
    }
    
    /** 进行迭代，对TOTAL个样本根据聚类中心进行分类 */
    public void order(){
//       int temp = 0; // 记录unknown[i]暂时在哪个类中
        for(int i = 0; i < TOTAL;i ++){
            for(int j = 0; j < K;j++){
                if(distance(unknown[i],z[temp]) > distance(unknown[i],z[j]))
                    temp = j;
            }
            type[i] = temp;
            System.out.println("经比较后，" + unknown[i].toString() + "被归为" + temp +"类" );
        }
    }
    
    public void main(){
        for(int i = 0; i < K;i++)
            System.out.println("初始时，第" + i + "类中心:" + z[i].toString());
        while(test != K){
        	System.out.println("----------------------------------------------------------");
            order();//分类
            for(int i = 0; i < K;i ++){
                z[i] = newCenter(i);
                System.out.println("第" + i + " 类新中心:" + z[i].toString());
                if(isEqual(z[i],z0[i]))
                    test++;//当上一次的类中心等于这一次的类中心时
                else		  //说明形成了一次正确的聚类
                {
                    z0[i] = z[i];
                    test = 0;
                }
            }
            I += 1;
            System.out.println("已完成第" + I + "次迭代");
            System.out.println("分类后有:");
            for(int j = 0;j < K;j++){
                System.out.println("第" + j + "类分类有: ");
                for(int i = 0;i < TOTAL;i++){
                    if(type[i] == j)
                        System.out.println(unknown[i].toString());
                }
                System.out.println("类中心"+z[j]);
            }
        }
        try
        {
			FileWriter cluster_writer = new FileWriter(cluster);
	        for (q = 0; q < K; q++)
	        {
	        	System.out.println("第"+q+"组:");
	        	for (int p = 0; p < clusterNum; p++)
	        	{       		
	        		if (type[p] == q)
	        		{
	        			System.out.println(clusterTag[p]);
	        			cluster_writer.write(clusterTag[p],0,clusterTag[p].length());
	        			cluster_writer.write("\r\n",0,2);
	        		}
	        	}
	        	cluster_writer.write("\r\n",0,2);
	        	System.out.println();
	        }
			cluster_writer.close();
        }
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

//    public static void main(String[] args){
//        new Kmeans("cluster_input.txt").main();
//    }
}
