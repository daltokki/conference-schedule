$(function () {
  var elements = {},
    service = {
      drawTimeOptions: function (scheduleDate) {
        $.ajax({
          url: '/schedule/time/get',
          type: 'get',
          data: { scheduleDate: scheduleDate },
          success: function (result) {
            if (result.success) {
              $('#scheduleTime').empty();
              $.each(result.result, function (idx, time) {
                $('[name=scheduleTime]').append('<option value="' + time + '">' + time + '</option>');
              });
            } else {
              alert(result.message);
            }
          }
        });
      },
      booking: function (requestData) {
        $.ajax({
          url: '/schedule/save',
          type: 'post',
          dataType: 'json',
          contentType: 'application/json',
          data: JSON.stringify(requestData),
          success: function (result) {
            if (result.success) {
              alert(result.message);
              location.href = '/schedule/list/' + requestData.scheduleDate;
            } else {
              alert(result.message);
            }
          }
        });
      }
    };

  elements['$onDatePicker'] = $('input[data-date-picker="on"]');
  elements['$btnSave'] = $('#saveBtn');

  elements.$onDatePicker.datepicker('option', 'dateFormat', 'yy-mm-dd');
  elements.$onDatePicker.on('change', function () {
    var scheduleDate = $(this).val().replace(/-/gi, '');
    service.drawTimeOptions(scheduleDate);
  });
  elements.$btnSave.on('click', function () {
    var conferenceTitle = $('#conferenceTitle').val();
    if (util.isEmpty(conferenceTitle)) {
      alert('회의주제를 입력해주세요.');
      return;
    }
    var scheduleDate = elements.$onDatePicker.val().replace(/-/gi, '');
    if (util.isEmpty(scheduleDate)) {
      alert('예약일자를 선택해주세요.');
      return;
    }
    var scheduleStartTime = $('#scheduleStartTime').val().replace(':', "");
    if (util.isEmpty(scheduleStartTime)) {
      alert('예약 시작시간을 선택해주세요.');
      return;
    }
    var scheduleEndTime = $('#scheduleEndTime').val().replace(':', "");
    if (util.isEmpty(scheduleEndTime)) {
      alert('예약 종료시간을 선택해주세요.');
      return;
    }
    if (scheduleStartTime === scheduleEndTime) {
      alert('예약 시작과 종료시간이 같습니다. 다시 설정해 주세요.');
      return;
    }
    var repeatChecked = $('#activeRepeat').is(':checked');
    var repeatCount = $('#repeatCount').val() | 0;
    if (repeatChecked) {
      if (util.isEmpty(repeatCount)) {
        alert('반복 수를 설정해주세요.');
        return;
      }
      if (repeatCount < 1) {
        alert('반복 횟 수는 1회 이상이어야 합니다.');
        return;
      }
      if (repeatCount > 8) {
        alert('반복 횟 수는 8회 이하까지 설정 가능합니다.');
        return;
      }
    }
    service.booking({
      conferenceRoom: $('#conferenceRoom').val(),
      conferenceTitle: conferenceTitle,
      scheduleDate: scheduleDate,
      scheduleStartTime: scheduleStartTime,
      scheduleEndTime: scheduleEndTime,
      repeatCount: repeatCount
    });
  });
});