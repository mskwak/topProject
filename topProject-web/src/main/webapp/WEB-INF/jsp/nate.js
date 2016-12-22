/**
 * 
 */


document.domain = "nate.com";

var xGlobal = {
	CookieDomain		: "nate.com",	// 도메인
	CookieMain			: "LOGIN",		// 메인 쿠키
	CookieXecureLevel	: "xlevel",		// 보안 레벨 쿠키
	CookieIPLevel		: "iplevel",	// IP보안 레벨 쿠키
	CookieKeepLogin		: "keeplogin",	// 로그인 유지기능 여부 쿠키
	CookieLoginId		: "loginid",	// 로그인 아이디
	//CookieSavePwd		: "savepwd",	// 로그인 아이디 저장 여부 쿠키
	CookieLoginRSAPwd	: "loginrsapwd",// 로그인 비밀번호
	//CookieALogin		: "alogin",		// 자동 로그인 여부
	//CookieALoginTime	: "alogin_time",// 자동 로그인 설정 시간
	//SKP 통합 ID : 통합 로그인 페이지 추가
	CookieSaveCid		: "savecid",	// 싸이월드 로그인 아이디 저장 여부
	CookieLoginCid		: "logincid",	// 싸이월드 로그인 아이디	
	//CookieALoginCid		: "alogincid",	// 싸이월드 자동 로그인 여부
	//CookieALoginTimeCid	: "alogintimecid",	// 싸이월드 자동 로그인 설정 시간
	//CookieLoginRSAPwdCid	: "loginrsapwdcid", // 싸이월드 로그인 비밀번호
	ShowLoginPwd		: "☆☆☆☆☆☆",	// 비밀번호저장시표시되는비번
	XecureActivexURL	: "/common/secure/xecure_frame/xecure_activex_nate.html",	// CKKeyPro 동작관련 URL
	XecureBlankURL		: "/common/secure/xecure_frame/xecure_blank.html",			// CKKeyPro 동작관련 빈 페이지
	XecureInstallURL	: "/common/secure/install/TouchEnKey_Installer_skcoms.exe"	// CKKeyPro 설치파일
 };

var xRSA = {
	evalue : null,
	nvalue : '10001',
	encrypt : function(id_obj, pwd_obj, pwd_rsa_obj, type) {
		
		if(type == 'NATE') {
			this.evalue = 'D1E4297D787302003419A2F758BD9C79A341255031E758D85D8FDA4E4577DEA0A0FEA7408B0E11A0505791BCA4E8E8DD1CA122873318F231A621C3C971B2BBAF1668BEAE76DAED7B2A1E510EE1292FDAF09BB4C930242BCB26ADBD7762BD02AC7D99E1BA2BCDDB4C5AC15D97CC5B0E31133D2C702BE762D29EADC6A12104B46B';
		} else if (type == 'CYWORLD') {
			this.evalue = 'DD303A4EB455BA81F12DFA168FBB044C99B412CF8EA149709E81A3362B6F3136D577121276CA0CB60D49F958F3FDBA66B6D6CD3FBE0789A237A2DDB42499613D77F74FE8E1DE505B8F768DBD7881759F94EFB5090AC724805759A5516702D35CDAEC7708621A0D39488CACD872BB7AD26F6F5C76E0092FC5F3377A2D2404E48F';
		} else {
			return false;
		}
		
		try {
//			http://stackoverflow.com/questions/5789685/rsa-encryption-with-given-public-key-in-java
//			http://egloos.zum.com/kwon37xi/v/4427199
//			var rsa = new RSAKey();
//			rsa.setPublic(rsaPublicKeyModulus, rsaPpublicKeyExponent);
			var rsa = new RSAKey();        
			rsa.setPublic(this.evalue, this.nvalue);
			var fullData = xCommon.getFullToday() + '|^|' +id_obj.value + '|^|' + pwd_obj.value;
			var res = rsa.encrypt(fullData);
	    
			if(res) {
				pwd_rsa_obj.value = hex2b64(res);
				return true;
	    	} else {
	    		return false;
	    	}
		} catch (e) {
		    return false;
		} finally {
			pwd_obj.value = "";
		}
	},
	
	encryptE2E : function(id_obj, frm_nm, pwd_nm, pwd_rsa_obj, type) {
		try {
			if(type == 'NATE') {
				this.evalue = 'D1E4297D787302003419A2F758BD9C79A341255031E758D85D8FDA4E4577DEA0A0FEA7408B0E11A0505791BCA4E8E8DD1CA122873318F231A621C3C971B2BBAF1668BEAE76DAED7B2A1E510EE1292FDAF09BB4C930242BCB26ADBD7762BD02AC7D99E1BA2BCDDB4C5AC15D97CC5B0E31133D2C702BE762D29EADC6A12104B46B';
			} else if (type == 'CYWORLD') {
				this.evalue = 'DD303A4EB455BA81F12DFA168FBB044C99B412CF8EA149709E81A3362B6F3136D577121276CA0CB60D49F958F3FDBA66B6D6CD3FBE0789A237A2DDB42499613D77F74FE8E1DE505B8F768DBD7881759F94EFB5090AC724805759A5516702D35CDAEC7708621A0D39488CACD872BB7AD26F6F5C76E0092FC5F3377A2D2404E48F';
			} else {
				return false;
			}
			
		    var rsa = new RSAKey();        
		    rsa.setPublic(this.evalue,this.nvalue);
			var CKKey_obj =  document.CKKeyPro;
	    	
	    	if (CKKey_obj == null || typeof (CKKey_obj) == "undefined" || CKKey_obj.object == null) {
				return false;
			} else {
		        CKKey_obj.E2EInitEx("rsa" , "" , xRSA.evalue, xRSA.nvalue, id_obj.value );
				var res = CKKey_obj.GetEncData("",frm_nm,pwd_nm);
				
			    if(res) {
			        pwd_rsa_obj.value = hex2b64(res);
			        return true;
			    } else {
			   		return false;
			   	}
			}
		} catch (e) {
		    return false;
		} finally {
			if (document.getElementById(pwd_nm)) {
				document.getElementById(pwd_nm).value = "";
			}
		}
	}
};

