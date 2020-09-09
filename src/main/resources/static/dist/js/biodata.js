//  get all data
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

// post biodata
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
                        text: result.message,
                    })

                }
            },
            erorrr: function (err) {
                console.log(err);
            }
        });

    },
    // setEditData: function (idCabang) {
    //     formBiodata.resetForm();

    //     $.ajax({
    //         url: '/api/biodata/' + idCabang,
    //         method: 'get',
    //         contentType: 'application/json',
    //         dataType: 'json',
    //         success: function (res, status, xhr) {
    //             if (xhr.status == 200 || xhr.status == 201) {
    //                 $('#form-biodata').fromJSON(JSON.stringify(res));
    //                 $('#modal-biodata').modal('show')
    //             } else {
    //             }
    //         },
    //         erorrr: function (err) {
    //             console.log(err);
    //         }
    //     });
};

// get biodata
var tableBiodataNik = {
    create: function (nik) {
        // jika table tersebut datatable, maka clear and dostroy
        if ($.fn.DataTable.isDataTable('#tableBiodataNik')) {
            //table yg sudah dibentuk menjadi datatable harus d rebuild lagi untuk di instantiasi ulang
            $('#tableBiodataNik').DataTable().clear();
            $('#tableBiodataNik').DataTable().destroy();
        }

        $.ajax({
            url: '/data/' + nik,
            method: 'get',
            contentType: 'application/json',
            success: function (result) {
                console.log(result);
                if (result[0].status == 'true') {
                    $('#tableBiodataNik').DataTable({
                        "paging": false,
                        "lengthChange": false,
                        "searching": false,
                        "ordering": false,
                        "info": false,
                        "autoWidth": false,
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
var formPendidikan = {
    create: function () {
        var dataResult = getJsonForm($("#pendidikanForm").serializeArray(), true);
        listpendidikan.push(dataResult);
        console.log(dataResult);
        $('#modal-pendidikan').modal('hide')

        // jika table tersebut datatable, maka clear and dostroy
        if ($.fn.DataTable.isDataTable('#tablePendidikan')) {
            //table yg sudah dibentuk menjadi datatable harus d rebuild lagi untuk di instantiasi ulang
            $('#tablePendidikan').DataTable().clear();
            $('#tablePendidikan').DataTable().destroy();
        }
        $('#tablePendidikan').DataTable({
            data: listpendidikan,
            columns: [
                {
                    title: "Jenjang",
                    data: "jenjang"
                },
                {
                    title: "Intitusi",
                    data: "institusi"
                },
                {
                    title: "Tahun Masuk",
                    data: "tahunMasuk"
                },
                {
                    title: "Tahun Lulus",
                    data: "tahunLulus"
                }
            ]

        });
    },
    resetForm: function () {
        $('#pendidikanForm')[0].reset();
    },
    saveForm: function (idPerson, listpendidikan) {
        $.ajax({
            url: '/data/pendidikan?idPerson=' + idPerson,
            method: 'post',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(listpendidikan),
            success: function (result) {
                if (result.status == 'true') {
                    Swal.fire({
                        title: 'Sukses!',
                        text: 'Data berhasil masuk!',
                        icon: 'success',
                    })
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: result.message,
                    })
                }
            },
            error: function (err) {
                console.log(err);
            }
        });
        $('#tablePendidikan').DataTable().clear();
        $('#tablePendidikan').DataTable().destroy();
        $('#tablePendidikan').empty();
    }
};