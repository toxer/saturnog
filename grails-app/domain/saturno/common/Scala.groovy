package saturno.common

class Scala {
	Integer id
	String tipologia
	Double intervalloNumericoStart
	Double intervalloNumericoEnd
	Date intervalloDateStart
	Date intervalloDateEnd
	static mapping = {
		version true
		id generator:'identity', column:'id_scala', type:'integer'
	}
	
}
