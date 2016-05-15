package saturnoController

import grails.converters.JSON

import org.codehaus.groovy.grails.web.json.JSONArray

import saturno.common.Ente
import saturno.piano.Piano

class CambiaAnnoController {
	def utilsService;

	def index(){

		render view:"index"
	}


	def getAnni(){
		def currentUserObject = utilsService.currentUserObject();
		if (currentUserObject==null){
			log.error("getAnni currentUserObject not found");
			render new JSONArray() as JSON
		}
		//prelevo tutti gli anni
		def piani = Piano.findAllByEnte(Ente.findById(currentUserObject.currentEnte));
		def anni = []

		if (piani != null){
			piani.each{
				anni.add(it.anno);
			}
		}

		def currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = 0; i < 3 ; i++){
			if (!anni.contains(currentYear+i)){
				anni.add(currentYear+i);
			}
		}
		anni = anni.sort();

		render anni as JSON
	}
}


