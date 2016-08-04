package saturno.piano

import saturno.common.Ente
import saturno.organigramma.Organigramma;

class Versione implements Serializable{
	Integer id
	Integer anno
	Integer versione
	String note
	Boolean aperto;
	String nomeVersione
	String creatoDa
	String modificatoDa
	Date dtIniSist
	Date dtFinSist
	static hasOne=[organigramma:Organigramma,ente:Ente]
	static hasMany = [obiettivi:Obiettivo,kpi:Kpi]
	

	static mapping = {
		version true
		id generator:'identity', column:'id_piano', type:'integer'
		note type:'text'
		//indice ricerche
		ente index: 'ente_anno_piano_idx'
		anno index: 'ente_anno_piano_idx'
		
		//ordinamento di default
		sort versione:"asc"
		
		
	}
	static constraints={
		versione(unique:['anno','ente'])
		ente nullable:false
		anno nullable:false
		
	}
	
}
