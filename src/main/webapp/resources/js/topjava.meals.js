var ctx, mealAjaxUrl = "profile/meals/";

// $(document).ready(function () {
$(function () {
    // https://stackoverflow.com/a/5064235/548473
    ctx = {
        ajaxUrl: mealAjaxUrl,
        datatableApi: $("#datatableMeal").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return date.replace("T", " ").substring(0, 16);
                        }
                        return date;
                    }
                },
                {
                    "data": "description",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return data;
                        }
                        return data;
                    }
                },
                {
                    "data": "calories",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return data;
                        }
                        return data;
                    }
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                if (data.excess) {
                    $(row).attr("data-mealExcess", true);
                } else {
                    $(row).attr("data-mealExcess", false);
                }
            }
        }),
        updateTable: function () {
            $.get(mealAjaxUrl, updateFilteredTable());
        }
    };
    makeEditable();
});