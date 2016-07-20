package pianificazione.controller

import grails.converters.JSON

import org.codehaus.groovy.grails.web.json.JSONObject

import saturno.piano.Versione

class PianificazioneController {
	def utilsService;
	def versioneService

	def index(){
	}
	
	def tree(){
	}
	
	def leftMenu(){
		
	}

	def testVersionExist(){
		
		def userObject = utilsService.currentUserObject()
		if (userObject == null){
			render status:500,text:'Errore, non trovato un utente valido'

			log.error("testVersionExist user object non trovato");
			return;
		}

		//estraggo l'anno del piano
		def anno = userObject.anno
		def ente = userObject.ente;
		log.debug("testVersionExist "+anno+" "+ente)
		if (anno == null){
			render  status: 500, text: 'Anno non trovato'
			return
		}
		if (ente==null){
			render status:500, text:'Ente non trovato'
			return
		}

		//controllo se esiste almeno una versione per quell'anno
		
		def piani = Versione.findAllByEnteAndAnno(ente,anno);
		def versione = new JSONObject();
		versione.versionePresente =false
		versione.versione=null;
		if (piani == null || piani.size()==0){
			render versione as JSON
			return

		}
		versione.versionePresente=true
		versione.versioni=piani
	
		render versione as JSON

	}
	
	//stampa l'albero del piano per essere renderizzato dal render template
	
	
	def stampaAlberoCompleto(){
		if (!utilsService.testTabId()){
			render status:500,text:'Identificativo della tab non valido, chiudere il browser e riprovare'
			return
		}
		def userObject = utilsService.currentUserObject()
		if (userObject==null){
			render status:500,text:'Utente non trovato'
			return;
		}
		def idVersione = request.JSON?.idVersione
		
		if (idVersione==null){
			render status:500,text:'Id versione non trovato'
			return;
		}
		
		if (idVersione != userObject.versione){
			render status:500,text:'L\'id versione trasmesso non coincide con quello in sessione'
			return;
		}
		
		Versione p = Versione.findById(idVersione)
		if (p == null){
			render status:500,text:'Versione non presente nel db'
			return;
		}
		JSONObject pianoJson = versioneService.stampaPianoObiettiviCompleto(p);
		render pianoJson as JSON
		
		
	}
	
	def creaNuovaVersione(){
		if (!utilsService.testTabId()){
			render status:500,text:'Identificativo della tab non valido, chiudere il browser e riprovare'
			return
		}
		def userObject = utilsService.currentUserObject()
		if (userObject==null){
			render status:500,text:'Utente non trovato'
			return;
		}
		
		Versione piano = request.JSON?.piano;
		if (piano == null){
			render status:500,text:'Piano non trovato'
			return
		}
		piano.setDtIniSist(new Date());
		piano.setDtFinSist(null);
		piano.setCreatoDa(userObject.currentUser?.userId)
		piano.save(true);
		
		render piano as JSON
		
		
		
	}
	
	def cancellaVersioneCorrente(){
		if (!utilsService.testTabId()){
			render status:500,text:'Identificativo della tab non valido, chiudere il browser e riprovare'
			return
		}
		def userObject = utilsService.currentUserObject()
		if (userObject==null){
			render status:500,text:'Utente non trovato'
			return;
		}
		def idVersione = request.JSON?.idVersione
		
		if (idVersione==null){
			render status:500,text:'Id versione non trovato'
			return;
		}
		
		if (idVersione != userObject.versione){
			render status:500,text:'L\'id versione trasmesso non coincide con quello in sessione'
			return;
		}
		
		Versione p = Versione.findById(idVersione)
		if (p == null){
			render status:500,text:'Versione non presente nel db'
			return;
		}
		def retObj = new JSONObject();
		retObj.nomeVersione = p.nomeVersione;
		p.delete(flush:true, failOnError:true);
		//per controllo, guardo che l'id versine ricevuto corrisponda a
		//quello presente nell'id utente in sessione.
		//Questi devono sempre coincidere
		render retObj as JSON
		
		
	}

	def prossimoPianoLibero(){
		def userObject = utilsService.currentUserObject()
		if (userObject == null){
			render status:500,text:'Errore, non trovato un utente valido'

			log.error("testVersionExist user object non trovato");
		}
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
	

		//recupero il prossimo codice versione per l'anno
		def piani = Versione.findAllByEnteAndAnno(ente, anno)
		Versione piano = null;
		if (piani == null || piani.size()==0){
			piano = new Versione()
			piano.setAnno(anno)
			piano.setVersione(1);
			piano.setEnte(ente);
			piano.setAperto(true);
			piano.setNomeVersione("Versione 1/"+anno);

		}else{
			Integer versione = piani.get(piani.size()-1).versione;
			versione = versione+1
		
			piano = new Versione();
			piano.setAnno(anno)
			piano.setVersione(versione);
			piano.setEnte(ente);
			piano.setAperto(true);
			piano.setNomeVersione("Versione "+piano.getVersione()+"/"+anno);
			

		}
		
		render piano as JSON



	}

}
