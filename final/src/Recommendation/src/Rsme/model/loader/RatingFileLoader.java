package Rsme.model.loader;

import Rsme.model.RatingDataModel;

public abstract class RatingFileLoader extends FileLoader
{
	
	public RatingFileLoader()
	{
		super();
	}
	abstract RatingDataModel getRatingData() throws Exception;
}
