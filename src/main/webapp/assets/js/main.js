$(document).ready(function() {
	
	// carousel
	$('.carousel').carousel({
		interval : 4000
	});

	// datepicker
	$.datepicker.setDefaults($.datepicker.regional["ru"]);
	$.datepicker.setDefaults({
		minDate : 0
		/*onSelect : function(text, elem) {
			// reset validation now in connectCombos method
			$(this).valid();
		}*/
	});
	
	// question icons styling
	$.each($("i.glyphicon-question-sign"), function(index, el) {
		$(el).addClass("iconcolor-lightgrey");
	});
	
	// cookies
	if (!navigator.cookieEnabled) {
		showAlert(null, "<div class=\"alert alert-danger\"><h4><strong>Внимание.</strong> Для работы с данным ресурсом необходимо разрешить использование cookies в настройках браузера.</h4></div>");
	}

	// buttons 
	$(document).on("click", "#contactUsButton", function(event){
		alert(document.getElementById('contactUsName').value);
		return false;
	});
	
	var resolution = findBootstrapEnvironment();
	if (resolution == 'xs' || resolution == 'sm') {
		$('#mobilenotify').html('Для вашего устройства отображена мобильная версия сайта');
		$('#mobilenotify').show();
	}
	
	initMenuLinks();

});

function initMenuLinks() {
	$('a.menulink').click(function() {
		window.location.href = $(this).attr("rel");
		return false;
	});
}
/**
 * Represents all fields of the object in readable way. 
 * Usage example: alert(dump(obj))
 * 
 * @param {Object} obj
 * @return {TypeName} 
 */
function dump(obj) {
	var str = "";
	for (var i in obj) {
		str += i + " = " + obj[i] + "\n";
	}
	return str;
}

// cities list load
function initCombos(selector) {
	var item = window.sessionStorage.getItem('citiesList'); 
	if (!item) {
		$.ajax({
			url : "/poputi/cities.list.json",
			success : function(msg) {
				window.sessionStorage.setItem('citiesList', msg);
				$(selector).append(msg);
			}
		});
	} else {
		$(selector).append(item);
	}
}

// connects combos on main and found pages
// called on page init, after combos init
function connectCombos(fromId, toId, dateId, is3daysId) {
	// init and connect components
	$(fromId).on('change', function() { window.sessionStorage.setItem('poputiFrom', $(this).val());});
	$(toId).on('change', function() { window.sessionStorage.setItem('poputiTo', $(this).val());});
	$(dateId).datepicker({onSelect : function() { /* reset validation */ $(this).valid(); window.sessionStorage.setItem('poputiDate', $(this).val());}}).val('');
	$(is3daysId).on('click', function() { window.sessionStorage.setItem('poputiIs3days', this.checked);});
	
	// set stored values
	var from = window.sessionStorage.getItem('poputiFrom');
	if (from) {
		$(fromId).val(from);
	}
	var to = window.sessionStorage.getItem('poputiTo');
	if (to) {
		$(toId).val(to);
	}
	var date = window.sessionStorage.getItem('poputiDate');
	if (date) {
		$(dateId).datepicker('setDate', date);
	}
	var is3days = window.sessionStorage.getItem('poputiIs3days');
	if (is3days) {
		$(is3daysId).prop('checked', 'true');
	}
}

// очищаем все поля формы (устанавливаем value в '')
function resetForm($form) {
    $form.find("input:text:not('#regTimepicker'), input:password, input:file, select, textarea").val('');
    $form.find('input:radio, input:checkbox')
         .removeAttr('checked').removeAttr('selected');
}

function animateValidationErrors(me, errorList) {
	$.each(me.validElements(), function(index, element) {
		var $element = $(element);
		$element.data("title", "").removeClass("error").tooltipster("hide");
	});
	$.each(errorList, function(index, error) {
		var $element = $(error.element);
		$element.tooltipster('update', error.message).addClass("error").tooltipster('show');
	});
}

/*function cleanValidationTooltips() {
	$.each($('form select'), function(index, element) {
		var $element = $(element);
		$element.data("title", "").removeClass("error").tooltipster("hide");
	});
	$.each($('form input[type="text"]'), function(index, element) {
		var $element = $(element);
		$element.data("title", "").removeClass("error").tooltipster("hide");
	});
}*/

// li
/*function styleAndSelectTab(id) {
	$('#mainTabBar > li').removeClass("custom-active");
	$('#' + id).addClass("custom-active");
}*/

// a href
/*function switchToTab(id) {
	$("a[href='#" + id + "']").tab("show");
	$(window).scrollTop(0);
}*/

