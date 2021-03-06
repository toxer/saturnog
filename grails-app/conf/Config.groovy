// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }


//configurazione albero standard di degfault
grails.standardConfiguration="{'class':'saturno.piano.VersioneCfg','id':null,'anno':null,'ente':{'class':'saturno.common.Ente','id':'111111','dsEnte':'INFOCAMERE S.C.p.A.','kpi':[],'versione':[]},'livelli':[{'class':'saturno.piano.LivelloCfg','id':null,'codice':'OS','colore':'#4CA9C0','livello':2,'nomePlurale':'Obiettivi strategici','nomeSingolare':'Obiettivo strategico','versioneCfg':{'_ref':'../..','class':'saturno.piano.VersioneCfg'}},{'class':'saturno.piano.LivelloCfg','id':null,'codice':'AS','colore':'#EB34CF','livello':1,'nomePlurale':'Aree strategiche','nomeSingolare':'Area strategica','versioneCfg':{'_ref':'../..','class':'saturno.piano.VersioneCfg'}},{'class':'saturno.piano.LivelloCfg','id':null,'codice':'OO','colore':'#D5CE29','livello':3,'nomePlurale':'Obiettivi operativi','nomeSingolare':'Obiettivo operativo','versioneCfg':{'_ref':'../..','class':'saturno.piano.VersioneCfg'}},{'class':'saturno.piano.LivelloCfg','id':null,'codice':'PR','colore':'#F19929','livello':4,'nomePlurale':'Programmi','nomeSingolare':'Programma','versioneCfg':{'_ref':'../..','class':'saturno.piano.VersioneCfg'}},{'class':'saturno.piano.LivelloCfg','id':null,'codice':'RT','colore':'#F0190A','livello':0,'nomePlurale':'Versioni','nomeSingolare':'Versione','versioneCfg':{'_ref':'../..','class':'saturno.piano.VersioneCfg'}},{'class':'saturno.piano.LivelloCfg','id':null,'codice':'AZ','colore':'D96722','livello':5,'nomePlurale':'Azioni','nomeSingolare':'Azione','versioneCfg':{'_ref':'../..','class':'saturno.piano.VersioneCfg'}}],'numeroLivelli':5}"

grails.gorm.default.constraints = {
	'*'(nullable:true,blank:true)
}
grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.app.context = '/saturno'
// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

//use json deep
grails.converters.json.default.deep = true



// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false



environments {

    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}
grails.applicationauth="FEBE"
environments {
	development {
		log4j = {
			appenders {
				console name: "stdout", threshold: org.apache.log4j.Level.ALL
			}
			debug 'grails.app'
		}

	}

	sviluppo {
		log4j = {
			appenders {
				console name: "stdout", threshold: org.apache.log4j.Level.ALL
			}
			debug 'grails.app'
		}

	}

	test {
		log4j = {
			appenders {
				console name: "stdout", threshold: org.apache.log4j.Level.DEBUG
			}
			debug 'grails.app'
		}

	}
	production {
		log4j = {
			appenders {
				console name: "stdout", threshold: org.apache.log4j.Level.INFO
			}
			debug 'grails.app'
		}

	}
}