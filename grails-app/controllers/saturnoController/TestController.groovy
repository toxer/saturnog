package saturnoController

import grails.converters.JSON
import saturno.anagrafica.Persona
import saturno.common.Ente
import saturno.organigramma.NodoOrganigramma
import saturno.organigramma.Organigramma
import saturno.piano.LivelloCfg
import saturno.piano.Obiettivo
import saturno.piano.Versione
import saturno.piano.VersioneCfg

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

	def stampaConfigurazione(){
		VersioneCfg versioneCfg = new VersioneCfg();
		versioneCfg.ente= Ente.findAll().get(0)
		versioneCfg.setNumeroLivelli(5);

		LivelloCfg l0=new LivelloCfg();
		l0.setNomePlurale("Versioni");
		l0.setNomeSingolare("Versione");
		l0.setCodice("RT")
		l0.setColore("#F0190A");
		l0.setLivello(0)
		versioneCfg.addToLivelli(l0);


		LivelloCfg l1=new LivelloCfg();
		l1.setNomePlurale("Aree strategiche");
		l1.setNomeSingolare("Area strategica");
		l1.setCodice("AS")
		l1.setColore("#EB34CF");
		l1.setLivello(1)
		versioneCfg.addToLivelli(l1);

		LivelloCfg l2=new LivelloCfg();
		l2.setNomePlurale("Obiettivi strategici");
		l2.setNomeSingolare("Obiettivo strategico");
		l2.setCodice("OS")
		l2.setColore("#4CA9C0");
		l2.setLivello(2)
		versioneCfg.addToLivelli(l2);

		LivelloCfg l3=new LivelloCfg();
		l3.setNomePlurale("Obiettivi operativi");
		l3.setNomeSingolare("Obiettivo operativo");
		l3.setCodice("OO")
		l3.setColore("#D5CE29");
		l3.setLivello(3)
		versioneCfg.addToLivelli(l3);

		LivelloCfg l4=new LivelloCfg();
		l4.setNomePlurale("Programmi");
		l4.setNomeSingolare("Programma");
		l4.setCodice("PR")
		l4.setColore("#F19929");
		l4.setLivello(4)
		versioneCfg.addToLivelli(l4);

		LivelloCfg l5=new LivelloCfg();
		l5.setNomePlurale("Azioni");
		l5.setNomeSingolare("Azione");
		l5.setCodice("AZ")
		l5.setColore("D96722");
		l5.setLivello(5)
		versioneCfg.addToLivelli(l5);

		JSON.use("deep")
		render versioneCfg as JSON




	}


	private inserimentoMassivo(){
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


		for (def i = 0; i < 100; i++){

			Obiettivo uno = new Obiettivo();
			uno.setNome("Obiettivo uno "+i);
			uno.setDescrizione("Descrizione obiettivo "+i);
			uno.setCodiceCamera("codice "+i)
			uno.setAnno(p.getAnno());
			p.addToObiettivi(uno);
			uno.setLivello(1)
			
			Obiettivo due = new Obiettivo();
			due.setNome("Obiettivo due "+i);
			due.setDescrizione("Descrizione obiettivo child "+i);
			due.setCodiceCamera("codice "+i)
			due.setAnno(p.getAnno());
			due.setPadre(uno);
			due.setLivello(1)
			p.addToObiettivi(uno);
			p.addToObiettivi(due);
			println("Inserito obiettivo "+i)
		}
		p.save(true)
		render p as JSON

	}
}
