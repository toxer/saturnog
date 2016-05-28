<div id="creaNuovaVersioneDialog" class="modal fade" tabindex="-1"
	role="dialog" aria-hidden="true" style="display: none;">

	<div class="modal-dialog">
		<div id="scriviModalContent" class="modal-content">
			<div id="scriviHeader" class="modal-header">
				<%--				<button type="button" class="close" data-dismiss="modal"--%>
				<%--					aria-hidden="true" style="position: relative;">×</button>--%>
				<h1 id="scriviLabel" class="modal-title btn-responsive">Crea
					una nuova versione</h1>
			</div>

			<div id="scriviModalBody" class="modal-body">
				<form novalidate class="css-form" name="pc.nuovaVersioneForm">
					<fieldset class="form-group">
						<label for="annoVersione">Anno della versione</label> <input
							type="number" class="form-control" id="annoVersione"
							ng-model="pc.piano.anno" required readonly> <small
							class="text-muted">Anno corrente</small>
					</fieldset>
					<fieldset class="form-group">
						<label for="codiceVersione">Codice della versione</label> <input
							type="text" class="form-control" id="codiceVersione"
							ng-model="pc.piano.nomeVersione" required readonly> <small
							class="text-muted">Identificativo della versione nel
							sistema</small>
					</fieldset>
					<fieldset class="form-group">
						<label for="noteVersione">Note</label>
						<textarea  style="resize: none" class="form-control"
							id="noteVersione" ng-model="pc.piano.note" rows="5" cols="50"></textarea>
						<small class="text-muted">Note sulla versione</small>
					</fieldset>
				</form>
			</div>
			<div id="modalFooter" class="modal-footer input-group-addon">
				<button class="btn btn-primary" ng-click="pc.creaNuovaVersione()">OK</button>
				<button class="btn btn-danger" data-dismiss="modal"
					ng-click="pc.exitNuovaVersioneDialog()">Annulla</button>

			</div>

		</div>


	</div>

</div>