<form class="form-horizontal">
	<fieldset>
		<div class="form-group marginTop40">
			<label for="detComboFrom" class="col-md-3 control-label fontColorGrey">Откуда</label>
			<div class="col-md-4">
				<input type="text" style="cursor:text;" class="form-control" id="detComboFrom" value="${regFrom}" readonly>
				</input>
			</div>
		</div>
		<div class="form-group">
			<label for="throughCity" class="col-md-3 control-label fontColorGrey">Через</label>
			<div class="col-md-6">
				<input type="text" style="cursor:text;" class="form-control" id="throughCity" value="${throughCities}" readonly>
				</input>
			</div>
		</div>
		<div class="form-group">
			<label for="detComboTo" class="col-md-3 control-label fontColorGrey">Куда</label>
			<div class="col-md-4">
				<input type="text" style="cursor:text;" class="form-control" id="detComboTo" value="${regTo}" readonly>
				</input>
			</div>
		</div>
		<div class="form-group">
			<label for="detDatepicker" class="col-md-3 control-label fontColorGrey">Дата отъезда</label>
			<div class="col-md-4">
				<input type="text" id="detDatepicker" style="cursor:text;" class="form-control" value="${regDate}" readonly>
				</input>
			</div>
		</div>
		<div class="form-group">
			<label for="detMesta" class="col-md-3 control-label fontColorGrey">Кол-во мест <i class="glyphicon glyphicon-question-sign iconcolor-lightgrey" title="Количество свободных мест"></i></label>
			<div class="col-md-2">
				<input type="text" class="form-control" style="cursor:text;" id="detMesta" value="${regMesta}" readonly>
				</input>
			</div>
		</div>
		<div class="form-group">
			<label for="detDopInfo" class="col-md-3 control-label fontColorGrey">Доп. информация</label>
			<div class="col-md-6">
				<textarea id="detDopInfo" class="form-control" style="cursor:text;" rows="2" readonly>${regDopInfo}</textarea>
			</div>
		</div>
		<div class="form-group">
			<label for="detDriverName" class="col-md-3 control-label fontColorGrey">Имя водителя</label>
			<div class="col-md-4">
				<input type="text" class="form-control" style="cursor:text;" id="detDriverName" value="${regDriverName}" readonly>
				</input>
			</div>
		</div>
		<div class="form-group">
			<label for="detEmail" class="col-md-3 control-label fontColorGrey">Email</label>
			<div class="col-md-6">
				<input type="text" class="form-control" style="cursor:text;" id="detEmail" value="${regEmail}" readonly>
				</input>
			</div>
		</div>
		<div class="form-group">
			<label for="detPhone" class="col-md-3 control-label fontColorGrey">Телефон</label>
			<div class="col-md-4">
				<input type="text" class="form-control" style="cursor:text;" id="detPhone" value="${regPhone}" readonly>
				</input>
			</div>
		</div>					
		<div class="form-group">
			<label for="detPrice" class="col-md-3 control-label fontColorGrey">Цена за место <i class="glyphicon glyphicon-question-sign iconcolor-lightgrey" title="Цена проезда на одного человека в рублях"></i></label>
			<div class="col-md-2">
				<input type="text" class="form-control" style="cursor:text;" id="detPrice" value="${regPrice}" readonly>
				</input>
			</div>
		</div>
	</fieldset>
</form>		