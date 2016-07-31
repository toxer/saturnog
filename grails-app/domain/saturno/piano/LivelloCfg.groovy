package saturno.piano

class LivelloCfg {
	Integer id
	static belongsTo=[versioneCfg:VersioneCfg]
	String colore;
	String nomeSingolare
	String nomePlurale
	String codice
	Integer livello
	
	static mapping = {
		version true
		id generator:'identity', column:'id_livello_cfg', type:'integer'
		sort livello:"asc"
	}

}
