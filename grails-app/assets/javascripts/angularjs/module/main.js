var myApp = angular.module('myApp', []);

// controller ereditato dal body e quindi da tutte le pagine
//contiene i metodi per la gestione dell'user object

myApp.controller('MainController', [
		'$scope',
		'$http',

		function($scope, $http) {

			console.log("MainController")
			var vm = this
			vm.tabId = sessionStorage.tabId;
			//selezione dell'ente da finesrta
			vm.selectEnte=function(){
				console.log("ente selezioato "+vm.enteSelezionato)
				
				$http.post(
						sessionStorage.context
								+ '/init/updateUserObject', {
							'tabId' : sessionStorage.tabId,
							'ente':vm.enteSelezionato
						}).success(
						function(response, status, headers, config) {
							sessionStorage.userObject=JSON.stringify(response.data)
							
						}).error(
						function(response, status, headers, config) {
							alert(response.data);
						});
			}

			vm.getEnti = function(tabId) {
				console.log("getEnti for tabID " + tabId)
				var entiPromise = $http.post(sessionStorage.context
						+ '/init/getEnti', {
					'tabId' : sessionStorage.tabId
				});

				entiPromise.then(function(response) {
					if (response.data.length == 1) {
						var userObject = new Object();
						
						$http.post(
								sessionStorage.context
										+ '/init/updateUserObject', {
									'tabId' : sessionStorage.tabId,
									'ente':response.data[0].aziendaId
								}).success(
								function(response, status, headers, config) {
									console.log(response.data)
									sessionStorage.userObject=JSON.stringify(response.data)
									
								}).error(
								function(response, status, headers, config) {
									alert(response.data);
								});
					}else{
						//attivare la finestra di ricerca enti
						vm.entiPossibili = response.data
						$('#cambiaCamera').modal()
					}

				}, function(response) {
					alert(response.data);
				});

			}

			if (sessionStorage.tabId == null) {
				console.log("tabId not found in sessionStorage")

				$http.post(sessionStorage.context + '/init/createSessionTabId',
						{
							'tabId' : vm.tabId
						}).success(function(data, status, headers, config) {
					console.log("tab id received " + data.tabId)
					sessionStorage.tabId = data.tabId
					vm.tabId = data.tabId
					vm.getEnti(sessionStorage.tabId)
				}).error(function(data, status, headers, config) {
					alert(data);
				});
			} else {
				//controllo se ho già un utente in session
				$http.post(sessionStorage.context + '/init/getUserObject',
						{
							'tabId' : sessionStorage.tabId
						}).success(function(response, status, headers, config) {
					if (response.data != undefined){
						//ho già un oggetto utente
						//lo carico in session client
						sessionStorage.userObject=sessionStorage.userObject=JSON.stringify(response.data);
						return;
					}else{
						//non ho un oggetto utente in sessione,
						//passo per la scelta dell'ente
						vm.getEnti(sessionStorage.tabId);
					}
				}).error(function(response, status, headers, config) {
					alert(response.data);
				});
				
			
			}

			

		} ]);
