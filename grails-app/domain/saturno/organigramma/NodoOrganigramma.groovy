package saturno.organigramma

import java.io.Serializable;

import saturno.anagrafica.Persona
import saturno.common.Ente

class NodoOrganigramma implements Serializable{
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
	static belongsTo=[Organigramma,Persona]
	static hasMany=[figli:NodoOrganigramma,persone:Persona]
	
	static mappedBy = [figli:'padre']
	
	static mapping = {
		version true
		id generator:'identity', column:'id_nodo_organigramma', type:'integer'
		descrizione type: 'text'
	}
}
