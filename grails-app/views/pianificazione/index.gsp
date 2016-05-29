<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main" />
</head>
<body>

	<div class="container-fluid"
		ng-controller="PianificazioneController as pc">
		<g:render template="/dialogsWarning/versioneNonPresente" />
		<g:render template="/dialogs/creaNuovaVersione" />
		<g:render template="/dialogs/segliVersione" />
		<div class="row-fluid form " align="center" ng-view>
			



		</div>
	</div>

</body>