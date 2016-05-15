package saturno.anagrafica

import saturno.common.Ente

class Persona {
	Integer id
	String nome
	String cognome
	String matricola
	String userid
	Ente ente
	Date dataInizioValidita
	Date dataFineValidita
	
	
	
	
	
	
	static mapping = {
		version true
		id generator:'identity', column:'id_persona', type:'integer'
	}
}
