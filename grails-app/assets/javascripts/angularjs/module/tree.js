//dipendente da pianificazione

var tree = angular.module("Tree", []);

tree
		.controller(
				'TreeController',
				[
						'$scope',
						'$http',
						'serviceUtils',
						function($scope, $http, serviceUtils) {
							var vm = this;
							vm.tabId = sessionStorage.tabId;
							// metodo di visualizzazione dell'albero corrente
							// basato sul watch della variabile di scope
							
							//va usato lo scope del controller superiore, altrimenti, alla prima modifica
							//fatta sulla variabile pianoJson il controller superiore perde la variabile
							//e quindi tutti i cambia versione non funzionano più
							$scope.pianificazioneControllerScope.$watch('pianoJson', function() {

								if ($scope.pianoCorrente != undefined) {
									vm.renderPiano($scope.pianificazioneControllerScope.pianoJson)
								} else {
									vm.tree = ""
								}

							})

							// funzione che renderizza il piano della
							// performance
							vm.renderPiano = function(piano) {
								// alert("render piano corrente")
								// renderizza il piano
								// TODO logica svg qui

								// prima elimino eventuali svg presenti
								$('#treeBody').empty();

								
								initTemplate(piano, vm)
								if (vm.obiettivo != undefined && vm.obiettivo.id != undefined){
									// mi posizione con la
									// funzione di ricerca sul
									// nodo
									openNodeById(vm.obiettivo.id,0);
								}

								// initTemplate(piano,vm,40);

							}

							// aggiunta nodi all'albero
							vm.addNode = function(idNodoPadre, titoloNodoPadre) {

								$('#creaModificaObiettivo').modal('toggle');
								if (vm.obiettivo == undefined) {
									vm.obiettivo = new Object();
								}
								vm.obiettivo.idParent = idNodoPadre
								vm.obiettivo.titoloParent = titoloNodoPadre

								$scope.$apply()

							}

							vm.saveNodo = function() {
								console.log($scope.pianoCorrente);
								$('#creaModificaObiettivo').modal('hide');

								$http
										.post(
												sessionStorage.context
														+ '/pianificazione/aggiungiNodo',
												{
													'tabId' : vm.tabId,
													'idVersione' : $scope.pianoCorrente.id,
													'obiettivo' : vm.obiettivo
												})
										.success(
												function(response, status,
														headers, config) {
													

													vm.obiettivo.id = response.nuovoId
													$scope.pianificazioneControllerScope.pianoJson = response.pianoJson
													$scope.pianificazioneControllerScope.pianoCorrente=response.piano

													
												}).error(
												function(response, status,
														headers, config) {
													alert("saveNodo "
															+ response)
												});

								vm.obiettivo = undefined
							}

							vm.addSibling = function(idNodo) {
								alert("Richiesta di aggiunta fratello al nodo con id "
										+ idNodo)
							}

							

							vm.apriNuovoObiettivo = function() {

							}

							vm.aggiungiNuovoObiettivo = function(idNodoPadre) {
								if (vm.nuovoObiettivoForm == undefined) {
									alert("Non trovato il form per la creazione dell'obiettivo")
								}
								// controllo validità
								if (vm.nuovoObiettivoForm.$valid) {

								} else {
									alert("Non sono stati completati tutti i campi obbligatori")
								}
							}

						} ]);
