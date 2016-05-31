package saturno.piano

import saturno.common.Ente

class Piano implements Serializable{
	Integer id
	Integer anno
	Ente ente
	Integer versione
	String note
	Boolean aperto;
	String nomeVersione
	String creatoDa
	String modificatoDa
	Date dtIniSist
	Date dtFinSist
	static hasMany = [obiettivi:Obiettivo]

	static mapping = {
		version true
		id generator:'identity', column:'id_piano', type:'integer'
		note type:'text'
		//indice ricerche
		ente index: 'ente_anno_piano_idx'
		anno index: 'ente_anno_piano_idx'
		
		//ordinamento di default
		sort versione:"desc"
	}
	static constraints={
		versione(unique:['anno','ente'])
		
	}
	
}
