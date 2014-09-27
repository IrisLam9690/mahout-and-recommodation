
package Rsme.recommender.item;

import java.util.ArrayList;
import java.util.List;

import Rsme.math.DoubleVector;
import Rsme.math.SparseMatrix;
import Rsme.model.*;
import Rsme.model.loader.*;

/**
 * this class implements floyd method to find the shortest path
 * 
 * @author Yuan Tianning (10284049@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 5/12/2012
 */
class ShortPath
{
	/*------------------------members----------------------------------*/
	private int INF = Integer.MAX_VALUE;
	// dist[i][j]=INF<==>no edge exists between i and j
	/** the matrix to store the distance between nodes */
	private int[][] dist;
	/** the matrix to store the shortest path for graph */
	private SparseMatrix path;
	/** the shortest path from a specific node to another */
	private List<Integer> result = new ArrayList<Integer>();
	
	
	/*------------------------constructor----------------------------------*/
	public ShortPath(int size)
	{
		this.path = new SparseMatrix(size, size);
		this.dist = new int[size][size];
	}
	
	
	/*------------------------public functions----------------------------------*/
	/**
	 * return the result of shortest path
	 * */
	public List<Integer> Result()
	{
		return result;
	}
	
	
	/**
	 * find the shortest path from a specific node to another with the start node
	 * and the end node
	 * 
	 * @param begin
	 *          the begining node's index
	 * @param end
	 *          the ending node's index
	 * @throws Exception
	 */
	public void findCheapestPath(int begin, int end) throws Exception
	{
		result.clear();
		result.add(begin);
		findPath(begin, end);
		result.add(end);
	}
	
	
	/**
	 * find the shortest path from a specific node to another without the start
	 * node and the end node
	 * 
	 * @param i
	 *          the begining node's index
	 * @param j
	 *          the ending node's index
	 * @throws Exception
	 */
	public void findPath(int i, int j) throws Exception
	{
		if (!path.containRowCol(i, j))
			return;
		int k = (int) path.at(i, j);
		findPath(i, k);   // ตÝน้
		result.add(k);
		findPath(k, j);
	}
	
	
	/**
	 * find the shortest path for a graph
	 * 
	 * @param graph
	 * @throws Exception
	 */
	public void floyd(TripartiteGraph graph) throws Exception
	{
		int size = graph.numNodes();
		SparseMatrix matrix = (SparseMatrix) graph.matrix();
		// initialize dist
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (!matrix.containRowCol(i, j))
					dist[i][j] = INF;
				else
					dist[i][j] = (int) matrix.at(i, j);
			}
		}
		for (int k = 0; k < size; k++)
		{
			for (int i = 0; i < size; i++)
			{
				for (int j = 0; j < size; j++)
				{
					if (dist[i][k] != INF && dist[k][j] != INF
							&& dist[i][k] + dist[k][j] < dist[i][j])
					{
						dist[i][j] = dist[i][k] + dist[k][j];
						path.set(i, j, k);
					}
				}
			}
		}
	}
	/*------------------------end class----------------------------------*/
}

/**
 * this class implements the Topic-Oriented Tag-Based method for tag
 * recommendation unimplemented parts:Topic Extraction
 * 
 * @author Yuan Tianning (10284049@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 6/12/2012
 */
