package pianificazione.controller

import grails.converters.JSON

import org.codehaus.groovy.grails.web.json.JSONObject

import saturno.common.Ente
import saturno.piano.LivelloCfg
import saturno.piano.Obiettivo
import saturno.piano.Versione
import saturno.piano.VersioneCfg

class PianificazioneController {

	def utilsService;
	def versioneService
	def obiettivoService;
	def grailsApplication
	def sessionFactory
	def index(){
	}

	def treeStandard(){
	}

	def bsc(){
	}

	def performance(){
	}

	def leftMenu(){
	}

	def noTree(){}

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
			//JSON.use("deep")
			render versione as JSON
			return

		}
		versione.versionePresente=true
		versione.versioni=piani
	//	JSON.use("deep")
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

		//conrollo che poteva causare errori perchè il settaggio dell'user object
		//viene fatto asincronamente.

		//viene lasciato in fase di salvataggio dei dati

		//		if (idVersione != userObject.versione){
		//			render status:500,text:'L\'id versione trasmesso non coincide con quello in sessione'
		//			return;
		//		}

		Versione p = Versione.findById(idVersione)
		if (p == null){
			render status:500,text:'Versione non presente nel db'
			return;
		}
		JSONObject pianoJson;
		try{
			pianoJson = versioneService.stampaPianoObiettiviCompleto(p);
		}
		catch (Exception exc){
			exc.printStackTrace();
			render status:500,text:'Errore nella stampa dell\'albero'
			return
		}
		def piano = new JSONObject();
		piano.pianoJson = pianoJson
		piano.configuratore = VersioneCfg.findByEnteAndAnno(p.ente,p.anno)

		if (piano.configuratore == null){
			log.debug("Creato il configuratore")
			piano.configuratore=configuratore(p.ente,p.anno);
		}


		//JSON.use("deep")
		render piano as JSON


	}





	def clonaVersione(){
		if (!utilsService.testTabId()){
			render status:503,text:'Identificativo della tab non valido, chiudere il browser e riprovare'
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
		//creo una nuova versione
		Versione pianoFrom = Versione.findById(idVersione);

		if (pianoFrom ==null){
			render status:500,text:'Versione non esistente'
			return;
		}

		Versione pianoTo = Versione.create();
		pianoTo.anno = pianoFrom.anno;
		pianoTo.setEnte(Ente.findById(pianoFrom.ente.id));
		pianoTo.setNomeVersione(pianoFrom.getNomeVersione()+"/bis");
		versioneService.clonaVersione(pianoFrom, pianoTo);
		//JSON.use("deep")
		render pianoTo as JSON
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

		def versioneRicevuta = request.JSON?.piano;
		Versione piano =Versione.create();

		if (versioneRicevuta.anno==null || versioneRicevuta.ente==null || versioneRicevuta.id!=null){
			render status:500,text:"Errore nella creazione della versione"
			return
		}


		if (piano == null){
			render status:500,text:'Piano non trovato'
			return
		}
			
		piano.setDtIniSist(new Date());
		piano.setDtFinSist(null);
		piano.setNomeVersione(versioneRicevuta.nomeVersione)
		piano.setVersione(versioneRicevuta.versione)
		piano.setAnno(versioneRicevuta.anno)
		piano.setEnte(Ente.findById(userObject.ente.id))
		piano.setNote(versioneRicevuta.note)
		piano.setAperto(true)
		piano.setCreatoDa(userObject.currentUser?.userId)

		def v = versioneService.creaNuovaVersione(piano);





	//	JSON.use("deep")
		render v as JSON
		



	}


	private configuratore(ente,anno){
		//controllo se esiste un configuratore per l'anno
		def versioneCfg=VersioneCfg.findByEnteAndAnno(ente,anno);
		if (versioneCfg == null){
			//salvo l'ultima versione presente per l'ente nell'anno corrente
			def versioni = VersioneCfg.findAllByEnte(ente)
			if (versioni != null && versioni.size()>0){
				versioneCfg = versioni.get(0)
			}
			//se nullo prendo l'oggetto di default nelle properties
			if (versioneCfg==null){
				versioneCfg = new VersioneCfg(JSON.parse(grailsApplication.config.grails.standardConfiguration))
			}


			if (versioneCfg != null){
				VersioneCfg nuovaVersione = new VersioneCfg();
				nuovaVersione.anno = anno;
				nuovaVersione.ente = ente;
				nuovaVersione.numeroLivelli=versioneCfg.numeroLivelli
				for (LivelloCfg l : versioneCfg.livelli){
					LivelloCfg nuovoLivello = new LivelloCfg();
					nuovoLivello.setCodice(l.codice)
					nuovoLivello.setNomePlurale(l.nomePlurale)
					nuovoLivello.setNomeSingolare(l.nomeSingolare);
					nuovoLivello.setColore(l.colore);
					nuovoLivello.setLivello(l.livello)
					nuovaVersione.addToLivelli(nuovoLivello)
				}
				nuovaVersione.save(true);
			}

		}
		return versioneCfg
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
		versioneService.cancellaVersione(p);
		//per controllo, guardo che l'id versine ricevuto corrisponda a
		//quello presente nell'id utente in sessione.
		//Questi devono sempre coincidere
		//JSON.use("deep")
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

		//JSON.use("deep")
		println(piano)
		render piano as JSON
	}

	def mostraObiettivo(){


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
		if (idVersione == null){
			render status:500,text:'Versione non presente nel db'
			return;
		}
		if (idVersione != userObject.versione){
			render status:500,text:'L\'id versione trasmesso non coincide con quello in sessione'
			return;
		}
		def obiettivo = request.JSON?.obiettivo;
		if (obiettivo == null){
			render status:500,text:'Non inviato obiettivo'
			return;
		}
		if (obiettivo.id==null){
			render status:500,text:'Non inviato obiettivo con id'
			return;
		}
		obiettivo = Obiettivo.findById(obiettivo.id)
		if (obiettivo == null){
			render status:500,text:'Non trovato nessun obiettivo'
			return;
		}
		//JSON.use("deep")
		render obiettivo as JSON
	}


	def salvaObiettivo(){
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
		if (idVersione == null){
			render status:500,text:'Versione non presente nel db'
			return;
		}
		if (idVersione != userObject.versione){
			render status:500,text:'L\'id versione trasmesso non coincide con quello in sessione'
			return;
		}
		def obiettivo = request.JSON?.obiettivo;
		if (obiettivo == null){
			render status:500,text:'Non inviato obiettivo'
			return;
		}
		Obiettivo obiettivoDominio = Obiettivo.findById(obiettivo.id)
		if (obiettivoDominio == null){
			render status:500,text:'Non trovato l\'obiettivo nel db'
			return;
		}

		obiettivoDominio.nome=obiettivo.nome
		obiettivoDominio.descrizione=obiettivo.descrizione
		obiettivoDominio.codiceCamera=obiettivo.codiceCamera
		obiettivoDominio.note=obiettivo.note
		obiettivoDominio = obiettivoService.saveObj(obiettivoDominio)
		JSONObject pianoJson = versioneService.stampaPianoObiettiviCompleto(obiettivoDominio.versione);
		def resp = new JSONObject();
		resp.pianoJson = pianoJson;
		resp.piano = obiettivoDominio.versione
		resp.nuovoId = obiettivoDominio.id;
		//JSON.use("deep")
		render resp as JSON



	}
	
	
	


	def aggiungiObiettivo(){
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
		if (idVersione == null){
			render status:500,text:'Versione non presente nel db'
			return;
		}
		if (idVersione != userObject.versione){
			render status:500,text:'L\'id versione trasmesso non coincide con quello in sessione'
			return;
		}
		def obiettivo = request.JSON?.obiettivo;
		if (obiettivo == null){
			render status:500,text:'Non inviato obiettivo'
			return;
		}

		Obiettivo obiettivoDominio = new Obiettivo( request?.JSON.obiettivo);



		obiettivoDominio.versione = Versione.findById(idVersione)


		if (obiettivoDominio.versione == null){
			render status:500,text:'Versione non trovata per id '+idVersione
			return
		}



		if (obiettivo?.padre?.id != null){
			Obiettivo parent = Obiettivo.findById(obiettivo?.padre.id);
			if (parent == null){
				render status:500,text:'Padre non identificato'
				return
			}
			if (parent.versione != obiettivoDominio.versione){
				render status:500,text:'Versione del padre non corrispondente a quella del figlio'
				return;
			}
			obiettivoDominio.setPadre(parent);

		}else{
			obiettivoDominio.setPadre(null)
		}

		obiettivoDominio.livello = obiettivoDominio.padre!=null?obiettivoDominio.padre.livello+1:0;
		obiettivoDominio.anno = obiettivoDominio.versione.anno
		obiettivoDominio = obiettivoService.saveObj(obiettivoDominio)


		JSONObject pianoJson = versioneService.stampaPianoObiettiviCompleto(obiettivoDominio.versione);
		def resp = new JSONObject();
		resp.pianoJson = pianoJson;
		resp.piano = obiettivoDominio.versione
		resp.nuovoId = obiettivoDominio.id;
		//JSON.use("deep")
		render resp as JSON






	}
	
	
	def eliminaObiettivo(){
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
		if (idVersione == null){
			render status:500,text:'Versione non presente nel db'
			return;
		}
		if (idVersione != userObject.versione){
			render status:500,text:'L\'id versione trasmesso non coincide con quello in sessione'
			return;
		}
		def idObiettivo = request.JSON?.idObiettivo;
		if (idObiettivo == null){
			render status:500,text:'Non inviato l\'id obiettivo'
			return;
		}
		Obiettivo obiettivoDominio = Obiettivo.findById(idObiettivo)
		if (obiettivoDominio == null){
			render status:500,text:'Non trovato l\'obiettivo nel db'
			return;
		}
		
		if (obiettivoDominio.versione.id!= idVersione){
			render status:500,text: 'Versione dell\'obiettivo non corrispondente alla versione attuale'
		}
		
		JSONObject resp = new JSONObject();
		resp.nome = obiettivoDominio.nome;
		resp.id = obiettivoDominio.id;
		resp.codice = obiettivoDominio.codiceCamera;
		resp.parentId=obiettivoDominio.padre?.id
		
		
		obiettivoDominio = obiettivoService.deleteObj(obiettivoDominio);
		
		resp.piano = obiettivoDominio.versione
		resp.pianoJson =  versioneService.stampaPianoObiettiviCompleto(resp.piano);
		
		

		
		//JSON.use("deep")
		render resp as JSON



	}




}
