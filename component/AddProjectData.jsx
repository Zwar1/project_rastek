import React, { useState, useEffect } from "react";
// import { Link } from "react-router-dom";
import Select from "react-select";
import api from "../apiService";
import API_BASE_URL from "../apiConfig";
import useProject from "../hook/useProject";
import useGetClient from "../hook/useGetClient";
import useGetEmployee from "../hook/useGetEmployee";
import { useNavigate } from 'react-router-dom';

const AddUserLayer = () => {
  const { addProject } = useProject();
  const navigate = useNavigate();
  const clientList = useGetClient();
  const employeeList = useGetEmployee();
  const [selectedClientProfilePicture, setSelectedClientProfilePicture] = useState(null);

  const [formData, setFormData] = useState({
    projectName: "",
    projectDescription: "",
    projectStart: "",
    projectEnd: "",
    summary: "",
    priority: "",
    status: "not_started",
    estimatedHours: 0,
    totalHours: 0,
    progress: 0,
    clientId: null,
    member: []
  });

  const [selectedTeamMembers, setSelectedTeamMembers] = useState([]);

  const handleChange = (e) => {
    const { name, value, type } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'number' ? parseInt(value) || 0 : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.projectName || !formData.projectStart || !formData.projectEnd) {
      alert("Required fields must be filled");
      return;
    }

    try {
      const projectData = {
        ...formData,
        member: selectedTeamMembers.map(member => member.value)
      };

      await addProject(projectData);
      alert("Project successfully created!");

      // Reset form
      setFormData({
        projectName: "",
        projectDescription: "",
        projectStart: "",
        projectEnd: "",
        summary: "",
        priority: "",
        status: "not_started",
        estimatedHours: 0,
        totalHours: 0,
        progress: 0,
        clientId: null,
        member: [],
        logo: null,
        logoType: null
      });
      setSelectedTeamMembers([]);
      setSelectedClientProfilePicture(null);

      navigate('/project');
    } catch (error) {
      alert(error.message);
    }
  };

  // const handleLogoChange = (e) => {
  //   const file = e.target.files[0];
  //   if (file) {
  //     const reader = new FileReader();
  //     reader.onloadend = () => {
  //       // Get base64 string and content type
  //       const base64String = reader.result.split(',')[1];
  //       const logoType = file.type;

  //       setFormData(prev => ({
  //         ...prev,
  //         logo: base64String,
  //         logoType: logoType
  //       }));
  //     };
  //     reader.readAsDataURL(file);
  //     setLogoFile(file); // Store file for preview
  //   }
  // };

  // const getLogoPreview = () => {
  //   if (logoFile) {
  //     return URL.createObjectURL(logoFile);
  //   }
  //   return null;
  // };

  const handleClientChange = async (selectedOption) => {
    setFormData(prev => ({
      ...prev,
      clientId: selectedOption.value
    }));

    // Fetch client profile picture when client is selected
    try {
      const response = await api.get(`/api/client/${selectedOption.value}/profile-picture`, {
        responseType: 'arraybuffer'
      });
      const base64 = btoa(
        new Uint8Array(response.data).reduce(
          (data, byte) => data + String.fromCharCode(byte),
          ''
        )
      );
      setSelectedClientProfilePicture(`data:${response.headers['content-type']};base64,${base64}`);
    } catch (error) {
      console.error('Error fetching client profile picture:', error);
      setSelectedClientProfilePicture(null);
    }
  };

  return (
    <div className="col-lg-12">
      <div className="card">
        <div className="card-header">
          <h5 className="card-title mb-0">Add New Project</h5>
        </div>
        <div className="card-body">
          <form onSubmit={handleSubmit}>
            <div className="row gy-3">
              <div className="col-md-4">
                <label className="form-label">Project Name*</label>
                <input
                  name="projectName"
                  value={formData.projectName}
                  onChange={handleChange}
                  type="text"
                  className="form-control"
                  required
                />
              </div>

              <div className="col-md-4">
                <label className="form-label">Start Date*</label>
                <input
                  name="projectStart"
                  type="date"
                  className="form-control"
                  value={formData.projectStart}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="col-md-4">
                <label className="form-label">End Date*</label>
                <input
                  name="projectEnd"
                  type="date"
                  className="form-control"
                  value={formData.projectEnd}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="col-md-6">
                <label className="form-label">Priority</label>
                <Select
                  options={[
                    { value: "highest", label: "Highest" },
                    { value: "high", label: "High" },
                    { value: "normal", label: "Normal" }
                  ]}
                  value={{ value: formData.priority, label: formData.priority }}
                  onChange={(option) => setFormData(prev => ({
                    ...prev,
                    priority: option.value
                  }))}
                />
              </div>

              <div className="col-md-6">
                <label className="form-label">Status</label>
                <Select
                  options={[
                    { value: "not_started", label: "Not Started" },
                    { value: "in_progress", label: "In Progress" },
                    { value: "completed", label: "Completed" }
                  ]}
                  value={{ value: formData.status, label: formData.status }}
                  onChange={(option) => setFormData(prev => ({
                    ...prev,
                    status: option.value
                  }))}
                />
              </div>

              <div className="col-md-4">
                <label className="form-label">Estimated Hours</label>
                <input
                  name="estimatedHours"
                  type="number"
                  className="form-control"
                  value={formData.estimatedHours}
                  onChange={handleChange}
                  min="0"
                />
              </div>

              <div className="col-md-4">
                <label className="form-label">Total Hours</label>
                <input
                  name="totalHours"
                  type="number"
                  className="form-control"
                  value={formData.totalHours}
                  onChange={handleChange}
                  min="0"
                />
              </div>

              <div className="col-md-4">
                <label className="form-label">Progress (%)</label>
                <input
                  name="progress"
                  type="number"
                  className="form-control"
                  value={formData.progress}
                  onChange={handleChange}
                  min="0"
                  max="100"
                />
              </div>

              <div className="col-md-6">
                <label className="form-label">Client*</label>
                <Select
                  options={clientList.map(client => ({
                    value: client.id,
                    label: client.clientName
                  }))}
                  value={clientList
                    .map(client => ({
                      value: client.id,
                      label: client.clientName
                    }))
                    .find(opt => opt.value === formData.clientId)}
                  onChange={handleClientChange}
                  required
                />
              </div>

              <div className="col-md-6">
                <label className="form-label">Team Members</label>
                <Select
                  isMulti
                  options={employeeList.map(employee => ({
                    value: employee.nik,
                    label: employee.name
                  }))}
                  value={selectedTeamMembers}
                  onChange={setSelectedTeamMembers}
                />
              </div>

              <div className="col-12">
                <label className="form-label">Summary</label>
                <input
                  name="summary"
                  type="text"
                  className="form-control"
                  value={formData.summary}
                  onChange={handleChange}
                />
              </div>

              <div className="col-12">
                <label className="form-label">Description</label>
                <textarea
                  name="projectDescription"
                  className="form-control"
                  rows={4}
                  value={formData.projectDescription}
                  onChange={handleChange}
                />
              </div>

              <div className="col-12">
                <label className="form-label">Client Logo</label>
                <div className="d-flex gap-3 align-items-start">
                  {formData.clientId ? (
                    selectedClientProfilePicture ? (
                      <div
                        className="client-logo-preview"
                        style={{
                          width: "60px",
                          height: "60px",
                          borderRadius: "8px",
                          overflow: "hidden",
                          border: "1px solid #dee2e6"
                        }}
                      >
                        <img
                          src={selectedClientProfilePicture}
                          alt="Client logo"
                          style={{
                            width: "100%",
                            height: "100%",
                            objectFit: "cover"
                          }}
                        />
                      </div>
                    ) : (
                      <div className="text-muted">No client profile picture available</div>
                    )
                  ) : (
                    <div className="text-muted">Select a client to view their logo</div>
                  )}
                </div>
              </div>

              <div className="col-12 mt-4">
                <button
                  type="submit"
                  className="btn btn-primary float-end"
                >
                  Create Project
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default AddUserLayer;