package saturno.anagrafica

import saturno.common.Ente

class ObiettivoAnagrafica {
	Integer id
	Integer anno
	String nome
	String descrizione
	String codiceCamera
	String codice
	String tipologia //area objstr ect.
	String note
	Ente ente
	
	
	
	
	
	
	static mapping = {
		version true
		id generator:'identity', column:'id_obiettivo_anagrafica', type:'integer'
		descrizione type: 'text'
		nome type:'text'
		note type:'text'
	}
}