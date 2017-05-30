package org.dsc.db.reporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dsc.db.model.CrSchema;
import org.dsc.db.model.CrTable;
import org.dsc.db.model.CrView;


/**
 * 1 archivo png por schema
 * 1 archivo png por tabla
 * @author bimalose
 * cmd: dot -Tpng schema.dot -o schema.png
 * FILE schema.dot: 
		digraph schema {
		 node [shape=record];
		 struct1 [shape=record,label="<f0> left|<f1> middle|<f2> right"];
		 struct2 [shape=record,label="<f0> one|<f1> two"];
		 struct3 [shape=record,label="hello\nworld |{ b |{c|<here> d|e}| f}| g | h"];
		 struct1:f1 -> struct2:f0;
		 struct1:f2 -> struct3:here;
		}
 */
public final class DotReporter extends AbstractReporter {
	private List<String> relations = new ArrayList<String>();
	
//	private static final String RANKSEP = "ranksep=.1";
//	private static final String NODESEP = "nodesep=.1";
	private static final String RATIO = "ratio=fill";
	private static final String SHAPE = "shape=record";
	private static final String FONTSIZE = "fontsize=24";
	
	public DotReporter(final String outDir, final String schemaName) throws Exception {
		super(outDir, schemaName);
	}
	
	public void generate(final CrSchema crschema) throws Exception {
		FileOutputStream fos = null;
		PrintStream schemaOut = null;
		try {
			String dotFile = outputFile.getAbsolutePath() + File.separator + schemaName + ".dot";
			File outFile = new File(dotFile);
			fos = new FileOutputStream(outFile);
			schemaOut = new PrintStream(fos);
			
			schemaOut.println("digraph " + schemaName + " {");
			schemaOut.println(IReporter.TAB + RATIO + "; node [" + SHAPE + ", " + FONTSIZE + "];");
			
			// diagrama de cada tabla
			List<String> tableList = new ArrayList<String>(crschema.tables.keySet());
			Collections.sort(tableList);
			for (String tableName : tableList) {
				CrTable crtable = (CrTable) crschema.tables.get(tableName);
				
				generateTable(crtable);
				schemaOut.println(IReporter.TAB + crtable.name + " [label=\"<" + crtable.name + "> " + crtable.fullName() 
						+ "\", URL=\"" + crtable.name + ".html\"];");
			}
			for (String relation : relations) {
				schemaOut.println(relation);
			}
			
			schemaOut.println("}");
			
			String pngFile = outputFile.getAbsolutePath() + File.separator + schemaName + ".png";
//			Process p = Runtime.getRuntime().exec("dot -Tpng " + dotFile + " -o " + pngFile);
			Runtime.getRuntime().exec("dot -Tcmap,png " + dotFile + " -o " + pngFile);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (null != schemaOut) schemaOut.close();
			if (null != fos) fos.close();			
		}
	}

	// TABLES
	private void generateTable(final CrTable crtable) throws Exception {
		FileOutputStream fos = null;
		PrintStream out = null;
		try {
			String dotFile = outputFile.getAbsolutePath() + File.separator + crtable.name + ".dot";
			File outFile = new File(dotFile);
			fos = new FileOutputStream(outFile);
			out = new PrintStream(fos);
			
			out.println("digraph " + crtable.name + " {");
			out.println(IReporter.TAB + RATIO + "; node [" + SHAPE + ", " + FONTSIZE + "];");
			out.println(IReporter.TAB + crtable.name + " [label=\"<" + crtable.name + "> " + crtable.fullName() + "\"];");
			
			for (String fkTable : crtable.relations.keySet()) {
				out.println(IReporter.TAB + fkTable + " [label=\"<" + fkTable + "> " + fkTable
						+ "\", URL=\"" + fkTable + ".html\"];");
			}
			for (String fkTable : crtable.relations.keySet()) {
				List<String> rel = (ArrayList<String>) crtable.relations.get(fkTable);
				String relation = IReporter.TAB + crtable.name + ":" + crtable.name + " -> " + fkTable + ":" + fkTable + " [label=\"" + rel + "\"];";
				out.println(relation);
				this.relations.add(relation);
			}
			out.println("}");
			
			String pngFile = outputFile.getAbsolutePath() + File.separator + crtable.name + ".png";
//			Process p = Runtime.getRuntime().exec("dot -Tpng " + dotFile + " -o " + pngFile);
			Runtime.getRuntime().exec("dot -Tcmap,png " + dotFile + " -o " + pngFile);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (null != out) out.close();
			if (null != fos) fos.close();			
		}
	}

	// VIEWS
	private void generateView(final CrView crview) throws Exception {
		List<String>[] rdo = new ArrayList[2];
		// entidades
		rdo[0] = new ArrayList<String>();
		// relaciones
		rdo[1] = new ArrayList<String>();

		File outFile = new File(outputFile.getAbsolutePath() + File.separator + crview.name + ".dot");
		FileOutputStream fos = new FileOutputStream(outFile);
		PrintStream out = new PrintStream(fos);
		out.println("digraph " + crview.name + " {");
		out.println(IReporter.TAB + "node [shape=record];");
		out.println(IReporter.TAB + "table [label=\"<" + crview.name + "> " + crview.fullName() + "\"];");
		
		out.println("}");
		out.close();
	}
}

