<div id="eliminaVersioneDialog" class="modal fade" tabindex="-1"
	role="dialog" aria-hidden="true" style="display: none;">

	<div class="modal-dialog">
		<div id="" class="modal-content">
			<div id="scriviHeader" class="modal-header">
				<%--				<button type="button" class="close" data-dismiss="modal"--%>
				<%--					aria-hidden="true" style="position: relative;">×</button>--%>
				<h1 id="scriviLabel" class="modal-title btn-responsive btn-danger">Elimina una versione </h1>
			</div>

			<div id="scriviModalBody" class="modal-body">
				<form novalidate  name="pc.versioneForm">
				<fieldset class="form-group">
						<label for="pianiSelect">Codice della versione</label> <input type="text" ng-model="pc.pianoCorrente.nomeVersione" readonly ></input>
							
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
						<label for="creataDa">Creata da</label>
						<input type="text" readonly style="resize: none" class="form-control"
							id="creataDa" ng-model="pc.pianoCorrente.creatoDa" 
							></textarea>
						<small class="text-muted">Utente creatore della versione</small>
					</fieldset>
				</form>
			</div>
			<div id="modalFooter" class="modal-footer ">
				<button class="btn btn-danger" ng-click="pc.eliminaVersioneOpenDialog()">Elimina</button>
				<button class="btn btn-primary" data-dismiss="modal">Annulla</button>

			</div>

		</div>


	</div>

</div>