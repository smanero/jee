package org.pcdm.analizador.context.node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.pcdm.analizador.context.R02xConst;


public class R02xFile extends R02xNode {
	
	public String pack; 
	public List<R02xMethod> metodos = new ArrayList<R02xMethod>();
	
	public R02xFile(final R02xModule module, final File path) {
		super(module, path, R02xNodeType.FILE, 3);
	}

	public BufferedReader getBufferedReader() throws Exception {
		return new BufferedReader(new InputStreamReader(new FileInputStream(file), R02xConst.UTF8));
	}
}
