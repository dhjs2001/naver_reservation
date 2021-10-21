/**
 * 
 */
const back = document.querySelector('.top > span');
back.addEventListener("click", () => {
	window.history.back();
})

function tikets(leftButtons, rightButtons, tiketsInputs, price, totalPrice) {
	this.leftButtons = document.querySelectorAll('.' + leftButtons);
	this.rightButtons = document.querySelectorAll('.' + rightButtons);
	this.tiketsInputs = document.querySelectorAll('.' + tiketsInputs);
	this.price = document.querySelectorAll('.' + price);
	this.totalPrice = document.querySelectorAll('.' + totalPrice);
	this.eventsAdder = function() {
		for (let i = 0; i < this.tiketsInputs.length; i++) {


			this.rightButtons[i].addEventListener("click", () => {
				const checkNumber = this.plusCount(this.tiketsInputs[i]);
				this.chekCountAndColoring("right", checkNumber, this.leftButtons[i], this.rightButtons[i], this.tiketsInputs[i]);
				this.drawingTotalPrice(this.price[i].innerText, this.tiketsInputs[i], this.totalPrice[i]);
			});

			this.leftButtons[i].addEventListener("click", () => {
				const checkNumber = this.minusCount(this.tiketsInputs[i]);
				this.chekCountAndColoring("left", checkNumber, this.leftButtons[i], this.rightButtons[i], this.tiketsInputs[i]);
				this.drawingTotalPrice(this.price[i].innerText, this.tiketsInputs[i], this.totalPrice[i]);
			});


			this.tiketsInputs[i].addEventListener("input", () => {
				this.drawingTotalPrice(this.price[i].innerText, this.tiketsInputs[i], this.totalPrice[i]);
			});

		}

	};

	this.drawingTotalPrice = function(priceString, inputNode, totalPriceNode) {
		let value = inputNode.value;
		priceString = priceString.split(',');
		let price = 0;
		priceString.forEach((v) => {
			price += v;
		});
		price = parseInt(price);
		value = parseInt(value);
		if (isNaN(value)) {
			value = 0;
			inputNode.value = value;
		}
		totalPrice = price * value;
		totalPriceNode.innerText = totalPrice.toLocaleString('ko-KR'); + "원";
		if (totalPrice != 0) {
			totalPriceNode.style.fontWeight = 'bold';
			totalPriceNode.style.color = 'black';
		} else {
			totalPriceNode.style.fontWeight = 'normal';
			totalPriceNode.style.color = 'gray';
		}
	};

	this.plusCount = function(node, max) {
		let maxNumber = max ?? 0;
		maxNumber = (maxNumber < 0) ? 0 : maxNumber;
		value = parseInt(node.value);
		if (maxNumber == 0) {
			value = value + 1;
		} else if (value < max) {
			value = value + 1;
		}

		node.value = value;

		if (value == maxNumber) {
			return 0
		} else {
			return 1;
		}
	};

	this.minusCount = function(node) {
		value = parseInt(node.value);
		if (0 < value) {
			value = value - 1;
		}

		node.value = value;

		if (value == 0) {
			return 0;
		} else {
			return 1;
		}
	};

	this.chekCountAndColoring = function(direction, checkNumber, leftButtonNode, rightButtonNode, inputNode) {
		if (!(direction != 'right' || direction != 'left')) {
			return 0;
		}

		if (!(checkNumber != 1 || checkNumber != 0)) {
			return 0;
		}
		const activeColor = 'green';
		const nonActiveColor = 'gray';

		if (direction == 'right') {
			if (checkNumber == 1) {
				leftButtonNode.src = '/getImage/img/colorMinusButton.png';
				leftButtonNode.style.cursor = 'pointer';
				inputNode.style.color = activeColor;
				inputNode.style.borderColor = activeColor;
			} else if (checkNumber == 0) {
				rightButtonNode.style.cursor = 'default'
				inputNode.style.color = nonActiveColor;
				inputNode.style.borderColor = nonActiveColor;
			}
		}
		if (direction == 'left') {
			if (checkNumber == 1) {
				inputNode.style.color = activeColor;
				inputNode.style.borderColor = activeColor;
			} else if (checkNumber == 0) {
				leftButtonNode.src = '/getImage/img/nonColorMinusButton.png';
				leftButtonNode.style.cursor = 'default';
				inputNode.style.color = nonActiveColor;
				inputNode.style.borderColor = nonActiveColor;
			}
		}
	}
}




