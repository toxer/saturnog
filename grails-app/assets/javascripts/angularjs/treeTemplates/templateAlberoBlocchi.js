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
var i = 0, duration = 750, rectW = 180, rectH = 120, nodeDepth = 180, maxLineLength = 3;

var tree = d3.layout.tree().nodeSize([ rectW + 10, rectH + 10 ]);
var diagonal = d3.svg.diagonal().projection(function(d) {
	return [ d.x + rectW / 2, d.y + rectH / 2 ];
});

var svg = undefined;
var yToGo = undefined
var configurazioneLivelli

// necessary so that zoom knows where to zoom and unzoom from

// _children figli collassati
// children figli non collassati



function initTemplate(_root, _treeController, configuratore) {
	treeController = _treeController
	root = _root;
	configurazioneLivelli = configuratore!=undefined?configuratore.livelli:undefined

	
	// collapse(root)

	// ricavo la massima ampiezza disponibile

	var maxIpoteticalWidth = (countAllNode(root) > 0 ? countAllNode(root) : 1)
			* (rectW * 2 + 60);	

	svg = d3.select('#treeBody').append("svg")
			.attr("width", maxIpoteticalWidth).attr("height", 3000).attr("id",
					"treeSvg").append("g").attr("cx", (maxIpoteticalWidth / 2))
			.attr('cy', 20).attr("id", "svgG");
	
	// zm.translate([ 350, 20 ]);
	setCorrectWidth(root);
	root.x0 = 0;
	root.y0 = height / 2;
	update(root, 0);
	scrollToNode(root)
	collapse(root)
	
	

}

function update(source, now) {

	durationTransform = now != undefined ? now : duration

	// Compute the new tree layout.
	var nodes = tree.nodes(root).reverse(), links = tree.links(nodes);

	// Normalize for fixed-depth.
	nodes.forEach(function(d) {
		d.y = d.depth * nodeDepth;
		var titles = []
		// formatto il testo creando delle righe
		if (d.name != undefined) {
			d.titleLines = d.name.match(/.{1,35}/g)
		} else {
			d.titleLines = []
		}

	});

	// Update the nodes…
	var node = svg.selectAll("g.node").data(nodes, function(d) {
		return d.id || (d.id = ++i);
	});

	// Enter any new nodes at the parent's previous position.
	var nodeEnter = node.enter().append("g").attr("class", function(d) {
		return "node level" + (d.livello != undefined ? d.livello : "root");
	}).attr("transform", function(d) {
		return "translate(" + source.x0 + "," + source.y0 + ")";
	}).on("click", click)
	//.on("dblclick",dblclick);
	
	

	nodeEnter.on("contextmenu", d3.contextMenu(contextMenuItems));	
	
	nodeEnter.append("rect").attr("width", rectW).attr("height", 3 * rectH / 3)
			.attr("rx", "15").attr("ry", "15")
	nodeEnter.append("rect").attr("width", rectW).attr("height", rectH / 3)
			.attr("rx", "15").attr("ry", "15")
			
			
	var codeNode = nodeEnter.append("text").attr("x", rectW / 2).attr("y",
			rectH / 6).attr("dy", ".35em").attr("text-anchor", "middle").append("tspan").attr("style","font-weight:bold").
			text(function(d){
				var nomeSingolare =configurazioneLivelli.find(x=> x.livello === d.depth).nomeSingolare
				if (nomeSingolare != undefined){
					nomeSingolare = nomeSingolare.toUpperCase();
				}
				return nomeSingolare
				})
			.attr("y",rectH/6-5).append("tspan").attr("x",rectW/2).attr("y",
					15+rectH / 6).attr("style","font-weight:bold").text(
			function(d) {
				return d.codiceCamera;
			});

	codeNode.attr("style", "font-weight: bold;")

	var nodeText = nodeEnter.append("text").attr("class", "testo").attr(
			"text-anchor", "middle").attr("x", rectW / 2).attr("y",
			rectH / 3 + 8).attr("dy", ".35em").attr("text-anchor", "middle")
			.each(
					function(d) {
						var testo = d3.select(this);
						var h = rectH / 3 + 15;
						d.titleLines.forEach(function(k) {
							testo.append("tspan").attr("x", rectW / 2).attr(
									"y", h).text(k)
							h = h + 15
						});
					});

	// applico l'id del nodo come attribute
	nodeEnter.attr("id", function(d) {
		return d.id
	})

	var nodeUpdate = node.transition().duration(durationTransform).attr(
			"transform", function(d) {
				return "translate(" + d.x + "," + d.y + ")";
			});

	nodeUpdate
			.select("rect")
			.attr(
					"stroke",
					function(d) {
						var confLivello = configurazioneLivelli.find(x=> x.livello === d.depth)

						return confLivello != undefined ? confLivello.colore
								: "#3182bd"
					}).attr("stroke-width", 1).style("fill", function(d) {
						var confLivello = configurazioneLivelli.find(x=> x.livello === d.depth)
						if (confLivello == undefined){
							confLivello = new Object();
							confLivello.colore= "#3182bd";
						}
				return d._children ? confLivello.colore :confLivello.colore;
			}).style("fill-opacity",function(d){
				return d._children?1:0.5
			});
	

	nodeUpdate.select("text").style("fill-opacity", 1);

	// Transition exiting nodes to the parent's new position.
	var nodeExit = node.exit().transition().duration(durationTransform).attr(
			"transform", function(d) {
				return "translate(" + source.x + "," + source.y + ")";
			}).remove();

	nodeExit.select("rect").attr("width", rectW).attr("height", rectH)
	// .attr("width", bbox.getBBox().width)""
	// .attr("height", bbox.getBBox().height)
	.attr("stroke", "#000000").attr("stroke-width", 1);

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
	link.transition().duration(durationTransform).attr("d", diagonal);

	// Transition exiting nodes to the parent's new position.
	link.exit().transition().duration(durationTransform).attr("d", function(d) {
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

function dblclick(d){
	if (d.idNodo){
		treeController.showNode(d.idNodo);
		scrollToNode(d)
	}
	
}

// Toggle children on click.
function click(d) {
	var lastY = $('#treeContainer').scrollTop();	
	closeContextMenu();
	var swift = nodeDepth - 4 * rectH
	if (d.children) {
		// chiusura del nodo
		d._children = d.children;
		d.children = null;
		// yToGo è la posizione del padre
	// yToGo = d.y0 - 2 * rectH + nodeDepth

	} else if (d._children) {
		// apertura nodo
		// yToGo è la posizione del padre + depth
	// yToGo = d.y0 + nodeDepth - 2 * rectH
		d.children = d._children;
		d._children = null;

	} else {
		// nodo senza figli
	// yToGo = d.y0 + nodeDepth - 2 * rectH
	}
	
	
	update(d);

	
	scrollToNode(d)
}

// Redraw for zoom
function redraw() {

	// console.log("here", d3.event.translate, d3.event.scale);
	// svg.attr("transform", "translate(" + d3.event.translate + ")" + " scale("
	// + d3.event.scale + ")");
}