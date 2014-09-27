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
 *    TagFileLoader.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.model.loader;

import java.io.IOException;

import Rsme.model.Folksonomy;

public abstract class TagFileLoader
																		extends FileLoader
{
	/*---------------------------members---------------------------------------*/
	/** the source file contain the tag id and tag name */
	protected String m_tagsFile;
	
	
	/*------------------------constructor--------------------------------------*/
	public TagFileLoader()
	{
		super();
		m_tagsFile = null;
	}
	
	
	/*----------------------public functions------ ----------------------------*/
	public void setSource(String preferencesFile, String tagsFile)
			throws IOException
	{
		m_preferencesFile = preferencesFile;
		m_tagsFile=tagsFile;
		
	}
	
	
	/*------------------------abstract functions--------------------------------*/
	public abstract Folksonomy getFolksonomy() throws Exception;
}
