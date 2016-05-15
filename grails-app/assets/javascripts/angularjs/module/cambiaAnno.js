var cambiaAnno = angular.module("CambiaAnno",[]);

cambiaAnno.controller('CambiaAnnoController', [ '$scope','$http',function($scope,$http) {
	var vm=this
	vm.tabId = sessionStorage.tabId;
	
	//richiedo gli anni disponibili
	$http.post(sessionStorage.context + '/cambiaAnno/getAnni', {
		'tabId' : vm.tabId
	}).success(function(response) {
		
		vm.anni=response
	});
	
}]);
