package saturno.common

import saturno.anagrafica.KpiAnagrafica
import saturno.piano.Versione

class Ente implements Serializable{
	String dsEnte	
	String id
	static hasMany=[versione:Versione,kpi:KpiAnagrafica]
	static mapping = {
		version false
		 id column: 'id_ente', generator: 'assigned'
	}
}
