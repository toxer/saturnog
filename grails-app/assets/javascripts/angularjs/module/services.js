var services = angular.module('Services', []);

services.service('serviceUtils', function($http) {

	// usato per aggiornare l'interfaccia da userObject

	updateInterfaces = function(scope) {

		// logo camerale

		scope.logoSrc = "" + sessionStorage.context + "/images/logo/"
				+ JSON.parse(sessionStorage.userObject).ente.id + ".jpg"
		scope.enteCss = "" + sessionStorage.context + "/css/"
				+ JSON.parse(sessionStorage.userObject).ente.id + "_style.css"

		// aggiornamento della label di anno

		scope.annoCorrente = JSON.parse(sessionStorage.userObject).anno

	}

	this.updateInterfaces = updateInterfaces;
	userObject = function(scope, enteSelezionato, anno, versione) {
		var data = new Object();
		if (enteSelezionato != undefined) {
			data.ente = enteSelezionato
		}
		if (anno != undefined) {
			data.anno = anno
		}
		if (versione != undefined) {
			data.versione = versione;
		}

		var updateUser = $http.post(sessionStorage.context
				+ '/init/updateUserObject', {
			'tabId' : sessionStorage.tabId,
			'data' : data

		});
		updateUser.then(function(response, status, headers, config) {

			sessionStorage.userObject = JSON.stringify(response.data)
			// aggiorno tutto ciò che dipende dall'ente
			updateInterfaces(scope)

			return response.data

		}, function(response, status, headers, config) {
			alert(response.data);
			return undefined
		});
	}

	this.userObject = userObject

	this.getEnti = function(scope) {
		var entiPromise = $http.post(sessionStorage.context + '/init/getEnti',
				{
					'tabId' : sessionStorage.tabId
				});

		entiPromise.then(function(response) {
			if (response.data.length == 1) {

				userObject(scope, response.data[0].aziendaId)

			} else {
				// attivare la finestra
				// di ricerca enti
				scope.entiPossibili = response.data
				$('#cambiaCamera').modal({
					backdrop : 'static',
					keyboard : true
				})
			}

		}, function(response) {
			alert(response.data);
		});
	}

}

);
