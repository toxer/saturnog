package saturno.common

class Scala implements Serializable{
	Integer id
	String tipologia
	String nome;
	String descrizione;
	static hasMany=[gradini:Gradino]

	static mapping = {
		version true
		id generator:'identity', column:'id_scala', type:'integer'
	}
}
