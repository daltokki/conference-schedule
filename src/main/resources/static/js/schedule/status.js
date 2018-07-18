$(function () {
  Date.prototype.yyyyMMdd = function()
  {
    var yyyy = this.getFullYear().toString();
    var mm = (this.getMonth() + 1).toString();
    var dd = this.getDate().toString();

    return yyyy + (mm[1] ? mm : '0'+mm[0]) + (dd[1] ? dd : '0'+dd[0]);
  };
  var scheduleDate = $('#scheduleDate').val();
  if (util.isEmpty(scheduleDate)) {
    scheduleDate = (new Date()).yyyyMMdd();
  }
  $('#calendar').fullCalendar({
    defaultView: 'agendaDay',
    defaultDate: scheduleDate,
    events: {
      url: '/schedule/get',
      type: 'GET',
      data: { scheduleDate: scheduleDate },
      error: function () {
        alert('스케줄이 생성되지 않았습니다. 예약을 진행해 주세요.');
      }
    },
    resources: {
      url: '/schedule/room/get',
      data: { scheduleDate: scheduleDate },
      type: 'GET',
      error: function () {
        alert('회의실이 할당되지 않았습니다. 예약을 진행해 주세요.');
      }
    }
  });
});