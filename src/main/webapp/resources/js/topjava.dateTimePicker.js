jQuery.datetimepicker.setLocale(navigator.language.substring(0, 2));

jQuery('.datepicker').datetimepicker({
    timepicker: false,
    format: 'Y-m-d'
});

jQuery('.timepicker').datetimepicker({
    datepicker: false,
    format: 'H:i'
});

jQuery('#datetimepicker').datetimepicker({
    format: 'Y-m-d H:i'
});