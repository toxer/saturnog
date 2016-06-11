package saturnoController

import grails.converters.JSON
import saturno.piano.Obiettivo
import saturno.piano.Piano

class TestController {
	def dataSource_portal
	public static final String APP_NAME="FEBE"
	def utilsService
	def versioneService
	
	
	def testClonazione(){
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
		Piano pianoFrom = Piano.findById(idVersione);
		
		if (pianoFrom ==null){
			render status:500,text:'Versione non esistente'
			return;
		}
		
		Piano pianoTo = new Piano();
		pianoTo.anno = pianoFrom.anno +1;
		pianoTo.setEnte(pianoFrom.ente);
		pianoTo.setNomeVersione(pianoTo.getNomeVersione()+"/bis");
		
		
		
	
		
		versioneService.clonaVersione(pianoFrom, pianoTo);
		render pianoTo as JSON
		
		
		
	}
	
	def test(){
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
		
		Piano p = Piano.findById(idVersione)
		if (p == null){
			render status:500,text:'Versione non presente nel db'
			return;
		}
		
		//inserisco dei dati di test nel piano
		
		Obiettivo uno = new Obiettivo();
		
		uno.setNome("Obiettivo uno");
		uno.setDescrizione("Descrizione obiettivo uno");
		uno.setAnno(p.getAnno());

		Obiettivo due = new Obiettivo();
		due.setNome("Obiettivo due");
		due.setDescrizione("Descrizione obiettivo due");
		due.setAnno(p.getAnno());
		
		Obiettivo tre = new Obiettivo();
		tre.setNome("Obiettivo tre");
		tre.setDescrizione("Descrizione obiettivo tre");
		tre.setAnno(p.getAnno());
		
		Obiettivo quattro = new Obiettivo();
		quattro.setNome("Obiettivo quattro");
		quattro.setDescrizione("Descrizione obiettivo quattro");
		quattro.setAnno(p.getAnno());
		
		
		uno.addToFigli(tre)		
		due.addToFigli(quattro)
		p.addToObiettivi(uno);
		p.addToObiettivi(due);
		p.addToObiettivi(tre);
		p.addToObiettivi(quattro);
		p.save(true);
		render p as JSON	
	}
}
