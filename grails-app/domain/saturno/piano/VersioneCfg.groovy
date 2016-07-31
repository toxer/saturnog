package saturno.piano

import saturno.common.Ente

class VersioneCfg {
	Integer id;
	Ente ente;
	Integer anno;
	Integer numeroLivelli
	
	static hasMany=[livelli:LivelloCfg]
	
	static mapping = {
		version true
		id generator:'identity', column:'id_versione_cfg', type:'integer'
		
		//ordinamento di default
		sort anno:"desc"
		
		
	}
	
	static constraints={
		ente  unique:'anno'
		
	}

}
