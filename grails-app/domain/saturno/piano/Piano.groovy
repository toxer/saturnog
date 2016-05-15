package saturno.piano

import saturno.common.Ente

class Piano implements Serializable{
	Integer id
	Integer anno
	Ente ente
	Integer versione
	String note
	static hasMany = [obiettivi:Obiettivo]

	static mapping = {
		version true
		id generator:'identity', column:'id_piano', type:'integer'
		note type:'text'
	}
}
