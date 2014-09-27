package Rsme.calculate;

import java.io.File;
import java.io.FileReader;
import java.lang.Math;

public class Cluster
{
	int i = 0,j = 0,k = 0,p = 0;
	int userNum = 0;//用户个数
	int sourceNum = 0;//资源组数
	int clusterNum = 0;//聚类个数
	String userTag[][] = new String[100][100];
	String userCount_s[][] = new String[100][100];
	int userCount[][] = new int[100][100];
	
	int userTagNum[] = new int[100];//userTagNum[i]:第i组用户有的标签数目
	int sourceTagNum[] = new int[100];//sourceTagNum[i]:第i组资源有的标签数目
	int clusterTagNum[] = new int[100];//sourceTagNum[i]:第i组聚簇有的标签数目
	
	String clusterTag[][] = new String[100][100];
	
	String sourceTag[][] = new String[100][100];
	String sourceCount_s[][] = new String[100][100];
	int sourceCount[][] = new int[100][100];
		
	int uc_w_up[][] = new int[100][100];
	double uc_w[][] = new double[100][100];
	int rc_w_up[][] = new int[100][100];
	double rc_w[][] = new double[100][100];
	
	double related[][] = new double[100][100];//related[i][j]用户i对资源j的兴趣程度
	
   double max[] = new double[100];
   double temp;
   int temp1;
   int list[][][] = new int[100][100][100];//推荐列表
   int n = 2;//推荐n个最大的
   
   double score[][][] = new double[100][100][100];
   //score[i][j][k]第i个用户的第j个标签标记第k个资源的评分
	
