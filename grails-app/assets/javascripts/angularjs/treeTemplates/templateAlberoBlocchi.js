/*context menu*/
function contextMenu(x,y,id){
	console.log(x,y,id)
	closeContextMenu();
}

function closeContextMenu(){
	  d3.select('.context-menu').remove();
}


//recupero l'oggetto piano corrente
var margin = {
	top : 20,
	right : 120,
	bottom : 20,
	left : 120
}, width = 960 - margin.right - margin.left, height = 800 - margin.top
		- margin.bottom;


var root = undefined
var i = 0, duration = 750, rectW =120, rectH = 60,nodeDepth=180;

var tree = d3.layout.tree().nodeSize([ rectW+10, rectH+10 ]);
var diagonal = d3.svg.diagonal().projection(function(d) {
	return [ d.x + rectW / 2, d.y + rectH / 2 ];
});

var svg = undefined;


// necessary so that zoom knows where to zoom and unzoom from

//_children figli collassati
//children figli non collassati

function collapse(d) {
	if (d.children) {
		d._children = d.children;
		d._children.forEach(collapse);
		d.children = null;
	}
}

function initTemplate(_root) {
	root = _root;

	
	
	svg= d3.select('#treeBody').append("svg").attr("width", "1024").attr("height",
			3000).call(
			zm = d3.behavior.zoom().scaleExtent([ 1, 3 ]).on("zoom", redraw))
			.append("g").attr("transform", "translate(" + 512 + "," + 20 + ")");
	//zm.translate([ 350, 20 ]);

	root.x0 = 0;
	root.y0 = height / 2;
	root.children.forEach(collapse);
	update(root);

	
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
	
	nodeEnter.on("contextmenu",function(){contextMenu(d3.mouse(this)[0], d3.mouse(this)[1],this.id)});

	nodeEnter.append("rect").attr("width", rectW).attr("height", rectH).attr(
			"stroke", "black").attr("stroke-width", 1).style("fill",
			function(d) {
				return d._children ? "lightsteelblue" : "#fff";
			});

	nodeEnter.append("text").attr("x", rectW / 2).attr("y", rectH / 2).attr(
			"dy", ".35em").attr("text-anchor", "middle").text(function(d) {
		return d.name;
	});
	
	//applico l'id del nodo come attribute
	nodeEnter.attr("id",function(d){return d.id})
	

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
	
	
	
	
}

// Toggle children on click.
function click(d) {
	
	closeContextMenu();
	
	
	if (d.children) {
		//chiusura del nodo
		$('#treeContainer').scrollTop(d.y)
		d._children = d.children;
		d.children = null;
	} else {
		//apertura del nodo
		$('#treeContainer').scrollTop((d.y+nodeDepth))
		d.children = d._children;
		d._children = null;
	}
	update(d);
}

// Redraw for zoom
function redraw() {
	
	// console.log("here", d3.event.translate, d3.event.scale);
//	svg.attr("transform", "translate(" + d3.event.translate + ")" + " scale("
//			+ d3.event.scale + ")");
}