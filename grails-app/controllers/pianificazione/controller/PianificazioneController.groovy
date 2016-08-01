package pianificazione.controller

import grails.converters.JSON

import org.codehaus.groovy.grails.web.json.JSONObject

import saturno.piano.LivelloCfg
import saturno.piano.Obiettivo
import saturno.piano.Versione
import saturno.piano.VersioneCfg

class PianificazioneController {
	def utilsService;
	def versioneService
	def grailsApplication

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

		configuratore(piano.ente,piano.anno)

		render piano as JSON



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

	def aggiungiNodo(){
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

		Obiettivo obiettivoDominio = new Obiettivo();
		obiettivoDominio.versione = Versione.findById(idVersione)
		if (obiettivoDominio.versione == null){
			render status:500,text:'Versione non trovata per id '+idVersione
			return
		}
		if (obiettivo?.idParent != null){
			Obiettivo parent = Obiettivo.findById(obiettivo?.idParent);
			if (parent == null){
				render status:500,text:'Padre non identificato'
				return
			}
			if (parent.versione != obiettivoDominio.versione){
				render status:500,text:'Versione del padre non corrispondente a quella del figlio'
				return;
			}
			obiettivoDominio.setPadre(parent);

		}

		obiettivoDominio.nome=obiettivo.titolo
		obiettivoDominio.descrizione=obiettivo.descrizione
		obiettivoDominio.codiceCamera=obiettivo.codiceCamera
		obiettivoDominio.livello = obiettivoDominio.padre!=null?obiettivoDominio.padre.livello+1:0;
		obiettivoDominio.save(true);


		JSONObject pianoJson = versioneService.stampaPianoObiettiviCompleto(obiettivoDominio.versione);
		def resp = new JSONObject();
		resp.pianoJson = pianoJson;
		resp.piano = obiettivoDominio.versione
		resp.nuovoId = obiettivoDominio.id;

		render resp as JSON




		//per sicurezza non mappo direttamente il JSON nell'oggetto di dominio,
		//ma lo creo con le informaizoni ricevute.

	}

}
