var cambiaAnno = angular.module("CambiaAnno", []);

cambiaAnno.controller('CambiaAnnoController', [ '$scope', '$http','serviceUtils',
		function($scope, $http,serviceUtils) {
		
			var vm = this
			vm.tabId = sessionStorage.tabId;

			// richiedo gli anni disponibili
			$http.post(sessionStorage.context + '/cambiaAnno/getAnni', {
				'tabId' : vm.tabId
			}).success(function(response) {
				
				vm.anno=JSON.parse(sessionStorage.userObject).anno
				vm.anni = response

			});
			
			vm.selezionaAnno = function(){
				serviceUtils.userObject($scope,undefined,vm.anno);
				goToMain();
			}

		} ]);
