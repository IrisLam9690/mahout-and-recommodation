
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
		items[0] = new String("�ļ�");
		items[1] = new String("����");
		
		MenuItem[] mItem = new MenuItem[2];
		for (int i = 0; i < mItem.length; i++)
		{
			mItem[i] = new MenuItem(menu, SWT.CASCADE);
			mItem[i].setText(items[i]);
			Menu submenu = new Menu(shell, SWT.DROP_DOWN);
			mItem[i].setMenu(submenu);
		}
		
		MenuItem item1 = new MenuItem(mItem[0].getMenu(), SWT.PUSH);
		item1.setText("�˳�");
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
		shell.setText("�Ƽ�ϵͳ����ƽ̨");
		Menu menu = new Menu(shell, SWT.BAR);
		createMenu(menu);
		shell.setMenuBar(menu);
		
		TabFolder folder=new TabFolder(parent,SWT.NONE);
		TabItem data=new TabItem(folder,SWT.NONE);
		data.setText("���ݼ�������");
		
		
		TabItem tagrecommender=new TabItem(folder,SWT.NONE);
		tagrecommender.setControl(new TagRecommenderPanel(folder));
		tagrecommender.setText("��ǩ�Ƽ��㷨");
		
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
