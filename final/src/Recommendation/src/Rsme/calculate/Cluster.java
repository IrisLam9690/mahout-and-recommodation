package Rsme.calculate;

import java.io.File;
import java.io.FileReader;
import java.lang.Math;

public class Cluster
{
	int i = 0,j = 0,k = 0,p = 0;
	int userNum = 0;//�û�����
	int sourceNum = 0;//��Դ����
	int clusterNum = 0;//�������
	String userTag[][] = new String[100][100];
	String userCount_s[][] = new String[100][100];
	int userCount[][] = new int[100][100];
	
	int userTagNum[] = new int[100];//userTagNum[i]:��i���û��еı�ǩ��Ŀ
	int sourceTagNum[] = new int[100];//sourceTagNum[i]:��i����Դ�еı�ǩ��Ŀ
	int clusterTagNum[] = new int[100];//sourceTagNum[i]:��i��۴��еı�ǩ��Ŀ
	
	String clusterTag[][] = new String[100][100];
	
	String sourceTag[][] = new String[100][100];
	String sourceCount_s[][] = new String[100][100];
	int sourceCount[][] = new int[100][100];
		
	int uc_w_up[][] = new int[100][100];
	double uc_w[][] = new double[100][100];
	int rc_w_up[][] = new int[100][100];
	double rc_w[][] = new double[100][100];
	
	double related[][] = new double[100][100];//related[i][j]�û�i����Դj����Ȥ�̶�
	
   double max[] = new double[100];
   double temp;
   int temp1;
   int list[][][] = new int[100][100][100];//�Ƽ��б�
   int n = 2;//�Ƽ�n������
   
   double score[][][] = new double[100][100][100];
   //score[i][j][k]��i���û��ĵ�j����ǩ��ǵ�k����Դ������
	
