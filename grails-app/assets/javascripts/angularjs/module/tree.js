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

								$('#creaModificaObiettivo').modal({
									backdrop : 'static',
									keyboard : true
								});

								vm.obiettivo = new Object();
								vm.obiettivo.padre = new Object();
								if (idNodoPadre) {
									vm.obiettivo.padre.id = idNodoPadre
									vm.obiettivo.padre.nome = titoloNodoPadre
								} else {
									vm.obiettivo.padre.nome = $scope.pianoCorrente.nomeVersione
								}
								$scope.$apply()

							}

							// mostra un nodo
							vm.showNode = function(idNodo) {
								vm.obiettivo = new Object();
								vm.obiettivo.id = idNodo

								$http
										.post(
												sessionStorage.context
														+ '/pianificazione/mostraObiettivo',
												{
													'tabId' : vm.tabId,
													'idVersione' : $scope.pianoCorrente.id,
													'obiettivo' : vm.obiettivo
												})
										.success(
												function(response, status,
														headers, config) {
													console.log(response)

													vm.obiettivo = response
													if (vm.obiettivo.padre == undefined) {
														vm.obiettivo.padre = new Object();
														vm.obiettivo.padre.nome = $scope.pianoCorrente.nomeVersione
													}
													console.log(vm.obiettivo)
													// $scope.$apply()

													$('#creaModificaObiettivo')
															.modal(
																	{
																		backdrop : 'static',
																		keyboard : true
																	})

												}).error(
												function(response, status,
														headers, config) {
													alert("mostraObiettivo "
															+ response)
													console.log("Statu "
															+ status)
													serviceUtils
															.chiudiSpinner();
												});
								$scope.$apply()

							}

							// update di un nodo dell'albero
							vm.updateNodo = function() {
								if (vm.obiettivo == undefined) {
									alert("Non trovato obiettivo da salvare")
									return

									

																		

									

																											

									

																		

									

								}

								if (vm.nuovoObiettivoForm.$valid) {
									$('#creaModificaObiettivo').modal('hide');

									// controllo se il nodo è da aggiungere o da
									// salvare

									if (vm.obiettivo.id == undefined) {
										// aggiunta

										$http
												.post(
														sessionStorage.context
																+ '/pianificazione/aggiungiObiettivo',
														{
															'tabId' : vm.tabId,
															'idVersione' : $scope.pianoCorrente.id,
															'obiettivo' : vm.obiettivo
														})
												.success(
														function(response,
																status,
																headers, config) {

															vm.obiettivo.id = response.nuovoId
															vm.obiettivoDaAprire = vm.obiettivo
															$scope.pianificazioneControllerScope.pianoJson = response.pianoJson
															$scope.pianificazioneControllerScope.pianoCorrente = response.piano

														})
												.error(
														function(response,
																status,
																headers, config) {
															alert("saveNodo "
																	+ response)
															serviceUtils
																	.chiudiSpinner();
														});

									} else {
										// update
										$http
												.post(
														sessionStorage.context
																+ '/pianificazione/salvaObiettivo',
														{
															'tabId' : vm.tabId,
															'idVersione' : $scope.pianoCorrente.id,
															'obiettivo' : vm.obiettivo
														})
												.success(
														function(response,
																status,
																headers, config) {

															vm.obiettivo.id = response.nuovoId
															$scope.pianificazioneControllerScope.pianoJson = response.pianoJson
															$scope.pianificazioneControllerScope.pianoCorrente = response.piano
															vm.obiettivoDaAprire = vm.obiettivo

														})
												.error(
														function(response,
																status,
																headers, config) {
															serviceUtils
																	.chiudiSpinner();
															alert("saveNodo "
																	+ response)
														});
									}

								} else {
									alert("Attenzione, non sono stati completati tutti i campi obbligatori del form")
								}

							}

							// elimina nodo

							vm.removeNodeWarning = function(idNodo, codiceNodo,
									nomeNodo,figliPresenti) {
								console.log(figliPresenti)
								if (figliPresenti){
									alert("Non si può eliminare un obiettivo con dei figli");
									return
								}
								
								vm.obiettivoDaEliminare = new Object();
								vm.obiettivoDaEliminare.nome = nomeNodo
								vm.obiettivoDaEliminare.codice = codiceNodo
								vm.obiettivoDaEliminare.id = idNodo
								// da fare per aggiornare le proprietà di tc,
								// altrimenti non viene visto in gsp
								// dato che è stata aggiunta prima
								$scope.$apply()
								$('#confermaEliminazioneObiettivo').modal({
									backdrop : 'static',
									keyboard : true
								})
							}

							vm.removeNode = function(idNodo) {
								$http
										.post(
												sessionStorage.context
														+ '/pianificazione/eliminaObiettivo',
												{
													'tabId' : vm.tabId,
													'idVersione' : $scope.pianoCorrente.id,
													'idObiettivo' : idNodo
												})
										.success(
												function(response, status,
														headers, config) {

													alert("Eliminato l'obiettivo "
															+ response.codice)

													
													vm.obiettivoDaAprire = new Object()
													vm.obiettivoDaAprire.id=response.parentId;
													$scope.pianificazioneControllerScope.pianoJson = response.pianoJson
													$scope.pianificazioneControllerScope.pianoCorrente = response.piano

												}).error(
												function(response, status,
														headers, config) {
													serviceUtils
															.chiudiSpinner();
													alert("removeNode "
															+ response)
												});
								vm.obiettivoDaEliminare.codice = undefined
								
							}

						} ]);
