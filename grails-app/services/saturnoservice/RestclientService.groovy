package saturnoservice

import grails.plugins.rest.client.RestBuilder

import org.codehaus.groovy.grails.web.json.JSONObject

import com.google.gson.JsonObject

class RestclientService {
	def restPlug = new RestBuilder();
	def JSONObject sendJsonRequest(def url,JSONObject jsonObject){
		try{
			def resp = restPlug.post(url){
				json( jsonObject)
			}
			log.debug("Call completed "+resp)
			return resp?.json;
		}
		catch (Exception exc){
			exc.printStackTrace();
		}
		return null
	}
}
