package saturno.anagrafica

import saturno.common.Ente
import saturno.piano.Piano

class Organigramma {
	Integer id
	String nome
	String descrizione
	Date dataInizioValidita
	Date dataFineValidida
	Ente ente
	static hasMany=[figli:Organigramma,presone:Persona]
	//sorgente diretta: il nodo da cui è stato clonato
	static belongsTo=[padre:Organigramma,piano:Piano,sorgenteDiretta:Organigramma]	
	
	static mappedBy = [figli:'padre']
	
	static mapping = {
		version true
		id generator:'identity', column:'id_organigramma', type:'integer'
		descrizione type: 'text'
	}
}
