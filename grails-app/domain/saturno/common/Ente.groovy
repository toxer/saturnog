package saturno.common

class Ente implements Serializable{
	String dsEnte	
	String id
	static mapping = {
		version false
		 id column: 'id_ente', generator: 'assigned'
	}
}
