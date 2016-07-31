package saturnoController

import javax.net.ssl.SSLEngineResult.Status;

import grails.converters.JSON
import groovy.sql.Sql
import it.ictechnology.eaco.client.framework.Client
import it.ictechnology.eaco.client.framework.FrameworkFacade

import org.codehaus.groovy.grails.web.json.JSONObject
import org.joda.time.DateTime

import saturno.common.Ente

class InitController {
	def dataSource_portal
	public static final String APP_NAME="FEBE"
	def utilsService
	def getEacoUser(){
		try{
			Client eacoClient = FrameworkFacade.getClient(request);
			log.debug("EacoClient: "+eacoClient)
			def currentUser = new JSONObject();
			currentUser.userId=eacoClient.getUserId()?.toUpperCase()
			currentUser.userName=eacoClient.getDescrCliente()
			currentUser.userEnteName=eacoClient.getCciaaApp()
			currentUser.userEnte=eacoClient.getCliente()
			currentUser.profiliEaco=eacoClient.getProfili()?.toList()
			return currentUser
		}
		catch (Exception exc){

			render status: 500, text: 'Eaco error'
			return null
		}

		

	}
	
	def getUserObject(){
		
		if (!utilsService.testTabId()){
			render status:500,text:'Identificativo della tab non valido, chiudere il browser e riprovare'
			return
		}
		def tabId = request.JSON.tabId;
		JSONObject data = new JSONObject();
		data.data =  session."${tabId}".userObject;
		render  session."${tabId}".userObject as JSON
		
		
	}
	
	

	def updateUserObject(){
		
		if (!utilsService.testTabId()){
			render status:503,text:'Identificativo della tab non valido, chiudere il browser e riprovare'
			return
		}
		def tabId = request.JSON?.tabId;		
		def data = request.JSON?.data;
		
		if (data == null){
			log.error("Data not found in request")
			render status: 500, text: 'Data not found '
			return
		}
		
		
		if(!utilsService.testTabId()){
			render status:503,text:'Identificativo della tab non valido, chiudere il browser e riprovare'
			return
		}
		
		def userObject = session."${tabId}".userObject;
		
		
		
		if (userObject==null){
			userObject = new JSONObject();
			userObject.currentUser = getEacoUser();	
			
		}
		
		//imposto l'ente se c'è nella richiesta
		//o lo prendo da eaco 
		//o mantengo quello che c'è
		if (data.ente!=null){
			log.debug("settaggio ente utente")
			//FIXME qui va un controllo per far abilitare solo l'utente
			//per le azinede a lui associate
			userObject.ente=getEnteFromEacoCode(data.ente);
		}else if (userObject.ente==null){
			log.debug("settaggio ente da eaco")
			userObject.ente=getEnteFromEacoCode(userObject.currentUser?.userEnte);
		}else{
			log.debug("Mantenuto l'ente "+userObject.ente)
		}
		
		//TODO fare lo stesso per anno e versione
		
		if (data.anno != null){
			userObject.anno=data.anno;
			log.debug("Anno settato: "+userObject.anno)
		}else if (data.anno == null && userObject.anno == null){
		//di default viene settato l'anno corrente
			userObject.anno = new DateTime().getYear();
			log.debug("Mantenuto anno: "+userObject.anno)
		}
		if (data.versione != null){
			userObject.versione = data.versione;
			log.debug("Versione settata "+userObject.versione)
		}else{
			log.debug("Mantenuta versione: "+userObject.versione)
		}
		
		session."${tabId}".userObject = userObject
	
		log.debug("Setup userObject : "+session."${tabId}"?.userObject)
		render userObject as JSON
		
		
		
		



	}
	
	//cerca l'ente dal codice eaco e
	//se non trovato ne crea uno dal db di autenticazione
	def getEnteFromEacoCode(String eacoCode){
		Ente ente = Ente.findById(eacoCode)
		if (ente != null ){
			return ente;
		}
		//cerco l'ente nel db di autenticazione
		def sql = Sql.newInstance(dataSource_portal);
		def parametri=[idAzienda:eacoCode];
		def rows= sql.rows("select aziendaId,nome from aziende az  where az.aziendaId=:idAzienda order by az.nome", parametri)
		log.debug("Enti trovati: "+rows.size());
		if (rows.size()==0){
			render status: 500, text: 'ente not found in db auth'
			return null
		}
		ente = new Ente();
		ente.setId(rows.get(0).aziendaId);
		ente.setDsEnte(rows.get(0).nome);
		ente.save(true);
	}

	def getEnti(){
		if (!utilsService.testTabId()){
			render status:500,text:'Identificativo della tab non valido, chiudere il browser e riprovare'
			return
		}
		//prelevo l'utente di eaco
		def currentUser = getEacoUser();
		if (currentUser==null){
			return;
		}

		//effettuo una query sul db di autenticazione

		def sql = Sql.newInstance(dataSource_portal);
		def parametri = [userId:currentUser.userId,appName:grailsApplication.config.grails.applicationauth];
		log.debug(parametri)
		def rows= sql.rows("select distinct(az.aziendaId),az.nome from utenti_aziende_aplicazioni ap join aziende az on (ap.aziendaId=az.aziendaId)  where sigla=:appName and userId=:userId order by az.nome", parametri)
		log.debug("Enti trovati: "+rows.size());
		render rows as JSON
	}

	def createSessionTabId(){

		def tabId = UUID.randomUUID().toString();
		while (session."${tabId}"!=null){
			tabId = UUID.randomUUID().toString();
		}
		session."${tabId}"=new JSONObject();
		log.debug("Create tabId: "+tabId)
		
		JSONObject obj = new JSONObject();
		obj.tabId=tabId
		render obj as JSON
	}
}


