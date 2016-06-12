package saturno.piano

import saturno.anagrafica.KpiAnagrafica
import saturno.common.Scala

class Kpi {
	Integer id
	Integer anno
	String nome
	String descrizione
	String codiceCamera
	String codice
	String note
	String algoritmo
	KpiAnagrafica kpiSorgente
	Versione versione
	Scala scala;

	static belongsTo=[obiettivo:Obiettivo]
	Boolean composto //se false è un valore singolo
	static hasMany = [targets:Target]
	Double performance
	Double performanceImposta
	Double valoreConsuntivo
	String prettyValueConsuntivo
	Date dataRilevazione
	Double valoreAutomatico
	Date dataUltimaRilevazione
	
	
	static mapping = {
		version true
		id generator:'identity', column:'id_kpi', type:'integer'
		descrizione type: 'text'
		nome type:'text'
		note type:'text'
		algoritmo type:'text'
	}
}
