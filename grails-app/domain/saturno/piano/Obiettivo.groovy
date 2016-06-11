package saturno.piano

import saturno.anagrafica.Organigramma
import saturno.anagrafica.Persona
import saturno.common.Scala

class Obiettivo {
	Integer id
	Integer anno
	String nome
	String descrizione
	String codiceCamera
	String codice
	String tipologia //area objstr ect.
	String note
	//in caso di clonazione
	//sorgente diretta: obiettivo dal quale deriva direttamente
	//sorgente principale: obiettivo che è stato il primo ad essere clonato

	static belongsTo = [piano:Piano,padre:Obiettivo,sorgenteDiretta:Obiettivo,sorgentePrincipale:Obiettivo,scala:Scala]
	static hasMany = [figli:Obiettivo,responsabile:Persona,persone:Persona,organigrammi:Organigramma]
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
