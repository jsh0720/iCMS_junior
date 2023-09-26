/*
* validation
*/
/* 
* 공백 입력 금지패턴
* 허용 : 한글,영문,숫자,특수문자 허용
*/
var setBlankToNull = function(obj) {
	obj.value = obj.value.replace(/\s/g, "");
}
/* 
* 전화 번호 패턴검사
* 허용 : 숫자, 하이픈(-) 자동 
*/
var setTelPattern = function(obj) {
	obj.value = obj.value.replace(/[^0-9]/g, "").replace(/(^02|^0502|^0503|^0504|^0505|^0506|^0507|^1[0-9]{3}|^0[0-9]{2})([0-9]+)?([0-9]{4})$/,"$1-$2-$3").replace("--", "-");
}

/*
* 허용 : 숫자, .
*/
var setIpPattern = function(obj) {
	obj.value = obj.value.replace(/[^0-9.*]/g, "")
}

/*
* 허용 : 숫자
*/
var setNumberPattern = function(obj) {
	obj.value = obj.value.replace(/[^0-9]/g, "")
}


/*
* 텍스트 특수문자 사용X
* 허용 : 숫자, 한글, 영어, 공백
*/
var setTextPattern = function(obj) {
	obj.value = obj.value.replace(/[^0-9ㄱ-ㅎ가-힣a-zA-Z/\s]/g, "");
}

/*
* 영문
* 허용 : 영어, 숫자
*/
var setEngPattern = function(obj) {
	obj.value = obj.value.replace(/[^0-9a-zA-Z]/g, "");
}

var setNamePattern = function(obj) {
	obj.value = obj.value.replace(/[^a-zA-Zㄱ-ㅎ가-힣/\s]/g, "");
}

/*
* 영문
* 허용 : 영어, 숫자, _
*/
var setcodePattern = function(obj) {
	obj.value = obj.value.replace(/[^0-9a-zA-Z_]/g, "");
}

/*
* url
* 허용 : 영어, :, /
*/
var setUrlPattern = function(obj) {
	obj.value = obj.value.replace(/[^0-9a-zA-Z.://]/g, "");
}
/*
* order
* 허용 : 숫자(0제외)
*/
var setOrderPattern = function(obj) {
	obj.value = obj.value.replace(/[^0-9]/g, "");
}


/*
* meta
* 허용 : 영어, 한글, 숫자, 쉼표, 공백
*/
var setMetaTagPattern = function(obj) {
	obj.value = obj.value.replace(/[^0-9ㄱ-ㅎ가-힣a-zA-Z,\s]/g, "");
}

var setCompanyNumberPattern = function(obj) {
	obj.value = obj.value.replace(/[^0-9]/g, '').replace(/^(\d{0,3})(\d{0,2})(\d{0,5})$/g, "$1-$2-$3").replace(/(\-{1,2})$/g, "");		
}

var setCompanyTelNumberPattern = function(obj) {
	obj.value = obj.value.replace(/[^0-9ㄱ-ㅎ가-힣-]/g, "");
}

var setEmailPattern = function(obj) {
	obj.value = obj.value.replace(/[^0-9a-zA-Z._@-]/g, "");
}
