package saturno.common

import java.io.Serializable;

class Gradino implements Serializable{
	Integer id	
	Double intervalloNumericoStart
	Double intervalloNumericoEnd
	Date intervalloDateStart
	Date intervalloDateEnd
	String giudizio
	static belongsTo=[scala:Scala]
	
	static mapping = {
		version true
		id generator:'identity', column:'id_gradino', type:'integer'
	}
	
}
