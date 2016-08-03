<!DOCTYPE html>
<html>
<head>


<meta name="layout" content="main" />

</head>
<%--Nota, per poter funzionare il watch dello scope, i controlli devono essere nested tra di loro--%>
<body>

	<div class="container-fluid"
		ng-controller="PianificazioneController as pc">
		<g:render template="/dialogsWarning/versioneNonPresente" />
		<g:render template="/dialogsWarning/confermaEliminazioneVersione" />
		<g:render template="/dialogs/creaNuovaVersione" />
		<g:render template="/dialogs/segliVersione" />
		<g:render template="/dialogs/eliminaVersione" />
		<div class="container-fluid menu" id="pianificazioneMenu">
			<div class="row-fluid">
				<div class="col-xs-3" align="left">
					<button class="btn btn-primary"
						ng-click="pc.scegliVersioneOpenDialog()">Cambia versione</button>
					<button class="btn btn-warning"
						ng-click="pc.creaNuovaVersioneOpenDialog();">Crea nuova
						versione</button>
<%--					<button class="btn btn-warning"--%>
<%--						ng-click="pc.inserimentoMassivo();">Inserimento massivo</button>--%>
<%--						<a class="btn btn-danger" ng-click="pc.clonaVersione()">Clona questa versione</a>--%>
				</div>
				<div class="col-xs-6" align="center">
				<a class="btn btn-success" ui-sref="alberoStandard">Modalità standard</a>
				<a class="btn btn-success" ui-sref="bsc">Modalità bsc</a>
				<a class="btn btn-success" ui-sref="performance">Modalità performance</a>
				</div>
				<div class="col-xs-3" align="right">
					<button class="btn btn-danger" ng-click="pc.eliminaVersione();">Elimina
						questa versione</button>
				</div>
			</div>
		</div>
		<div class="row-fluid" id="treeRender"
			 align="center">

<%--			<div ui-view="leftMenu" class="col-md-3"></div>--%>
			<div ui-view="mainView"  id ="treeContainer" class="col-md-12 pre-scrollable" style="height:540px">
				
			</div>


		</div>


	</div>




</body>