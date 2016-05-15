package saturno.anagrafica

import saturno.common.Ente

class Organigramma {
	Integer id
	String nome
	String descrizione
	Date dataInizioValidita
	Date dataFineValidida
	Ente ente
	static hasMany=[figli:Organigramma,presone:Persona]
	static belongsTo=[padre:Organigramma]	
	
	static mapping = {
		version true
		id generator:'identity', column:'id_organigramma', type:'integer'
		descrizione type: 'text'
	}
}