var xXecurePop = {
	PopWin : null,
	PopWinURL : "/common/secure/login_desc_pop.html",
	PopWinURL2 : "/common/secure/login_desc_pop_cy.html",
	TabNo : 1 ,
	
 	openWin : function(tabno) {
		if (parseInt(tabno) == 1) { // 설명 팝업
			this.TabNo = tabno;
			this.PopWin = window.open(this.PopWinURL, 'popXeWin', 'scrollbars=no, toolbar=no, location=no, status=no, menubar=no, resizable=no, , width=500, height=650');
			this.PopWin.focus();
		} else if (parseInt(tabno) == 2) {	// Nate OTP 페이지로 이동
			window.open("https://member.nate.com/Uotp/Intro.sk");
		} else if (parseInt(tabno) == 3) { // 설명 팝업
			this.TabNo = tabno;
			this.PopWin = window.open(this.PopWinURL, 'popXeWin', 'scrollbars=no, toolbar=no, location=no, status=no, menubar=no, resizable=no, , width=500, height=650');
			this.PopWin.focus();
		} else if (parseInt(tabno) == 4) { // 설명 팝업
			this.TabNo = tabno;
			this.PopWin = window.open(this.PopWinURL2, 'popXeWin', 'scrollbars=no, toolbar=no, location=no, status=no, menubar=no, resizable=no, , width=500, height=650');
			this.PopWin.focus();
		} else if (parseInt(tabno) == 5) {	// Cyworld OTP 페이지로 이동
			window.open("https://cymember.cyworld.com/main/uotp/OtpMain.jsp");
		} else if (parseInt(tabno) == 6) { // 설명 팝업
			this.TabNo = tabno;
			this.PopWin = window.open(this.PopWinURL2, 'popXeWin', 'scrollbars=no, toolbar=no, location=no, status=no, menubar=no, resizable=no, , width=500, height=650');
			this.PopWin.focus();
		}
	},

	resizePop : function(W,H) {
		
		var browser = navigator.userAgent.toLowerCase();
		//alert(browser);
		var nt = (browser.indexOf("nt") != -1);
		var ie6 = (browser.indexOf("msie 6") != -1);
		var ie7 = (browser.indexOf("msie 7") != -1);
		var ie8 = (browser.indexOf("msie 8") != -1);
		var ff = (browser.indexOf("firefox") != -1);

		if (nt) {
			if (ie7) {
            	window.resizeTo(W, H);
			} else if (ie6) {
				window.resizeTo(W, H - 19);
			} else if (ie8) {
            	window.resizeTo(W, H + 2);
			} else if (ff) {
            	window.resizeTo(W - 2, H + 6);
			} else {
            	window.resizeTo(W, H - 19);
			}
		} else {
            window.resizeTo(W, H + 0);
		}
	},
	
	resizePopWithDiv : function(W,H,divName) {
		var browser = navigator.userAgent.toLowerCase();
		//alert(browser);
		var nt = (browser.indexOf("nt") != -1);
		var ie6 = (browser.indexOf("msie 6") != -1);
		var ie7 = (browser.indexOf("msie 7") != -1);
		var ie8 = (browser.indexOf("msie 8") != -1);
		var ie11 = (browser.indexOf("rv:11") != -1);
		var ff = (browser.indexOf("firefox") != -1);
		var chrome = (browser.indexOf("chrome") != -1);

		if (nt) {
			if (ie7) {
			} else if (ie6) {
			} else if (ie8) {
			} else if (ff) {
            	W = W - 2;
			} else if (chrome) {
				H = H - 22;
			}
		}
		
		window.resizeTo(W, H);

		if(divName && !chrome && !ie11) {
			var divHeight = document.documentElement.offsetHeight + 6;

			// 주소줄,상태표시줄 유무에 따른 로그인 팝업 사이즈 조정 
			if(window.innerHeight) {
				H = H - (window.innerHeight - divHeight);
			} else {
				H = H - (document.documentElement.clientHeight - divHeight);
			}
			window.resizeTo(W, H);
		}
	}
};

