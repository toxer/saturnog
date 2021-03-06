var pianificazione = angular.module("Pianificazione", [ 'ui.router', 'Tree' ]);
// view per la pagina di pianificazione
pianificazione.config([ '$stateProvider', function($stateProvider) {
	$stateProvider.state('default', {
		// risponde all'url /pianificazione/index/#/prova
		url : '',

		views : {
			'mainView' : {
				// template: "<div>Test</div>"
				templateUrl : "/saturno/pianificazione/noTree"
			// add controller
			}

		}
	}).state('alberoStandard', {
		// risponde all'url /pianificazione/index/#/prova
		url : 'standard',

		views : {
			'mainView' : {
				// template: "<div>Test</div>"
				templateUrl : "/saturno/pianificazione/treeStandard"
			// add controller
			}

		}
	}).state('bsc', {
		// risponde all'url /pianificazione/index/#/prova
		url : 'standard',

		views : {
			'mainView' : {
				// template: "<div>Test</div>"
				templateUrl : "/saturno/pianificazione/bsc"
			// add controller
			}

		}
	}).state('performance', {
		// risponde all'url /pianificazione/index/#/prova
		url : 'standard',

		views : {
			'mainView' : {
				// template: "<div>Test</div>"
				templateUrl : "/saturno/pianificazione/performance"
			// add controller
			}

		}
	});
} ]);

// servizio usato per comunicare con TreeController

