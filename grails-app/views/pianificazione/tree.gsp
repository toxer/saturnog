<asset:javascript src="angularjs/treeTemplates/templateAlberoBlocchi.js" />
<style>

.node rect {
  cursor: pointer;
  fill: #fff;
  fill-opacity: .5;
  stroke: #3182bd;
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
	<div class="cointainer-fluid">
		<div id ="treeContainer" class="row-fluid pre-scrollable">
			<div id="treeBody"></div>
		</div>
	</div>
</div>