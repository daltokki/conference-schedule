var util = {
  isEmpty: function (val) {
    return !val || val.length === 0 || val === '';
  },
  isNotEmpty: function (val) {
    return !util.isEmpty(val);
  },
  cleanSearchForm: function (form_name) {
    var $form = $('form').find(form_name);
    $form[0].reset();
  },
  initDatePicker: function () {
    $('input[data-date-picker="on"]').datepicker({
      format: 'yyyy-mm-dd',
      titleformat: 'yyyy MM',
      language: '{{locale}}',
      todayBtn: true,
      autoclose: true
    });
    $('input[data-month-picker="on"]').datepicker({
      format: 'yyyy-mm',
      titleformat: 'yyyy MM',
      viewMode: "months",
      minViewMode: "months",
      language: '{{locale}}',
      todayBtn: true,
      autoclose: true
    });
  },
  preventEnterEvent: function () {
    $('input[type="text"]').keydown(function() {
      if (event.keyCode === 13) {
        event.preventDefault();
      }
    });
  },
  numFormatter: function () {
    Number.prototype.format = function(){
      if(this==0) return 0;

      var reg = /(^[+-]?\d+)(\d{3})/;
      var n = (this + '');

      while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');

      return n;
    };
    String.prototype.format = function(){
      var num = parseFloat(this);
      if( isNaN(num) ) return "0";

      return num.format();
    };
  }
};
