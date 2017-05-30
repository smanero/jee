package com.dev.cmielke.datatype;

public class Collection
{
	@SuppressWarnings("unchecked")
	public static <S extends Comparable> PRCycle PRCycle( Integer from, Integer to, String name )
	{
		return new PRCycle( from, to, name );
	}
}