// 구매자 정보
function purchaserInfo(tel, email, tikets, checkTerms, sumTiketsNode, formNode, buttonNode) {
	this.reservationTel = document.querySelector("#" + tel);
	this.reservationEmail = document.querySelector("#" + email);
	this.tikets = document.querySelectorAll("." + tikets);
	this.checkTerms = document.querySelector("#" + checkTerms);
	this.sumTiketsNode = document.querySelector('.' + sumTiketsNode);
	this.formNode = document.querySelector('#' + formNode);
	this.buttonNode = document.querySelector('#' + buttonNode);

	this.checkTel = function(telNode) {
		const regExp = /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/;
		const tel = telNode.value
		if (regExp.test(tel)) {
			return true;
		} else {
			return telNode;
		}
	};

	this.checkEmail = function(emailNode) {
		const regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
		const email = emailNode.value;
		if (regExp.test(email)) {
			return true;
		} else {
			return emailNode;
		}
	};

	this.checkAgreeTerms = function(checkTermsNode) {
		if (checkTermsNode.checked) {
			return checkTermsNode.checked;
		} else {
			return checkTermsNode;
		}


	};

	this.checkTiketsNumber = function(tikets) {
		let count = 0;
		const length = tikets.length;
		tikets.forEach((v, i, o) => {
			if (0 <= v.value) {
				count += 1;
			} else {
				this.requestCondition(v, 1);
			}

		});
		if (count == length) {
			return true;
		} else {
			return false;
		}
	};

	this.checkBeforeSubmit = function() {
		const error = document.querySelectorAll('.error');
		error.forEach((v) => {
			v.remove();
		})
		let trueNum = 0;

		const tiketsNumber = this.checkTiketsNumber(this.tikets);

		if (tiketsNumber) {
			const checkSumTikets = this.checkSumTikets(this.sumTiketsNode);

			if (checkSumTikets == true) {
				trueNum += 1;
			} else {
				this.requestCondition(checkSumTikets, 1);
			}
		}


		const tel = this.checkTel(this.reservationTel);
		if (tel == true) {
			trueNum += 1;
		} else {
			this.requestCondition(tel);
		}

		const email = this.checkEmail(this.reservationEmail);
		if (email == true) {
			trueNum += 1;
		} else {
			this.requestCondition(email);
		}

		const agreeTerms = this.checkAgreeTerms(this.checkTerms);
		if (agreeTerms == true) {
			trueNum += 1;
		} else {
			this.requestCondition(agreeTerms);
		}

		if (trueNum != 4) {
			this.activeButton(0);
			return false;
		} else {
			this.activeButton(1);
			return true;
		}
	};

	this.getDate = function() {
		const date = new Date();
		const dateString = date.getFullYear() + '.' + date.getMonth() + '.' + date.getDate();
		return dateString;
	};
	this.drawingAndInputDate = function(Drawingnode) {
		const date = this.getDate();
		Drawingnode.innerText = date;
	};

	this.drawingSumTikets = function(node) {
		value = 0;
		this.tikets.forEach((v) => {
			value += parseInt(v.value);
		});
		node.innerText = value + '매';
	};
	this.checkSumTikets = function(node) {
		const value = node.innerText;
		if (parseInt(value) > 0) {
			return true;
		} else {
			return node;
		}
	}

	this.addEventDrawingSumTikets = function(targetNodes, drawingNode) {
		targetNodes.forEach((v) => {
			v.addEventListener("click", () => {
				this.drawingSumTikets(drawingNode);
				let children = v.children;
				for (let i = 0; i < children.length; i++) {
					let tagName = children[i].tagName;
					if (tagName = 'input') {
						const inputNode = children[i];
						inputNode.addEventListener("input", () => {
							this.drawingSumTikets(drawingNode);
						});
					}
				}

			});
		});
	};

	this.requestCondition = function(node, number) {
		if (number) {
			const errorText = "<div class='error'>티켓 수를 확인해주세요.</div>"
			node.parentElement.insertAdjacentHTML("afterend", errorText);

		} else {
			const errorText = "<div class='error'>조건에 맞춰서 입력해주세요.</div>"
			node.insertAdjacentHTML("afterend", errorText);
		}
	}

	this.activeButton = function() {
		if (this.checkTiketsNumber(this.tikets) == true
			&& this.checkSumTikets(this.sumTiketsNode) == true
			&& this.checkTel(this.reservationTel) == true
			&& this.checkEmail(this.reservationEmail) == true
			&& this.checkAgreeTerms(this.checkTerms) == true) {
			const name = document.querySelector('#reservation-name');
			if (name.value != '') {
				this.buttonNode.style.backgroundColor = '#00cd33';
			} else {
				this.buttonNode.style.backgroundColor = '#b7b4b4';

			}
		} else {
			this.buttonNode.style.backgroundColor = '#b7b4b4';
		}
		console.log(this.checkTiketsNumber(this.tikets)
			+ this.checkSumTikets(this.sumTiketsNode)
			+ this.checkTel(this.reservationTel)
			+ this.checkEmail(this.reservationEmail)
			+ this.checkAgreeTerms(this.checkTerms));

	}


	this.openTerms = function(triggerNodeClass, termsNodeClass) {
		const triggerNodes = document.querySelectorAll('.' + triggerNodeClass);
		const termsNodes = document.querySelectorAll('.' + termsNodeClass);
		for (let i = 0; i < triggerNodes.length; i++) {
			triggerNodes[i].addEventListener("click", () => {
				if (termsNodes[i].clientHeight != 0) {
					termsNodes[i].style.height = '0';
					triggerNodes[i].innerHTML = "보기" + "<img src='/getImage/img/openTopClamp.png'>";
				} else {
					termsNodes[i].style.height = '10em';
					triggerNodes[i].innerHTML  = "보지 않기" + "<img src='/getImage/img/openBottomClamp.png'>";
					
					
				}
			});
		}
	}

	this.addFormEvent = function() {
		this.formNode.addEventListener("change", () => {
			this.activeButton();
		});
		const tiketButtons = document.querySelectorAll('.tiket-number-button');
		tiketButtons.forEach((v) => {
			console.log(v);
			v.addEventListener("click", () => {
				setTimeout(()=>{
					this.activeButton();
				});
				
			});
		})
	}
}



const selectTikets = new tikets('left-button', 'right-button', 'tikets-input', 'price', 'total-price');
const purChaserInfo = new purchaserInfo('reservation-tel', 'reservation-email', 'tikets-input', 'check-terms-checkbox', 'tikets-sum', 'reservation-form', 'reservation-button');

window.addEventListener("load", () => {
	selectTikets.eventsAdder();
	const dateNode = document.querySelector('.date');
	purChaserInfo.drawingAndInputDate(dateNode);

	const tiketsTargetNode = document.querySelectorAll('.tiket-number-button-container');
	const sumTiketsNode = document.querySelector('.tikets-sum');
	purChaserInfo.addEventDrawingSumTikets(tiketsTargetNode, sumTiketsNode);
	purChaserInfo.openTerms('terms-span', 'specific-personal-info-terms');
	purChaserInfo.addFormEvent();

});
