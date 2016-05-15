package saturno.piano

import java.util.Date;

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
	static belongsTo=[obiettivo:Obiettivo,kpiSorgente:KpiAnagrafica]
	Boolean composto //se false è un valore singolo
	static hasMany = [targets:Target,scale:Scala]
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
