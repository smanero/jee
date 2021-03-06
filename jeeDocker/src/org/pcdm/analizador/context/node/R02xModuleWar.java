package org.pcdm.analizador.context.node;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.pcdm.analizador.context.R02xConst;
import org.pcdm.util.velocity.R02xVelocity;


public class R02xModuleWar extends R02xModule {

	public R02xModuleWar(final R02xProfile profile, final String functionalName)  throws Exception {
		super(profile, new File(profile.getAbsolutePath() + File.separator + profile.getAppCode() + functionalName + "ProcessWar"));
		getFile().mkdir();
		R02xConst.copyDirectory(new File(R02xConst.ARQUETIPO_WAR), getFile());
		
		File baseMig = new File(getFile().getAbsolutePath() + File.separator + "/com/ejie/" + profile.getAppCode().toLowerCase());
		baseMig.mkdir();
		// ficheros template de proyecto eclipse
		final Map<String, Object> props = new HashMap<String, Object>(1);
		props.put("moduleName", moduleName);
		R02xVelocity.merge(props, R02xConst.ARQUETIPO_WAR + R02xConst.TPLT_ECLIPSE_PROJECT, 
				getFile().getAbsolutePath() + File.separator + ".project");
//		R02xVelocity.merge(props, R02xConst.ARQUETIPO_WAR + R02xConst.TPLT_ECLIPSE_MYMETADATA, 
//				getFile().getAbsolutePath() + File.separator + ".mymetadata");
		
		// ficheros para generacion de web-service.xml a partir de wsdl
		// se realiza en cada jws
	}
}