	public Cluster(String userAddress,String clusterAddress,String sourceAddress)
	{
		File user = new File(userAddress);
		File source = new File(sourceAddress);
		File cluster = new File(clusterAddress);
		
		for (i = 0; i <= 99; i++)//初始化
		{		
			userTagNum[i] = 0;
			sourceTagNum[i] = 0;
			for (j = 0; j <= 99; j++)
			{
				userTag[i][j] = "";
				userCount_s[i][j] = "";
				userCount[i][j] = 0;
				clusterTag[i][j] = "";
				sourceTag[i][j] = "";
				sourceCount_s[i][j] = "";
				sourceCount[i][j] = 0;
				uc_w_up[i][j] = 0;
				rc_w_up[i][j] = 0;
				related[i][j] = 0.0;
			}
		}
		i = 0;
		try
		{
/*--------------------------------------------------------------------------------------
			   读入userProfile
----------------------------------------------------------------------------------------*/
			i = 0;j = 0;
			   FileReader fr1 = new FileReader(user);
			   int ch1 = 0;
			   while ((ch1 = fr1.read()) != -1)
			   {
				   if (ch1 == 13)//下一组资源
				   {
					   userTagNum[i]--;
					   ch1 = fr1.read();
					   ch1 = fr1.read();
					   i++;
					   j = 0;
				   }
				   if ((ch1 != 32)&&(ch1 != 13)&&(ch1 != 10))//读入Tag
				   {
					   userTag[i][j] = userTag[i][j]+(char)ch1;
				   }
				   if (ch1 == 32)//读入Count
				   {
					   while ((ch1 != 13)&&(ch1 != -1))
					   {
						   ch1 = fr1.read();
						   if ((ch1 != 13)&&(ch1 != -1))
							   userCount_s[i][j] = userCount_s[i][j]+(char)ch1;
					   }
					   j++;
					   userTagNum[i]++;//用户i的标签数目
					   ch1 = fr1.read();
				   }			   
			   }
			   userNum = i;
			   userTagNum[i]--;
/*--------------------------------------------------------------------------------------
			   读入clusters
----------------------------------------------------------------------------------------*/
			   i = 0;j = 0;
			   FileReader fr2 = new FileReader(cluster);
			   int ch2 = 0;
			   while ((ch2 = fr2.read()) != -1)
			   {
				   if (ch2 == 13)//下一组资源
				   {
					   clusterTagNum[i]--;
					   ch2 = fr2.read();
					   ch2 = fr2.read();
					   i++;
					   j = 0;
				   }
				   while ((ch2 != 13)&&(ch2 != -1))
				   {
					   clusterTag[i][j] = clusterTag[i][j]+(char)ch2;
					   ch2 = fr2.read();
				   }
				   j++;
				   clusterTagNum[i]++;//聚簇i的标签数目
				   ch2 = fr2.read();
			   }
			   clusterNum = i;
			   clusterTagNum[i]--;
/*--------------------------------------------------------------------------------------
			   读入source
----------------------------------------------------------------------------------------*/
			   i = 0;j = 0;
			   FileReader fr3 = new FileReader(source);
			   int ch3 = 0;
			   while ((ch3 = fr3.read()) != -1)
			   {
				   if (ch3 == 13)//下一组资源
				   {
					   sourceTagNum[i]--;
					   ch3 = fr3.read();
					   ch3 = fr3.read();
					   i++;
					   j = 0;
				   }
				   if ((ch3 != 32)&&(ch3 != 13)&&(ch3 != 10))//读入Tag
				   {
					   sourceTag[i][j] = sourceTag[i][j]+(char)ch3;
				   }
				   if (ch3 == 32)//读入Count
				   {
					   while ((ch3 != 13)&&(ch3 != -1))
					   {
						   ch3 = fr3.read();
						   if ((ch3 != 13)&&(ch3 != -1))
							   sourceCount_s[i][j] = sourceCount_s[i][j]+(char)ch3;
					   }
					   j++;
					   sourceTagNum[i]++;//资源i的标签数目
					   ch3 = fr3.read();
				   }			   
			   }
			   sourceNum = i;
			   sourceTagNum[i]--;
/*-----------------------------------------------------------------------------------------
 						计算余弦相似度
 ------------------------------------------------------------------------------------------*/
			   int flag[][][] = new int[100][100][100];//flag[i][j][k]第i个用户的第j个标签与第k个资源是否相关
			   double cos[][][] = new double[100][100][100];//cos[i][j][k]第i个用户的第j个标签与第k个资源的余弦相似度
			   int ve_mod[] = new int[100];//ve_mod[i]第i个资源标签向量的模;
			   int cos_up[][][] = new int[100][100][100];//计算对应位置的余弦相似度时的分子
			   for (i = 0; i <= userNum; i++)
			   {
				   	ve_mod[i] = 0;
					for (j = 0; j <= userTagNum[i]; j++)
					{
						userCount[i][j] = Integer.parseInt(userCount_s[i][j]);
						sourceCount[i][j] = Integer.parseInt(sourceCount_s[i][j]);
						ve_mod[i] = sourceCount[i][j]*sourceCount[i][j]+ve_mod[i];
						for (k = 0; k <= sourceNum; k++)
						{
							flag[i][j][k] = 0;
							cos[i][j][k] = 0;
							cos_up[i][j][k] = 0;
						}
					}
			   }
			   for (i = 0; i <= userNum; i++)//用户
				   for (j = 0; j <= userTagNum[i]; j++)//用户的某一标签
				   {
					   for (k = 0; k <= sourceNum; k++)//资源
					   {
						   for (p = 0; p <= sourceTagNum[k]; p++)//资源的某一标签
						   {
							   if (userTag[i][j].equals(sourceTag[k][p]))
							   {
								   flag[i][j][k] = 1;
								   cos_up[i][j][k] = cos_up[i][j][k]+sourceCount[k][p];
							   }
						   }
					       if (flag[i][j][k] == 0)
					    	   cos[i][j][k] = 0;
					       else
					    	   cos[i][j][k] = cos_up[i][j][k]/Math.sqrt(ve_mod[k]);
//				   		   System.out.println(userTag[i][j]+" "+k+" "+cos[i][j][k]);
					   }
//					   System.out.println();
				   }
/*-------------------------------------------------------------------------------------------
 * 		个性推荐
 -------------------------------------------------------------------------------------------*/
			   i = 0;j = 0;k = 0;
			   for (k = 0; k <= userNum; k++)//用户序号
			   {
				   for (i = 0; i <= clusterNum; i++)//聚簇序号
				   {
					   for (j = 0; j <= userTagNum[k]; j++)//用户中标签序号
					   {
						   for (p = 0; p <= clusterTagNum[i]; p++)//聚簇中标签序号
						   {
							   if (userTag[k][j].equals(clusterTag[i][p]))
								   uc_w_up[k][i]++;
						   }
					   }
					   uc_w[k][i] = (double)uc_w_up[k][i]/userTagNum[k];
				   }
			   }
			   
			   for (k = 0; k <= sourceNum; k++)//资源序号
			   {
				   for (i = 0; i <= clusterNum; i++)//聚簇序号
				   {
					   for (j = 0; j <= sourceTagNum[k]; j++)//资源中标签序号
					   {
						   for (p = 0; p <= clusterTagNum[i]; p++)//聚簇中标签序号
						   {
							   if (sourceTag[k][j].equals(clusterTag[i][p]))
								   rc_w_up[k][i]++;
						   }
					   }
					   rc_w[k][i] = (double)rc_w_up[k][i]/sourceTagNum[k]; 
				   }
			   }
			   
			   for (i = 0; i <= userNum; i++)//用户
			   {
				   for (j = 0; j <= sourceNum; j++)//资源
				   {
					   for (k = 0; k <= Math.max(userTagNum[i],sourceTagNum[j]); k++)//标签
					   {
						   related[i][j] = uc_w[i][k]*rc_w[j][k]+related[i][j];	   
					   }
//					   System.out.println(related[i][j]);
				   }
//				   System.out.println();
			   }
/*------------------------------------------------------------------------------------
 * 			   资源评分
 ---------------------------------------------------------------------------------------*/
			   for (i = 0; i <= userNum; i++)
				   for (j = 0; j <= userTagNum[i]; j++)
				   {
					   for (k = 0; k <= sourceNum; k++)
					   {
						   score[i][j][k] = related[i][k]*cos[i][j][k];
//						   System.out.println("用户"+i+"关键词'"+userTag[i][j]+"' 对于第"+k+"组资源的评分为"+score[i][j][k]);
					   }
//					   System.out.println();
				   }

/*---------------------------------------------------------------------------------
 *      	资源推荐
 ----------------------------------------------------------------------------------*/
			   for (i = 0; i <= userNum; i++)
				   for (j = 0; j <= userTagNum[i]; j++)
					   for (k = 0; k <= sourceNum; k++)
						   list[i][j][k] = k;
			   for (i = 0; i <= userNum; i++)
			   {
				 for (j = 0; j <= userTagNum[i]; j++)
				 {
					for (k = 0; k <= sourceNum; k++)//排序
						for (p = k; p <= sourceNum; p++)
						{
							if (score[i][j][k] < score[i][j][p])
							{
								temp = score[i][j][k];
								score[i][j][k] = score[i][j][p];
								score[i][j][p] = temp;
								temp1 = list[i][j][k];
								list[i][j][k] = list[i][j][p];
								list[i][j][p] = temp1;
							}
						}
/*					System.out.println("用户"+i+"的标签\""+userTag[i][j]+"\"推荐资源");
					for (p = 0; p < n; p++)
					{
						if (score[i][j][p] != 0)
						{
							System.out.print(p+1);
							System.out.print(":第");
							System.out.print(list[i][j][p]);
							System.out.println("组");
						}
					}*/
				 }
//				 System.out.println();
			   }
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public double[][][] getScore()
	{
		return score;
	}
	public int[][][] getResult()
	{
		return list;
	}
	public int getUserNum()
	{
		return userNum;
	}
	public int[] getUserTagNum()
	{
		return userTagNum;
	}
	public String[][] getUserTag()
	{
		return userTag;
	}
	
	public int getClusterNum()
	{
		return clusterNum;
	}
	public int[] getClusterTagNum()
	{
		return clusterTagNum;
	}
	public String[][] getClusterTag()
	{
		return clusterTag;
	}
	
	public int getSourceNum()
	{
		return sourceNum;
	}
	public int[] getSourceTagNum()
	{
		return sourceTagNum;
	}
	public String[][] getSourceTag()
	{
		return sourceTag;
	}
	
	public int getResultNum()
	{
		return n;
	}
}