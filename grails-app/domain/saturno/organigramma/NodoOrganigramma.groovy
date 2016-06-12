package saturno.organigramma

import saturno.anagrafica.Persona
import saturno.common.Ente

class NodoOrganigramma {
	Integer id
	String codiceCamera	
	String nome
	String descrizione
	Date dataInserimento
	Date dataModifica
	Integer livello
	NodoOrganigramma padre;
	
	//sorgente diretta: id del nodo da cui è stato clonato	
	Integer sorgenteDiretta
	static belongsTo=[organigramma:Organigramma]
	static hasMany=[figli:NodoOrganigramma,persone:Persona]
	
	static mappedBy = [figli:'padre']
	
	static mapping = {
		version true
		id generator:'identity', column:'id_organigramma', type:'integer'
		descrizione type: 'text'
	}
}
