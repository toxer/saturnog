//dipendente da pianificazione

var tree = angular.module("Tree", []);

tree.controller('TreeController', [ '$scope', '$http', 'serviceUtils',
		function($scope, $http, serviceUtils) {
			var vm = this;
			vm.tabId = sessionStorage.tabId;
			// metodo di visualizzazione dell'albero corrente
			// basato sul watch della variabile di scope

			$scope.$watch('pianoJson', function() {
				
				if ($scope.pianoCorrente != undefined) {
					vm.renderPiano($scope.pianoJson)
				}else{
					vm.tree=""
				}

			})

			// funzione che renderizza il piano della performance
			vm.renderPiano = function(piano) {
				// alert("render piano corrente")
				// renderizza il piano
				// TODO logica svg qui
				vm.tree = piano
				
				initTemplate(piano);
			}

		} ]);