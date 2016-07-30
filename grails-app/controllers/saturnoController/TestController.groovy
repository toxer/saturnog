package saturnoController

import grails.converters.JSON
import saturno.anagrafica.Persona
import saturno.organigramma.NodoOrganigramma
import saturno.organigramma.Organigramma
import saturno.piano.Obiettivo
import saturno.piano.Versione

class TestController {
	def dataSource_portal
	public static final String APP_NAME="FEBE"
	def utilsService
	def versioneService
	
	
	def prova(){
		def o = Obiettivo.findAll().get(0);
		render o as JSON
	}
	
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
		Versione pianoFrom = Versione.findById(idVersione);
		
		if (pianoFrom ==null){
			render status:500,text:'Versione non esistente'
			return;
		}
		
		Versione pianoTo = new Versione();
		pianoTo.anno = pianoFrom.anno;
		pianoTo.setEnte(pianoFrom.ente);
		pianoTo.setNomeVersione(pianoTo.getNomeVersione()+"/bis");
		
		
		
	
		
		versioneService.clonaVersione(pianoFrom, pianoTo);
		render pianoTo as JSON
		
		
		
	}
	
	def deletePersonTest(){
		Persona p = Persona.findByNome("Mario");
		p.delete(flush:true, failOnError:true);
		render "";
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
		
		Versione p = Versione.findById(idVersione)
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
		
		Obiettivo cinque = new Obiettivo();
		cinque.setNome("Obiettivo cinque");
		cinque.setDescrizione("Descrizione obiettivo cinque");
		cinque.setAnno(p.getAnno());
		
		Obiettivo sei = new Obiettivo();
		sei.setNome("Obiettivo sei");
		sei.setDescrizione("Descrizione obiettivo sei");
		sei.setAnno(p.getAnno());
		
		uno.addToFigli(tre)		
		tre.addToFigli(sei);
		due.addToFigli(quattro)
		due.addToFigli(cinque);
		
		
		//persone
		Persona p1 = new Persona();
		p1.nome="Mario"
		p1.cognome="Rossi";
		p1.ente=p.ente;
		
		Persona p2 = new Persona();
		p2.nome="Giacomo"
		p2.cognome="Leopardi";
		p2.ente=p.ente;
		
		p1.save();
		p2.save(true);
		
		Organigramma o1 = new Organigramma();
		o1.ente=p.ente
		
		NodoOrganigramma nodo1 = new NodoOrganigramma();
		nodo1.nome="Area1";
		nodo1.addToPersone(p1);
		o1.addToNodi(nodo1);
		
		NodoOrganigramma nodo2 = new NodoOrganigramma();
		nodo2.nome="Area2";
		nodo2.addToPersone(p2);
		o1.addToNodi(nodo2);
		
		nodo1.addToFigli(nodo2)
		
		uno.addToNodiOrganigramma(nodo1);
		due.addToNodiOrganigramma(nodo2);
		
		
		
		
		p.addToObiettivi(uno);
		p.addToObiettivi(due);
		p.addToObiettivi(tre);
		p.addToObiettivi(quattro);
		p.addToObiettivi(cinque);
		p.addToObiettivi(sei);
		p.organigramma=o1;
		p.save(true);
		render p as JSON	
	}
}
