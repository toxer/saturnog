package saturno.common

class Scala {
	Integer id
	String tipologia
	String nome;
	String desc;
	String descrizione;
	static hasMany=[gradini:Gradino]

	static mapping = {
		version true
		id generator:'identity', column:'id_scala', type:'integer'
	}
}
