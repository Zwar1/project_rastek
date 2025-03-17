import React, { useEffect, useState } from "react";
import ClientAccountSetting from "./child/ClientAccountSetting";
import ClientInformation from "./child/ClientInformation";
import useGetClientById from "../../hook/useGetClientById";
import useClient from '../../hook/useGetClient';
import { useParams, useNavigate } from "react-router-dom";
import api from "../../apiService";

const ClientEdit = ({ clientId, clientData }) => {
  const { id } = useParams();
  const navigate = useNavigate();
  console.log("ClientEdit rendered with ID:", clientId);
  const {
    data: client,
    loading,
    error,
    profilePicture,
    refreshClient
  } = useGetClientById(clientId);

  useEffect(() => {
    console.log("ClientEdit useEffect - Current ID:", clientId);
    if (!clientId) {
      console.warn("No ID provided, redirecting to clients list");
      navigate('/client');
      return;
    }

    // Force refresh when component mounts or id changes
    refreshClient();
  }, [clientId, navigate, refreshClient]);

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center min-vh-100">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert alert-danger m-3" role="alert">
        <h4 className="alert-heading">Error</h4>
        <p>{error}</p>
      </div>
    );
  }

  if (!clientData) {
    return (
      <div className="alert alert-warning m-3" role="alert">
        <h4 className="alert-heading">No Data Found</h4>
        <p>Could not find client information.</p>
      </div>
    );
  }

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
                src={profilePicture || "assets/images/user.png"}
                alt=""
                className="border br-white border-width-2-px w-200-px h-200-px rounded-circle object-fit-cover"
              />
              <h6 className="mb-0 mt-16">{clientData?.clientName}</h6>
            </div>
            <div className="mt-24">
              <h6 className="text-xl mb-16">Client Info</h6>
              <ul>
                <li className="d-flex align-items-center gap-1 mb-12">
                  <span className="w-30 text-md fw-semibold text-primary-light">
                    Full Name
                  </span>
                  <span className="w-70 text-secondary-light fw-medium">
                    : {client?.clientName}
                  </span>
                </li>
                <li className="d-flex align-items-center gap-1 mb-12">
                  <span className="w-30 text-md fw-semibold text-primary-light">
                    Email
                  </span>
                  <span className="w-70 text-secondary-light fw-medium">
                    : {client?.clientEmail}
                  </span>
                </li>
                <li className="d-flex align-items-center gap-1 mb-12">
                  <span className="w-30 text-md fw-semibold text-primary-light">
                    Country
                  </span>
                  <span className="w-70 text-secondary-light fw-medium">
                    : {client?.clientCountry || "-"}
                  </span>
                </li>
                <li className="d-flex align-items-center gap-1 mb-12">
                  <span className="w-30 text-md fw-semibold text-primary-light">
                    Address
                  </span>
                  <span className="w-70 text-secondary-light fw-medium">
                    : {client?.clientAddress || "-"}
                  </span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div className="col-lg-8">
        <div className="card">
          <div className="card-body p-24">
            <ul className="nav border-gradient-tab nav-pills mb-20 d-inline-flex">
              <li className="nav-item">
                <button
                  className="nav-link d-flex align-items-center px-24 active"
                  data-bs-toggle="pill"
                  data-bs-target="#pills-client-information"
                  type="button"
                >
                  Client Information
                </button>
              </li>
              <li className="nav-item">
                <button
                  className="nav-link d-flex align-items-center px-24"
                  data-bs-toggle="pill"
                  data-bs-target="#pills-accountSet"
                  type="button"
                >
                  Account Setting
                </button>
              </li>
            </ul>
            <div className="tab-content">
              <div className="tab-pane fade show active" id="pills-client-information">
                <ClientInformation clientId={clientId} clientData={client} />
              </div>
              <div className="tab-pane fade" id="pills-accountSet">
                <ClientAccountSetting clientId={clientData?.id} clientData={clientData} />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ClientEdit;