public class ToastRank
											extends ItemRecommender
{
	/*------------------------------members------------------------------------*/
	/** training dataset */
	Folksonomy m_folksonomy;
	/** the weight between users and tags */
	private SparseMatrix weight_ut;
	/** the weight between tags and items */
	private SparseMatrix weight_ti;
	/** the graph used to store all the nodes, include users, items, and tags */
	private UITGraph m_graph;
	/** the short path for m_graph */
	private ShortPath path;
	/** the number of users */
	private int user_num;
	/** the number of items */
	private int item_num;
	
	
	/*------------------------public functions----------------------------------*/
	@Override
	public void buildRecommender(DataModel dataModel) throws Exception
	{
		m_folksonomy = (Folksonomy) dataModel;
		createWeightMatrix(m_folksonomy);
		m_graph = new UITGraph(m_folksonomy);
		path = new ShortPath(m_graph.numNodes());
		path.floyd(m_graph);
		user_num = m_folksonomy.numUsers();
		item_num = m_folksonomy.numItems();
		
	}
	
	
	@Override
	public RatingItem[] ratingsForItems(int userId) throws Exception
	{
		int position = m_graph.getUserIndex(userId);
		int i;
		double[] score = new double[item_num];
		List<Integer> list;
		for (i = user_num; i < user_num + item_num; i++)
		{
			path.findCheapestPath(position, i);
			list = path.Result();
			score[i - user_num] = ui_score(list);
		}
		
		RatingItem[] results = new RatingItem[item_num];
		for (int j = 0; j < item_num; j++)
		{
			int itemId = m_folksonomy.getItemByIndex(j);
			results[j] = new RatingItem(itemId, score[j]);
		}
		
		return results;
	}
	
	
	/*------------------------protected functions----------------------------------*/
	
	/**
	 * create the weight matrix
	 * 
	 * @throws Exception
	 */
	protected void createWeightMatrix(Folksonomy trainingData) throws Exception
	{
		int i, j, k;
		int num_tag = trainingData.numTags();
		int num_user = trainingData.numUsers();
		int num_item = trainingData.numItems();
		int num_temp, itemIndex, tagIndex;
		UserTagProfile utp;
		AnnotatedItem ai;
		ArrayList<Integer> tags;
		weight_ti = new SparseMatrix(num_tag, num_item);
		weight_ut = new SparseMatrix(num_user, num_tag);
		int[] t_sum = new int[num_tag];
		int[] u_sum = new int[num_user];
		for (i = 0; i < num_user; i++)
		{
			utp = trainingData.getUserProfile(trainingData.getUserByIndex(i));
			num_temp = utp.numItems();
			for (j = 0; j < num_temp; j++)
			{
				ai = utp.getAnnotatedItem(j);
				tags = ai.getTags();
				itemIndex = trainingData.getItemIndex(ai.getItemId());
				for (k = 0; k < tags.size(); k++)
				{
					tagIndex = trainingData.getTagIndex(tags.get(k));
					t_sum[tagIndex] += 1;
					weight_ti.set(tagIndex, itemIndex,
							weight_ti.at(tagIndex, itemIndex) + 1);
					weight_ut.set(i, tagIndex, weight_ut.at(i, tagIndex) + 1);
				}
				u_sum[i] += ai.numTags();
			}
		}
		for (i = 0; i < num_tag; i++)
		{
			for (j = 0; j < num_item; j++)
			{
				if (weight_ti.containRowCol(i, j))
					weight_ti.set(i, j, weight_ti.at(i, j) / t_sum[i]);
			}
			for (k = 0; k < num_user; k++)
			{
				if (weight_ut.containRowCol(k, i))
					weight_ut.set(k, i, weight_ut.at(k, i) / u_sum[k]);
			}
		}
		
	}
	
	
	/**
	 * caculate the sum of all the weight around a specific tag
	 * 
	 * @param t
	 *          the tag's index
	 * @throws Exception
	 **/
	protected double t_weight_sum(int t) throws Exception
	{
		double sum = 0;
		int i, j;
		for (i = 0; i < user_num; i++)
		{
			sum += weight_ut.at(i, t);
		}
		for (j = 0; j < item_num; j++)
		{
			sum += weight_ti.at(t, j);
		}
		return sum;
	}
	
	
	/**
	 * get the preference score between a user and a item according the shortest
	 * path from the user node to the item node
	 * 
	 * @param ret
	 *          the shortest path
	 * @throws Exception
	 **/
	protected double ui_score(List<Integer> ret) throws Exception
	{
		int i;
		double score = 1;
		double temp;
		int index, index_next;
		for (i = 0; i < ret.size() - 1; i++)
		{
			index = ret.get(i);
			index_next = ret.get(i + 1);
			if (index >= 0 && index < user_num && index_next >= user_num + item_num
					&& index_next < m_graph.numNodes())
				score *= weight_ut.at(index, index_next - user_num - item_num);
			if (index >= user_num + item_num && index < m_graph.numNodes())
			{
				temp = t_weight_sum(index - user_num - item_num);
				if (index_next < user_num)
					score *= weight_ut.at(index_next, index - user_num - item_num) / temp;
				if (index_next >= user_num)
				{
					score *= weight_ti.at(index - user_num - item_num, index_next
							- user_num)
							/ temp;
				}
			}
			if (index >= user_num && index < user_num + item_num
					&& index_next >= user_num + item_num)
			{
				score *= weight_ti.at(index_next - user_num - item_num, index
						- user_num);
			}
		}
		return score;
	}
	
	
	/*------------------------end class----------------------------------*/
	
	/*------------------------test----------------------------------*/
	public static void main(String[] args) throws Exception
	{
		MovielensTagDataLoader loader = new MovielensTagDataLoader(
				"D:\\temps\\new.txt", "D:\\temps\\tags.txt");
		Folksonomy data = loader.getFolksonomy();
		
		ToastRank ranker = new ToastRank();
		ranker.buildRecommender(data);
		int[] values = ranker.recommendItems(78);
		
		for (int i = 0; i < values.length; i++)
			System.out.println(values[i]);
	}
}
