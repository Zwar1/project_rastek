import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import MasterLayout from "../masterLayout/MasterLayout";
import Breadcrumb from "../components/NavigationTitle";
import ProjectDetails from "../components/ProjectDetails";
import useGetProjectById from "../hook/useGetProjectById";

const ProjectDetailsPage = () => {
  const navigate = useNavigate();
  const selectedProjectId = useSelector(state => state.project.selectedProjectId);

  console.log("ProjectDetailsPage - Selected ID:", selectedProjectId);

  const { data: project, loading, error } = useGetProjectById(selectedProjectId);

  useEffect(() => {
    console.log("ProjectDetailsPage useEffect - ID:", selectedProjectId);
    if (!selectedProjectId) {
      console.log("No project ID found, redirecting to projects list");
      navigate('/project');
      return;
    }
  }, [selectedProjectId, navigate]);

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

  if (error || !project) {
    return (
      <MasterLayout>
        <div className="alert alert-danger m-3" role="alert">
          <h4 className="alert-heading">Error</h4>
          <p>{error || 'Project not found'}</p>
          <button
            className="btn btn-outline-danger mt-3"
            onClick={() => navigate('/project')}
          >
            Back to Projects
          </button>
        </div>
      </MasterLayout>
    );
  }

  return (
    <MasterLayout>
      <Breadcrumb title="Project Details" />
      <ProjectDetails
        projectId={selectedProjectId}
        projectData={project}
      />
    </MasterLayout>
  );
};

export default ProjectDetailsPage;