function refreshRoutesTable() {
	$.ajax({
		url : "get.route.list.json",
		data : {
			tableType : "DRIVER"
		},
		success : function(msg) {
			$('#regTable > tbody').empty();
			$('#regTable > tbody:last').append(msg == '' ? '<tr><td colspan=9>Нет зарегистрированных поездок</td></tr>' : msg);
		}
	});
}

function showAlert(title, html, button) {
	if (!button) {
		button = { label : "Ok", className : "btn-warning" };
	}
	bootbox.dialog({
		title : title,
		message : html,
		animate : false,
		onEscape : function() {
			bootbox.hideAll();
		},
		buttons : {
			success : button
		}
	});
}

function removeRoute(icon) {
	bootbox.dialog({
		title : 'Подтверждение удаления',
		message : "<div class='alert alert-danger'>Пожалуйста, подтвердите, что хотите удалить данную поездку.</div>",
		animate : false,
		onEscape : function() {
			bootbox.hideAll();
		},
		buttons : {
			success : {
				label : "Удалить",
				className : "btn-default",
				callback : function() {
					var row = $(icon).parent().parent('tr');
					var routeId = row.attr('routeId');
					$.post("post.route.deactivate.json", {
						routeId : routeId
					}, function(res) {
						row.hide();
						showToast('Поездка удалена');
					});
				}
			},
			close : {
				label : 'Отмена',
				className : "btn-default",
				callback : function() {
					bootbox.hideAll();
				}
			}
		}
	});
}

function showToast(text) {
	var t = $('#routeSuccesToast');
	t.empty();
	t.append("<i class=\"glyphicon glyphicon-info-sign\"></i>&nbsp;&nbsp;" + text);
	t.fadeIn(200).delay(4000).fadeOut(400);
}

function showProgress() {
	$('#progressBarDiv').fadeIn(50);
}

function hideProgress() {
	$('#progressBarDiv').fadeOut(200);
}

function setCookie(c_name, value, exdays) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + exdays);
	var c_value = escape(value) + ((exdays == null) ? "" : "; expires=" + exdate.toUTCString());
	document.cookie = c_name + "=" + c_value;
}

function clearAllCookies() {
	setCookie('poputiUserName', '', 3);
	setCookie('poputiUserIdetity', '', 3);
	setCookie('poputiUserPhotoUrl', '', 3);
}
 
function findRoutes(cityFrom, cityTo, routeDate, is3daysStr) {
	if (!cityFrom && !cityTo) {
		return;
	}
	showProgress();
	$.ajax({
		url : "/poputi/get.route.list.json",
		data : {
			tableType : "FOUND",
			cityFrom : cityFrom,
			cityTo : cityTo,
			routeDate : routeDate,
			threeDays : is3daysStr
		},
		success : function(msg) {
			$('#searchResultPanel').show();
			$('#foundTable > tbody').empty();
			$('#foundTable > tbody:last').append(msg == '' ? '<tr><td colspan=7><strong>Нет найденных поездок. Попробуйте изменить запрос.</strong></td></tr>' : msg);
			if (msg == '') {
				$(window).scrollTop(0);
				showToast('Нет найденных поездок. Попробуйте изменить запрос.');
			}
			hideProgress();
		}
	});
}

function contactUsFunc() {
	showProgress();
	$.post("post.contactus.json", {
		userName : $("#contactUsName").val(),
		userEmail : $("#contactUsEmail").val(),
		userMsg : $("#contactUsMsg").val()
	}, function(resp) {
		$('#contactUsModal').modal('hide');
		$("#contactUsName").val('');
		$("#contactUsEmail").val('');
		$("#contactUsMsg").val('');
		hideProgress();
		$(window).scrollTop(0);
		showToast('Ваше сообщение отправлено.');
	});
}

function citiesListFunc() {
	showAlert('Список городов в системе', $('#citiesListDiv').html());
}

// var query = getQueryParams(document.location.search); alert(query.foo);
function getQueryParams(qs) {
	qs = qs.split("+").join(" ");
	var params = {}, tokens, re = /[?&]?([^=]+)=([^&]*)/g;
	while (tokens = re.exec(qs)) {
		params[decodeURIComponent(tokens[1])] = decodeURIComponent(tokens[2]);
	}
	return params;
}

function findBootstrapEnvironment() {
	var envs = [ 'xs', 'sm', 'md', 'lg' ];
	$el = $('<div>');
	$el.appendTo($('body'));
	for ( var i = envs.length - 1; i >= 0; i--) {
		var env = envs[i];

		$el.addClass('hidden-' + env);
		if ($el.is(':hidden')) {
			$el.remove();
			return env;
		}
	}
	;
}