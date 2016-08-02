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

							// va usato lo scope del controller superiore,
							// altrimenti, alla prima modifica (es. aggiunta
							// nodo)
							// fatta sulla variabile pianoJson da TreeController
							// il controller
							// superiore (PianificazioneController) perde la
							// variabile (che passa allo
							// scope di TreeController)
							// e quindi tutti i cambia versione non funzionano
							// più perchè fatti su una variabile diversa da
							// quella in watch qui
							$scope.pianificazioneControllerScope
									.$watch(
											'pianoJson',
											function() {

												if ($scope.pianoCorrente != undefined) {
													vm
															.renderPiano($scope.pianificazioneControllerScope.pianoJson)
												} else {
													vm.tree = ""
												}

											})

							var configuratore = $scope.pianificazioneControllerScope.configuratore

							// funzione che renderizza il piano della
							// performance

							vm.renderPiano = function(piano) {
								// alert("render piano corrente")
								// renderizza il piano
								// TODO logica svg qui

								// prima elimino eventuali svg presenti
								$('#treeBody').empty();

								initTemplate(piano, vm, configuratore)
								if (vm.obiettivoDaAprire != undefined
										&& vm.obiettivoDaAprire.id != undefined) {
									// mi posizione con la
									// funzione di ricerca sul
									// nodo e poi scarico l'obiettivo altrimenti
									// da problemi con le altre versioni.

									// tanto viene ricaricato dalle funzioni
									// tipo addNode
									openNodeById(vm.obiettivoDaAprire.id, 0);
									vm.obiettivoDaAprire = undefined
								}

								// initTemplate(piano,vm,40);

							}

							// aggiunta nodi all'albero
							vm.addNode = function(idNodoPadre, titoloNodoPadre) {

								$('#creaModificaObiettivo').modal('toggle');
								vm.obiettivo = new Object();
								vm.obiettivo.idParent = idNodoPadre
								vm.obiettivo.titoloParent = titoloNodoPadre
								$scope.$apply()

							}

							vm.saveNodo = function() {

								if (vm.nuovoObiettivoForm.$valid) {
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
														$scope.pianificazioneControllerScope.pianoCorrente = response.piano
														vm.obiettivoDaAprire = vm.obiettivo

													}).error(
													function(response, status,
															headers, config) {
														alert("saveNodo "
																+ response)
													});

								} else {
									alert("Attenzione, non sono stati completati tutti i campi obbligatori del form")
								}
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
