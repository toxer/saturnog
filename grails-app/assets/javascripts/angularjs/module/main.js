var myApp = angular.module('myApp', [ 'CambiaAnno', 'Pianificazione',
		'ngCkeditor' ]);

// servizio invocato ogni qual volta si aggiornane le
// caratteristiche dell'untete: ente in uso, anno, versione

myApp.run(function($rootScope) {
	// setup editor options
	$rootScope.editorOptions = {
		language : 'it'
	// ,uiColor: '#000000'
	};

	// torna alla pagina principale
	$rootScope.goToMain = function() {
		window.location = location.protocol + "//" + location.host + "/"
				+ sessionStorage.context
	}

});

// interceptor per avere lo spinner ad ogni richiesta post
myApp.config([ '$httpProvider', function($httpProvider) {
	$httpProvider.interceptors.push('httpSpinnerInterceptor');
} ]);

myApp.factory('httpSpinnerInterceptor', function($q, $rootScope) {

	return {

		request : function(request) {
			$('#spinner').fadeIn();
			return request
		},

		response : function(response) {
			$('#spinner').fadeOut('fast');
			return response;
		}
	// ,

	// responseError: function(responseError) {
	// $('#spinner').fadeOut('fast');
	// console.log(responseError)
	// return responseError;
	// }
	};
});

myApp.service('serviceUtils', function($http) {

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

	updateBreadcumb = function(text) {
		$('#breadcumb').html(text);
	}
	this.updateBreadcumb = updateBreadcumb;

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
			// alert(response.data);
			return response
		});
	}

	this.userObject = userObject

	this.chiudiSpinner = function() {
		$('#spinner').fadeOut('fast');
	}

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

// controller ereditato dal body e quindi da tutte le pagine
// contiene i metodi per la gestione dell'user object

myApp
		.controller(
				'MainController',
				[
						'$scope',
						'$http',
						'serviceUtils',

						function($scope, $http, serviceUtils) {
							$scope.breadcumb = ""

							// questo serve per poter permettere
							// alla finsetra changeCamera di essere aperta
							// da pulsante

							$scope.cambiaCamera = function() {
								serviceUtils.getEnti($scope)

							}

							var vm = this
							vm.tabId = sessionStorage.tabId;

							// selezione dell'ente da finestra
							vm.selectEnte = function() {

								serviceUtils.userObject($scope,
										vm.enteSelezionato);

							}

							if (sessionStorage.tabId == null) {
								// console.log("tabId not found in
								// sessionStorage")

								$http
										.post(
												sessionStorage.context
														+ '/init/createSessionTabId',
												{
													'tabId' : vm.tabId
												})
										.success(
												function(data, status, headers,
														config) {
													console
															.log("tab id received "
																	+ data.tabId)
													sessionStorage.tabId = data.tabId
													vm.tabId = data.tabId
													// vm.getEnti(sessionStorage.tabId)
													serviceUtils
															.getEnti($scope)
												}).error(
												function(data, status, headers,
														config) {
													alert("fuction selectEnte"
															+ data);
												});
							} else {
								// controllo se ho già un utente in session
								$http
										.post(
												sessionStorage.context
														+ '/init/getUserObject',
												{
													'tabId' : sessionStorage.tabId
												})
										.success(
												function(response, status,
														headers, config) {
													console.log(status)

													if (response != undefined
															&& !$
																	.isEmptyObject(response)) {

														// ho già un oggetto
														// utente
														// lo carico in session
														// client
														sessionStorage.userObject = sessionStorage.userObject = JSON
																.stringify(response);
														// e aggiorno
														// l'interfaccia
														serviceUtils
																.updateInterfaces($scope)
														return;
													} else {
														// non ho un oggetto
														// utente in sessione,
														// passo per la scelta
														// dell'ente
														// vm
														// .getEnti(sessionStorage.tabId);
														serviceUtils
																.getEnti($scope)

													}
												})
										.error(
												function(response, status,
														headers, config) {

													alert("Tab id invalido");
													sessionStorage
															.removeItem('tabId')
													window.location = location.protocol
															+ "//"
															+ location.host
															+ "/"
															+ sessionStorage.context
												});

							}

						} ]);
