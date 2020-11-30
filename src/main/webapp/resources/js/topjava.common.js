var form;

function makeEditable() {
    form = $('#detailsForm');
    $(".delete").click(function () {
        if (confirm('Are you sure?')) {
            deleteRow($(this).closest('tr').attr("id"));
        }
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});
}

function add() {
    form.find(":input").val("");
    $("#editRow").modal();
}

function deleteRow(id) {
    $.ajax({
        url: ctx.ajaxUrl + id,
        type: "DELETE"
    }).done(function () {
        updateTable();
        successNoty("Deleted");
    });
}

function updateTable() {
    $.get(ctx.ajaxUrl, function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
    });
}

function save() {
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        updateTable();
        successNoty("Saved");
    });
}

var failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;Error status: " + jqXHR.status,
        type: "error",
        layout: "bottomRight"
    }).show();
}

function filterMeals() {
    // get all the form inputs into an array.
    var inputs = $('#filterForm :input');
    var values = {};
    inputs.each(function () {
        values[this.name] = $(this).val();
    });

    const querystring = createQueryString(values);

    $.ajax({
        url: ctx.ajaxUrl + 'filter?' + querystring,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
            successNoty("Filtering is completed")
        }
    });
}

function createQueryString(data) {
    const ret = [];
    for (let d in data)
        ret.push(encodeURIComponent(d) + "=" + encodeURIComponent(data[d]));
    return ret.join("&");
}

function clearFilter() {
    $.ajax({
        url: ctx.ajaxUrl + 'filter',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
            successNoty("Filtering is cleared")
        }
    });
}