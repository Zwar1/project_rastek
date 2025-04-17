import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import useGetClientById from "../../../hook/useGetClientById";
import { a } from "@react-spring/web";

const ClientInformation = ({ clientId, clientData }) => {
  const [statusTemp, setStatusTemp] = useState("");
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const {editClient} = useGetClientById(clientId);
  const [formData, setFormData] = useState({
    clientName: '',
    isActive: true,
    clientCountry: '',
    clientAddress: '',
    clientEmail: '',
    picName: '',
    picPhoneNumber: ''
  });

  useEffect(() => {
    if (clientData) {
      setFormData({
        clientName: clientData?.clientName || "",
        isActive: clientData?.isActive ?? true,
        clientCountry: clientData?.clientCountry || "",
        clientAddress: clientData?.clientAddress || "",
        clientEmail: clientData?.clientEmail || "",
        picName: clientData?.picName || "",
        picNumber: clientData?.picNumber || "",
      });
    }
  }, [clientData]);

  const handleChange = (e) => {
    const { id, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [id]: value,
    }));
  };

  const toggleDropdown = () => {
    setDropdownOpen(!dropdownOpen);
  };

  const handleStatusChange = (newStatus) => {
    console.log("Changing status to:", newStatus);
    setFormData(prev => ({
      ...prev,
      isActive: newStatus === 'Active'
    }));
    setDropdownOpen(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      console.log("Submitting form data:", formData);
      await editClient(formData);
      alert("Client data updated successfully");
      window.location.reload();
    } catch (error) {
      console.error("Failed to update client data:", error);
      alert(error.message || "Failed to update client data");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="row">
      <div className="col-sm-6">
        <label
          htmlFor="clientName"
          className="form-label fw-semibold text-primary-light text-sm mb-8"
        >
          Client Name
        </label>

        <input type="text"
          className="form-control radius-8"
          id="clientName"
          value={formData.clientName}
          onChange={handleChange}
        />
      </div>

      <div className="col-sm-6">
        <label
          htmlFor="Status"
          className="form-label fw-semibold text-primary-light text-sm mb-8"
        >
          Status
        </label>
        <div className={`dropdown ${dropdownOpen ? 'show' : ''}`}>
          <button
            className="btn btn-outline-primary-600 px-18 py-11 dropdown-toggle w-100"
            type="button"
            onClick={toggleDropdown}
            aria-expanded={dropdownOpen}
          >
            {formData.isActive ? 'Active' : 'Inactive'}
          </button>
          <div className={`dropdown-menu w-100 ${dropdownOpen ? 'show' : ''}`}>
            <button
              className={`dropdown-item px-16 py-8 ${formData.isActive ? 'active' : ''}`}
              onClick={() => handleStatusChange('Active')}
            >
              Active
            </button>
            <button
              className={`dropdown-item px-16 py-8 ${!formData.isActive ? 'active' : ''}`}
              onClick={() => handleStatusChange('Inactive')}
            >
              Inactive
            </button>
          </div>
        </div>
      </div>

      <div className="col-sm-6">
        <label htmlFor="clientCountry" className="form-label fw-semibold text-primary-light text-sm mb-8">
          Country
        </label>
        <input
          type="text"
          className="form-control radius-8"
          id="clientCountry"
          value={formData.clientCountry}
          onChange={handleChange}
        />
      </div>

      <div className="col-sm-6">
        <label htmlFor="clientAddress" className="form-label fw-semibold text-primary-light text-sm mb-8">
          Address
        </label>
        <input
          type="text"
          className="form-control radius-8"
          id="clientAddress"
          value={formData.clientAddress}
          onChange={handleChange}
        />
      </div>

      <div className="col-sm-6">
        <label htmlFor="clientEmail" className="form-label fw-semibold text-primary-light text-sm mb-8">
          Email
        </label>
        <input
          type="email"
          className="form-control radius-8"
          id="clientEmail"
          value={formData.clientEmail}
          onChange={handleChange}
        />
      </div>

      <div className="col-sm-6">
        <label htmlFor="picName" className="form-label fw-semibold text-primary-light text-sm mb-8">
          PIC Name
        </label>
        <input
          type="text"
          className="form-control radius-8"
          id="picName"
          value={formData.picName}
          onChange={handleChange}
        />
      </div>

      <div className="col-sm-6">
        <label htmlFor="picNumber" className="form-label fw-semibold text-primary-light text-sm mb-8">
          PIC Phone Number
        </label>
        <input
          type="text"
          className="form-control radius-8"
          id="picNumber"
          value={formData.picNumber}
          onChange={handleChange}
        />
      </div>

      <div className="d-flex justify-content-end mt-3">
        <button
          className="btn btn-primary border border-primary-600 text-white px-24 py-6 radius-4"
          onClick={handleSubmit}
          disabled={loading}
        >
          {loading ? 'Saving...' : 'Save'}
        </button>
      </div>
    </div >
  );
};

export default ClientInformation;