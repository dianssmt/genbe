var tableBiodata = {
    create: function () {
        // jika table tersebut datatable, maka clear and dostroy
        if ($.fn.DataTable.isDataTable('#tableBiodata')) {
            //table yg sudah dibentuk menjadi datatable harus d rebuild lagi untuk di instantiasi ulang
            $('#tableBiodata').DataTable().clear();
            $('#tableBiodata').DataTable().destroy();
        }

        $.ajax({
            url: '/data/getAllData',
            method: 'get',
            contentType: 'application/json',
            success: function (res, status, xhr) {
                if (xhr.status == 200 || xhr.status == 201) {
                    $('#tableBiodata').DataTable({
                        data: res,
                        columns: [
                            {
                                title: "NIK",
                                data: "nik"
                            },
                            {
                                title: "Nama Lengkap",
                                data: "nama"
                            },
                            {
                                title: "Alamat",
                                data: "alamat"
                            },
                            {
                                title: "No. HP",
                                data: "noHp"
                            },
                            {
                                title: "Tanggal Lahir",
                                data: "tanggalLahir"
                            },
                            {
                                title: "Tempat Lahir",
                                data: "tempatLahir"
                            },
                            {
                                title: "Action",
                                data: null,
                                render: function (data, type, row) {
                                    return "<p align='center'><button class='btn-orange btn btn-sm' onclick=formBiodata.setEditData('" + data.id + "')><i class='fas fa-edit'></i></button></p>"
                                }
                            }
                        ]
                    });

                } else {

                }
            },
            error: function (err) {
                console.log(err);
            }
        });


    }
};

var formBiodata = {
    resetForm: function () {
        $('#biodataForm')[0].reset();
    },
    saveForm: function () {

        var dataResult = getJsonForm($("#biodataForm").serializeArray(), true);

        $.ajax({
            url: '/data',
            method: 'post',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(dataResult),
            success: function (result) {
                $('#modal-biodata').modal('hide')
                if (result.status == 'true') {
                    tableBiodata.create();
                    Swal.fire(
                        'Sukses!',
                        'Data berhasil masuk!',
                        'success'
                    )
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: result[0].message,
                    })

                }
            },
            erorrr: function (err) {
                console.log(err);
            }
        });

    }, setEditData: function (idCabang) {
        formBiodata.resetForm();

        $.ajax({
            url: '/api/biodata/' + idCabang,
            method: 'get',
            contentType: 'application/json',
            dataType: 'json',
            success: function (res, status, xhr) {
                if (xhr.status == 200 || xhr.status == 201) {
                    $('#form-biodata').fromJSON(JSON.stringify(res));
                    $('#modal-biodata').modal('show')
                } else {
                }
            },
            erorrr: function (err) {
                console.log(err);
            }
        });
    }
};
var tableBiodataNik = {
    create: function (nik) {
        // jika table tersebut datatable, maka clear and dostroy
        if ($.fn.DataTable.isDataTable('#tableBiodataNik')) {
            //table yg sudah dibentuk menjadi datatable harus d rebuild lagi untuk di instantiasi ulang
            $('#tableBiodataNik').DataTable().clear();
            $('#tableBiodataNik').DataTable().destroy();
        }

        $.ajax({
            url: '/data/'+nik,
            method: 'get',
            contentType: 'application/json',
            success: function (result) {
                console.log(result);
                if (result[0].status=='true') {
                    $('#tableBiodataNik').DataTable({
                        data: [result[0].data],
                        columns: [
                            {
                                title: "NIK",
                                data: "nik"
                            },
                            {
                                title: "Nama Lengkap",
                                data: "nama"
                            },
                            {
                                title: "Alamat",
                                data: "alamat"
                            },
                            {
                                title: "No. HP",
                                data: "noHp"
                            },
                            {
                                title: "Tanggal Lahir",
                                data: "tanggalLahir"
                            },
                            {
                                title: "Tempat Lahir",
                                data: "tempatLahir"
                            },
                            {
                                title: "Umur",
                                data: "umur"
                            },
                            {
                                title: "Pendidikan Terakhir",
                                data: "pendidikanTerakhir"
                            },
                        ]
                        
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: result[0].message,
                    })
                }
            },
            error: function (err) {
                console.log(err);
            }
        });


    }
};
