/**
 * 
 */





// 프로모션 슬라이드

function Promotion(slideClass) {
	this.count = 1;

	this.slide = document.querySelector('.' + slideClass);
	this.promotionSwitch = true;
	this.length = 0;
	this.timeOutVal = null;
	this.setTimeOutVal = function(value) {
		this.timeOutVal = value;
	}
	this.getTimeOutVal = function() {
		return this.timeOutVal;
	}
	this.setLength = function(value) {
		this.length = value;
	}
	this.getLength = function() {
		return this.length;
	}

	this.createClone = function() {
		const clone = this.slide.firstElementChild.cloneNode(true);
		this.slide.appendChild(clone);
		this.setLength(this.slide.childElementCount);
		this.slide.style.width = this.getLength() * 100 + "%";
		this.slide.style.left = 0;

	}
	this.setCount = function(value) {
		this.count = value;
	}
	this.getCount = function() {
		return this.count;
	}

	this.setPromotionSwitch = function(value) {
		this.promotionSwitch = value;
	}
	this.getPromotionSwitch = function() {
		return this.promotionSwitch;
	}


	this.promotionAction = function() {
		const slide = this.slide;
		setTimeout(() => {

			if (!this.getPromotionSwitch()) {
			} else {
				if (this.getCount() != this.getLength()) {
					if (this.getCount() == 1) {
						slide.classList.add("animation");
					}
					slide.style.left = -this.getCount() * 100 + "%";
					this.setCount(this.getCount() + 1);

				} else {
					slide.classList.remove("animation");
					slide.style.left = 0;
					this.setCount(1);
				}
			}
			this.promotionAction();
		}, 5000);

	}
}

const promotionAction = new Promotion('promotion-container');
const categoryUl = document.querySelector("ul");
const categoryLi = document.querySelectorAll('ul > li');
const more = document.querySelector('.more');
window.addEventListener("load", () => {
	// Category 클릭시 초록색 밑줄 추가

	categoryUl.onclick = function(evt) {

		if (evt.target.tagName == "LI") {
			for (i = 0; i < categoryLi.length; i++) {
				categoryLi[i].classList.remove("clicked-list");
			}
			evt.target.classList.add("clicked-list");
		}
	}

	promotionAction.createClone();
	promotionAction.promotionAction();



	// 처음 목록 가져오기
	getItems(0, moreStandard);

	//더 보기 클릭 시 아이템 더 가져오기
	more.addEventListener("click", function() {
		getItems(moreStandard.currentId, moreStandard);
	});
	//카테고리에 이벤트 추가
	addEventOnCategory(category);

});
let timeOutVal = null;
window.addEventListener("focus", () => {
	promotionAction.setPromotionSwitch(true);
});
window.addEventListener("blur", () => {
	promotionAction.setPromotionSwitch(false);
});




// 더 보기 클릭시 카테고리 아이디 확인 용 객체
const moreStandard = {
	start: 0,
	currentId: 1,
	allItemsNumber: 0,

	getStart: function() { return this.start; },
	setStart: function(value) { this.start = value; },
	getCurrentId: function() { return this.currentId; },
	setCurrentId: function(value) { this.currentId = value; },
	getAllItemsNumber: function() { return this.allItemsNumber; },
	setAllItemsNumber: function(value) { this.allItemsNumber = value; },
};



// ajax를 통해 category에 따른 product 목록 가져오기
function ajax(id, start, moreStandard) {
	return new Promise((resolv, reject) => {
		const oReq = new XMLHttpRequest();
		oReq.addEventListener("load", function() {
			const context = JSON.parse(this.responseText);
			moreStandard.setCurrentId(id);
			resolv(context);
		});
		oReq.open("GET", "/api/products/" + id + "/" + start);
		oReq.send();
	});
}


function getItems(id, moreStandard) {
	if (id == moreStandard.getCurrentId()) {
		const addStartNumber = 4;
		const start = moreStandard.getStart() + addStartNumber;
		moreStandard.setStart(start);
		ajax(id, moreStandard.getStart(), moreStandard).then((context) => deploy(context, 1, moreStandard));
	} else {
		moreStandard.setCurrentId(id);
		moreStandard.setStart(0);
		ajax(id, moreStandard.getStart(), moreStandard).then((context) => deploy(context, 0, moreStandard));
	}
}

// 아이템 배치하기
function deploy(context, isSame, moreStandard) {
	const source = document.getElementById("product-template").innerHTML;
	const template = Handlebars.compile(source);
	const productList1 = document.querySelector(".product-list1");
	const productList2 = document.querySelector(".product-list2");
	const length = context.length;
	let html1 = '';
	let html2 = '';
	const AllItemsNumber = moreStandard.getAllItemsNumber();
	if (AllItemsNumber != length) {
		moreStandard.setAllItemsNumber(length);
	}
	document.querySelector(".item-length").innerText = length;
	if (isSame == 1) {
		context.product.forEach((v, i) => {

			if (i % 2 != 0) {
				html1 += template(v);
			} else {
				html2 += template(v);
			}
		});
		productList1.innerHTML += html1;
		productList2.innerHTML += html2;
		const more = document.querySelector('.more');
		const currentItemsNumber = document.querySelectorAll('.product').length;
		if (currentItemsNumber == moreStandard.getAllItemsNumber()) {
			more.hidden = true;
		}
	} else {
		if (more.hidden) {
			more.hidden = false;
		}
		context.product.forEach((v, i) => {

			if (i % 2 != 0) {
				html1 += template(v);
			} else {
				html2 += template(v);
			}
		});
		productList1.innerHTML = html1;
		productList2.innerHTML = html2;
	}
}




//카테고리 마다 클릭 이벤트 추가
// 전시 1, 뮤지컬 2, 콘서트 3, 클래식 4, 연극 5
//카테고리 목록
const category = { "전체": 0, "전시": 1, "뮤지컬": 2, "콘서트": 3, "클래식": 4, "연극": 5 };

//카테고리에 이벤트 추가 함수
function addEventOnCategory(category) {
	for (const i in category) {
		const categoryId = category[i];
		const categoryElement = document.querySelector('.category' + categoryId);
		categoryElement.addEventListener("click", function() {
			getItems(categoryId, moreStandard);
		});
	}
}







