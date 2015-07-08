function getCookie(c_name) {
	var c_value = document.cookie;
	var c_start = c_value.indexOf(" " + c_name + "=");
	if (c_start == -1) {
		c_start = c_value.indexOf(c_name + "=");
	}
	if (c_start == -1) {
		c_value = null;
	} else {
		c_start = c_value.indexOf("=", c_start) + 1;
		var c_end = c_value.indexOf(";", c_start);
		if (c_end == -1) {
			c_end = c_value.length;
		}
		c_value = unescape(c_value.substring(c_start, c_end));
	}
	return c_value;
}

function isLoggedIn() {
	return (getCookie("poputiUserName") && getCookie("poputiUserIdetity"));
}

function updateUserPicAndButtons() {
	var cookieVal = getCookie("poputiUserName");
	var isLogged = cookieVal && isLoggedIn();
	document.getElementById('liEnter').style.display = (isLogged ? 'none' : 'block');
	document.getElementById('liLogout').style.display = (isLogged ? 'block' : 'none');
	var pic = document.getElementById('userPicDiv');
	if (isLogged) {
		if (pic) {
			pic.innerHTML = "<span style='font-size: 0.94em;'>Здравствуйте, " + cookieVal + "</span>&nbsp;&nbsp;"
					+ "<img style='max-height:50px'src='" + getCookie("poputiUserPhotoUrl") + "'></img>";
		}
	} else {
		if (pic) {
			pic.innerHTML = '';
		}
	}
}

updateUserPicAndButtons();
