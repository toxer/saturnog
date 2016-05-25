package pianificazione.controller

import grails.converters.JSON

import org.codehaus.groovy.grails.web.json.JSONObject

import saturno.piano.Piano

class PianificazioneController {
	def utilsService;
	
	def index(){
		
	}
	
	def testVersionExist(){
		def userObject = utilsService.currentUserObject()
		if (userObject == null){
			render status:500,text:'Errore, non trovato un utente valido'
			
			log.error("testVersionExist user object non trovato");			
		}
		
		//estraggo l'anno del piano
		def anno = userObject.anno
		def ente = userObject.ente;
		if (anno == null){
			render  status: 500, text: 'Anno non trovato'
			return
		}
		if (ente==null){
			render status:500, text:'Ente non trovato'
			return
		}
		
		//controllo se esiste almeno una versione per quell'anno
		Piano p = Piano.findByEnteAndAnno(ente,anno);
		def versione = new JSONObject();
		versione.versionePresente =false
		versione.versione=null;
		if (p == null){
			render versione as JSON
			return
			
		}
		versione.versionePresente=true
		versione.versione=p
		render versione as JSON		
		
	}

}
