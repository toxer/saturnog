package saturno.anagrafica

import saturno.common.Ente
import saturno.common.Scala

class KpiAnagrafica {
	Integer id
	Integer anno
	String nome
	String descrizione
	String codiceCamera
	String codice
	String note
	String algoritmo
	Ente ente
	Boolean composto //se false è un valore singolo
	static hasMany = [targets:TargetAnagrafica]
	static hasOne=[scala:Scala]
	Double performance
	Double performanceImposta
	Double valoreConsuntivo
	Double valoreAutomatico
	Date dataUltimaRilevazione
	
	static mapping = {
		version true
		id generator:'identity', column:'id_kpi_anagrafica', type:'integer'
		descrizione type: 'text'
		nome type:'text'
		note type:'text'
		algoritmo type:'text'
		
	}
}
