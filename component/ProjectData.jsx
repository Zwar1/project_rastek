import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../apiService";
import useProject from "../hook/useProject";
import useGetClient from "../hook/useGetClient";

const ProjectData = () => {
  const [dateSortDir, setDateSortDir] = useState('asc');
  const [prioritySortDir, setPrioritySortDir] = useState('asc');
  const { 
    projects, 
    loading, 
    error, 
    deleteProject,
    sortBy,
    setSortBy 
  } = useProject();
  const clientList = useGetClient();
  const navigate = useNavigate();

  const handleOpenClick = (project) => {
    navigate("/project-details", { state: { project } });
  };

  const handleDeleteClick = (projectId) => {
    deleteProject(projectId);
  };

  const formatDate = (dateString) => {
    if (!dateString) return "Not set";
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const getClientDetails = (clientId) => {
    return clientList.find(client => client.id === clientId);
  };

  const getClientProfilePicture = (client) => {
    if (!client) return null;
    if (client.profilePicture && client.profilePictureType) {
      return `data:${client.profilePictureType};base64,${client.profilePicture}`;
    }
    return null;
  };

  const getPriorityWeight = (priority) => {
    switch (priority?.toLowerCase()) {
      case 'highest': return 3;
      case 'high': return 2;
      case 'normal': return 1;
      default: return 0;
    }
  };

  const toggleDateSort = () => {
    setDateSortDir(prev => prev === 'asc' ? 'desc' : 'asc');
  };

  const togglePrioritySort = () => {
    setPrioritySortDir(prev => prev === 'asc' ? 'desc' : 'asc');
  };

  const sortedProjects = projects.sort((a, b) => {
    const priorityA = getPriorityWeight(a.priority);
    const priorityB = getPriorityWeight(b.priority);
    const dateA = new Date(a.endDate || '9999-12-31');
    const dateB = new Date(b.endDate || '9999-12-31');

    if (priorityA !== priorityB) {
      return prioritySortDir === 'asc' ? priorityA - priorityB : priorityB - priorityA;
    }

    return dateSortDir === 'asc' ? dateA - dateB : dateB - dateA;
  });

  return (
    <div>
      <div className="card basic-data-table">
        <div className="card-header d-flex justify-content-between align-items-center">
          <div>
            <h5 className="card-title mb-0">Project Data</h5>
          </div>
          <div className="d-flex gap-2">
            <button
              onClick={toggleDateSort}
              className="btn btn-outline-primary btn-sm"
            >
              Sort by End Date {dateSortDir === 'asc' ? '↑' : '↓'}
            </button>
            <button
              onClick={togglePrioritySort}
              className="btn btn-outline-primary btn-sm"
            >
              Sort by Priority {prioritySortDir === 'asc' ? '↑' : '↓'}
            </button>
            <Link
              to="/add-project"
              className="btn btn-primary text-sm btn-sm radius-8 px-16 py-9"
            >
              Add Project
            </Link>
          </div>
        </div>
      </div>

      {loading && <div className="text-center mt-4">Loading...</div>}
      {error && <div className="alert alert-danger mt-4">{error}</div>}

      <div className="row gy-4 mt-10">
        {projects.length === 0 ? (
          <div>No projects available</div>
        ) : (
          sortedProjects.map((project) => (
            <div key={project.id} className="col-xxl-4 col-sm-6">
              <div className="card radius-12 h-100">
                <div className="card-header py-16 px-24 bg-base d-flex align-items-start gap-3">
                  <div
                    className="project-logo"
                    style={{
                      width: "48px",
                      height: "48px",
                      borderRadius: "8px",
                      overflow: "hidden",
                      flexShrink: 0
                    }}
                  >
                    {project.client ? (
                      getClientProfilePicture(project.client) ? (
                        <img
                          src={getClientProfilePicture(project.client)}
                          alt={project.client.name}
                          style={{
                            width: "100%",
                            height: "100%",
                            objectFit: "cover"
                          }}
                          onError={(e) => {
                            e.target.style.display = 'none';
                            e.target.parentElement.innerHTML = `<div style="width:100%;height:100%;background:#e9ecef;display:flex;align-items:center;justify-content:center;color:#6c757d;font-size:20px">${project.projectName.charAt(0)}</div>`;
                          }}
                        />
                      ) : (
                        <div
                          style={{
                            width: "100%",
                            height: "100%",
                            backgroundColor: "#e9ecef",
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            color: "#6c757d",
                            fontSize: "20px"
                          }}
                        >
                          {project.projectName.charAt(0)}
                        </div>
                      )
                    ) : (
                      <div
                        style={{
                          width: "100%",
                          height: "100%",
                          backgroundColor: "#e9ecef",
                          display: "flex",
                          alignItems: "center",
                          justifyContent: "center",
                          color: "#6c757d",
                          fontSize: "20px"
                        }}
                      >
                        {project.projectName.charAt(0)}
                      </div>
                    )}
                  </div>
                  <div className="flex-grow-1">
                    <h6 className="text-lg mb-1">{project.projectName}</h6>
                    <span className="text-muted">{project.client?.name || 'No Client'}</span>
                  </div>
                </div>

                <div className="card-body py-16 px-24">
                  <div className="d-flex flex-column gap-3">
                    <div>
                      <small className="text-muted d-block">Address - Country</small>
                      <span>{project.client?.clientAddress || 'Not specified'} - {project.client?.clientCountry || 'Not specified'}</span>
                    </div>

                    <div>
                      <small className="text-muted d-block">End Date</small>
                      <span>{formatDate(project.endDate)}</span>
                    </div>

                    <div>
                      <small className="text-muted d-block">Priority</small>
                      <span className={`badge bg-${project.priority === 'highest' ? 'danger' :
                        project.priority === 'high' ? 'warning' : 'info'
                        }`}>
                        {project.priority || 'Normal'}
                      </span>
                    </div>

                    <div>
                      <small className="text-muted d-block mb-1">Progress</small>
                      <div className="progress" style={{ height: "6px" }}>
                        <div
                          className="progress-bar"
                          role="progressbar"
                          style={{ width: `${project.progress || 0}%` }}
                          aria-valuenow={project.progress || 0}
                          aria-valuemin="0"
                          aria-valuemax="100"
                        />
                      </div>
                      <small className="d-block text-end mt-1">{project.progress || 0}%</small>
                    </div>
                  </div>
                </div>

                {/* Card Footer */}
                <div className="card-footer py-12 px-24 d-flex justify-content-between align-items-center">
                  <small className="text-muted">
                    Start: {formatDate(project.startDate)}
                  </small>
                  <button
                    onClick={() => handleOpenClick(project)}
                    className="btn btn-primary btn-sm"
                  >
                    Open
                  </button>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default ProjectData;