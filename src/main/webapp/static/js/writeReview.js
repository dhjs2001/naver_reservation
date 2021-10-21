/**
 * 
 */
function Rank(starClass, scoreClass, scoreInputId) {
	this.starNode = document.querySelectorAll("." + starClass);
	this.scoreNode = document.querySelector('.' + scoreClass);
	this.scoreInputNode = document.querySelector('#' + scoreInputId);

	this.drawingStars = function(redStarNumber) {
		const grayStarURL = "/getImage/img/grayStar.png";
		const redStarURL = "/getImage/img/redStar.png";
		this.starNode.forEach((node) => {
			node.src = grayStarURL;
		});

		this.starNode.forEach((node, i) => {
			if (i + 1 > redStarNumber) {
				return false;
			} else if (i + 1 == redStarNumber) {
				this.scoreNode.innerHTML = redStarNumber;
				this.scoreNode.style.color = "black";
				this.scoreInputNode.value = redStarNumber;
			}
			node.src = redStarURL;
		});
	}

	this.addEventStarNodes = function() {
		this.starNode.forEach((node, i) => {
			node.addEventListener("click", () => {
				this.drawingStars(i + 1);
			});
		});
	}
}

function Validation(scoreInputId, textareaNodeId) {
	this.scoreInputNode = document.querySelector('#' + scoreInputId);
	this.textareaNode = document.querySelector('#' + textareaNodeId);
	this.checkValidation = function() {
		if (!parseInt(this.scoreInputNode.value)) {
			alert('평점을 선택해주세요.');
			return false;
		}

		if (!this.textareaNode.value) {
			alert('리뷰 내용을 입력해주세요.');
			return false;
		}

		return true;

	}
}
function Thumbnail(fileInputNodeId, imgContainerNodeClass) {
	this.fileNode = document.querySelector("#" + fileInputNodeId);
	this.imgContainerNode = document.querySelector("." + imgContainerNodeClass);
	this.fileBuffer;
	this.getFileBuffer = function() {
		return this.fileBuffer;
	}
	this.setFileBuffer = function(fileList) {
		this.fileBuffer = Array.from(fileList);
	}

	this.createThumbnail = function(node, imgContainerNode) {
		const files = node.files;
		const filesLength = files.length;
		let html = '';
		for (let i = 0; i < filesLength; i++) {
			const file = files[i];
			if(!this.validImageType(file)) {
				alert('이미지가 아닌 파일이 존재합니다.');
				node.value = null;
				return;
			}
			const url = window.URL.createObjectURL(file);
			html += "<div class='img-item'><img class = 'thumbnail' src=" + url + "><div class='cancel'>X</div></div>";
		}

		this.setFileBuffer(files);
		node.value = null;
		imgContainerNode.innerHTML = html;
	}
	this.validImageType = function(file) {
		const result = (['image/jpeg', 'image/png', 'image/jpg'].indexOf(file.type) > -1);
		return result;
	}


	this.deleteThumbnail = function(index, node) {
		node.remove();
		this.fileBuffer.splice(index, 1);
	}

	this.addEventThumbnail = function() {
		this.fileNode.addEventListener("change", (evt) => {
			this.createThumbnail(evt.target, this.imgContainerNode);
			this.addEventCancelBox();
		});
	}

	this.addEventCancelBox = function() {
		const cancelNode = document.querySelectorAll('.cancel');
		cancelNode.forEach((v, i) => {
			v.addEventListener("click", (evt) => {
				this.deleteThumbnail(i, evt.target.parentElement);
			});
		});
	}
}


function ajax(formNode, fileList) {
	let result;
	const formData = new FormData(formNode);
	if (fileList) {
		for (let i = 0; i < fileList.length; i++) {
			formData.append("fileList", fileList[i]);
		}
	}

	const request = new XMLHttpRequest();
	request.addEventListener("load", (evt) => {
		result = evt.target.responseText;
	});

	request.open("POST", "/writeReview", false);
	request.send(formData);
	return result;
}



function drawingTextLength() {
	const textareaNode = document.querySelector('#review-content');
	const currentTextLengthNode = document.querySelector('.text-limit > span');
	const textLength = textareaNode.textLength;
	currentTextLengthNode.innerText = textLength;
}

const rank = new Rank("star", "score", "score");
const validation = new Validation("score", "review-content");
const thumbnail = new Thumbnail("picture-input", "img-container")

window.addEventListener("load", () => {
	rank.addEventStarNodes(); const textareaNode = document.querySelector('#review-content');
	textareaNode.addEventListener("input", drawingTextLength);
	thumbnail.addEventThumbnail();
	const formNode = document.querySelector('form');
	formNode.addEventListener('submit', (evt) => {
		let result;
		if (validation.checkValidation()) {
			result = ajax(evt.target, thumbnail.getFileBuffer());
		} else {
			return false;
		}
		if (result) {
			document.location.href = "/review";
		} else {
			alert('ajax 오류');
		}

	});
});