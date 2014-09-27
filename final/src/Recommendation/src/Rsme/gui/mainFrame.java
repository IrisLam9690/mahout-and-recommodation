
package Rsme.gui;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;


public class mainFrame
											extends ApplicationWindow
{
	
	private static Shell shell;
	
	
	public mainFrame()
	{
		super(null);
	}
	
	
	protected void createMenu(Menu menu)
	{
		String[] items = new String[2];
		items[0] = new String("文件");
		items[1] = new String("帮助");
		
		MenuItem[] mItem = new MenuItem[2];
		for (int i = 0; i < mItem.length; i++)
		{
			mItem[i] = new MenuItem(menu, SWT.CASCADE);
			mItem[i].setText(items[i]);
			Menu submenu = new Menu(shell, SWT.DROP_DOWN);
			mItem[i].setMenu(submenu);
		}
		
		MenuItem item1 = new MenuItem(mItem[0].getMenu(), SWT.PUSH);
		item1.setText("退出");
		item1.addSelectionListener(new SelectionListener()
		{
			public void widgetSelected(SelectionEvent arg0)
			{
				shell.close();
			}
			
			public void widgetDefaultSelected(SelectionEvent arg0)
			{
				shell.close();
			}
		});
	}
	
	
	protected Control createContents(Composite parent)
	{
		shell = parent.getShell();
		shell.setText("推荐系统试验平台");
		Menu menu = new Menu(shell, SWT.BAR);
		createMenu(menu);
		shell.setMenuBar(menu);
		
		TabFolder folder=new TabFolder(parent,SWT.NONE);
		TabItem data=new TabItem(folder,SWT.NONE);
		data.setText("数据集合描述");
		
		
		TabItem tagrecommender=new TabItem(folder,SWT.NONE);
		tagrecommender.setControl(new TagRecommenderPanel(folder));
		tagrecommender.setText("标签推荐算法");
		
		return parent;
	}
	
	
	public static void main(String[] args)
	{
		mainFrame frame = new mainFrame();
		frame.setBlockOnOpen(true);
		frame.open();
		Display.getCurrent().dispose();
	}
}