pianificazione
		.controller(
				'PianificazioneController',
				[
						'$scope',
						'$state',
						'$http',
						'serviceUtils',
						function($scope, $state, $http, serviceUtils) {

							var vm = this
							vm.tabId = sessionStorage.tabId;
							$scope.pianificazioneControllerScope = $scope

							// funzione che esegue il test per vedere se ho
							// almeno una versione
							// presente
							// se si apre la finestra di selezione, altrimenti
							// apre quella di
							// creazione
							vm.scegliVersioneOpenDialog = function() {
								$http
										.post(
												sessionStorage.context
														+ '/pianificazione/testVersionExist',
												{
													'tabId' : vm.tabId
												})
										.success(
												function(response, status,
														headers, config) {
													var versione = response

													if (!versione.versionePresente) {
														$(
																'#versioneNonPresenteDialog')
																.modal(
																		{
																			backdrop : 'static',
																			keyboard : true
																		})
													} else {
														// salvo le versioni
														// presenti nel piano

														vm.piani = versione.versioni
														vm.pianoCorrente = versione.versioni[0];
														// apri seleziona
														// versione
														$(
																'#scegliVersioneDialog')
																.modal(
																		{
																			backdrop : 'static',
																			keyboard : true
																		});
													}

												}).error(
												function(response, status,
														headers, config) {
													alert(response)
												});

								// funzioni del controller
							}

							vm.creaNuovaVersioneOpenDialog = function() {

								// recupero il prossimo piano

								$http
										.post(
												sessionStorage.context
														+ '/pianificazione/prossimoPianoLibero',
												{
													'tabId' : vm.tabId
												})
										.success(
												function(response, status,
														headers, config) {

													vm.piano = response
													$(
															'#creaNuovaVersioneDialog')
															.modal(
																	{
																		backdrop : 'static',
																		keyboard : true
																	})

												}).error(
												function(response, status,
														headers, config) {
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
									vm.piano.id = undefined
									
									$http
											.post(
													sessionStorage.context
															+ '/pianificazione/creaNuovaVersione',
													{
														'tabId' : vm.tabId,
														'piano' : vm.piano
													})
											.success(
													function(response, status,
															headers, config) {

														$(
																'#creaNuovaVersioneDialog')
																.modal('toggle');

														// set della variabile
														// locale al controlle
														// e di quello scope in
														// watch dal
														// TreeController
														vm.pianoCorrente = response
														$scope.pianoCorrente = vm.pianoCorrente
														serviceUtils
																.userObject(
																		$scope,
																		undefined,
																		undefined,
																		vm.pianoCorrente.id)
														serviceUtils
																.updateBreadcumb("Pianificazione "
																		+ vm.pianoCorrente.nomeVersione)
														alert("Nuova versione creata "
																+ vm.pianoCorrente.nomeVersione)

														// vado in noTree
														$http
																.post(
																		sessionStorage.context
																				+ '/pianificazione/stampaAlberoCompleto',
																		{
																			'tabId' : vm.tabId,
																			'idVersione' : vm.pianoCorrente.id
																		})
																.success(
																		function(
																				response,
																				status,
																				headers,
																				config) {

																			$scope.pianoJson = response.pianoJson
																			$scope.configuratore = response.configuratore

																		})
																.error(
																		function(
																				response,
																				status,
																				headers,
																				config) {
																			serviceUtils.chiudiSpinner();
																			alert(response)
																		});
														$state.go('default')

													}).error(
													function(response, status,
															headers, config) {
														alert(response)
													});
								} else {
									// alert('form non valido')
								}

							}

							// elimina tutte le variabili d'ambiente
							// e lo user object
							vm.resettaVersione = function() {
								// cancellazioni delle variabili d'ambiente
								vm.pianoCorrente = null
								$scope.pianoCorrente = null
								// aggiornamento dell'userObject locale
								serviceUtils.userObject($scope, undefined,
										undefined, "-1")
								serviceUtils.updateBreadcumb("Pianificazione ")

							}

							// seleziona una versione
							vm.scegliVersione = function() {
								// controllo se c'è il form

								if (vm.versioneForm == undefined) {
									alert("Non trovato il form di scelta versione");
									return;
								}

								serviceUtils.userObject($scope, undefined,
										undefined, vm.pianoCorrente.id)
								serviceUtils.updateBreadcumb("Pianificazione "
										+ vm.pianoCorrente.nomeVersione)

								// aggiorno la variabile di scope che è in watch
								// su
								// TreeController

								$scope.pianoCorrente = vm.pianoCorrente;
								$('#scegliVersioneDialog').modal('toggle');

								// renderizzo il piano completo
								$http
										.post(
												sessionStorage.context
														+ '/pianificazione/stampaAlberoCompleto',
												{
													'tabId' : vm.tabId,
													'idVersione' : vm.pianoCorrente.id
												})
										.success(
												function(response, status,
														headers, config) {
													// queste variabili servono
													// solo in stampa,
													// devono corrispondere al
													// pianoCorrente
													$scope.pianoJson = response.pianoJson
													$scope.configuratore = response.configuratore

												}).error(
												function(response, status,
														headers, config) {
													serviceUtils.chiudiSpinner();
													alert(response)
												});

							}

							// eliminazione della versione

							vm.eliminaVersione = function() {
								if (vm.pianoCorrente != undefined) {

									$('#eliminaVersioneDialog').modal({
										backdrop : 'static',
										keyboard : true
									})
								} else {
									alert("Piano corrente non trovato")
								}

							}

							vm.eliminaVersioneOpenDialog = function() {
								$('#eliminaVersioneDialog').modal('toggle');
								$('#confermaEliminazioneVersione').modal({
									backdrop : 'static',
									keyboard : true
								})
							}

							vm.eliminaVersioneDefinitivamente = function() {
								// eliminazione della versione dal db
								// i controlli di legitimità sono demandati al
								// backend
								$http
										.post(
												sessionStorage.context
														+ '/pianificazione/cancellaVersioneCorrente',
												{
													'tabId' : vm.tabId,
													'idVersione' : vm.pianoCorrente.id
												})
										.success(
												function(response, status,
														headers, config) {

													// cancellazioni delle
													// variabili d'ambiente
													// e quelle per la stampa
													vm.pianoCorrente = null
													$scope.pianoJson = null
													$scope.configuratore = null
													$scope.pianoCorrente = null
													// aggiornamento
													// dell'userObject locale
													serviceUtils.userObject(
															$scope, undefined,
															undefined, "-1")
													serviceUtils
															.updateBreadcumb("Pianificazione ")
													// avviso di eliminazione
													alert("Versione eliminata "
															+ response.nomeVersione)
													$state.go('default')

												}).error(
												function(response, status,
														headers, config) {
													serviceUtils.chiudiSpinner();
													alert(response)
												});

								

							}

							// per prima cosa, quando eseguo il controller,
							// faccio un test di
							// esistenza
							// delle versioni con la funzione
							vm.scegliVersioneOpenDialog();

							// eseguo il watch della variabile

							// $scope.$watch('pc.pianoCorrente',function(){
							// alert("Ok")
							// })

							// funzione per inserire dati di test in una
							// versione
							vm.datiDiTest = function() {
								$http.post(
										sessionStorage.context + '/test/test',
										{
											'tabId' : vm.tabId,
											'idVersione' : vm.pianoCorrente.id
										}).success(
										function(response, status, headers,
												config) {

											alert("Dati di test inseriti "
													+ response.nomeVersione)

										}).error(
										function(response, status, headers,
												config) {
											serviceUtils.chiudiSpinner();
											alert(response)
										});

							}

							vm.clonaVersione = function() {
								$http
										.post(
												sessionStorage.context
														+ '/test/testClonazione',
												{
													'tabId' : vm.tabId,
													'idVersione' : vm.pianoCorrente.id
												})
										.success(
												function(response, status,
														headers, config) {
													
													
													vm.pianoCorrente = null
													$scope.pianoJson = null
													$scope.configuratore = null
													$scope.pianoCorrente = null
													// aggiornamento
													// dell'userObject locale
													serviceUtils.userObject(
															$scope, undefined,
															undefined, "-1")
													serviceUtils
															.updateBreadcumb("Pianificazione ")
													// avviso di eliminazione
													alert("Versione eliminata "
															+ response.nomeVersione)
													$state.go('default')
													
													
													

													alert("Versione clonata con successo "
															+ response.nomeVersione)
													// elimino le variabili e
													// passo a noTree

												}).error(
												function(response, status,
														headers, config) {
													serviceUtils.chiudiSpinner();
													alert(response)
												});
							}
							
							vm.inserimentoMassivo=function(){

								$http
										.post(
												sessionStorage.context
														+ '/test/inserimentoMassivo',
												{
													'tabId' : vm.tabId,
													'idVersione' : vm.pianoCorrente.id
												})
										.success(
												function(response, status,
														headers, config) {

													alert("Inserimento completato")

												}).error(
												function(response, status,
														headers, config) {
													serviceUtils.chiudiSpinner();
													alert(response)
												});

							}
							
							

						} ]);
