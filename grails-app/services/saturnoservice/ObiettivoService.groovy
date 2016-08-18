package saturnoservice

import grails.transaction.Transactional
import saturno.piano.Obiettivo

@Transactional

class ObiettivoService {

	def saveObj(Obiettivo o ){
		o.save(true);
		return o;
	}
	
	def deleteObj(Obiettivo o ){
		o.delete(flush: true, failOnError:true);
		return o;
	}
}
