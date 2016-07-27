package saturno.organigramma

import java.io.Serializable;

import saturno.anagrafica.Persona;
import saturno.common.Ente
import saturno.piano.Versione

class Organigramma implements Serializable{
	Integer id
	Ente ente
	String note
	Integer anno
	
	static belongsTo=[versione:Versione]
	static hasMany=[nodi:NodoOrganigramma]
	//sorgente diretta: il nodo da cui è stato clonato
		
	
	
	
	static mapping = {
		version true
		id generator:'identity', column:'id_organigramma', type:'integer'
		note type: 'text'
	}
}
