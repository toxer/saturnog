<div id="cambiaCamera" class="modal fade" tabindex="-1" role="dialog"
	aria-hidden="true" style="display: none;">
	
	<div id="cambiaCameraDialog" class="modal-dialog">
		<div id="scriviModalContent" class="modal-content">
			<div id="scriviHeader" class="modal-header">
				<%--				<button type="button" class="close" data-dismiss="modal"--%>
				<%--					aria-hidden="true" style="position: relative;">×</button>--%>
				<h1 id="scriviLabel"  class="modal-title btn-responsive">Seleziona
					la camera</h1>
			</div>
			<div id="scriviModalBody" class="modal-body form">
				<div class="form-group">
					<select ng-model="mc.enteSelezionato" id="aziendaIdSelect"  class="form-control">
						  <option ng-repeat="ente in mc.entiPossibili" value="{{ente.aziendaId}}">{{ente.nome}}</option>
					</select>
				</div>
			</div>
			<div id="modalFooter" class="modal-footer form">
						<button class="btn-btn-primary" data-dismiss="modal" ng-click="mc.selectEnte()" >OK</button>
			
<%--			<button class="btn-btn-primary" data-dismiss="modal" onclick="saveCurrentObject({ente:$('#aziendaIdSelect').val()})">OK</button>--%>
			
<%--				<button class="btn btn-default" data-dismiss="modal"--%>
<%--					onclick="setupEnteInSession('${request.contextPath}',$('#aziendaId').val());console.log('test'+testEnteBeforeSubmit('${request.contextPath}'))">OK</button>--%>
<%--				<button class="btn btn-default" data-dismiss="modal"--%>
<%--					onclick="jQuery.ajax({type:'POST',data:'ente=' + $('#aziendaId').val(), url:'/saturno/start/updateEnte',success:function(data,textStatus){updateEnteInStorage(data);},error:function(XMLHttpRequest,textStatus,errorThrown){}});">OK</button>--%>
			</div>
		</div>


	</div>

</div>