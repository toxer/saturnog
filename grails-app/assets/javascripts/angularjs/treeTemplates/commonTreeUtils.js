var contextMenuItems = [ {
	title : 'Aggiungi un figlio',
	action : function(elm, d, i) {
		// utilizzo le funzioni del TreeController passato in fase di init

		treeController.addChild(d.idNodo);

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



function findById(idNodo, nodoRadice,open) {

	if (nodoRadice == undefined) {
		return undefined
	}
	if (nodoRadice.idNodo == idNodo) {
		//applico l'apertura ricorsiva del path
		if (open){
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
			nodoTrovato = findById(idNodo, d,open)

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
			nodoTrovato = findById(idNodo, d,open)

		});
	}
	
	
	return nodoTrovato

}

function openNodeById(idNodo) {
	
	var nodo = undefined
	if (idNodo != undefined && root != undefined) {
		nodo = findById(idNodo, root,true);
		if (nodo != undefined){
			
			update(root)
			
			
		}else{
			alert("Nodo con id: "+idNodo+" non trovato");
		}
	}

}

function openNode(nodo) {
	if (nodo == undefined) {
		return;
	}
	console.log("Nodo: "+nodo.name+" "+nodo._children)
	//apro il nodo se nencessario
	if (nodo._children){
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
