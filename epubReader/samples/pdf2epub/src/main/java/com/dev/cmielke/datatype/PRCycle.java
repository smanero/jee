package com.dev.cmielke.datatype;

public class PRCycle extends Range<Integer>
{
	private String name;

	public PRCycle(Integer start, Integer end, String name)
	{
		super(start, end);
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
