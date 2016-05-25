<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main" />
</head>
<body>

	<div class="container-fluid" ng-controller="CambiaAnnoController as ca">
		<div class="row-fluid form " align="center">

			<div class="col-xs-3 col-xs-push-4 form-border">
				<label for="anniSelect">Seleziona l'anno su cui operare</label> <select
					style="padding-bottom: 5px" id="anniSelect" class="form-control"
					ng-model="ca.anno" ng-options="anno for anno in ca.anni"></select>
				<button ng-click="ca.selezionaAnno();" style="margin-top:10px" class="btn btn-primary">Ok</button>

			</div>


		</div>
	</div>

</body>