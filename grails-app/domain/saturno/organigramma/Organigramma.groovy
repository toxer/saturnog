package saturno.organigramma

import saturno.anagrafica.Persona;
import saturno.common.Ente
import saturno.piano.Versione

class Organigramma {
	Integer id
	Ente ente
	String note
	Integer anno
	Versione versione
	static hasMany=[nodi:NodoOrganigramma]
	//sorgente diretta: il nodo da cui è stato clonato
		
	
	
	
	static mapping = {
		version true
		id generator:'identity', column:'id_organigramma', type:'integer'
		note type: 'text'
	}
}
