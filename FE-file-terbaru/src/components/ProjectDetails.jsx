import React, { useState, useEffect } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import Select from 'react-select';
import useProject from '../hook/useProject';
import useGetProjectById from '../hook/useGetProjectById';

const ProjectDetails = ({ projectId, projectData }) => {
    const { id } = useParams();
    // const [projectData, setProjectData] = useState(null);
    const { data: project, loading, error } = useGetProjectById(projectId);

    // State for managing active tab and project status
    const [activeTab, setActiveTab] = useState('overview');
    const [progress, setProgress] = useState(projectData?.progress || 0);
    const [status, setStatus] = useState(projectData?.status || 'not_started');
    const [manualProgress, setManualProgress] = useState(projectData?.progress || 0);
    const [isUpdating, setIsUpdating] = useState(false);
    const { updateProject } = useProject();

    useEffect(() => {
        console.log("Project data received in ProjectDetails:", projectData);
        if (projectData) {
            const currentProgress = projectData.progress || 0;
            setProgress(currentProgress);
            setStatus(projectData.status);
            setManualProgress(currentProgress);
            console.log("Setting progress to:", currentProgress);
        }
    }, [projectData]);

    // Function to change the active tab
    const handleTabClick = (tab) => {
        setActiveTab(tab);
    };

    // Function to update progress manually via the range input
    const handleProgressChange = (e) => {
        const value = parseInt(e.target.value);
        setProgress(value);
        setManualProgress(value);
    };

    const handleManualInputChange = (e) => {
        const value = parseInt(e.target.value);
        if (value >= 0 && value <= 100) {
            setManualProgress(value);
            setProgress(value);
        }
    };

    const handleStatusChange = (selectedOption) => {
        setStatus(selectedOption.value);
    };

    const handleUpdateStatus = async () => {
        if (!projectData?.id) return;

        try {
            setIsUpdating(true);
            await updateProject(projectData.id, {
                progress: parseInt(progress),
                status: status
            });
            alert('Project updated successfully');
        } catch (error) {
            alert(error.message);
        } finally {
            setIsUpdating(false);
        }
    };

    const formatDate = (dateString) => {
        if (!dateString) return "Not set";
        return new Date(dateString).toLocaleDateString('en-GB', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    };

    // Update the status options to match your backend values
    const statusOptions = [
        { label: 'Not Started', value: 'not_started' },
        { label: 'In Progress', value: 'in_progress' },
        { label: 'Completed', value: 'completed' }
    ];

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

    if (!projectData) {
        return (
            <div className="d-flex justify-content-center align-items-center min-vh-100">
                <div className="alert alert-warning m-3" role="alert">
                    <h4 className="alert-heading">No Data Found</h4>
                    <p>Could not find project information.</p>
                </div>
            </div>
        );
    }

    return (
        <div className="container-fluid">
            <div className="project-details">
                {/* Card Header with Menu Options */}
                <div className="card">
                    <div className="card-header d-flex justify-content-between align-items-center">
                        <div className="nav nav-pills">
                            <button
                                className={`nav-link ${activeTab === 'overview' ? 'active' : ''}`}
                                onClick={() => handleTabClick('overview')}
                            >
                                Overview
                            </button>
                            <button
                                className={`nav-link ${activeTab === 'edit' ? 'active' : ''}`}
                                onClick={() => handleTabClick('edit')}
                            >
                                Edit
                            </button>
                            <button
                                className={`nav-link ${activeTab === 'task' ? 'active' : ''}`}
                                onClick={() => handleTabClick('task')}
                            >
                                Task
                            </button>
                            <button
                                className={`nav-link ${activeTab === 'discussion' ? 'active' : ''}`}
                                onClick={() => handleTabClick('discussion')}
                            >
                                Discussion
                            </button>
                            <button
                                className={`nav-link ${activeTab === 'note' ? 'active' : ''}`}
                                onClick={() => handleTabClick('note')}
                            >
                                Note
                            </button>
                            <button
                                className={`nav-link ${activeTab === 'attachFiles' ? 'active' : ''}`}
                                onClick={() => handleTabClick('attachFiles')}
                            >
                                Attach Files
                            </button>
                            <button
                                className={`nav-link ${activeTab === 'bugs' ? 'active' : ''}`}
                                onClick={() => handleTabClick('bugs')}
                            >
                                Bugs
                            </button>
                        </div>
                    </div>
                </div>

                <div className="d-flex" style={{ marginTop: '20px' }}>
                    {/* Sidebar Card (1/4 width) */}
                    <div className="card" style={{ width: '25%', marginRight: '20px' }}>
                        <div className="card-header border-bottom bg-base py-16 px-24">
                            <h6 className="text-lg fw-semibold mb-0">Project Status</h6>
                        </div>
                        <div className="card-body">
                            {/* Progress bars with projectData values */}
                            <div className="mb-3">
                                <strong>Progress: </strong>
                                <div className="slider-container">
                                    <div
                                        className="progress-background"
                                        style={{ width: `${progress}%`, background: `linear-gradient(to right, ${getProgressColor(progress)} ${progress}%, #ddd ${progress}%)` }}
                                    ></div>
                                    <input
                                        type="range"
                                        min="0"
                                        max="100"
                                        value={progress}
                                        onChange={handleProgressChange}
                                        className="custom-range"
                                    />
                                </div>
                                <div className="text-end">{progress}%</div>
                            </div>

                            <div className="mb-3">
                                <strong>Set Progress Manually: </strong>
                                <input
                                    type="number"
                                    value={manualProgress}
                                    onChange={handleManualInputChange}
                                    className="form-control"
                                    min="0"
                                    max="100"
                                    style={{ width: '100px' }}
                                />
                            </div>

                            {/* Dropdown to select project status */}
                            <div className="mb-4">
                                <strong>Status:</strong>
                                <Select
                                    value={statusOptions.find(option => option.value === status)}
                                    onChange={handleStatusChange}
                                    options={statusOptions}
                                />
                            </div>

                            {/* Update Status Button */}
                            <div className="d-flex justify-content-end">
                                <button
                                    className="btn btn-primary"
                                    onClick={handleUpdateStatus}
                                    disabled={isUpdating}
                                >
                                    {isUpdating ? 'Updating...' : 'Update Status'}
                                </button>
                            </div>
                        </div>
                    </div>

                    {/* Main Content Card (3/4 width) */}
                    <div className="card" style={{ width: '75%' }}>
                        <div className="card-header border-bottom bg-base py-16 px-24">
                            <h6 className="text-lg fw-semibold mb-0">Project : {projectData.projectName}</h6>
                        </div>
                        <div className="card-body">
                            {/* Conditional Rendering based on Active Tab */}
                            {activeTab === 'overview' && (
                                <div className="ml-5">
                                    {/* Project Information with added margin for separation */}
                                    <div className="mb-3">
                                        <p><strong>Project Name: </strong><span className="ml-3">{projectData.projectName}</span></p>
                                    </div>
                                    <div className="mb-3">
                                        <p><strong>Client: </strong><span className="ml-3">{projectData.client?.name || 'Not assigned'}</span></p>
                                    </div>
                                    {/* <div className="mb-3">
                                        <p><strong>PIC</strong><span className="ml-3"> {projectData.client?.name || 'Not assigned'}</span></p>
                                    </div> */}
                                    <div className="mb-3">
                                        <p><strong>Start Date: </strong><span className="ml-3">{formatDate(projectData.startDate)}</span></p>
                                    </div>
                                    <div className="mb-3">
                                        <p><strong>End Date: </strong><span className="ml-3">{formatDate(projectData.endDate)}</span></p>
                                    </div>

                                    {/* Description section with title and content below */}
                                    <div className="mb-3">
                                        <h6><strong>Description:</strong></h6>
                                        <p>{projectData.projectDescription}</p>
                                    </div>

                                    {/* Progress bar section */}
                                    <div>
                                        <strong>Progress: </strong>
                                        <div className="progress">
                                            <div
                                                className="progress-bar"
                                                role="progressbar"
                                                style={{ width: `${progress}%`, backgroundColor: '#4CAF50' }}
                                                aria-valuenow={progress}
                                                aria-valuemin="0"
                                                aria-valuemax="100"
                                            >
                                                {progress}%
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            )}

                            {/* Other tabs content will go here (edit, task, etc.) */}
                            {activeTab === 'edit' && (
                                <div>
                                    <h4>Edit Project</h4>
                                    <p>Form to edit project details will go here.</p>
                                </div>
                            )}

                            {activeTab === 'task' && (
                                <div>
                                    <h4>Task Management</h4>
                                    <p>Task details and management options will go here.</p>
                                </div>
                            )}

                            {activeTab === 'discussion' && (
                                <div>
                                    <h4>Discussion</h4>
                                    <p>Discussion forum for this project will go here.</p>
                                </div>
                            )}

                            {activeTab === 'note' && (
                                <div>
                                    <h4>Notes</h4>
                                    <p>Project notes will go here.</p>
                                </div>
                            )}

                            {activeTab === 'attachFiles' && (
                                <div>
                                    <h4>Attach Files</h4>
                                    <p>Option to upload or view attached files will go here.</p>
                                </div>
                            )}

                            {activeTab === 'bugs' && (
                                <div>
                                    <h4>Bugs</h4>
                                    <p>Bug tracking and management will go here.</p>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

// Helper function to get progress (could also be moved to a separate util file)
const getProgress = (status) => {
    switch (status) {
        case 'Completed': return 100;
        case 'In Progress': return 50;
        case 'Not Started': return 0;
        case 'On Hold': return 10;
        default: return 0;
    }
};

const getProgressColor = (progress) => {
    if (progress < 30) return '#FF5733';
    if (progress < 70) return '#FFC107';
    return '#4CAF50';
};

export default ProjectDetails;
