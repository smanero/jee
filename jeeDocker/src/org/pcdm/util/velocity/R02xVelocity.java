package org.pcdm.util.velocity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * Generador de archivos usando Velocity 1.5
 */
public class R02xVelocity {

	// PLANTILLA = "./resources/eclipse-classpath.vm";
	// OUT = "./out/VelocityTest.txt"
	// private Map<String, Object> propiedades = new HashMap<String, Object>();

	private static boolean init = false;

	public static void merge(final Map<String, Object> props, final String plantillaPath, final String outPath)
	      throws Exception {
		if (!init) {
			Velocity.init();
			init = true;
		}
		// elementos velocity
		VelocityContext vlcCtx = new VelocityContext(props);
		Template vlcTpl = Velocity.getTemplate(plantillaPath);
		// generacion fichero segun template
		if (null != vlcTpl) {
			File outFile = new File(outPath);
			BufferedWriter outWr = new BufferedWriter(new FileWriter(outFile, false));
			vlcTpl.merge(vlcCtx, outWr);
			outWr.close();
		}
	}
}
