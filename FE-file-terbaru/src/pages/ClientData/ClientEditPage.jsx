import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import MasterLayout from "../../masterLayout/MasterLayout";
import Breadcrumb from "../../components/NavigationTitle";
import useGetClientById from "../../hook/useGetClientById";
import ClientEdit from "./ClientEdit";

const ClientEditPage = () => {
  const navigate = useNavigate();
  const selectedClientId = useSelector(state => state.client?.selectedClientId);

  console.log("ClientEditPage - Selected ID:", selectedClientId);

  const { data: client, loading, error } = useGetClientById(selectedClientId);

  useEffect(() => {
    console.log("ClientEditPage useEffect - ID:", selectedClientId);
    if (!selectedClientId) {
      console.log("No client ID found, redirecting to clients list");
      navigate('/client');
      return;
    }
  }, [selectedClientId, navigate]);

  if (loading) {
    return (
      <MasterLayout>
        <div className="d-flex justify-content-center align-items-center min-vh-100">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      </MasterLayout>
    );
  }

  if (error || !client) {
    return (
      <MasterLayout>
        <div className="alert alert-danger m-3" role="alert">
          <h4 className="alert-heading">Error</h4>
          <p>{error || 'client not found'}</p>
          <button
            className="btn btn-outline-danger mt-3"
            onClick={() => navigate('/client')}
          >
            Back to Client list
          </button>
        </div>
      </MasterLayout>
    );
  }

  return (
    <MasterLayout>
      <Breadcrumb title="Edit Client" />
      <ClientEdit
        clientId={selectedClientId}
        clientData={client}
      />
    </MasterLayout>
  );
};

export default ClientEditPage;