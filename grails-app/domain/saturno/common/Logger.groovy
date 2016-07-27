package saturno.common

import java.io.Serializable;

class Logger implements Serializable{
	Integer id
	String oggetto
	String codice
	Date data
	String azione
	String utente
	String valorePrecedente
	String valoreAttuale
	Ente ente
	
	
	static mapping = {
		version false
		id generator:'identity', column:'id_logger', type:'integer'
	}
}
