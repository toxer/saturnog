package saturno.piano

import java.io.Serializable;


class Target implements Serializable{
	Integer id
	Double valore
	String prettyValue
	Integer simbolo 	
	String tipologia
	Date dtInizio
	Date dtFine
	static belongsTo=[kpi:Kpi]
	
	
	
	static mapping = {
		version true
		id generator:'identity', column:'id_target', type:'integer'
		
	}
}
