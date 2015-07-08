<tr routeId='${routeId}'><td>${regFrom}</td><td>${throughCities}</td><td>${regTo}</td>
<td>${regDate}</td><td>${regMesta}</td>
<td align="center">${regPrice}
	<#if isThrough == 'true' && regPrice != ''>
		&nbsp;<i class='glyphicon glyphicon-info-sign iconcolor-grey' title='Стоимость указана для поездки ${regFrom} - ${regTo}. Стоимость вашего путешествия должна быть уточнена у водителя.'></i>
	</#if>
</td>
<td align="center">
<a href="show-route.html?route_id=${routeId}">Тел., Эл.почта водителя</a>
</td></tr>\n

