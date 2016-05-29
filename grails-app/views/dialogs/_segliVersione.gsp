<div id="scegliVersioneDialog" class="modal fade" tabindex="-1"
	role="dialog" aria-hidden="true" style="display: none;">

	<div class="modal-dialog">
		<div id="" class="modal-content">
			<div id="scriviHeader" class="modal-header">
				<%--				<button type="button" class="close" data-dismiss="modal"--%>
				<%--					aria-hidden="true" style="position: relative;">×</button>--%>
				<h1 id="scriviLabel" class="modal-title btn-responsive">Seleziona
					una versione</h1>
			</div>

			<div id="scriviModalBody" class="modal-body">
				<form novalidate  name="pc.versioneForm">
				<fieldset class="form-group">
						<label for="pianiSelect">Codice della versione</label> <select
							ng-model="pc.pianoCorrente" id="pianiSelect"
							class="form-control"
							ng-options="piano as piano.nomeVersione for piano in pc.piani ">
							
						</select> <small class="text-muted">Identificativo della versione
							nel sistema</small>
					</fieldset>
					<fieldset class="form-group">
						<label for="annoVersione">Anno della versione</label> <input
							type="number" class="form-control" id="annoVersione"
							ng-model="pc.pianoCorrente.anno" required readonly> <small
							class="text-muted">Anno corrente</small>
					</fieldset>
					
					<fieldset class="form-group">
						<label for="noteVersione">Note</label>
						<textarea readonly style="resize: none" class="form-control"
							id="noteVersione" ng-model="pc.pianoCorrente.note" rows="5"
							cols="50"></textarea>
						<small class="text-muted">Note sulla versione</small>
					</fieldset>
					<fieldset class="form-group">
						<button class="btn btn-success" data-dismiss="modal" ng-click="pc.creaNuovaVersioneOpenDialog();">Crea una nuova versione</button>
					</fieldset>
				</form>
			</div>
			<div id="modalFooter" class="modal-footer ">
				<button class="btn btn-primary" ng-click="pc.segliVersione()">OK</button>
				<button class="btn btn-danger" data-dismiss="modal">Annulla</button>

			</div>

		</div>


	</div>

</div>