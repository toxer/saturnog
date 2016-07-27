package saturno.anagrafica

import java.io.Serializable;

import saturno.common.Ente
import saturno.organigramma.NodoOrganigramma

class Persona implements Serializable{
	Integer id
	String nome
	String cognome
	String matricola
	String userid
	Ente ente
	Date dataInizioValidita
	Date dataFineValidita
	static hasMany=[NodoOrganigramma]
	
	
	static mapping = {
		version true
		id generator:'identity', column:'id_persona', type:'integer'
	}
}
