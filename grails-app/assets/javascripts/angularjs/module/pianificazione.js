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
								if (!versione.versionePresente){
									$('#versioneNonPresenteDialog').modal({
										backdrop : 'static',
										keyboard : true
									})
								}

							}).error(function(response, status, headers, config) {
						alert(response)
					});
			

			//funzioni del controller
			
			vm.creaNuovaVersione=function(){
				
				$('#creaNuovaVersioneDialog').modal({
					backdrop : 'static',
					keyboard : true
				})
			}
			
			
		
			
			
		} ]);