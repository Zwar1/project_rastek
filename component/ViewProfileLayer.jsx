import { Icon } from "@iconify/react/dist/iconify.js";
import React, { useState, useEffect } from "react";
import useDetailedEmployee from "../hook/useDetailedEmployee";
import useAddCV from "../hook/useCV";


const ViewProfileLayer = () => {
  const detailedEmployee = useDetailedEmployee();
  const nik = detailedEmployee?.data?.employee?.nik;

  const [imagePreview, setImagePreview] = useState(
    "https://th.bing.com/th/id/OIP.8KQE1OJfKq-ZZHd1FOQJFQHaHa?rs=1&pid=ImgDetMain"
  );
  const [passwordVisible, setPasswordVisible] = useState(false);
  const [confirmPasswordVisible, setConfirmPasswordVisible] = useState(false);

  const [projects, setProjects] = useState([]);
  const [project, setProject] = useState({
    name: "",
    position: "",
    start: "",
    end: "",
    description: "",
  });

  const {
    cvData,
    setCvData,
    handleChange: handleCVChange,
    handleSubmit: handleSaveCV,
    cvList,
    handleDelete
  } = useAddCV(nik);

  const handleChange = (e) => {
    setProject({ ...project, [e.target.id]: e.target.value });
  };

  const handleSaveProject = () => {
    setProjects([...projects, project]);
    setProject({ name: "", position: "", start: "", end: "", description: "" });
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const day = date.getDate();
    const month = date.toLocaleString("en-US", { month: "long" });
    const year = date.getFullYear();
    return `${day} ${month} ${year}`;
  };

  // Toggle function for password field
  const togglePasswordVisibility = () => {
    setPasswordVisible(!passwordVisible);
  };

  // Toggle function for confirm password field
  const toggleConfirmPasswordVisibility = () => {
    setConfirmPasswordVisible(!confirmPasswordVisible);
  };

  const readURL = (input) => {
    if (input.target.files && input.target.files[0]) {
      const reader = new FileReader();
      reader.onload = (e) => {
        setImagePreview(e.target.result);
      };
      reader.readAsDataURL(input.target.files[0]);
    }
  };
  return (
    <div className="row gy-4">
      <div className="col-lg-4">
        <div className="user-grid-card position-relative border radius-16 overflow-hidden bg-base h-100">
          <img
            src="https://i.pinimg.com/736x/70/c9/ba/70c9ba95b3e724529fcaaf370c739819.jpg"
            alt=""
            className="w-100 object-fit-cover"
          />
          <div className="pb-24 ms-16 mb-24 me-16  mt--100">
            <div className="text-center border border-top-0 border-start-0 border-end-0">
              <img
                src="assets/images/user.png"
                alt=""
                className="border br-white border-width-2-px w-200-px h-200-px rounded-circle object-fit-cover"
              />
              <h6 className="mb-0 mt-16">
                {detailedEmployee?.data?.employee?.name}
              </h6>
              <span className="text-secondary-light mb-16">
                {detailedEmployee?.data?.email}
              </span>
            </div>
            <div className="mt-24">
              <h6 className="text-xl mb-16">User Info</h6>
              <ul>
                <li className="d-flex align-items-center gap-1 mb-12">
                  <span className="w-30 text-md fw-semibold text-primary-light">
                    Full Name
                  </span>
                  <span className="w-70 text-secondary-light fw-medium">
                    : {detailedEmployee?.data?.employee?.name}
                  </span>
                </li>
                <li className="d-flex align-items-center gap-1 mb-12">
                  <span className="w-30 text-md fw-semibold text-primary-light">
                    {" "}
                    Email
                  </span>
                  <span className="w-70 text-secondary-light fw-medium">
                    : {detailedEmployee?.data?.email}
                  </span>
                </li>
                <li className="d-flex align-items-center gap-1 mb-12">
                  <span className="w-30 text-md fw-semibold text-primary-light">
                    {" "}
                    Phone Number
                  </span>
                  <span className="w-70 text-secondary-light fw-medium">
                    : {detailedEmployee?.data?.employee?.noTelp}
                  </span>
                </li>
                <li className="d-flex align-items-center gap-1 mb-12">
                  <span className="w-30 text-md fw-semibold text-primary-light">
                    {" "}
                    Position
                  </span>
                  <span className="w-70 text-secondary-light fw-medium">
                    :{" "}
                    {
                      detailedEmployee?.data?.employee?.riwayatJabatan?.[0]
                        ?.kodeJabatan?.namaJabatan
                    }
                  </span>
                </li>
                <li className="d-flex align-items-center gap-1 mb-12">
                  <span className="w-30 text-md fw-semibold text-primary-light">
                    {" "}
                    Departement
                  </span>
                  <span className="w-70 text-secondary-light fw-medium">
                    :{" "}
                    {
                      detailedEmployee?.data?.employee?.riwayatJabatan?.[0]
                        ?.kodeJabatan?.departement
                    }
                  </span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div className="col-lg-8">
        <div className="card h-100">
          <div className="card-body p-24">
            <ul
              className="nav border-gradient-tab nav-pills mb-20 d-inline-flex"
              id="pills-tab"
              role="tablist"
            >
              <li className="nav-item" role="presentation">
                <button
                  className="nav-link d-flex align-items-center px-24 active"
                  id="pills-edit-profile-tab"
                  data-bs-toggle="pill"
                  data-bs-target="#pills-edit-profile"
                  type="button"
                  role="tab"
                  aria-controls="pills-edit-profile"
                  aria-selected="true"
                >
                  Basic Information
                </button>
              </li>
              <li className="nav-item" role="presentation">
                <button
                  className="nav-link d-flex align-items-center px-24"
                  id="pills-edit-profile-tab"
                  data-bs-toggle="pill"
                  data-bs-target="#pills-edit-personal"
                  type="button"
                  role="tab"
                  aria-controls="pills-edit-personal"
                  aria-selected="true"
                >
                  Personal Information
                </button>
              </li>
              <li className="nav-item" role="presentation">
                <button
                  className="nav-link d-flex align-items-center px-24"
                  id="pills-change-passwork-tab"
                  data-bs-toggle="pill"
                  data-bs-target="#pills-change-passwork"
                  type="button"
                  role="tab"
                  aria-controls="pills-change-passwork"
                  aria-selected="false"
                >
                  Change Password
                </button>
              </li>
              <li className="nav-item" role="presentation">
                <button
                  className="nav-link d-flex align-items-center px-24"
                  id="pills-cv-tab"
                  data-bs-toggle="pill"
                  data-bs-target="#pills-cv"
                  type="button"
                  role="tab"
                  aria-controls="pills-cv"
                  aria-selected="false"
                  tabIndex={-1}
                >
                  CV
                </button>
              </li>
            </ul>
            <div className="tab-content" id="pills-tabContent">
              <div
                className="tab-pane fade show active"
                id="pills-edit-profile"
                role="tabpanel"
                aria-labelledby="pills-edit-profile-tab"
                tabIndex={0}
              >
                <h6 className="text-md text-primary-light mb-16">
                  Profile Image
                </h6>
                {/* Upload Image Start */}
                <div className="mb-24 mt-16">
                  <div className="avatar-upload">
                    <div className="avatar-edit position-absolute bottom-0 end-0 me-24 mt-16 z-1 cursor-pointer">
                      <input
                        type="file"
                        id="imageUpload"
                        accept=".png, .jpg, .jpeg"
                        hidden
                        onChange={readURL}
                      />
                      <label
                        htmlFor="imageUpload"
                        className="w-32-px h-32-px d-flex justify-content-center align-items-center bg-primary-50 text-primary-600 border border-primary-600 bg-hover-primary-100 text-lg rounded-circle"
                      >
                        <Icon
                          icon="solar:camera-outline"
                          className="icon"
                        ></Icon>
                      </label>
                    </div>
                    <div className="avatar-preview">
                      <div
                        id="imagePreview"
                        style={{
                          backgroundImage: `url(${imagePreview})`,
                          backgroundSize: "cover",
                          backgroundPosition: "center",
                        }}
                      />
                    </div>
                  </div>
                </div>
                {/* Upload Image End */}
                <form action="#">
                  <div className="row">
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="name position"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Position
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="text"
                          className="form-control radius-8"
                          id="position"
                          value={
                            detailedEmployee?.data?.employee
                              ?.riwayatJabatan?.[0]?.kodeJabatan?.namaJabatan
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="contract-start-date"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Contract Start Date{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="date"
                          className="form-control radius-8"
                          id="contract-start-date"
                          value={
                            detailedEmployee?.data?.employee
                              ?.riwayatJabatan?.[0]?.tmt_awal
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="division"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Division <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="division"
                          className="form-control radius-8"
                          id="division"
                          value={
                            detailedEmployee?.data?.employee
                              ?.riwayatJabatan?.[0]?.kodeJabatan?.division
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="contract-start-date"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Contract Second Start Date{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="date"
                          className="form-control radius-8"
                          id="contract-start-date"
                          value={
                            detailedEmployee?.data?.employee
                              ?.riwayatJabatan?.[0]?.tmt_akhir
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="departement"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Departement <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="departement"
                          className="form-control radius-8"
                          id="departement"
                          value={
                            detailedEmployee?.data?.employee
                              ?.riwayatJabatan?.[0]?.kodeJabatan?.departement
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="sub divisi"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Sub Divisi <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="sub divisi"
                          className="form-control radius-8"
                          id="sub divisi"
                          placeholder="Enter sub divisi"
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="number"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Salary
                        </label>
                        <input
                          type="salary"
                          className="form-control radius-8"
                          id="salary"
                          value={
                            detailedEmployee?.data?.employee
                              ?.riwayatJabatan?.[0]?.salary
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="contract status"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Contract Status
                        </label>
                        <input
                          type="contract status"
                          className="form-control radius-8"
                          id="contract status"
                          value={
                            detailedEmployee?.data?.employee
                              ?.riwayatJabatan?.[0].statusKontrak
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="attachment"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Attachment
                        </label>
                        <input
                          type="attachment"
                          className="form-control radius-8"
                          id="attachment"
                          value={detailedEmployee?.data?.employee?.attachment}
                        />
                      </div>
                    </div>
                  </div>
                  <div className="d-flex align-items-center justify-content-end gap-3 pe-0">
                    <button
                      type="button"
                      className="border border-danger-600 bg-hover-danger-200 text-danger-600 text-sm px-24 py-6 radius-4"
                    >
                      Cancel
                    </button>
                    <button
                      type="button"
                      className="btn btn-primary border border-primary-600 text-sm px-24 py-6 radius-4"
                    >
                      Save
                    </button>
                  </div>
                </form>
              </div>
              {/* Isi Personal Information */}
              <div
                className="tab-pane fade"
                id="pills-edit-personal"
                role="tabpanel"
                aria-labelledby="pills-edit-profile-tab"
                tabIndex="0"
              >
                <form action="#">
                  <div className="row">
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="nama"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Nama <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="text"
                          className="form-control radius-8"
                          id="nama"
                          value={detailedEmployee?.data?.employee?.name}
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="nomor induk karyawan"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Nomor Induk Karyawan{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="nomor induk karyawan"
                          className="form-control radius-8"
                          id="nomor induk karyawan"
                          value={detailedEmployee?.data?.employee?.nik}
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="nomor ktp"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Nomor KTP<span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="nomor ktp"
                          className="form-control radius-8"
                          id="nomor ktp"
                          value={detailedEmployee?.data?.employee?.no_ktp}
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="nomor npwp"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Nomor NPWP <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="nomor npwp"
                          className="form-control radius-8"
                          id="nomor npwp"
                          value={detailedEmployee?.data?.employee?.npwp}
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="kartu keluarga"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Kartu Keluarga{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="text"
                          className="form-control radius-8"
                          id="kartu keluarga"
                          value={
                            detailedEmployee?.data?.employee?.kartuKeluarga
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="jenis kelamin"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Jenis Kelamin{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="text"
                          className="form-control radius-8"
                          id="jenis kelamin"
                          value={detailedEmployee?.data?.employee?.jenisKelamin}
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="agama"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Agama <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="agama"
                          className="form-control radius-8"
                          id="agama"
                          value={detailedEmployee?.data?.employee?.agama}
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="tempat tanggal lahir"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Tempat Tanggal Lahir{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="tempat tanggal lahir"
                          className="form-control radius-8"
                          id="tempat tanggal lahir"
                          value={detailedEmployee?.data?.employee?.tanggalLahir}
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="alamat lengkap"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Alamat Lengkap{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="alamat lengkap"
                          className="form-control radius-8"
                          id="alamat lengkap"
                          value={
                            detailedEmployee?.data?.employee?.alamatLengkap
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="alamat domisili"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Alamat Domisili{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="alamat domisili"
                          className="form-control radius-8"
                          id="alamat domisili"
                          value={
                            detailedEmployee?.data?.employee?.alamatDomisili
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="nama telopon"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Nomor Telepon{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="nama telopon"
                          className="form-control radius-8"
                          id="nama telopon"
                          value={detailedEmployee?.data?.employee?.noTelp}
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="nama kontak darurat"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Nama Kontak Darurat{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="nama kontak darurat"
                          className="form-control radius-8"
                          id="nama kontak darurat"
                          value={
                            detailedEmployee?.data?.employee?.kontakDarurat
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="nomor kontak darurat"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Nomor Kontak Darurat{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="nomor kontak darurat"
                          className="form-control radius-8"
                          id="nomor kontak darurat"
                          value={
                            detailedEmployee?.data?.employee?.noKontakDarurat
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="email"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Email <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="email"
                          className="form-control radius-8"
                          id="email"
                          value={detailedEmployee?.data?.email}
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="pendidikan terakhir"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Pendidikan Terakhir
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="pendidikan terakhir"
                          className="form-control radius-8"
                          id="pendidikan terakhir"
                          value={
                            detailedEmployee?.data?.employee?.pendidikanTerakhir
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="jurusan"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Jurusan <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="jurusan"
                          className="form-control radius-8"
                          id="jurusan"
                          value={detailedEmployee?.data?.employee?.jurusan}
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="nama universitas"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Nama Universitas{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="nama universitas"
                          className="form-control radius-8"
                          id="nama universitas"
                          value={
                            detailedEmployee?.data?.employee?.namaUniversitas
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="nama ibu kandung"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Nama Ibu Kandung{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="nama ibu kandung"
                          className="form-control radius-8"
                          id="nama ibu kandung"
                          value={
                            detailedEmployee?.data?.employee?.namaIbuKandung
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="status pernikahan"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Status Pernikahan{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="status pernikahan"
                          className="form-control radius-8"
                          id="status pernikahan"
                          value={
                            detailedEmployee?.data?.employee?.statusPernikahan
                          }
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="jumlah anak"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Jumlah Anak <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="jumlah anak"
                          className="form-control radius-8"
                          id="jumlah anak"
                          value={detailedEmployee?.data?.employee?.jumlahAnak}
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="nama-bank"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Nama Bank <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="nama-bank"
                          className="form-control radius-8"
                          id="nama-bank"
                          value={detailedEmployee?.data?.employee?.bank}
                        />
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="mb-20">
                        <label
                          htmlFor="Nomor Rekening"
                          className="form-label fw-semibold text-primary-light text-sm mb-8"
                        >
                          Nomor Rekening{" "}
                          <span className="text-danger-600">*</span>
                        </label>
                        <input
                          type="Nomor Rekening"
                          className="form-control radius-8"
                          id="Nomor Rekening"
                          value={
                            detailedEmployee?.data?.employee?.nomorRekening
                          }
                        />
                      </div>
                    </div>
                  </div>
                  <div className="d-flex align-items-center justify-content-end gap-3 pe-0">
                    <button
                      type="button"
                      className="border border-danger-600 bg-hover-danger-200 text-danger-600 text-sm px-24 py-6 radius-4"
                    >
                      Cancel
                    </button>
                    <button
                      type="submit"
                      className="btn btn-primary border border-primary-600 text-sm px-24 py-6 radius-4"
                    >
                      Save
                    </button>
                  </div>
                </form>
              </div>

              <div
                className="tab-pane fade"
                id="pills-change-passwork"
                role="tabpanel"
                aria-labelledby="pills-change-passwork-tab"
                tabIndex="0"
              >
                <div className="mb-20">
                  <label
                    htmlFor="your-password"
                    className="form-label fw-semibold text-primary-light text-sm mb-8"
                  >
                    New Password <span className="text-danger-600">*</span>
                  </label>
                  <div className="position-relative">
                    <input
                      type={passwordVisible ? "text" : "password"}
                      className="form-control radius-8"
                      id="your-password"
                      placeholder="Enter New Password*"
                    />
                    <span
                      className={`toggle-password ${passwordVisible ? "ri-eye-off-line" : "ri-eye-line"
                        } cursor-pointer position-absolute end-0 top-50 translate-middle-y me-16 text-secondary-light`}
                      onClick={togglePasswordVisibility}
                    ></span>
                  </div>
                </div>

                <div className="mb-20">
                  <label
                    htmlFor="confirm-password"
                    className="form-label fw-semibold text-primary-light text-sm mb-8"
                  >
                    Confirm Password <span className="text-danger-600">*</span>
                  </label>
                  <div className="position-relative">
                    <input
                      type={confirmPasswordVisible ? "text" : "password"}
                      className="form-control radius-8"
                      id="confirm-password"
                      placeholder="Confirm Password*"
                    />
                    <span
                      className={`toggle-password ${confirmPasswordVisible
                        ? "ri-eye-off-line"
                        : "ri-eye-line"
                        } cursor-pointer position-absolute end-0 top-50 translate-middle-y me-16 text-secondary-light`}
                      onClick={toggleConfirmPasswordVisibility}
                    ></span>
                  </div>
                </div>
              </div>

              {/* CV Start */}
              <div
                className="tab-pane fade"
                id="pills-cv"
                role="tabpanel"
                aria-labelledby="pills-cv-tab"
                tabIndex="0"
              >
                <div className="mb-20">
                  <label
                    htmlFor="project-name"
                    className="form-label fw-semibold text-primary-light text-sm mb-8"
                  >
                    Project Name
                  </label>
                  <div className="position-relative">
                    <input
                      type="text"
                      className="form-control radius-8"
                      id="projectName"
                      value={cvData.projectName}
                      onChange={handleCVChange}
                      placeholder="Enter Project Name"
                    />
                  </div>
                </div>

                <div className="mb-20">
                  <label
                    htmlFor="project-position"
                    className="form-label fw-semibold text-primary-light text-sm mb-8"
                  >
                    Position
                  </label>
                  <div className="position-relative">
                    <input
                      type="text"
                      className="form-control radius-8"
                      id="projectRole"
                      value={cvData.projectRole}
                      onChange={handleCVChange}
                      placeholder="Enter Position"
                    />
                  </div>
                </div>

                <div className="row">
                  <div className="col">
                    <div className="mb-20">
                      <label
                        htmlFor="project-duration-start"
                        className="form-label fw-semibold text-primary-light text-sm mb-8"
                      >
                        Project Start
                      </label>
                      <div className="position-relative">
                        <input
                          type="date"
                          className="form-control radius-8"
                          id="projectStart"
                          value={cvData.projectStart}
                          onChange={handleCVChange}
                        />
                      </div>
                    </div>
                  </div>

                  <div className="col">
                    <div className="mb-20">
                      <label
                        htmlFor="project-duration-end"
                        className="form-label fw-semibold text-primary-light text-sm mb-8"
                      >
                        Project End
                      </label>
                      <div className="position-relative">
                        <input
                          type="date"
                          className="form-control radius-8"
                          id="projectEnd"
                          value={cvData.projectEnd}
                          onChange={handleCVChange}
                        />
                      </div>
                    </div>
                  </div>
                </div>

                <div className="mb-20">
                  <label
                    htmlFor="project-description"
                    className="form-label fw-semibold text-primary-light text-sm mb-8"
                  >
                    Description
                  </label>
                  <div className="position-relative">
                    <textarea
                      type="text"
                      className="form-control radius-8 h-100"
                      id="projectDescription"
                      value={cvData.projectDescription}
                      onChange={handleCVChange}
                      placeholder="Enter Description"
                    />
                  </div>
                  <div className="d-flex align-items-center justify-content-end gap-3 pe-0 pt-3">
                    <button
                      onClick={handleSaveCV}
                      className="btn btn-primary border border-primary-600 text-sm px-24 py-6 radius-4"
                    >
                      Add
                    </button>
                  </div>
                  <ul className="pt-3">
                    {Array.isArray(cvList) && cvList.length > 0 ? (
                      cvList.map((project, index) => (
                        <div key={index} className="col-12 col-md-6 mb-4">
                          <div className="project-card p-3 border rounded">
                            <div className="d-flex justify-content-between">
                              <div>
                                <strong>{project.projectName} / </strong> {project.projectRole}
                                <div className="text-lg text-muted">
                                  {new Date(project.projectStart).toLocaleDateString()} - {new Date(project.projectEnd).toLocaleDateString()}
                                </div>
                                <div>{project.projectDescription}</div>
                              </div>
                              <div>
                                <button
                                  className="btn btn-outline-danger"
                                  onClick={() => handleDelete(project.id)}
                                >
                                  <Icon
                                    icon="material-symbols:delete"
                                    width="24"
                                    height="24"
                                  />
                                </button>
                              </div>
                            </div>
                          </div>
                        </div>
                      ))
                    ) : (
                      <div className="col-12">
                        <p className="text-muted">No CV entries found</p>
                      </div>
                    )}
                  </ul>
                </div>
              </div>

              {/* CV End */}

              <div
                className="tab-pane fade"
                id="pills-notification"
                role="tabpanel"
                aria-labelledby="pills-notification-tab"
                tabIndex={0}
              >
                <div className="form-switch switch-primary py-12 px-16 border radius-8 position-relative mb-16">
                  <label
                    htmlFor="companzNew"
                    className="position-absolute w-100 h-100 start-0 top-0"
                  />
                  <div className="d-flex align-items-center gap-3 justify-content-between">
                    <span className="form-check-label line-height-1 fw-medium text-secondary-light">
                      Company News
                    </span>
                    <input
                      className="form-check-input"
                      type="checkbox"
                      role="switch"
                      id="companzNew"
                    />
                  </div>
                </div>
                <div className="form-switch switch-primary py-12 px-16 border radius-8 position-relative mb-16">
                  <label
                    htmlFor="pushNotifcation"
                    className="position-absolute w-100 h-100 start-0 top-0"
                  />
                  <div className="d-flex align-items-center gap-3 justify-content-between">
                    <span className="form-check-label line-height-1 fw-medium text-secondary-light">
                      Push Notification
                    </span>
                    <input
                      className="form-check-input"
                      type="checkbox"
                      role="switch"
                      id="pushNotifcation"
                      defaultChecked=""
                    />
                  </div>
                </div>
                <div className="form-switch switch-primary py-12 px-16 border radius-8 position-relative mb-16">
                  <label
                    htmlFor="weeklyLetters"
                    className="position-absolute w-100 h-100 start-0 top-0"
                  />
                  <div className="d-flex align-items-center gap-3 justify-content-between">
                    <span className="form-check-label line-height-1 fw-medium text-secondary-light">
                      Weekly News Letters
                    </span>
                    <input
                      className="form-check-input"
                      type="checkbox"
                      role="switch"
                      id="weeklyLetters"
                      defaultChecked=""
                    />
                  </div>
                </div>
                <div className="form-switch switch-primary py-12 px-16 border radius-8 position-relative mb-16">
                  <label
                    htmlFor="meetUp"
                    className="position-absolute w-100 h-100 start-0 top-0"
                  />
                  <div className="d-flex align-items-center gap-3 justify-content-between">
                    <span className="form-check-label line-height-1 fw-medium text-secondary-light">
                      Meetups Near you
                    </span>
                    <input
                      className="form-check-input"
                      type="checkbox"
                      role="switch"
                      id="meetUp"
                    />
                  </div>
                </div>
                <div className="form-switch switch-primary py-12 px-16 border radius-8 position-relative mb-16">
                  <label
                    htmlFor="orderNotification"
                    className="position-absolute w-100 h-100 start-0 top-0"
                  />
                  <div className="d-flex align-items-center gap-3 justify-content-between">
                    <span className="form-check-label line-height-1 fw-medium text-secondary-light">
                      Orders Notifications
                    </span>
                    <input
                      className="form-check-input"
                      type="checkbox"
                      role="switch"
                      id="orderNotification"
                      defaultChecked=""
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewProfileLayer;