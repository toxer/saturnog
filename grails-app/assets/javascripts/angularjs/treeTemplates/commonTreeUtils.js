var contextMenuItems = [ {
	title : 'Aggiungi un figlio',
	action : function(elm, d, i) {
		// utilizzo le funzioni del TreeController passato in fase di init

		treeController.addChild(d.idNodo,d.name);

	},
	disabled : false
// optional, defaults to false
}, {
	title : 'Aggiungi un fratello',
	action : function(elm, d, i) {
		if (d.idNodo == undefined) {
			
		} else {
			treeController.addSiblings(d.idNodo);
		}
	},
	disabled : false
} ]

function findById(idNodo, nodoRadice, open) {

	if (nodoRadice == undefined) {
		return undefined
	}
	if (nodoRadice.idNodo == idNodo) {
		// applico l'apertura ricorsiva del path
		if (open) {
			openNode(nodoRadice)
		}

		return nodoRadice;
	}

	var nodoTrovato = undefined;

	if (nodoRadice._children != undefined) {
		nodoRadice._children.forEach(function(d) {
			if (d.parent == undefined) {
				d.parent = nodoRadice;
			}

			if (nodoTrovato != undefined) {
				return true;
			}
			nodoTrovato = findById(idNodo, d, open)

		});
	}

	if (nodoTrovato != undefined) {

		return nodoTrovato
	}

	if (nodoRadice.children != undefined) {

		nodoRadice.children.forEach(function(d) {
			if (d.parent == undefined) {
				d.parent = nodoRadice;
			}

			if (nodoTrovato != undefined) {
				return true;
			}
			nodoTrovato = findById(idNodo, d, open)

		});
	}
	return nodoTrovato

}

function openNodeById(idNodo,duration) {

	var nodo = undefined
	if (idNodo != undefined && root != undefined) {
		nodo = findById(idNodo, root, true);
		if (nodo != undefined) {
			update(root,duration)
			// scrolla fino al nodo

			$('#treeContainer').scrollTop(nodo.y0 - nodeDepth)
			$('#treeContainer').scrollLeft(nodo.x0)

		} else {
			alert("Nodo con id: " + idNodo + " non trovato");
		}
	}

}

function openNode(nodo) {
	if (nodo == undefined) {
		return;
	}
	// apro il nodo se nencessario
	if (nodo._children) {
		nodo.children = nodo._children;
		nodo._children = undefined
	}

	// controllo se il nodo è aperto controllando che il padre sia aperto

	openNode(nodo.parent);

}

function openChild(parentNode) {
	if (parentNode._children != undefined && parentNode._children.length > 0) {
		if (parentNode.children == undefined) {
			parentNode.children = []
		}
		parentNode._children.forEach(function(d) {
			parentNode.children.push(d);
		});
		parentNode.children = undefined;

	}
}

function countAllNode(rootNode) {
	var actualNode = 1;// se stesso
	var figli = allChild(rootNode);
	figli.forEach(function(d) {
		actualNode += countAllNode(d) // il conteggio dei figli del figlio
	});
	return actualNode;
}

function nodiInArray(rootNode, onlyOpen) {
	return explodeNodeToArray(rootNode, undefined, onlyOpen)[0]
}

function explodeNodeToArray(rootNode, nodeArray, onlyOpen) {
	rootNode = tree(rootNode)
	if (rootNode == undefined) {
		return nodeArray;
	}
	if (nodeArray == undefined) {
		nodeArray = [];
	}
	nodeArray.push(rootNode);
	var figli = allChild(rootNode, onlyOpen);

	figli.forEach(function(d) {
		explodeNodeToArray(d, nodeArray, onlyOpen);
	});

	return nodeArray;

}

// mappa livello,numero nodi
function maxNodeForLevel(rootNode,onlyOpen) {
	// divido per depth
	var mappaNodi = {};
	var nodi = nodiInArray(rootNode,onlyOpen);
	nodi.forEach(function(d) {

		if (mappaNodi[d.depth] == undefined) {
			mappaNodi[d.depth] = 0;
		}
		mappaNodi[d.depth] = mappaNodi[d.depth] + 1;
	});
	return mappaNodi
}

//numero massimo di nodi trovato in un livello
function maxNodeInLevel(rootNode,onlyOpen){
	var maxNode =0;
	var mappaNodi = maxNodeForLevel(rootNode,onlyOpen)
	
	Object.keys(mappaNodi).forEach(function(d){
		if(mappaNodi[d]>maxNode){
			maxNode=mappaNodi[d]
		}
	});
	return maxNode;
	
	
	
}

function allChild(rootNode, onlyOpen) {

	var figli = []

	if (rootNode.children != undefined) {
		rootNode.children.forEach(function(d) {
			figli.push(d);
		})
	}
	if (onlyOpen == undefined || !onlyOpen) {
		if (rootNode._children != undefined) {
			rootNode._children.forEach(function(d) {
				figli.push(d)
			})
		}
	}
	return figli;

}



function setCorrectWidth(rootNode) {
	// tutti i soli nodi aperti in array
	var nodiAperti = nodiInArray(rootNode, true)
	var minWidth = undefined
	var maxWidth = undefined

	nodiAperti.forEach(function(d) {
		if (minWidth == undefined) {
			minWidth = d.x
			maxWidth = d.x + rectW
		} else {

			if (d.x < minWidth) {
				minWidth = d.x;
			}
		}
		if (d.x > maxWidth) {
			maxWidth = d.x;
		}

	});
	var numeroMassimoNodiInLivello=maxNodeInLevel(rootNode);
	var width = (Math.abs(minWidth) + Math.abs(maxWidth)) + rectW * numeroMassimoNodiInLivello
	// modifico la width dell'svg
		$('#treeSvg').attr("width", width)
		$('#svgG').attr("transform",
				"translate(" + ((width-rectW) / 2)+ "," + 20 + ")")
	

	return (Math.abs(minWidth) + Math.abs(maxWidth))
}
