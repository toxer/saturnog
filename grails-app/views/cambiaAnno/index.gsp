<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main" />
</head>
<body>

	<div class="container-fluid" ng-controller="CambiaAnnoController as ca">
		<div class="row-fluid" align="center">
			<div class="col-3 form-group">
				<select class="form-control" ng-model="ca.anno"
					ng-options="anno for anno in ca.anni"></select>

			</div>


		</div>
	</div>

</body>