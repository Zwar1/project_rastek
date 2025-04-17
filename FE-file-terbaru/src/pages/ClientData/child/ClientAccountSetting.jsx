import React, { useState, useEffect } from "react";
import useUser from "../../../hook/useUser";

const ClientAccountSetting = ({ clientId, clientData }) => {
  console.log("ClientAccountSetting rendered with clientData:", clientData);
  console.log("ClientAccountSetting rendered with clientId:", clientId);

  useEffect(() => {
    if (!clientId) {
      console.error("No clientId provided to ClientAccountSetting");
    }
    if (!clientData) {
      console.error("No clientData provided to ClientAccountSetting");
    }
  }, [clientId, clientData]);

  const { userData, loading, error, updateUserAccount } = useUser(clientId);
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    confirmPassword: ''
  });
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (userData) {
      setFormData(prev => ({
        ...prev,
        username: userData.username || ''
      }));
    }
  }, [userData, loading, error]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleAccountSet = async (e) => {
    e.preventDefault();
    if (formData.password !== formData.confirmPassword) {
      alert("Passwords don't match!");
      return;
    }

    try {
      setSaving(true);
      await updateUserAccount(userData.id, {
        username: formData.username,
        password: formData.password || undefined
      });
      alert('Account settings updated successfully');
      window.location.reload();
    } catch (error) {
      alert(error.message);
    } finally {
      setSaving(false);
    }
  };


  if (loading) {
    return (
      <div className="d-flex justify-content-center p-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert alert-danger m-3">
        <h4 className="alert-heading">Error</h4>
        <p>{error}</p>
      </div>
    );
  }

  return (
    <div className="row">
      <div className="col-sm-6">
        <label
          htmlFor="usernameClient"
          className="form-label fw-semibold text-primary-light text-sm mb-8"
        >
          Username
        </label>
        <input
          type="text"
          className="form-control radius-8"
          id="username"
          name="username"
          value={formData.username}
          onChange={handleChange}
        />
      </div>

      <div className="col-sm-6">
        <label
          htmlFor="passwordClient"
          className="form-label fw-semibold text-primary-light text-sm mb-8"
        >
          Password
        </label>
        <input
          type="password"
          className="form-control radius-8"
          id="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          placeholder="Enter new password"
        />
      </div>

      <div className="col-sm-6">
        <label
          htmlFor="confirmPassword"
          className="form-label fw-semibold text-primary-light text-sm mb-8"
        >
          Confirm Password
        </label>
        <input
          type="password"
          className="form-control radius-8"
          id="confirmPassword"
          name="confirmPassword"
          value={formData.confirmPassword}
          onChange={handleChange}
          placeholder="Confirm new password"
        />
      </div>

      <div className="d-flex justify-content-end mt-3">
        <button
          className="btn btn-primary border border-primary-600 text-white px-24 py-6 radius-4"
          onClick={handleAccountSet}
          disabled={saving}
        >
          {saving ? 'Saving...' : 'Save'}
        </button>
      </div>
    </div>
  );
};

export default ClientAccountSetting;