var xXecureLayer = { 
	LayerClose : function() {
	    document.getElementById('layerXKDisabled').style.display = "none";
//	    20130719 junhee 네이트로그인의 도메인 선택 박스 사라지면서 발생한 버그 수정
//	    if (document.getElementById('domain').style.position != "absolute") {
//	    	document.getElementById('domain').style.visibility = "visible";
//	    }
	}
};

var xXecureLayerCy = { 
	LayerClose : function() {
		document.getElementById('layerXKDisabledCy').style.display = "none";
//		    20130719 junhee 네이트로그인의 도메인 선택 박스 사라지면서 발생한 버그 수정		    
//		    if (document.getElementById('domain').style.position != "absolute") {
//		    	document.getElementById('domain').style.visibility = "visible";
//		    }
	}
};

var xLogin = {
	cKeepLogin		: "off",
	cLoginId		: "",
	cSavePwd		: "off",
	cALogin			: "off",	
	cLoginRSAPwd	: "",
	
	getCookie : function ()	{
		
		if ( (! xCommon.getSubCookie(xGlobal.CookieMain, xGlobal.CookieKeepLogin)) ||  xCommon.getSubCookie(xGlobal.CookieMain, xGlobal.CookieKeepLogin) == this.cKeepLogin ) {
			
			if (! this.getOldCookie()) {
				xCommon.setSubCookie(xGlobal.CookieMain, xGlobal.CookieKeepLogin, this.cKeepLogin);
				this.cKeepLogin = "off";
				this.cLoginId = "";
				this.cSavePwd = "off";
				this.cALogin = "off";
				this.cLoginRSAPwd = "";
			}
		} else {
			this.cKeepLogin = xCommon.getSubCookie(xGlobal.CookieMain, xGlobal.CookieKeepLogin);
			this.cLoginId = xCommon.getSubCookie(xGlobal.CookieMain, xGlobal.CookieLoginId);
			//this.cSavePwd = xCommon.getSubCookie(xGlobal.CookieMain, xGlobal.CookieSavePwd);
			//this.cALogin = xCommon.getSubCookie(xGlobal.CookieMain, xGlobal.CookieALogin);
			this.cLoginRSAPwd = xCommon.getSubCookie(xGlobal.CookieMain, xGlobal.CookieLoginRSAPwd);
			if (this.cLoginRSAPwd)
				this.cLoginRSAPwd = this.cLoginRSAPwd.replace(/@@/g,"=");	// RSA 암호화 시 @@ 버그로 = 변환
		}
	},
	
	setCookie : function() {	//keeplogin_obj, id_obj, domain_obj) {
		var keeplogin_obj	= arguments[0];
		var id_obj			= arguments[1];
		var domain_obj		= arguments[2];
			
		this.setNateId(id_obj, domain_obj);
		
		if(keeplogin_obj.checked == true) {
			this.cKeepLogin = "on";
			keeplogin_obj.value = this.cKeepLogin;
			this.cLoginId  = id_obj.value;
		} else {
			this.cKeepLogin = "off";
			keeplogin_obj.value = this.cKeepLogin;
			this.cLoginId = "";
		}
		xCommon.setSubCookie(xGlobal.CookieMain, xGlobal.CookieKeepLogin, this.cKeepLogin);
		xCommon.setSubCookie(xGlobal.CookieMain, xGlobal.CookieLoginId, this.cLoginId);
		
		// 자동로그인 활성화시만  비밀번호 저장
		/*
		if(savepasswd_obj != null && savepasswd_obj.disabled == false) { 
			if(savepasswd_obj.checked == true) {
				this.cSavePwd = "on";
				this.cLoginRSAPwd  = RSApasswd_obj.value;
				this.cLoginRSAPwd = this.cLoginRSAPwd.replace(/=/g,"@@");	// RSA 암호화 시 = 버그로 @@ 변환
			} else {
				this.cSavePwd = "off";
				this.cLoginRSAPwd = "";
			}
			xCommon.setSubCookie(xGlobal.CookieMain, xGlobal.CookieSavePwd, this.cSavePwd);
			xCommon.setSubCookie(xGlobal.CookieMain, xGlobal.CookieLoginRSAPwd, this.cLoginRSAPwd);
		}
		*/
		
		this.setOldCookie();
	},
	
	setNateId : function (id_obj, domain_obj) {
		if (domain_obj.value == "nate.com" ) {
			id_obj.value = id_obj.value; //+ "@" + domain_obj.value;
		}
	},

	setLoginForm : function() { // (keeplogin_obj, id_obj, domain_obj) {
		var keeplogin_obj	= arguments[0];
		var id_obj			= arguments[1];
		var domain_obj		= arguments[2];
		var savepasswd_obj	= (arguments[3] != null ? arguments[3] : "");
		var passwd_obj		= (arguments[4] != null ? arguments[4] : "");
		var RSApasswd_obj	= (arguments[5] != null ? arguments[5] : "");
		
		this.getCookie();
		
		if(this.cKeepLogin == "on") {
			
			keeplogin_obj.checked = true;
			checkLong(keeplogin_obj);
			
			var loginid = "";
			var logindomain = "";
			
			var nIdx = this.cLoginId.indexOf("@");

			if (nIdx != -1) { 
				loginid = this.cLoginId.substring(0, nIdx);
				logindomain = this.cLoginId.substring(nIdx+1, this.cLoginId.length);
				
				if(logindomain!="nate.com"&&logindomain!=null&&logindomain!="") {
					loginid = this.cLoginId;
				}
				
			} else {
				loginid = this.cLoginId;
				logindomain = "nate.com";
			}

			id_obj.value = loginid;
			// 서비스의 경우 null이 기본
			//id_obj.value = "";
			
			for (var i=0; i< domain_obj.length; i++) {
				if (domain_obj[i].text == logindomain) {
					domain_obj.selectedIndex = i;
					break;
				}
			}
			
		} else {
			keeplogin_obj.checked = false;
			id_obj.value = "";
		}
		
		if(this.cALogin == "on") {
			savepasswd_obj.checked = true;
			passwd_obj.value = xGlobal.ShowLoginPwd;
			RSApasswd_obj.value = this.cLoginRSAPwd;
		} else {
			if (savepasswd_obj != "") {
				savepasswd_obj.checked = false;
				passwd_obj.value = "";
				RSApasswd_obj.value = "";
			}
		}
		
		// 2010.03.03 ydahn IP보안 추가 
		ipSecure.setText(ipSecure.getCookie());
	},
	
	chkCookie : function() {
		var savepasswd_obj	= (arguments[0] != null ? arguments[0] : "");
		var passwd_obj		= (arguments[1] != null ? arguments[1] : "");
		
		if (savepasswd_obj.checked && passwd_obj.value == xGlobal.ShowLoginPwd && this.cLoginRSAPwd != "") {
			return false;
		} else {
			return true;
		}
	},
	
	setOldCookie : function() {
		if (this.cKeepLogin == "on") {
			var loginid = xOldBase64.base64encode(this.cLoginId);
			xCommon.setCookie("SAVED_NATEID",loginid + "|1");
		} else {
			xCommon.setCookie("SAVED_NATEID","|0");
		}
		
		xCommon.setCookie("SSL_LOGIN","1");
	},
	
	getOldCookie : function() {
		var oldLoginId = xCommon.getCookie("SAVED_NATEID");
		
		if (oldLoginId) {
			var nIdx = oldLoginId.indexOf("|");
	
			if (nIdx != -1) { 
				this.cLoginId = xOldBase64.base64decode(oldLoginId.substring(0, nIdx));
	
				if(oldLoginId.substring(nIdx+1, oldLoginId.length) == "1") {
					this.cKeepLogin = "on";
				} else {
					this.cKeepLogin = "off";
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}	
	
};

var xOldBase64 = {
	base64list : 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=',

	base64encode : function(s)
	{
	  var t = '', p = -6, a = 0, i = 0, v = 0, c;
	
	  while ( (i < s.length) || (p > -6) ) {
	    if ( p < 0 ) {
	      if ( i < s.length ) {
	        c = s.charCodeAt(i++);
	        v += 8;
	      } else {
	        c = 0;
	      }
	      a = ((a&255)<<8)|(c&255);
	      p += 8;
	    }
	    if ( v > 0 )
	      t += this.base64list.charAt((a>>p)&63);
	    else
	      t += this.base64list.charAt(64);
	    p -= 6;
	    v -= 6;
	  }
	  return t;
	},

	base64decode : function(s)
	{
	  var t = '', p = -8, a = 0, q = 0, c, m, n;
	
	  for( var i = 0; i < s.length; i++ ) {
	    c = this.base64list.indexOf(s.charAt(i));
	    if ( c < 0 )
	      continue;
	    a = (a<<6)|(c&63);
	    p += 6;
	    if ( p >= 0 ) {
	      c = (a>>p)&255;
	      if ( c > 0 )
	        t += String.fromCharCode(c);
	      a &= 63;
	      p -= 8;
	    }
	  }
	  return t;
	}
};

// Common 
var xCommon = {

	getCookie : function (name)	{
		var nameOfCookie  = name + "=";

		var x = 0;
		while ( x <= document.cookie.length )
		{
			var y = ( x + nameOfCookie.length );
			if ( document.cookie.substring( x, y ) == nameOfCookie )
			{
				if ((endOfCookie = document.cookie.indexOf( ";", y )) == -1 ) endOfCookie = document.cookie.length;
				return unescape (document.cookie.substring( y, endOfCookie));
			}
			x = document.cookie.indexOf( " ", x ) + 1;
			if (x == 0) break;
		}
		return;
	},

	getSubCookie : function (name, name2) {
		var CookieValue = this.getCookie(name);

		if (CookieValue == '' || CookieValue == null) { return false; }

		var x = 0;
		var y = 0;
		while ( x <= CookieValue.length && x != -1)
		{
			if (x != 0) { x = CookieValue.indexOf('&', x) + 1; }
			y = CookieValue.indexOf('=', y + 1);

			if (CookieValue.substring(x, y) == name2) {
				z = CookieValue.indexOf('&', x) + 0;
				if (z <= 0) { z = CookieValue.length; }
				return (CookieValue.substring(y + 1, z));
				break;
			}
			else {
				x = y;
			}
		}
	},

	setCookie : function (name, value, isEscape) {

		isEscape = (isEscape == undefined) ? true : false;

		if (isEscape) {
			value = escape(value);
		}

		var todayDate = new Date();
		todayDate.setDate( todayDate.getDate() + 9999 );
		document.cookie = name + "=" + value + "; domain="+ xGlobal.CookieDomain+"; path=/;expires=" + todayDate.toGMTString() + ";";
	},

	setSubCookie : function (name, name2, value) {
		var _value = this.getCookie(name);
		var _subvalue = this.getSubCookie(name, name2);

		if (_value == undefined || _value == "") {
			_value = name2 + "=" + value;
		} else if (_subvalue == undefined) {
			_value += "&" + name2 + "=" + value;
		} else {
			var CookieDic = _value.split("&");
			for (var i = 0; i < CookieDic.length; i++) {
				var CookiePair = CookieDic[i].split("=");
				if (CookiePair[0] == name2) {
					CookiePair[1] = escape(value);
				}
				CookieDic[i] = CookiePair.join("=");
			}

			_value = CookieDic.join("&");
		}

		this.setCookie(name, _value, false);
	},
	
	checkBrowser : function() {
		if (navigator.userAgent.toUpperCase().indexOf("MSIE") != -1) return "IE";
		else if (navigator.userAgent.toUpperCase().indexOf("MOZILLA") != -1) return "FF";
		else return null;
	},
			
	trim : function(str) { 
		if (!str) return ''; 
		return str.replace(/^\s*|\s*$/g, ''); 
	}, 
	
	getFullToday : function()
	{
	    var today = new Date();
	    var buf = "";
	
	    buf += today.getYear() + "y";
	    buf += (today.getMonth() + 1) + "m";
	    buf += today.getDate() + "d ";
	    buf += today.getHours() + "h";
	    buf += today.getMinutes() + "m";
	    buf += today.getSeconds() + "s";
		
		return buf;
	}
};

// Xecure
var xXecure = {
	preXlevel  : 2,
	
	getCookie : function ()	{
			var cXecureLevel = 2;

			if ( (! xCommon.getSubCookie(xGlobal.CookieMain, xGlobal.CookieXecureLevel)) ||  xCommon.getSubCookie(xGlobal.CookieMain, xGlobal.CookieXecureLevel) == cXecureLevel ) {
				xCommon.setSubCookie(xGlobal.CookieMain, xGlobal.CookieXecureLevel, cXecureLevel);
			} else {
				cXecureLevel = xCommon.getSubCookie(xGlobal.CookieMain, xGlobal.CookieXecureLevel);
				
				if (! (cXecureLevel == 1 || cXecureLevel == 2 || cXecureLevel == 3)) {
					cXecureLevel = 2;
					xCommon.setSubCookie(xGlobal.CookieMain, xGlobal.CookieXecureLevel, cXecureLevel);
				}
			};
			return cXecureLevel ;
	},
	
	setCookie : function (value)	{
		xCommon.setSubCookie(xGlobal.CookieMain, xGlobal.CookieXecureLevel, value);
	},
	
	checkBrowser : function ()	{
		if (xCommon.checkBrowser() == "IE")
			return true;
		else
			return false;
	},
	
	isCKKeyPro : function () {
			
		if (document.CKKeyPro == null || typeof (document.CKKeyPro) == "undefined" || document.CKKeyPro.object == null)
			return false;
		else
			return true;
	},

	installActiveX : function () {
		var ckKey = null;
	
		try {
			ckKey = new ActiveXObject("CKSKComm.CKCommInst");
		} catch (e) {
			
		}

		return (ckKey != null);
	},
		
	excCKKeyPro : function (preXlevel) {
		document.getElementById("XecureFrame").src=xGlobal.XecureActivexURL;
		this.preXlevel = preXlevel;
	},
	
	excCKKeyProCy : function (preXlevel) {
		document.getElementById("XecureFrameCy").src=xGlobal.XecureActivexURL;
		this.preXlevel = preXlevel;
	},
	
	clearCKKeyPro : function() {
		if (document.getElementById("XecureFrame").src != xGlobal.XecureBlankURL)
			document.getElementById("XecureFrame").src = xGlobal.XecureBlankURL;
			
		if (parent.document.CKKeyPro == null || typeof (parent.document.CKKeyPro) == "undefined" || parent.document.CKKeyPro.object == null) {
			return;
		} else {
			parent.document.getElementById("XecureDiv").innerHTML = "";

		try { parent.document.CKKeyPro.Clear("f_login", "XecureDiv", 0); } catch (e) { }

		}
	},
	
	clearCKKeyProCy : function() {
		if (document.getElementById("XecureFrameCy").src != xGlobal.XecureBlankURL)
			document.getElementById("XecureFrameCy").src = xGlobal.XecureBlankURL;
			
		if (parent.document.CKKeyPro == null || typeof (parent.document.CKKeyPro) == "undefined" || parent.document.CKKeyPro.object == null) {
			return;
		} else {
			parent.document.getElementById("XecureDiv").innerHTML = "";

		try { parent.document.CKKeyPro.Clear("login", "XecureDiv", 0); } catch (e) { }

		}
	},

	getCKKeyProInstall : function () {
		location.href = xGlobal.XecureInstallURL;
	},
	
	getCKKeyProInstallPop : function () {
		window.open(xGlobal.XecureInstallURL, 'popInstall', 'scrollbars=no, toolbar=no, location=no, status=no, menubar=no, resizable=no, , width=0, height=0');
	},
	
	showCKKeyProPopup : function () {
		var x, y;
		x = screen.width - 2;
		y = screen.height - 45; // 43 is popup image height
		try { 
			document.CKKeyPro.ShowPopup(100, 8000, 100, 5, x, y);
		} catch (err) { }
	},
	
	hideCKKeyProPopup : function () {
		try { 
			if(document.CKKeyPro.HidePopup)
				document.CKKeyPro.HidePopup();
			} catch (err) { }
	},
	
	clearPASSWD : function() {
		parent.document.f_login.PASSWD.value="";
	},
	
	clearPASSWDCy : function() {
		parent.document.login.password.value="";
	},
	
	clearAutoInput : function() {
		var pwd = parent.f_login.PASSWD.value;
		if ( ! (pwd.indexOf("A") != -1 || pwd.indexOf("0") != -1 || pwd.indexOf("_") != -1 ) ) { 
			parent.f_login.PASSWD.value = "";
		}	
	
	},
	clearAutoInputCy : function() {
		var pwd = parent.login.password.value;
		if ( ! (pwd.indexOf("A") != -1 || pwd.indexOf("0") != -1 || pwd.indexOf("_") != -1 ) ) { 
			parent.login.password.value = "";
		}	
	
	}
};

// CKKeyPro OnKeyUp Bug Fix
function XecureCK_UIEevents(frm,ele,event,keycode) {
	var obj;
	var eventObj;

	try{
		obj=document.forms[frm].elements[ele];
		if( document.createEventObject )
		{
			eventObj = document.createEventObject();
			eventObj.keyCode=keycode;
			if(obj)
			{
				obj.fireEvent(event,eventObj);
			}
		}
	}
	catch(e) {}
}

// 2010.03.03 ydahn IP보안 추가 
var ipSecure = {

	getCookie : function ()	{
			var cXecureLevel = 2;

			if ( (! xCommon.getSubCookie(xGlobal.CookieMain, xGlobal.CookieIPLevel)) ||  xCommon.getSubCookie(xGlobal.CookieMain, xGlobal.CookieIPLevel) == cXecureLevel ) {
				xCommon.setSubCookie(xGlobal.CookieMain, xGlobal.CookieIPLevel, cXecureLevel);
			} else {
				cXecureLevel = xCommon.getSubCookie(xGlobal.CookieMain, xGlobal.CookieIPLevel);
				
				if (! (cXecureLevel == 1 || cXecureLevel == 2 || cXecureLevel == 3)) {
					cXecureLevel = 2;
					xCommon.setSubCookie(xGlobal.CookieMain, xGlobal.CookieIPLevel, cXecureLevel);
				}
			};
			return cXecureLevel ;
	},
	
	setCookie : function (value)	{
		xCommon.setSubCookie(xGlobal.CookieMain, xGlobal.CookieIPLevel, value);
	},
	
	setText : function (ipLevel) {
		var objIPLevelText = document.getElementById("ipLevelText");
		if (objIPLevelText != null) {
		//	objIPLevelText.className="level"+ipLevel;
			if(ipLevel=='3') {
				objIPLevelText.className="three";
				objIPLevelText.innerHTML = "ON";
			} else if(ipLevel=='2') {
				objIPLevelText.className="two";
				objIPLevelText.innerHTML = "ON";
			} else {
				objIPLevelText.className="one";
				objIPLevelText.innerHTML = "OFF";
			}
		}
	},
	
	setTextCy : function (ipLevel) {
		var objIPLevelText = document.getElementById("ipLevelTextCy");
		if (objIPLevelText != null) {
		//	objIPLevelText.className="level"+ipLevel;
			if(ipLevel=='3') {
				objIPLevelText.className="three";
				objIPLevelText.innerHTML = "ON";
			} else if(ipLevel=='2') {
				objIPLevelText.className="two";
				objIPLevelText.innerHTML = "ON";
			} else {
				objIPLevelText.className="one";
				objIPLevelText.innerHTML = "OFF";
			}
		}
	}
};