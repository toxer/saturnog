var pianificazione = angular.module("Pianificazione", []);

pianificazione.controller('PianificazioneController', [
		'$scope',
		'$http',
		'serviceUtils',
		function($scope, $http, serviceUtils) {
			console.log("PianificazioneController")
			var vm = this
			vm.tabId = sessionStorage.tabId;

			// eseguo il test per vedere se ho almeno una versione presente

			$http
					.post(
							sessionStorage.context
									+ '/pianificazione/testVersionExist', {
								'tabId' : vm.tabId
							}).success(
							function(response, status, headers, config) {
								var versione = JSON.stringify(response)
								if (!versione.versionePresente) {
									$('#versioneNonPresenteDialog').modal({
										backdrop : 'static',
										keyboard : true
									})
								}

							}).error(
							function(response, status, headers, config) {
								alert(response)
							});

			// funzioni del controller

			vm.creaNuovaVersioneOpenDialog = function() {

				// recupero il prossimo piano

				$http.post(
						sessionStorage.context
								+ '/pianificazione/prossimoPianoLibero', {
							'tabId' : vm.tabId
						}).success(function(response, status, headers, config) {
				
					vm.piano =response
					$('#creaNuovaVersioneDialog').modal({
						backdrop : 'static',
						keyboard : true
					})

				}).error(function(response, status, headers, config) {
					alert(response)
				});

			}

			// crea una nuova versione
			vm.creaNuovaVersione = function() {
				// controllo se c'è il form

				if (vm.nuovaVersioneForm == undefined) {
					alert("Non trovato il form di nuova versione");
					return;
				}
				if (vm.nuovaVersioneForm.$valid) {
					$http.post(
							sessionStorage.context
									+ '/pianificazione/creaNuovaVersione', {
								'tabId' : vm.tabId,
								'piano':vm.piano
							}).success(function(response, status, headers, config) {
					
						
						$('#creaNuovaVersioneDialog').hide();
						alert("Nuova versione creata")

					}).error(function(response, status, headers, config) {
						alert(response)
					});
				}else{
					//alert('form non valido')
				}

			}

		} ]);