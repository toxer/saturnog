package saturno.piano

import saturno.anagrafica.ObiettivoAnagrafica
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
	static belongsTo = [piano:Piano,padre:Obiettivo,obiettivoSorgente:ObiettivoAnagrafica]
	static hasMany = [figli:Obiettivo,scale:Scala,responsabile:Persona,persone:Persona,organigrammi:Organigramma]
	
	Double performance


	static mapping = {
		version true
		id generator:'identity', column:'id_obiettivo', type:'integer'
		descrizione type: 'text'
		nome type:'text'
		note type:'text'
	}
}
