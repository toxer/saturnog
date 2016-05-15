package saturno.anagrafica


class TargetAnagrafica {
	Integer id
	Double valore
	String prettyValue
	Integer simbolo 	
	String tipologia
	Double performance
	Date dtInizio
	Date dtFine
	static belongsTo=[kpi:KpiAnagrafica]

	static mapping = {
		version true
		id generator:'identity', column:'id_target_anagrafica', type:'integer'
		
		
	}
}
