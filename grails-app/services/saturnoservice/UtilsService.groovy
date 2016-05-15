package saturnoservice



import grails.converters.JSON

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils

class UtilsService {
	def currentUserObject(){
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
}
