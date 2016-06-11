package saturnoservice

import java.util.Date;

import saturno.anagrafica.Organigramma
import saturno.anagrafica.Persona
import saturno.common.Ente;
import saturno.piano.Obiettivo
import saturno.piano.Piano

class VersioneService {

	def clonaVersione(Piano versioneSorgente,Piano versioneDestinazione){
		//clono l'organigramma perchè poi mi serve nella clonazione dei figli
		Organigramma nuovoOrganigramma = clonaOrganigramma(versioneDestinazione.organigramma, versioneDestinazione)
		
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


	def Obiettivo clonaObiettivo(Obiettivo o,Piano versioneSuCuiClonare,Organigramma nuovoOrganigramma){
		Obiettivo newO = new Obiettivo();
		newO.anno= versioneSuCuiClonare.anno;
		newO.nome=o.nome;
		newO.codice=o.codice;
		newO.codiceCamera=o.codiceCamera;
		newO.tipologia=o.tipologia;
		newO.note=o.note;
		newO.scala=o.scala;
		for (Persona persona : o.persone){
			newO.addToPersone(persona);
		}
		
		//l'organigramma va clonato e riassociato
		//in modo da consentirne la modifica per singola versione
		
		//attacco al nuovo obiettivo il nodo organigramma
		//derivato dalla clonazione di quello che era attaccato
		//al vecchio obiettivo
		for (Organigramma org : o.organigrammi){
			for (Organigramma nOrg : nuovoOrganigramma){
				if (org.equals(nOrg.sorgenteDiretta)){
					o.addToOrganigrammi(nOrg);
				}
			}
		}
		
		//sorgente diretta: obiettivo dal quale deriva direttamente
		//sorgente principale: obiettivo che è stato il primo ad essere clonato	
		if (o.sorgentePrincipale==null){
			newO.sorgentePrincipale=o;
		}else{
			newO.sorgentePrincipale=o.sorgentePrincipale;
		}
		o.sorgenteDiretta=o;
		versioneSuCuiClonare.addToObiettivi(newO);
		//per tutti i figli applico il processo di clonazione
		//in modo ricorsivo


		for (Obiettivo c : o.figli){
			Obiettivo newC = clonaObiettivo(c,versioneSuCuiClonare);
			newO.addToFigli(newC);
		}
		return newO


	}
	
	
	def Organigramma clonaOrganigramma(Organigramma organigramma,Piano versioneSuCuiClonare){
		Organigramma newOrg = new Organigramma()
	
		newOrg.nome=organigramma.ente
		newOrg.descrizione=organigramma.descrizione
		newOrg.dataInizioValidita=organigramma.dataInizioValidita
		newOrg.dataFineValidida=organigramma.dataFineValidida
		newOrg.ente=organigramma.ente
		versioneSuCuiClonare.addToOrganigrammi(newOrg);
		newOrg.sorgenteDiretta=organigramma;
		for (Organigramma c : newOrg.figli){
			Organigramma newOrgC = clonaOrganigramma(c,versioneSuCuiClonare);
			newOrg.addToFigli(c);
		}
		
		return newOrg;
		
		
	}


}
