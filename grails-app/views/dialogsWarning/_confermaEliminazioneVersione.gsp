<div id="confermaEliminazioneVersione" class="modal fade" tabindex="-1"
	role="dialog" aria-hidden="true" style="display: none;">

	<div class="modal-dialog">
		<div id="scriviModalContent" class="modal-content">
			<div id="scriviHeader" class="modal-header">
				<%--				<button type="button" class="close" data-dismiss="modal"--%>
				<%--					aria-hidden="true" style="position: relative;">×</button>--%>
				<h1 id="scriviLabel" class="modal-title btn-danger">Attenzione</h1>
			</div>
			<div id="scriviModalBody" class="modal-body form">
				<h4>L'eliminazione della versione comporta l'eliminazione
					completa di tutte le rilevazioni associate. Continuare?</h4>
			</div>
			<div id="modalFooter" class="modal-footer form">
				<button class="btn btn-danger" data-dismiss="modal"
					ng-click="pc.eliminaVersioneDefinitivamente()">SI</button>
				<button class="btn btn-success" data-dismiss="modal"
					>NO</button>

			</div>
		</div>


	</div>

</div>