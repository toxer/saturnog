var treeController = undefined


function closeContextMenu() {
	d3.select('.context-menu').remove();
}

// recupero l'oggetto piano corrente
var margin = {
	top : 20,
	right : 120,
	bottom : 20,
	left : 120
}, width = 1024 - margin.right - margin.left, height = 800 - margin.top
		- margin.bottom;

var root = undefined
var i = 0, duration = 750, rectW = 120, rectH = 60, nodeDepth = 180;

var tree = d3.layout.tree().nodeSize([ rectW + 10, rectH + 10 ]);
var diagonal = d3.svg.diagonal().projection(function(d) {
	return [ d.x + rectW / 2, d.y + rectH / 2 ];
});

var svg = undefined;
var yToGo = undefined

// necessary so that zoom knows where to zoom and unzoom from

// _children figli collassati
// children figli non collassati

function collapse(d) {
	if (d.children) {
		d._children = d.children;
		d._children.forEach(collapse);
		d.children = null;
	}
}

function initTemplate(_root, _treeController) {
	treeController = _treeController
	root = _root;
	root.children.forEach(collapse);
	// ricavo la massima ampiezza disponibile
	
	
	
	var maxIpoteticalWidth = (countAllNode(root) > 0 ? countAllNode(root)
			: 1)
			* (rectW * 2 + 60);
	

	svg = d3.select('#treeBody').append("svg")
			.attr("width", maxIpoteticalWidth).attr("height", 3000).attr("id","treeSvg").call(
					zm = d3.behavior.zoom().scaleExtent([ 1, 3 ]).on("zoom",
							redraw)).append("g").attr("transform",
					"translate(" + (maxIpoteticalWidth / 2) + "," + 20 + ")").attr("id","svgG");
	// zm.translate([ 350, 20 ]);
	setCorrectWidth(root);
	root.x0 = 0;
	root.y0 = height / 2;
	

	

	update(root);
	// forzo lo scroll al centro orizzontalmente basandomi sul primo nodo

	if (root.children != undefined && root.children.length > 0) {

		$('#treeContainer').scrollLeft(
				(maxIpoteticalWidth / 2) - (Math.abs(root.children[0].x0))
						+ (rectW + 20))
	} else {
		// $('#treeContainer').scrollLeft((maxIpoteticalWidth/2)-(Math.abs(root.x0))+(rectW+20))
	}

}





function update(source) {

	// Compute the new tree layout.
	var nodes = tree.nodes(root).reverse(), links = tree.links(nodes);

	// Normalize for fixed-depth.
	nodes.forEach(function(d) {
		d.y = d.depth * nodeDepth;
	});

	// Update the nodes…
	var node = svg.selectAll("g.node").data(nodes, function(d) {
		return d.id || (d.id = ++i);
	});

	// Enter any new nodes at the parent's previous position.
	var nodeEnter = node.enter().append("g").attr("class", "node").attr(
			"transform", function(d) {
				return "translate(" + source.x0 + "," + source.y0 + ")";
			}).on("click", click);

	// nodeEnter.on("contextmenu", function() {
	// contextMenu(d3.mouse(this)[0], d3.mouse(this)[1], this)
	// });

	nodeEnter.on("contextmenu", d3.contextMenu(contextMenuItems));

	nodeEnter.append("rect").attr("width", rectW).attr("height", rectH).attr(
			"stroke", "black").attr("stroke-width", 1).style("fill",
			function(d) {
				return d._children ? "lightsteelblue" : "#fff";
			});

	nodeEnter.append("text").attr("x", rectW / 2).attr("y", rectH / 2).attr(
			"dy", ".35em").attr("text-anchor", "middle").text(function(d) {
		return d.name;
	});

	// applico l'id del nodo come attribute
	nodeEnter.attr("id", function(d) {
		return d.id
	})

	// Transition nodes to their new position.
	var nodeUpdate = node.transition().duration(duration).attr("transform",
			function(d) {
				return "translate(" + d.x + "," + d.y + ")";
			});

	nodeUpdate.select("rect").attr("width", rectW).attr("height", rectH).attr(
			"stroke", "black").attr("stroke-width", 1).style("fill",
			function(d) {
				return d._children ? "lightsteelblue" : "#fff";
			});

	nodeUpdate.select("text").style("fill-opacity", 1);

	// Transition exiting nodes to the parent's new position.
	var nodeExit = node.exit().transition().duration(duration).attr(
			"transform", function(d) {
				return "translate(" + source.x + "," + source.y + ")";
			}).remove();

	nodeExit.select("rect").attr("width", rectW).attr("height", rectH)
	// .attr("width", bbox.getBBox().width)""
	// .attr("height", bbox.getBBox().height)
	.attr("stroke", "black").attr("stroke-width", 1);

	nodeExit.select("text");

	// Update the links…
	var link = svg.selectAll("path.link").data(links, function(d) {
		return d.target.id;
	});

	// Enter any new links at the parent's previous position.
	link.enter().insert("path", "g").attr("class", "link").attr("x", rectW / 2)
			.attr("y", rectH / 2).attr("d", function(d) {
				var o = {
					x : source.x0,
					y : source.y0
				};
				return diagonal({
					source : o,
					target : o
				});
			});

	// Transition links to their new position.
	link.transition().duration(duration).attr("d", diagonal);

	// Transition exiting nodes to the parent's new position.
	link.exit().transition().duration(duration).attr("d", function(d) {
		var o = {
			x : source.x,
			y : source.y
		};
		return diagonal({
			source : o,
			target : o
		});
	}).remove();

	// Stash the old positions for transition.

	nodes.forEach(function(d) {

		d.x0 = d.x;
		d.y0 = d.y;
	});
	
	setCorrectWidth(root)

}

// Toggle children on click.
function click(d) {
	var lastY = $('#treeContainer').scrollTop();
	console.log(d)

	closeContextMenu();

	var swift = nodeDepth - 4 * rectH

	if (d.children) {

		// chiusura del nodo

		d._children = d.children;
		d.children = null;
		// yToGo è la posizione del padre
		yToGo = d.y0 - 4 * rectH + nodeDepth

	} else if (d._children) {

		// apertura nodo
		// yToGo è la posizione del padre + depth
		yToGo = d.y0 + nodeDepth - 4 * rectH
		d.children = d._children;
		d._children = null;

	}
	update(d);
	$('#treeContainer').scrollTop(yToGo)

}

// Redraw for zoom
function redraw() {

	// console.log("here", d3.event.translate, d3.event.scale);
	// svg.attr("transform", "translate(" + d3.event.translate + ")" + " scale("
	// + d3.event.scale + ")");
}