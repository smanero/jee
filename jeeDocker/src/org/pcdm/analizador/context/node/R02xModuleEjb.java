package org.pcdm.analizador.context.node;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.pcdm.analizador.context.R02xConst;
import org.pcdm.util.velocity.R02xVelocity;


public class R02xModuleEjb extends R02xModule {

	public R02xModuleEjb(final R02xProfile profile, final String functionalName)  throws Exception {
		super(profile, new File(profile.getAbsolutePath() + File.separator + profile.getAppCode() + functionalName + "ProcessEJB"));
		getFile().mkdir();
		R02xConst.copyDirectory(new File(R02xConst.ARQUETIPO_EJB), getFile());
		
		File baseMig = new File(getFile().getAbsolutePath() + File.separator + "/com/ejie/" + profile.getAppCode().toLowerCase());
		baseMig.mkdir();
		// ficheros template de proyecto eclipse
		final Map<String, Object> props = new HashMap<String, Object>(1);
		props.put("moduleName", moduleName);
		R02xVelocity.merge(props, R02xConst.ARQUETIPO_EJB + R02xConst.TPLT_ECLIPSE_PROJECT, 
				getFile().getAbsolutePath() + File.separator + ".project");
//		R02xVelocity.merge(props, R02xConst.ARQUETIPO_EJB + R02xConst.TPLT_ECLIPSE_MYMETADATA, 
//				getFile().getAbsolutePath() + File.separator + ".mymetadata");
	}
}
