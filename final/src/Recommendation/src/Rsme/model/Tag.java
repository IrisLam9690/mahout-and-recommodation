/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA.
 */

/*
 *    Tag.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.model;

/**
 * class that represents a tag
 * 
 * @author Bin Fu (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$
 * 
 */

public class Tag
{
	/*---------------------------members---------------------------------------*/
	/**tag's Id*/
	private int m_id;
	
	/**tag's name*/
	private String m_name;
	
	
	/*------------------------constructor--------------------------------------*/
	public Tag(int id, String name)
	{
		m_id = id;
		m_name = name;
	}
	
	public Tag(Tag tag)
	{
		m_id=tag.m_id;
		m_name=new String(tag.m_name);
	}
	
	/*------------------------public functions----------------------------------*/
	public int getId()
	{
		return m_id;
	}
	
	
	public String getName()
	{
		return m_name;
	}
	
	
	public void setName(String name)
	{
		m_name = name;
	}
}
