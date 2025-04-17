import React, { useEffect, useState } from "react";
import ClientAccountSetting from "./child/ClientAccountSetting";
import ClientInformation from "./child/ClientInformation";
import useGetClientById from "../../hook/useGetClientById";
import useClient from '../../hook/useGetClient';
import { useParams, useNavigate } from "react-router-dom";
import api from "../../apiService";

const ClientEdit = ({ clientId, clientData }) => {
  const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
  const ALLOWED_FILE_TYPES = ['image/jpeg', 'image/png', 'image/gif'];
  const [isHovered, setIsHovered] = useState(false);
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

  const [uploadLoading, setUploadLoading] = useState(false);

  const handleProfilePictureChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    console.log('File details:', {
      name: file.name,
      type: file.type,
      size: file.size
    });

    if (!ALLOWED_FILE_TYPES.includes(file.type)) {
      alert('Please upload an image file (JPEG, PNG, or GIF)');
      return;
    }

    if (file.size > MAX_FILE_SIZE) {
      alert('File size should be less than 5MB');
      return;
    }

    try {
      setUploadLoading(true);
      const base64 = await convertImageToBase64(file);
      console.log('Base64 preview:', {
        length: base64.length,
        start: base64.substring(0, 100) + '...',
        type: file.type
      });

      const payload = {
        profilePicture: base64,
        profilePictureType: file.type
      };
      console.log('Request payload:', {
        endpoint: `/api/update/client/${clientId}/profile-picture`,
        type: payload.profilePictureType,
        dataLength: payload.profilePicture.length
      });

      const response = await api.put(`/api/update/client/${clientId}/profile-picture`, payload);
      console.log('Upload success:', response.data);

      alert('Profile picture updated successfully');
      setUploadLoading(false);
      window.location.reload();
    } catch (error) {
      console.error('Error uploading profile picture:', error);
      alert('Failed to update profile picture: ' + error.message);
    } finally {
      setUploadLoading(false);
    }
  };

  const convertImageToBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);

      reader.onload = () => {
        resolve(reader.result.split(',')[1]);
      };

      reader.onerror = (error) => {
        reject(error);
      }
    });
  };

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
          <div className="pb-24 ms-16 mb-24 me-16 mt--100">
            <div className="text-center border border-top-0 border-start-0 border-end-0">
              <div className="position-relative d-inline-block"
                onMouseEnter={() => setIsHovered(true)}
                onMouseLeave={() => setIsHovered(false)}
              >
                <img
                  src={profilePicture || "assets/images/user.png"}
                  alt="Profile Picture"
                  className="border br-white border-width-2-px w-200-px h-200-px rounded-circle object-fit-cover"
                />
                {isHovered && (
                  <label
                    htmlFor="profile-upload"
                    className="position-absolute w-100 h-100 top-0 start-0 d-flex align-items-center justify-content-center"
                    style={{
                      cursor: 'pointer',
                      background: 'rgba(0, 0, 0, 0.5)', // Semi-transparent overlay
                      borderRadius: '50%',
                      transition: 'all 0.3s ease'
                    }}
                  >
                    <div className="d-flex flex-column align-items-center text-white">
                      <i className="bi bi-camera-fill fs-3 mb-2"></i>
                      <span className="small">Change Photo</span>
                    </div>
                    <input
                      type="file"
                      id="profile-upload"
                      accept="image/*"
                      onChange={handleProfilePictureChange}
                      style={{ display: 'none' }}
                    />
                  </label>
                )}
                {uploadLoading && (
                  <div className="position-absolute top-50 start-50 translate-middle w-100 h-100 d-flex align-items-center justify-content-center">
                    <div className="spinner-border text-primary" role="status" style={{ width: '3rem', height: '3rem' }}>
                      <span className="visually-hidden">Uploading...</span>
                    </div>
                  </div>
                )}
              </div>
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
    </div >
  );
};

export default ClientEdit;