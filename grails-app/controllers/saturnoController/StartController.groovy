package saturnoController

import grails.converters.JSON
import groovy.sql.Sql
import it.ictechnology.eaco.client.framework.Client
import it.ictechnology.eaco.client.framework.FrameworkFacade

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import saturno.common.Ente

class StartController {

	def dataSource_portal
	public static final String SESSION_CREDENTIALS="sessionCredentialsMap"

	
	
	
	//inserire qui un identificatore di tab
	def setupUserObject(){
		JSONObject userObject = new JSONObject();
		def tabId = params.tabId;
		if (tabId==null){
			log.error("Non trovata tabId");
			return;
		}
		
		def tabIdSessionObj = session."${tabId}"
		if (tabIdSessionObj==null){
			session."${tabId}"=new JSONObject();
		}
		
		//se questi parametri vengono passati sovrascrivono
		//quelli in sessione
	
		def anno = params.anno;
		def versione = params.versione;
		def currentEnte = params.currentEnte
		def multiente = params.multiente
		def currentEnteName=params.currentEnteName;
		
		if (currentEnte==null){
			currentEnte=session."${tabId}"?.userObject?.currentEnte
		}
		
		if (currentEnteName==null){
			currentEnteName=session."${tabId}"?.userObject?.currentEnteName
		}
		if (multiente==null){
			multiente=session."${tabId}"?.userObject?.multiente
		}
		if (anno ==null){
			anno=session."${tabId}"?.userObject?.anno;
		}
		if (versione ==null){
			versione=session."${tabId}"?.userObject?.versione;
		}
		
		userObject.currentEnte = currentEnte
		userObject.currentEnteName = currentEnteName
		userObject.anno = anno;
		userObject.versione=versione
		userObject.tabId=tabId;
		userObject.multiente=[];
		
		
	
		if (userObject.anno == null){
			userObject.anno= Calendar.getInstance().get(Calendar.YEAR);
		}
		

		if (session.currentUser == null){

			Client eacoClient = FrameworkFacade.getClient(request);
			log.debug("EacoClient: "+eacoClient)
			def currentUser = new JSONObject();
			currentUser.userId=eacoClient.getUserId()?.toUpperCase()
			currentUser.userName=eacoClient.getDescrCliente()
			currentUser.userEnteName=eacoClient.getCciaaApp()
			currentUser.userEnte=eacoClient.getCliente()
			currentUser.profiliEaco=eacoClient.getProfili()?.toList()
			currentUser.abilitazioni=[];

			userObject.currentUser=currentUser
			
		}
		
		
		//se richiesto effettuo l'esplicito set dell'ente
		if (currentEnte != null){
			log.debug("Ente corrente selzionato "+currentEnte)
			session."${tabId}".userObject=userObject;
			def ente=Ente.findAllById(currentEnte);
			if (!ente){
				Ente e = new Ente();
				e.setDsEnte(userObject.currentEnteName);
				e.setId(userObject.currentEnte);
				e.save(true)
			}
			render session."${tabId}" as JSON
			return;
		}

		//popolo le abilitazioni in base al portale per connessione diretta

		def sql = Sql.newInstance(dataSource_portal);

		//ricavo le aziende per cui è abilitato

		def parametri = [userId:userObject.currentUser.userId,appName:'FEBE'];

		def rows= sql.rows("select distinct(az.aziendaId),az.nome from utenti_aziende_aplicazioni ap join aziende az on (ap.aziendaId=az.aziendaId)  where sigla=:appName and userId=:userId order by az.nome", parametri)
		log.debug("ROWS: "+rows.size());
		
		if (rows.size==0){
			//devo aggiungere in automatico l'ente eaco
			//controllo se c'è l'untete
			
			//controllo che esista l'ente
			
			def aziendaCount = sql.rows("select nome as c from aziende where aziendaId=:userEnte",session.currentUser);
			
			if (aziendaCount.size()>0){
				//inserisco l'abilitazione
				sql.execute("insert into utenti_aziende_aplicazioni (userId,aziendaId,sigla) values (:userId,:userEnte,'FEBE')",session.currentUser);
				sql.commit();
				session."${tabId}".currentEnte=session.currentUser.userEnte
				log.debug("Settato l'unico ente appena aggiunto nelle abilitazioni : "+session."${tabId}".currentEnte);
				userObject.currentEnte=session.currentUser.userEnte
				userObject.currentEnteName=aziendaCount.get(0)?.nome
				session."${tabId}".userObject=userObject;
				render session."${tabId}" as JSON
				def ente=Ente.findAllById(currentEnte);
				if (!ente){
					Ente e = new Ente();
					e.setDsEnte(userObject.currentEnteName);
					e.setId(userObject.currentEnte);
					e.save(true)
				}
				return;
				
			}else{
				log.error("Ente non mappato "+session.currentUser?.userEnte)
			}


		}


		else if (rows.size()==1){
			userObject.currentEnte=rows.get(0).aziendaId
			userObject.currentEnteName=rows.get(0).nome
			session."${tabId}".userObject=userObject;
			def ente=Ente.findAllById(currentEnte);
			if (!ente){
				Ente e = new Ente();
				e.setDsEnte(userObject.currentEnteName);
				e.setId(userObject.currentEnte);
				e.save(true)
			}
			render session."${tabId}" as JSON
			return;


		}
		else{
			userObject.multiente=rows
			session."${tabId}".userObject=userObject;
			render session."${tabId}" as JSON
		}


	}

	
	def getMultienteByUser(){
		def tabId = params.tabId
		
		def sql = Sql.newInstance(dataSource_portal);
		
		if (session."${tabId}"?.userObject?.currentUser==null){
			render new JSONArray();
			return
		}
		
		def rows= sql.rows("select distinct(az.aziendaId),az.nome from utenti_aziende_aplicazioni ap join aziende az on (ap.aziendaId=az.aziendaId)  where sigla='FEBE' and userId=:userId order by az.nome", session."${tabId}"?.userObject?.currentUser)
		session."${tabId}"?.userObject?.multiente=rows
		render session."${tabId}" as JSON
		 
	}
	
	def currentUserObject(){
		log.info(request.JSON.tabId)
		if(request.JSON?.tabId==null && params.tabId==null){
			log.error("tabId non presente")
			def nil=[]
			render nil as JSON
			return;
		}
		def tabId = (request.JSON?.tabId!=null)? request.JSON?.tabId:params.tabId
		log.debug("Riciesto oggetto sessione per "+tabId)
		
		render session."${tabId}"?.userObject as JSON
	}
	
	
	def getCurrentEnteAsString(){
		render session."${tabId}"?.userObject?.currentEnte;
	}
	
	


	def test(){
		log.info(request.JSON)
		render request.JSON as JSON
	}



}
