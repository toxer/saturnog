<div id="creaModificaObiettivo" class="modal fade" tabindex="-1"
	role="dialog" aria-hidden="true" style="display: none;">

	<div class="modal-dialog">
		<div id="scriviModalContent" class="modal-content">
			<div id="scriviHeader" class="modal-header">
				<%--				<button type="button" class="close" data-dismiss="modal"--%>
				<%--					aria-hidden="true" style="position: relative;">×</button>--%>
				<h1 id="scriviLabel" class="modal-title btn-responsive">Aggiungi
					un obiettivo</h1>
			</div>

			<div id="scriviModalBody" class="modal-body" align="left">
				<form novalidate class="css-form" name="tc.nuovoObiettivoForm">
					<fieldset class="form-group">
						<label for="titoloPadre">Obiettivo padre</label> <input
							type="text" class="form-control" id="titoloPadre"
							ng-model="tc.obiettivo.titoloParent" required readonly> <small
							class="text-muted">Titolo dell'obiettivo padre</small>
					</fieldset>
					<fieldset class="form-group" hidden>
						<label for="idObiettivoPadre">Id obiettivo padre</label> <input
							type="number" class="form-control" id="idObiettivoPadre"
							ng-model="tc.obiettivo.idParent" required readonly> <small
							class="text-muted">Id obiettivo padre</small>
					</fieldset>
					<fieldset class="form-group">
						<label for="annoVersione">Titolo dell'obiettivo</label> <input
							type="text" class="form-control" id="titoloObiettivo"
							ng-model="tc.obiettivo.titolo" required> <small
							class="text-muted">Titolo sintetico dell'obiettivo</small>
					</fieldset>
					<fieldset class="form-group">
					
					<textarea ckeditor="editorOptions" ng-model="tc.obiettivo.descrizione"></textarea>
					</fieldset>
				</form>
			</div>
			<div id="modalFooter" class="modal-footer input-group-addon">
				<button class="btn btn-success"
					ng-click="tc.aggiungiNuovoObiettivo()">OK</button>
				<button class="btn btn-danger" data-dismiss="modal">Annulla</button>

			</div>

		</div>


	</div>

</div>