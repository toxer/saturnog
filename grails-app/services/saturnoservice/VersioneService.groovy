package saturnoservice

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import saturno.anagrafica.Persona
import saturno.organigramma.NodoOrganigramma
import saturno.organigramma.Organigramma
import saturno.piano.Obiettivo
import saturno.piano.Versione

class VersioneService {

	def clonaVersione(Versione versioneSorgente,Versione versioneDestinazione){
		//clono l'organigramma perchè poi mi serve nella clonazione dei figli
		Organigramma nuovoOrganigramma = null;
		if (versioneSorgente.organigramma != null){
			 nuovoOrganigramma = clonaOrganigramma(versioneSorgente.organigramma, versioneDestinazione)
		}
		versioneDestinazione.organigramma=nuovoOrganigramma
		//prelevo tutti gli obiettivi della versione sorgente
		//che non hanno padre. Il processo di clonazione
		//ricorsiva clona poi i figli

		for (Obiettivo o : versioneSorgente.obiettivi.findAll({it.padre==null})){
			clonaObiettivo(o,versioneDestinazione,nuovoOrganigramma);
		}
		versioneDestinazione.save(true);




	}




	//clonazione ricorsia degli obiettivi

	//FIXME clonazione kpi
	//FIXME clonazione organigramma e anangrafiche
	//FIXME clonazione scale




	def Obiettivo clonaObiettivo(Obiettivo o,Versione versioneSuCuiClonare,Organigramma nuovoOrganigramma){
		Obiettivo newO = new Obiettivo();
		newO.anno= versioneSuCuiClonare.anno;
		newO.nome=o.nome;
		newO.codice=o.codice;
		newO.codiceCamera=o.codiceCamera;
		newO.tipologia=o.tipologia;
		newO.note=o.note;
		newO.scala=o.scala;
		newO.livello=o.livello;
		for (Persona persona : o.persone){
			newO.addToPersone(persona);
		}

		//l'organigramma va clonato e riassociato
		//in modo da consentirne la modifica per singola versione

		//attacco al nuovo obiettivo il nodo organigramma
		//derivato dalla clonazione di quello che era attaccato
		//al vecchio obiettivo

		if (nuovoOrganigramma!=null){
			for (NodoOrganigramma org : o.nodiOrganigramma){
				for (NodoOrganigramma nOrg : nuovoOrganigramma.nodi){
					if (org.equals(nOrg.sorgenteDiretta)){
						o.addToNodiOrganigramma(nOrg);
					}
				}
			}
		}
		//sorgente diretta: obiettivo dal quale deriva direttamente
		//sorgente principale: obiettivo che è stato il primo ad essere clonato
		if (o.sorgentePrincipale==null){
			newO.sorgentePrincipale=o.id;
		}else{
			newO.sorgentePrincipale=o.sorgentePrincipale;
		}
		newO.sorgenteDiretta=o.id;
		versioneSuCuiClonare.addToObiettivi(newO);
		//per tutti i figli applico il processo di clonazione
		//in modo ricorsivo


		for (Obiettivo c : o.figli){
			Obiettivo newC = clonaObiettivo(c,versioneSuCuiClonare,nuovoOrganigramma);
			newO.addToFigli(newC);
		}
		return newO


	}


	def Organigramma clonaOrganigramma(Organigramma organigramma,Versione versioneSuCuiClonare){
		Organigramma newOrg = new Organigramma()
		newOrg.anno = versioneSuCuiClonare.anno;
		newOrg.note = organigramma.note;

		//prelevo tutti i nodi senza padre
		//riocorsivamente il metodo clona i figli


		for (NodoOrganigramma o : organigramma.nodi.findAll({it.padre==null})){
			clonaNodoOrganigramma(o,newOrg);
		}

		return newOrg;


	}

	private NodoOrganigramma clonaNodoOrganigramma(NodoOrganigramma nodo,Organigramma organigrammaSuCuiClonare){
		NodoOrganigramma newNodo = new NodoOrganigramma();
		newNodo.codiceCamera = nodo.codiceCamera;
		newNodo.nome=nodo.nome
		newNodo.descrizione=nodo.descrizione;
		newNodo.livello=nodo.livello;
		newNodo.dataInserimento=nodo.dataInserimento
		newNodo.dataModifica=nodo.dataModifica
		newNodo.sorgenteDiretta = nodo.id
		for (Persona p : nodo.persone){
			newNodo.addToPersone(p)
		}
		organigrammaSuCuiClonare.addToNodi(newNodo);
		for (NodoOrganigramma c :nodo.figli){
			NodoOrganigramma newC = clonaNodoOrganigramma(c,organigrammaSuCuiClonare);
			newNodo.addToFigli(newC);
		}
		println("Clonazione organigramma terminata")
		
		return newNodo;

	}

	
	def stampaPianoObiettiviCompleto(Versione v){
		JSONObject versione=new JSONObject();
		versione.name = v.nomeVersione;
		versione.children = new JSONArray();
		
		//scorro solo gli obiettivi senza padre, il metodo stampaObiettivo
		//farà il resto con la ricorsione
		for (Obiettivo o : v.obiettivi.findAll({it.padre==null})){
			JSONObject obj = new JSONObject()
			versione.children.add(stampaObiettivo(o,null));
		}
		return versione
		
	}
	
	private JSONObject stampaObiettivo (Obiettivo o,JSONObject obj){
		if (obj == null){
			obj = new JSONObject();
			
		}
		obj.name=o.nome;
		obj.children = new JSONArray();
		
		for (Obiettivo c : o.figli){
			JSONObject objc = new JSONObject()
			stampaObiettivo (c,objc)
			obj.children.add(objc);
		}
		return obj;
	}

}
