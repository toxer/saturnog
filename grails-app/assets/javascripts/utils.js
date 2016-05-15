function sendRequestToController( controllerName, action, params,
		fToCall) {
	var context = sessionStorage.context
	// viene sempre inserito l'oggetto id tab
	if (sessionStorage.tabId == undefined) {
		sessionStorage.tabId = new Date().getTime();
	}

	if (params == undefined) {
		params = new Object();
	}

	params.tabId = sessionStorage.tabId
	var url = context + "/" + controllerName + "/" + action;

	jQuery.ajax({
		type : 'POST',
		data : params,
		url : url,
		success : function(data, textStatus) {
			if (fToCall) {
				
				fToCall(data)
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});

}

function renderChangeCamera(data) {
	
	
	$('#aziendaIdSelect').find('option').remove().end();
	$.each(data.userObject.multiente, function (i, item) {
	    $('#aziendaIdSelect').append($('<option>', { 
	        value: item.aziendaId,
	        text : item.nome 
	    }));
	});
	$('#cambiaCamera').modal();
}

function changeCamera() {
	
	sendRequestToController( 'start', 'getMultienteByUser', undefined,renderChangeCamera)
}

function goToMain(){
	window.location=location.protocol+"//"+location.host+"/"+sessionStorage.context
}

//semplifica il settaggio dell'oggetto utente
function setupUserObject(params,functionToCallAfter){

	 sendRequestToController( 'start','setupUserObject',params,functionToCallAfter)
	 
}
