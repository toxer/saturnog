<%--Importazione css e js per context menu --%>
<asset:stylesheet src="contextMenu/3d-context-menu.css" />
<%-- Template dell'albero --%>
<asset:javascript src="angularjs/contextMenu/3d-context-menu.js" />
<asset:javascript src="angularjs/treeTemplates/commonTreeUtils.js" />
<asset:javascript src="angularjs/treeTemplates/templateAlberoBlocchi.js" />

<style>

.node rect {
  cursor: pointer;
  fill: #fff;
  fill-opacity: .5;
  stroke-width: 1.5px;
}


.deselectedNode rect{
  cursor: pointer;
  fill: #999!important;
  fill-opacity: .5;
  stroke-width: 1.5px;
}



.node text {
  font: 10px sans-serif;
  pointer-events: none;
}

path.link {
  fill: none;
  stroke: #9ecae1;
  stroke-width: 1.5px;
}

</style>


<div ng-controller="TreeController as tc">
	<g:render template="/dialogs/aggiungiModificaObiettivo" />
	<div style="max-height: none">

		<div id="treeBody"></div>
	</div>

</div>