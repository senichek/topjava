var form;

function makeEditable(datatableOpts) {
    ctx.datatableApi = $("#datatable").DataTable(
        // https://api.jquery.com/jquery.extend/#jQuery-extend-deep-target-object1-objectN
        $.extend(true, datatableOpts,
            {
                "ajax": {
                    "url": ctx.ajaxUrl,
                    "dataSrc": ""
                },
                "paging": false,
                "info": true
            }
        ));

    form = $('#detailsForm');
    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
}

function add() {
    $("#modalTitle").html(i18n["addTitle"]);
    form.find(":input").val("");
    $("#editRow").modal();
}

function updateRow(id) {
    form.find(":input").val("");
    $("#modalTitle").html(i18n["editTitle"]);
    $.get(ctx.ajaxUrl + id, function (data) {
        $.each(data, function (key, value) {
            form.find("input[name='" + key + "']").val(value);
        });
        $('#editRow').modal();
    });
}

function deleteRow(id) {
    if (confirm(i18n['common.confirm'])) {
        $.ajax({
            url: ctx.ajaxUrl + id,
            type: "DELETE"
        }).done(function () {
            ctx.updateTable();
            successNoty("common.deleted");
        });
    }
}

function updateTableByData(data) {
    ctx.datatableApi.clear().rows.add(data).draw();
}

function save() {
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        ctx.updateTable();
        successNoty("common.saved");
    });
}

var failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(key) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + i18n[key],
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function validationErrorMessage(jqXHR) {
    debugger;
    var errorInfo = JSON.parse(jqXHR.responseText);
    var i;
    var errorMessage = "";
    for (i = 0; i < errorInfo.field.length; i++) {
        if (errorInfo.field[i] == "dateTime") {
            if (errorInfo.detail[i].includes("null")) {
                errorMessage += "[" + i18n["meal.dateTime"] + "]" + " " + i18n["common.empty"] + "<br>";
            } else {
                errorMessage += "[" + i18n["meal.dateTime"] + "]" + " " + errorInfo.detail[i] + "<br>";
            }
        }
        if (errorInfo.field[i] == "description") {
            if (errorInfo.detail[i].includes("null")) {
                errorMessage += "[" + i18n["meal.description"] + "]" + " " + i18n["common.empty"] + "<br>";
            } else {
                errorMessage += "[" + i18n["meal.description"] + "]" + " " + errorInfo.detail[i] + "<br>";
            }
        }
        if (errorInfo.field[i] == "calories") {
            if (errorInfo.detail[i].includes("null")) {
                errorMessage += "[" + i18n["meal.calories"] + "]" + " " + i18n["common.empty"] + "<br>";
            } else {
                errorMessage += "[" + i18n["meal.calories"] + "]" + " " + errorInfo.detail[i] + "<br>";
            }
        }
        if (errorInfo.field[i] == "name") {
            if (errorInfo.detail[i].includes("null")) {
                errorMessage += "[" + i18n["user.name"] + "]" + " " + i18n["common.empty"] + "<br>";
            } else {
                errorMessage += "[" + i18n["user.name"] + "]" + " " + errorInfo.detail[i] + "<br>";
            }
        }
        if (errorInfo.field[i] == "email") {
            if (errorInfo.detail[i].includes("null")) {
                errorMessage += "[" + i18n["user.email"] + "]" + " " + i18n["common.empty"] + "<br>";
            } else {
                errorMessage += "[" + i18n["user.email"] + "]" + " " + errorInfo.detail[i] + "<br>";
            }
        }
        if (errorInfo.field[i] == "password") {
            if (errorInfo.detail[i].includes("null")) {
                errorMessage += "[" + i18n["user.password"] + "]" + " " + i18n["common.empty"] + "<br>";
            } else {
                errorMessage += "[" + i18n["user.password"] + "]" + " " + errorInfo.detail[i] + "<br>";
            }
        }
        if (errorInfo.field[i] == "dateTime" && errorInfo.detail[i].includes("meals_unique_user_datetime_idx")) {
            errorMessage = i18n["meal.dupeDateTime"];
        }
        if (errorInfo.field[i] == "email" && errorInfo.detail[i].includes("users_unique_email_idx")) {
            errorMessage = i18n["user.emailExists"];
        }
    }
    return errorMessage;
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;" + i18n["common.dataTypeError"] +
            "<br>" + validationErrorMessage(jqXHR),
        type: "error",
        layout: "bottomRight"
    }).show();
}

function renderEditBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='updateRow(" + row.id + ");'><span class='fa fa-pencil'></span></a>";
    }
}

function renderDeleteBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='deleteRow(" + row.id + ");'><span class='fa fa-remove'></span></a>";
    }
}