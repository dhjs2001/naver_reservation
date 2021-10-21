/**
 * 
 */

function CheckEmail(formNode, inputNode) {
	this.formNode = document.querySelector('#' + formNode);
	this.inputNode = document.querySelector('#' + inputNode);
	this.button = document.querySelector('button');
	this.checkregExp = function() {
		const regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
		const email = this.inputNode.value;
		if (regExp.test(email)) {
			this.button.style.backgroundColor = '#07bf07';
			this.button.style.color = 'white';
			return true;
		}else{
			this.button.style.backgroundColor = 'white';
			this.button.style.borderColor = '#07bf07';
			this.button.style.color = '#07bf07';
			return false;
		}
	}
	this.addEvent = function(){
		this.inputNode.addEventListener("input",()=>{
			this.checkregExp();
		});
	}
	this.checkEmail = function() {
		if (!this.checkregExp()) {
			const error = document.querySelectorAll('.error');
			error.forEach((v) => {
				v.remove();
			})
			const node = "<div class='error'>이메일을 제대로 작성했는지 확인해주세요.</div>";
			this.inputNode.parentElement.insertAdjacentHTML("afterend", node);
			console.log('전송실패');
			return false;
		} else {
			this.formNode.submit();
			return true;
		}
	}

}
const check = new CheckEmail("login", "email");
check.addEvent();