	public Cluster(String userAddress,String clusterAddress,String sourceAddress)
	{
		File user = new File(userAddress);
		File source = new File(sourceAddress);
		File cluster = new File(clusterAddress);
		
		for (i = 0; i <= 99; i++)//��ʼ��
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
			   ����userProfile
----------------------------------------------------------------------------------------*/
			i = 0;j = 0;
			   FileReader fr1 = new FileReader(user);
			   int ch1 = 0;
			   while ((ch1 = fr1.read()) != -1)
			   {
				   if (ch1 == 13)//��һ����Դ
				   {
					   userTagNum[i]--;
					   ch1 = fr1.read();
					   ch1 = fr1.read();
					   i++;
					   j = 0;
				   }
				   if ((ch1 != 32)&&(ch1 != 13)&&(ch1 != 10))//����Tag
				   {
					   userTag[i][j] = userTag[i][j]+(char)ch1;
				   }
				   if (ch1 == 32)//����Count
				   {
					   while ((ch1 != 13)&&(ch1 != -1))
					   {
						   ch1 = fr1.read();
						   if ((ch1 != 13)&&(ch1 != -1))
							   userCount_s[i][j] = userCount_s[i][j]+(char)ch1;
					   }
					   j++;
					   userTagNum[i]++;//�û�i�ı�ǩ��Ŀ
					   ch1 = fr1.read();
				   }			   
			   }
			   userNum = i;
			   userTagNum[i]--;
/*--------------------------------------------------------------------------------------
			   ����clusters
----------------------------------------------------------------------------------------*/
			   i = 0;j = 0;
			   FileReader fr2 = new FileReader(cluster);
			   int ch2 = 0;
			   while ((ch2 = fr2.read()) != -1)
			   {
				   if (ch2 == 13)//��һ����Դ
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
				   clusterTagNum[i]++;//�۴�i�ı�ǩ��Ŀ
				   ch2 = fr2.read();
			   }
			   clusterNum = i;
			   clusterTagNum[i]--;
/*--------------------------------------------------------------------------------------
			   ����source
----------------------------------------------------------------------------------------*/
			   i = 0;j = 0;
			   FileReader fr3 = new FileReader(source);
			   int ch3 = 0;
			   while ((ch3 = fr3.read()) != -1)
			   {
				   if (ch3 == 13)//��һ����Դ
				   {
					   sourceTagNum[i]--;
					   ch3 = fr3.read();
					   ch3 = fr3.read();
					   i++;
					   j = 0;
				   }
				   if ((ch3 != 32)&&(ch3 != 13)&&(ch3 != 10))//����Tag
				   {
					   sourceTag[i][j] = sourceTag[i][j]+(char)ch3;
				   }
				   if (ch3 == 32)//����Count
				   {
					   while ((ch3 != 13)&&(ch3 != -1))
					   {
						   ch3 = fr3.read();
						   if ((ch3 != 13)&&(ch3 != -1))
							   sourceCount_s[i][j] = sourceCount_s[i][j]+(char)ch3;
					   }
					   j++;
					   sourceTagNum[i]++;//��Դi�ı�ǩ��Ŀ
					   ch3 = fr3.read();
				   }			   
			   }
			   sourceNum = i;
			   sourceTagNum[i]--;
/*-----------------------------------------------------------------------------------------
 						�����������ƶ�
 ------------------------------------------------------------------------------------------*/
			   int flag[][][] = new int[100][100][100];//flag[i][j][k]��i���û��ĵ�j����ǩ���k����Դ�Ƿ����
			   double cos[][][] = new double[100][100][100];//cos[i][j][k]��i���û��ĵ�j����ǩ���k����Դ���������ƶ�
			   int ve_mod[] = new int[100];//ve_mod[i]��i����Դ��ǩ������ģ;
			   int cos_up[][][] = new int[100][100][100];//�����Ӧλ�õ��������ƶ�ʱ�ķ���
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
			   for (i = 0; i <= userNum; i++)//�û�
				   for (j = 0; j <= userTagNum[i]; j++)//�û���ĳһ��ǩ
				   {
					   for (k = 0; k <= sourceNum; k++)//��Դ
					   {
						   for (p = 0; p <= sourceTagNum[k]; p++)//��Դ��ĳһ��ǩ
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
 * 		�����Ƽ�
 -------------------------------------------------------------------------------------------*/
			   i = 0;j = 0;k = 0;
			   for (k = 0; k <= userNum; k++)//�û����
			   {
				   for (i = 0; i <= clusterNum; i++)//�۴����
				   {
					   for (j = 0; j <= userTagNum[k]; j++)//�û��б�ǩ���
					   {
						   for (p = 0; p <= clusterTagNum[i]; p++)//�۴��б�ǩ���
						   {
							   if (userTag[k][j].equals(clusterTag[i][p]))
								   uc_w_up[k][i]++;
						   }
					   }
					   uc_w[k][i] = (double)uc_w_up[k][i]/userTagNum[k];
				   }
			   }
			   
			   for (k = 0; k <= sourceNum; k++)//��Դ���
			   {
				   for (i = 0; i <= clusterNum; i++)//�۴����
				   {
					   for (j = 0; j <= sourceTagNum[k]; j++)//��Դ�б�ǩ���
					   {
						   for (p = 0; p <= clusterTagNum[i]; p++)//�۴��б�ǩ���
						   {
							   if (sourceTag[k][j].equals(clusterTag[i][p]))
								   rc_w_up[k][i]++;
						   }
					   }
					   rc_w[k][i] = (double)rc_w_up[k][i]/sourceTagNum[k]; 
				   }
			   }
			   
			   for (i = 0; i <= userNum; i++)//�û�
			   {
				   for (j = 0; j <= sourceNum; j++)//��Դ
				   {
					   for (k = 0; k <= Math.max(userTagNum[i],sourceTagNum[j]); k++)//��ǩ
					   {
						   related[i][j] = uc_w[i][k]*rc_w[j][k]+related[i][j];	   
					   }
//					   System.out.println(related[i][j]);
				   }
//				   System.out.println();
			   }
/*------------------------------------------------------------------------------------
 * 			   ��Դ����
 ---------------------------------------------------------------------------------------*/
			   for (i = 0; i <= userNum; i++)
				   for (j = 0; j <= userTagNum[i]; j++)
				   {
					   for (k = 0; k <= sourceNum; k++)
					   {
						   score[i][j][k] = related[i][k]*cos[i][j][k];
//						   System.out.println("�û�"+i+"�ؼ���'"+userTag[i][j]+"' ���ڵ�"+k+"����Դ������Ϊ"+score[i][j][k]);
					   }
//					   System.out.println();
				   }

/*---------------------------------------------------------------------------------
 *      	��Դ�Ƽ�
 ----------------------------------------------------------------------------------*/
			   for (i = 0; i <= userNum; i++)
				   for (j = 0; j <= userTagNum[i]; j++)
					   for (k = 0; k <= sourceNum; k++)
						   list[i][j][k] = k;
			   for (i = 0; i <= userNum; i++)
			   {
				 for (j = 0; j <= userTagNum[i]; j++)
				 {
					for (k = 0; k <= sourceNum; k++)//����
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
/*					System.out.println("�û�"+i+"�ı�ǩ\""+userTag[i][j]+"\"�Ƽ���Դ");
					for (p = 0; p < n; p++)
					{
						if (score[i][j][p] != 0)
						{
							System.out.print(p+1);
							System.out.print(":��");
							System.out.print(list[i][j][p]);
							System.out.println("��");
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