
package Rsme.gui;

import java.io.FileNotFoundException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import Rsme.evaluation.ItemEvaluation;
import Rsme.evaluation.ItemEvaluator;
import Rsme.evaluation.TagEvaluator;
import Rsme.model.DataModel;
import Rsme.model.Folksonomy;
import Rsme.model.loader.MovielensTagDataLoader;
import Rsme.recommender.item.ItemRecommender;
import Rsme.recommender.item.PRBC_Hierarchy;
import Rsme.recommender.item.PRBC_Kmeans;
import Rsme.recommender.tag.AbstractTagRecommender;
import Rsme.recommender.tag.BeyesRecommender;
//import Rsme.recommender.tag.FolkRank;
import Rsme.recommender.tag.PRBFR;
import Rsme.recommender.tag.TagRecommender;
/*----------------B E G I N-----------------------------
 * 
 */
import Rsme.calculate.*;
/*----------------E N D---------------------------------
 * 
 */

public class TagRecommenderPanel
																extends Composite
{
	
	public static Shell shell;
	
	protected static String preferenceFile = "";
	protected static String tagFile = "";
	protected static String testPreferenceFile = "";
	protected static String userFile = "";
	protected static String clusterFile = "";
	protected static String sourceFile = "";
	protected static Folksonomy datamodel;
	protected static String result = null;
	String userAddress = null,clusterAddress = null,sourceAddress = null;
	String userName = null,clusterName = null,sourceName = null;
	
	protected Text text;
	
	public static MouseAdapter linster = new MouseAdapter()
	{
		public void mouseUp(MouseEvent e)
		{
			FileDialog dialog = new FileDialog(shell, SWT.MULTI);
			dialog.setFilterExtensions(new String[]
			{ "*.pref" });
			dialog.open();
		}
	};
	
	
	protected void createButtons()
	{
		Group buttongroup = new Group(this, SWT.SHADOW_ETCHED_IN);
		buttongroup.setText("生成数据集合");
		GridData groupdata = new GridData(GridData.FILL_HORIZONTAL);
		groupdata.horizontalSpan = 3;
		buttongroup.setLayoutData(groupdata);		

						
		Button openpreference1 = new Button(buttongroup, SWT.PUSH);
		openpreference1.setText("选择训练文件");
		openpreference1.addMouseListener(new MouseAdapter()
		{
			public void mouseUp(MouseEvent e)
			{
				FileDialog dialog = new FileDialog(shell, SWT.MULTI);
				dialog.setFilterExtensions(new String[]
				{ "*.txt" });
				preferenceFile = dialog.open();
			}
		});
		openpreference1.setLocation(450, 20);
		openpreference1.pack();
		
		Button opentag1 = new Button(buttongroup, SWT.PUSH);
		opentag1.setText("选择tag文件");
		opentag1.addMouseListener(new MouseAdapter()
		{
			public void mouseUp(MouseEvent e)
			{
				FileDialog dialog = new FileDialog(shell, SWT.MULTI);
				dialog.setFilterExtensions(new String[]
				{ "*.txt" });
				tagFile = dialog.open();
			}
		});
		opentag1.setLocation(550, 20);
		opentag1.pack();
		
		Button createData1 = new Button(buttongroup, SWT.PUSH);
		createData1.setText("生成训练集");
		createData1.addMouseListener(new MouseAdapter()
		{
			public void mouseUp(MouseEvent e)
			{
				MovielensTagDataLoader loader;
				try
				{
					loader = new MovielensTagDataLoader(preferenceFile, tagFile);
					datamodel = loader.getFolksonomy();
				}
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		createData1.setLocation(650, 20);
		createData1.pack();

	}
	
	
	protected void createAlgorithmTree()
	{
		final Tree tree = new Tree(this, SWT.BORDER);
		GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.FILL_BOTH);
		griddata.horizontalSpan = 1;
		tree.setLayoutData(griddata);
		
		TreeItem item1 = new TreeItem(tree, SWT.NONE);
		item1.setText("基于协同过滤算法");
		TreeItem basicCF = new TreeItem(item1, SWT.NONE);
		basicCF.setText("user-based CF");
		
		//beyes
		TreeItem beyes = new TreeItem(item1, SWT.NONE);
		beyes.setText("based on beyes");
		
		TreeItem item2 = new TreeItem(tree, SWT.NONE);
		item2.setText("基于标记排序的滤算法");

		TreeItem kmeansCluster = new TreeItem(item2, SWT.NONE);
		kmeansCluster.setText("based on Kmeans cluster");
		
		//hierarchy-cluster
		TreeItem hierarchyCluster = new TreeItem(item2, SWT.NONE);
		hierarchyCluster.setText("based on hierarchy cluster");
		
		//folkrank
		TreeItem folkRankSelf = new TreeItem(item2, SWT.NONE);
		folkRankSelf.setText("based on folkRank(self)");
		
		tree.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseDoubleClick(MouseEvent e)
			{
				TreeItem item = tree.getItem(new Point(e.x, e.y));
				if (item != null)
				{
					String str = item.getText();
					if (str.equals("folkRank"))
					{
//						
					}
/*------------------B E G I N-----------------------------------------------
 * 
 */
					if (str.equals("based on Kmeans cluster"))
					{
						PRBC_Kmeans kmeans = new PRBC_Kmeans();
						try
						{
							kmeans.buildRecommender(datamodel);
							ItemEvaluator itemEvaluator = new ItemEvaluator();
							StringBuilder ss=new StringBuilder();
							ItemEvaluation evaluation = itemEvaluator.evaluate(kmeans, datamodel);
							ss.append("\n");
							ss.append("测试算法为based on kmeans cluster算法\n");
							ss.append("\n");
							ss.append("================相应的评价结果如下：==============\n");
							ss.append("\n");
							ss.append(evaluation);
							System.out.println(evaluation);
							text.setText(ss.toString());
						}
						catch (Exception e2)
						{
							e2.printStackTrace();
						}
					}
/*-------------------E N D-------------------------------------------------
 * 
 */
					if (str.equals("based on hierarchy cluster"))
					{
						PRBC_Hierarchy hierarchy = new PRBC_Hierarchy();
						try
						{
							hierarchy.buildRecommender(datamodel);
							ItemEvaluator itemEvaluator = new ItemEvaluator();
							StringBuilder ss=new StringBuilder();
							ItemEvaluation evaluation = itemEvaluator.evaluate(hierarchy, datamodel);
							ss.append("\n");
							ss.append("测试算法为based on hierarchy cluster算法\n");
							ss.append("\n");
							ss.append("================相应的评价结果如下：==============\n");
							ss.append("\n");
							ss.append(evaluation);
							System.out.println(evaluation);
							text.setText(ss.toString());
						}
						catch (Exception e2)
						{
							e2.printStackTrace();
						}
					}
					
					if (str.equals("based on folkRank(self)"))
					{
						PRBFR prbfr = new PRBFR();
						try
						{
							prbfr.buildRecommender(datamodel);
							TagEvaluator evaluator = new TagEvaluator();
							String result=evaluator.evaluate(prbfr, (Folksonomy) datamodel).toString();
							
							StringBuilder ss=new StringBuilder();
							ss.append("\n");
							ss.append("测试算法为folkRank(self)算法\n");
							ss.append("\n");
							ss.append("================相应的评价结果如下：==============\n");
							ss.append("\n");
							ss.append(result);
							System.out.println(result);
							text.setText(ss.toString());
						}
						catch (Exception e2)
						{
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					}
					
					if (str.equals("based on beyes"))
					{
						BeyesRecommender beyes = new BeyesRecommender();
						try
						{
							beyes.buildRecommender(datamodel);
							TagEvaluator evaluator = new TagEvaluator();
							String result=evaluator.evaluate(beyes, (Folksonomy) datamodel).toString();
							
							StringBuilder ss=new StringBuilder();
							ss.append("\n");
							ss.append("测试算法为based on beyes算法\n");
							ss.append("\n");
							ss.append("================相应的评价结果如下：==============\n");
							ss.append("\n");
							ss.append(result);
							System.out.println(result);
							text.setText(ss.toString());
						}
						catch (Exception e2)
						{
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					}
				}
			}
		});
	}
	
	
	public TagRecommenderPanel(Composite parent)
	{
		/* 设置父类的属性 */
		super(parent, SWT.NONE);
		shell = parent.getShell();
		
		GridLayout gridLayout = new GridLayout(3, true);
		setLayout(gridLayout);
		
		/* 建立按钮 */
		createButtons();
		createAlgorithmTree();
		text = new Text(this, SWT.BORDER|SWT.MULTI|SWT.V_SCROLL);
		GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.FILL_BOTH);
		griddata.horizontalSpan = 2;
		text.setLayoutData(griddata);
	}
	
}
