package saturno.piano

import java.io.Serializable;

import saturno.anagrafica.Persona
import saturno.common.Scala
import saturno.organigramma.NodoOrganigramma

class Obiettivo implements Serializable{
	Integer id
	Integer anno
	String nome
	String descrizione
	String codiceCamera
	String codice
	String tipologia //area objstr ect.
	String note
	//in caso di clonazione
	//sorgente diretta: id obiettivo dal quale deriva direttamente
	//sorgente principale: id obiettivo che è stato il primo ad essere clonato
	Integer sorgenteDiretta
	Integer sorgentePrincipale
	//se sposto il padre in belongTo l'eliminazione del padre 
	//eliminerebbe anche i figli
	Obiettivo padre;
	
	Scala scala
	static belongsTo=[versione:Versione]
	
	
	//static belongsTo = [versione:Versione,padre:Obiettivo,sorgenteDiretta:Obiettivo,sorgentePrincipale:Obiettivo,scala:Scala]
	static hasMany = [figli:Obiettivo,responsabile:Persona,persone:Persona,nodiOrganigramma:NodoOrganigramma]
	Integer livello
	
	
	
	Double performance


	static mappedBy = [figli:'padre']
	
	static mapping = {
		version true
		id generator:'identity', column:'id_obiettivo', type:'integer'
		descrizione type: 'text'
		nome type:'text'
		note type:'text'
		
		
		
	}
}
