---------------------------------------------------------------
Estrategias seguidas
---------------------------------------------------------------

Herramienta automatizacion generacion genera proyecto WAR

Para cada proyecto WAR: 

1) autotype + wsdl2service : generacion de types.xml y web-services.xml a partir de un wsdl (path o url)
    Se trabaja directamente sobre el wsdl
    Se regeneran el types.xml, el jar y el web-services.xml mediante tareas ant 

2) source2wsdd + serviceGen para generar web-services.xml a partir de una clase java
    Partiendo del jws reescrito como clase java con comentarios wlsw en el WAR
    No se ha conseguido poner en funcionamiento
    <project name="wsgen.build" basedir=".." default="wsgen.build">
	    <property name="java_home" value="C:/bea/jdk142_11"/>
	    <property name="bea_lib" value="C:/bea/weblogic810/server/lib"/>
	    <property name="wl_classpath" value="${java_home}/lib/tools.jar;${wl_lib_home}/weblogic_sp.jar;${wl_lib_home}/weblogic.jar"/>
	    <property name="ws_gen_classpath" value="./WEB-INF/classes;${wl_classpath};${java_home}/jre/lib/rt.jar;${wl_lib_home}/webservices.jar"/>
	    
	    
	    <!-- TODO definir ${weblogic.classpath} con la ejecucion previa de SetEnv.cmd de Bea  -->
	    <taskdef name="source2wsdd" classpath='${ws_gen_classpath}' classname="weblogic.ant.taskdefs.webservices.autotype.JavaSource2DD"/>
	
	    <target name="wsgen.build">
	        <source2wsdd description="create web service descriptor and wsdl from source file" 
	            javaSource="./src/com/ejie/oce/Q99tOCEFacadeWS.java"
	         typesInfo="${build.gensrc.serverdd}/types.xml"          
	            ddFile="./WebRoot/WEB-INF/web-services.xml" 
	            serviceURI="/com/ejie/oce/Q99tOCEFacadeWS.jws" 
	            wsdlFile="./WebRoot/WEB-INF/Q99tOCEFacadeWS.wsdl">
	        </source2wsdd>
	    </target>
	</project>
	
	

3) autobuild para generar web-services.xml a partir de una clase java (simplificacion de source2wsdd)
    Partiendo del jws reescrito como wlsw en el WAR
    Ver lanzador.bat y autobuild-1.0 : No genera un wsdl compatible ni types.xml