package saturnoservice



import grails.converters.JSON
import grails.gsp.PageRenderer

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils

class UtilsService {
	PageRenderer groovyPageRenderer
	def currentUserObject(){
		if (!testTabId()){
			return
		}
		//recupero il tabId o dal param, o dal json
		GrailsWebRequest webUtils = WebUtils.retrieveGrailsWebRequest()
		def request = webUtils.getCurrentRequest()
		def tabId = request.JSON?.tabId;
		if (tabId==null){
			tabId=webUtils.getParams()?.tabId;
		}
		if (tabId==null){
			log.error("UtilsService: Tab id not found")
			return new JSONArray() as JSON
		}
		return request.session."${tabId}".userObject
		
	}
	
	def testTabId(){
		GrailsWebRequest webUtils = WebUtils.retrieveGrailsWebRequest()
		def sess = webUtils.getSession()
		def req = webUtils.getRequest()
		sess.getAttributeNames().each{
			log.debug("SessionAttribute: "+it)
		}
		def tabId = req.JSON?.tabId;
		log.info("TESTTAB: "+tabId)
		if (tabId == null){
			log.error("tabId not found in request")

			groovyPageRenderer.render status: 500, text: 'tabId not found in request'

			return false;
		}
		//se non esiste in session il tabId lo creo
		if (sess."${tabId}"==null){
			log.error("tabId not found in session")

			 groovyPageRenderer.render status: 500, text: 'tabId not found in session'
			return false;
		}


		return true
	}

	
}
