function AddSaleRow() {
	let container = document.getElementById("list-container");
	let clone = container.lastElementChild.cloneNode(true);
	let id = clone.id.substring(0, 5);
	let count = clone.id.substring(5);
	clone.childNodes[1].value = "";
	clone.childNodes[3].value = "";
	count++;
	clone.id = id + count;
	clone.lastElementChild.disabled = false;
	container.appendChild(clone);
}

function removeSaleRow(sender) {
	sender.parentNode.remove();
}