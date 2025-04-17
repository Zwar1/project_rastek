import React from "react";
import { Icon } from "@iconify/react/dist/iconify.js";
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";

import api from "../apiService";

const UsersListLayer = () => {
  // const [fileNames, setFileNames] = useState([]);

  // const handleFileChange = (event) => {
  //   const files = Array.from(event.target.files);
  //   const newFileNames = files.map((file) => file.name);
  //   setFileNames((prev) => [...prev, ...newFileNames]);
  // };

  // const removeFileName = (name) => {
  //   setFileNames((prev) => prev.filter((fileName) => fileName !== name));
  // };

  // CONST POST
  const [clientName, setclientName] = useState("");
  const [clientNumber, setclientNumber] = useState("");
  const [clientEmail, setclientEmail] = useState("");

  // CONST GET
  const [clients, setClients] = useState([]);

  // CONST ADD LEADS
  const [isAddLeadsVisible, setIsAddLeadsVisible] = useState(false); // State to control card visibility
  const [currentPage, setCurrentPage] = useState(1);
  const [searchTerm, setSearchTerm] = useState("");
  const entriesPerPage = 10;

  const filteredClientList = clients.filter(client => {
    const searchLower = searchTerm.toLowerCase().trim();
    const matchesSearch = searchTerm === "" ? true :
      (client.clientName && client.clientName.toLowerCase().includes(searchLower)) ||
      (client.clientEmail && client.clientEmail.toLowerCase().includes(searchLower)) ||
      (client.clientContact && client.clientContact.toLowerCase().includes(searchLower));

    return matchesSearch;
  });

  // Handle pagination and navigation
  const indexOfLastEntry = currentPage * entriesPerPage;
  const indexOfFirstEntry = indexOfLastEntry - entriesPerPage;
  const currentEntries = filteredClientList.slice(indexOfFirstEntry, indexOfLastEntry);
  const totalPages = Math.ceil(filteredClientList.length / entriesPerPage);

  const handlePageChange = (page) => {
    if (page < 1 || page > totalPages) return;
    setCurrentPage(page);
  };

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
    setCurrentPage(1);
  };

  useEffect(() => {
    const fetchClients = async () => {
      try {
        const response = await api.get("api/get/all/leads");
        console.log("Client data structure:", response.data);
        setClients(response.data);
      } catch (error) {
        console.error("Error Fetching Client Data: ", error);
      }
    };
    fetchClients();
  }, []);
  const handleAddLeadsClick = () => {
    setIsAddLeadsVisible(!isAddLeadsVisible); // Toggle visibility of Add Leads card
  };

  const fetchClients = async () => {
    try {
      const response = await api.get("api/get/all/leads");
      setClients(response.data);
    } catch (error) {
      console.error("Error Fetching Client Data: ", error);
    }
  };

  const handleSubmitGeneric = async (
    endpoint,
    method,
    bodyData,
    successMessage,
    fetchDataCallback
  ) => {
    try {
      const response = await api({
        method: method,
        url: `/${endpoint}`,
        data: bodyData,
      });
      console.log(successMessage, response.data);
      // alert(successMessage);
      resetForm(endpoint);
      if (fetchDataCallback) {
        fetchDataCallback(); // Panggil callback untuk refresh data
      }
    } catch (error) {
      console.error("HTTP ERROR! Status:", error.message);
      alert(`${error.message || "Unknown Error"}`);
    }
  };

  const resetForm = (endpoint) => {
    if (endpoint === "api/create/leads") {
      setclientName("");
      setclientEmail("");
      setclientNumber("");
    }
  };

  const handleUpdateStatusAccepted = async (leadsId) => {
    try {
      const response = await api.patch(`api/update/accepted/${leadsId}`, {
        isApproved: true
      });

      if (response.data) {
        console.log("Status Updated:", response.data);
        await fetchClients(); // Refresh the list
        alert("Client status updated to Approved!");
      } else {
        throw new Error("Failed to update status");
      }
    } catch (error) {
      console.error("Error Updating Status: ", error);
      alert(`Error updating status: ${error.message}`);
    }
  };

  const handleUpdateStatusRejected = async (leadsId) => {
    try {
      const response = await api.patch(`api/update/rejected/${leadsId}`);
      console.log("Status Updated: ", response.data);
      alert("Client status updated to Rejected!");
      fetchClients();
    } catch (error) {
      console.error("Error Updating Status: ", error);
      alert("Error updating status!");
    }
  };

  const handleSubmitClient = () => {
    // e.preventDefault(); // Tidak usah memakai preventDefault jika tidak ingin memakai form onSubmit
    if (!clientName || !clientEmail || !clientNumber) {
      alert("Fill The Form");
      return;
    }
    handleSubmitGeneric(
      "api/create/leads",
      "post",
      { clientName, clientEmail, clientNumber },
      "Client successfully added",
      fetchClients
    );
  };

  return (
    <div className="row gy-4">
      <div className="col-xxl-12">
        <div className="card h-100 p-0 radius-12">
          <div className="card-header border-bottom bg-base py-16 px-24 d-flex align-items-center flex-wrap gap-3 justify-content-between">
            <div className="d-flex align-items-center flex-wrap gap-3">
              <form className="navbar-search" onSubmit={(e) => e.preventDefault()}>
                <input
                  type="text"
                  className="bg-base h-40-px w-auto"
                  name="search"
                  placeholder="Search by name, email or contact"
                  value={searchTerm}
                  onChange={handleSearch}
                  autoComplete="off"
                />
                <Icon icon="ion:search-outline" className="icon" />
              </form>
            </div>
            <Link
              onClick={handleAddLeadsClick}
              className="btn btn-primary text-sm btn-sm radius-8 px-16 py-9 radius-8 d-flex align-items-center gap-2"
            >
              <Icon
                icon="ic:baseline-plus"
                className="icon text-xl line-height-1"
              />
              Add Leads
            </Link>
          </div>
          <div className="card-body p-24">
            <div className="table-responsive scroll-sm">
              <table className="table bordered-table sm-table mb-0">
                <thead>
                  <tr>
                    <th scope="col">
                      <div className="d-flex align-items-center gap-10">No</div>
                    </th>
                    <th scope="col" className="text-center">
                      Client
                    </th>
                    <th scope="col" className="text-center">
                      Contact Number
                    </th>
                    <th scope="col" className="text-center">
                      Email
                    </th>
                    <th scope="col" className="text-center">
                      Status
                    </th>
                    <th scope="col" className="text-center">
                      Action
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {currentEntries.map((client, index) => (
                    <tr key={client.id}>
                      <td>
                        <div className="d-flex align-items-center gap-10">
                          {indexOfFirstEntry + index + 1}
                        </div>
                      </td>
                      <td>
                        <div className="d-flex align-items-center">
                          <div className="flex-grow-1">
                            <span className="text-md mb-0 fw-normal text-secondary-light">
                              {client.clientName}
                            </span>
                          </div>
                        </div>
                      </td>
                      <td>
                        <span className="text-md mb-0 fw-normal text-secondary-light">
                          {client.clientNumber}
                        </span>
                      </td>
                      <td>{client.clientEmail}</td>
                      <td className="text-center">
                        <span
                          className={`
                      px-20 py-6 rounded-pill fw-medium text-sm 
                      ${client.status === "Pending"
                              ? "bg-warning-focus text-warning-main"
                              : client.status === "Rejected"
                                ? "bg-danger-focus text-danger-main"
                                : "bg-success-focus text-success-main"
                            }
                    `}
                        >
                          {client.status}
                        </span>
                      </td>
                      <td className="text-center">
                        <div className="d-flex align-items-center gap-10 justify-content-center">
                          {/* Tombol View */}
                          <button
                            type="button"
                            className="bg-info-focus bg-hover-info-200 text-info-600 fw-medium w-40-px h-40-px d-flex justify-content-center align-items-center rounded-circle"
                          >
                            <Icon
                              icon="majesticons:eye-line"
                              className="icon text-xl"
                            />
                          </button>

                          {/* Tombol Accept hanya tampil jika statusnya belum Accepted atau Rejected */}
                          {client.status !== "Accepted" &&
                            client.status !== "Rejected" && (
                              <button
                                type="button"
                                className="bg-success-focus text-success-600 bg-hover-success-200 fw-medium w-40-px h-40-px d-flex justify-content-center align-items-center rounded-circle"
                                onClick={() => {
                                  console.log('Lead ID being sent:', client.id); // Debug log
                                  handleUpdateStatusAccepted(client.id);
                                }}
                              >
                                <Icon
                                  icon="akar-icons:check"
                                  className="menu-icon"
                                />
                              </button>
                            )}

                          {/* Tombol Reject hanya tampil jika statusnya belum Accepted atau Rejected */}
                          {client.status !== "Accepted" &&
                            client.status !== "Rejected" && (
                              <button
                                type="button"
                                className="remove-item-btn bg-danger-focus bg-hover-danger-200 text-danger-600 fw-medium w-40-px h-40-px d-flex justify-content-center align-items-center rounded-circle"
                                onClick={() =>
                                  handleUpdateStatusRejected(client.id)
                                }
                              >
                                <Icon
                                  icon="akar-icons:cross"
                                  className="menu-icon"
                                />
                              </button>
                            )}
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <div className="d-flex align-items-center justify-content-between flex-wrap gap-2 mt-24">
              <span>
                Showing {indexOfFirstEntry + 1} to {Math.min(indexOfLastEntry, filteredClientList.length)} of {filteredClientList.length} entries
              </span>
              <ul className="pagination d-flex flex-wrap align-items-center gap-2 justify-content-center">
                <li className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
                  <button
                    className="page-link bg-neutral-200 text-secondary-light fw-semibold radius-8 border-0 d-flex align-items-center justify-content-center h-32-px text-md"
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 1}
                  >
                    <Icon icon="ep:d-arrow-left" />
                  </button>
                </li>

                {[...Array(totalPages)].map((_, index) => (
                  <li key={index} className="page-item">
                    <button
                      className={`page-link fw-semibold radius-8 border-0 d-flex align-items-center justify-content-center h-32-px w-32-px text-md ${currentPage === index + 1
                          ? 'bg-primary-600 text-white'
                          : 'bg-neutral-200 text-secondary-light'
                        }`}
                      onClick={() => handlePageChange(index + 1)}
                    >
                      {index + 1}
                    </button>
                  </li>
                ))}

                <li className={`page-item ${currentPage === totalPages ? 'disabled' : ''}`}>
                  <button
                    className="page-link bg-neutral-200 text-secondary-light fw-semibold radius-8 border-0 d-flex align-items-center justify-content-center h-32-px text-md"
                    onClick={() => handlePageChange(currentPage + 1)}
                    disabled={currentPage === totalPages}
                  >
                    <Icon icon="ep:d-arrow-right" />
                  </button>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      {isAddLeadsVisible && (
        <div className="col">
          <div className="card h-100 p-0 radius-12 w-50 ms-4">
            <div className="card-header border-bottom bg-base py-16 px-24">
              <h5 className="text-md mb-0 fw-medium">Add New Lead</h5>
            </div>
            <div className="card-body p-24">
              <div className="mb-3">
                <label htmlFor="client-name" className="form-label">
                  Client Name
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="clientName"
                  placeholder="Enter client name"
                  value={clientName}
                  onChange={(e) => setclientName(e.target.value)}
                />
              </div>
              <div className="mb-3">
                <label htmlFor="contact-number" className="form-label">
                  Contact Number
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="clientNumber"
                  placeholder="Enter contact number"
                  value={clientNumber}
                  onChange={(e) => setclientNumber(e.target.value)}
                />
              </div>
              <div className="mb-3">
                <label htmlFor="email" className="form-label">
                  Email
                </label>
                <input
                  type="email"
                  className="form-control"
                  id="clientEmail"
                  placeholder="Enter email address"
                  value={clientEmail}
                  onChange={(e) => setclientEmail(e.target.value)}
                />
              </div>
              {/* <div className="mb-3">
                <label htmlFor="email" className="form-label">
                  Client Image
                </label>
                <label
                  htmlFor="file-upload-name"
                  className="mb-16 border border-neutral-600 fw-medium text-secondary-light px-16 py-12 radius-12 d-inline-flex align-items-center gap-2 bg-hover-neutral-200"
                >
                  <Icon icon="solar:upload-linear" className="text-xl"></Icon>
                  Click to upload
                  <input
                    type="file"
                    className="form-control w-auto mt-24 form-control-lg"
                    id="file-upload-name"
                    multiple
                    hidden
                    onChange={handleFileChange}
                  />
                </label>

                {fileNames.length > 0 && (
                  <ul
                    id="uploaded-img-names"
                    className="show-uploaded-img-name"
                  >
                    {fileNames.map((fileName, index) => (
                      <li
                        key={index}
                        className="uploaded-image-name-list text-primary-600 fw-semibold d-flex align-items-center gap-2"
                      >
                        <Icon
                          icon="ph:link-break-light"
                          className="text-xl text-secondary-light"
                        ></Icon>
                        {fileName}
                        <Icon
                          icon="radix-icons:cross-2"
                          className="remove-image text-xl text-secondary-light text-hover-danger-600"
                          onClick={() => removeFileName(fileName)}
                        ></Icon>
                      </li>
                    ))}
                  </ul>
                )}
              </div> */}
              <div className="d-flex justify-content-end gap-2">
                <button
                  onClick={handleSubmitClient}
                  className="btn btn-primary text-sm btn-sm radius-8 px-16 py-9 radius-8 d-flex align-items-center gap-2"
                >
                  <Icon icon="akar-icons:check" className="icon text-xl" />
                  Add
                </button>
                <button
                  type="button"
                  className="btn btn-danger text-sm btn-sm radius-8 px-16 py-9 radius-8 d-flex align-items-center gap-2"
                  onClick={handleAddLeadsClick}
                >
                  <Icon icon="akar-icons:cross" className="icon text-xl" />
                  Cancel
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default UsersListLayer;
