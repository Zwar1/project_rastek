import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Icon } from '@iconify/react';
import useTask from '../hook/useTask';
import useGetClient from '../hook/useGetClient';
import useProject from '../hook/useProject';

const TaskData = () => {
    // State untuk status filter proyek
    const [filterStatus, setFilterStatus] = useState('All');
    const { tasks, loading, error } = useTask();
    const navigate = useNavigate();
    const clientList = useGetClient();

    console.log('Current tasks:', tasks);

    // Safer filtering logic with null checks and case handling
    const filteredTasks = tasks ? tasks.filter(task => {
        if (!filterStatus || filterStatus === 'All') return true;

        // Convert status to match the format from your API
        const normalizedFilter = filterStatus.toLowerCase().replace(' ', '_');
        return task.status?.toLowerCase() === normalizedFilter;
    }) : [];

    // Calculate statistics with safer checks
    const taskStats = {
        completed: tasks?.filter(task => task.progress === 100)?.length || 0,
        inProgress: tasks?.filter(task => task.progress > 0 && task.progress < 100 && !task.onHold)?.length || 0,
        notStarted: tasks?.filter(task => task.progress === 0 && !task.onHold)?.length || 0,
        onHold: tasks?.filter(task => task.onHold)?.length || 0
    };

    // Update your filter click handlers to match API values
    const handleFilterClick = (status) => {
        if (status === 'All') {
            setFilterStatus('');  // Clear filter
        } else {
            // Convert UI status to API format
            const apiStatus = status.toLowerCase().replace(' ', '_');
            setFilterStatus(apiStatus);
        }
    };

    const formatDate = (dateString) => {
        if (!dateString) return "Not set";
        return new Date(dateString).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    };

    const handleOpenClick = (taskId) => {
        if (taskId) {
            console.log('Navigating to task:', taskId);
            navigate(`/task/${taskId}`);
        }
    };

    const getStatusDisplay = (task) => {
        if (task.progress === 100) {
            return {
                text: 'Completed',
                className: 'bg-success-focus text-success-main'
            };
        }
        if (task.progress > 0 && !task.onHold) {
            return {
                text: 'In Progress',
                className: 'bg-warning-focus text-warning-main'
            };
        }
        if (task.onHold) {
            return {
                text: 'On Hold',
                className: 'bg-danger-focus text-danger-main'
            };
        }
        return {
            text: 'Not Started',
            className: 'bg-neutral-focus text-neutral-main'
        };
    };

    return (
        <div>
            {/* Card With Background Color */}
            <div className="mb-24">
                <div className="row gy-4">
                    <div className="col-xxl-3 col-sm-6">
                        <div className="card h-100 radius-12 bg-gradient-purple" style={{ height: '150px', width: '300px' }}>
                            <div className="card-body p-16 d-flex flex-column justify-content-between">
                                <div className="d-flex align-items-center gap-3">
                                    <div
                                        className="w-40-px h-40-px d-inline-flex align-items-center justify-content-center bg-lilac-600 text-white radius-8"
                                        style={{ flexShrink: 0 }}
                                    >
                                        <Icon icon="mdi:account-check" className="h5 mb-0" />
                                    </div>
                                    <h6 className="mb-0 text-white">{taskStats.completed}</h6>
                                </div>
                                <Link
                                    to="#"
                                    className="btn text-lilac-600 hover-text-lilac px-2 py-2 d-inline-flex align-items-center gap-2"
                                >
                                    Total Completed{" "}
                                    {/* <Icon icon="iconamoon:arrow-right-2" className="text-xl" /> */}
                                </Link>
                            </div>
                        </div>
                    </div>
                    <div className="col-xxl-3 col-sm-6">
                        <div className="card h-100 radius-12 bg-gradient-primary" style={{ height: '150px', width: '300px' }}>
                            <div className="card-body p-16 d-flex flex-column justify-content-between">
                                <div className="d-flex align-items-center gap-3">
                                    <div
                                        className="w-40-px h-40-px d-inline-flex align-items-center justify-content-center bg-primary-600 text-white radius-8"
                                        style={{ flexShrink: 0 }}
                                    >
                                        <Icon icon="game-icons:progression" className="h5 mb-0" />
                                    </div>
                                    <h6 className="mb-0 text-white">{taskStats.inProgress}</h6>
                                </div>
                                <Link
                                    to="#"
                                    className="btn text-primary-600 hover-text-primary px-2 py-2 d-inline-flex align-items-center gap-2"
                                >
                                    Total in Progress{" "}
                                    {/* <Icon icon="iconamoon:arrow-right-2" className="text-xl" /> */}
                                </Link>
                            </div>
                        </div>
                    </div>
                    <div className="col-xxl-3 col-sm-6">
                        <div className="card h-100 radius-12 bg-gradient-success text-end" style={{ height: '150px', width: '300px' }}>
                            <div className="card-body p-16 d-flex flex-column justify-content-between">
                                <div className="d-flex align-items-center gap-3">
                                    <div
                                        className="w-40-px h-40-px d-inline-flex align-items-center justify-content-center bg-success-600 text-white radius-8"
                                        style={{ flexShrink: 0 }}
                                    >
                                        <Icon icon="lets-icons:progress" className="h5 mb-0" />
                                    </div>
                                    <h6 className="mb-0 text-white">{taskStats.notStarted}</h6>
                                </div>
                                <Link
                                    to="#"
                                    className="btn text-success hover-text-success px-0 py-0 d-inline-flex align-items-center gap-2"
                                >
                                    Total Not Started{" "}
                                    {/* <Icon icon="iconamoon:arrow-right-2" className="text-xl" /> */}
                                </Link>
                            </div>
                        </div>
                    </div>
                    <div className="col-xxl-3 col-sm-6">
                        <div className="card h-100 radius-12 bg-gradient-danger text-center" style={{ height: '150px', width: '300px' }}>
                            <div className="card-body p-16 d-flex flex-column justify-content-between">
                                <div className="d-flex align-items-center gap-3">
                                    <div
                                        className="w-40-px h-40-px d-inline-flex align-items-center justify-content-center bg-danger text-white radius-8"
                                        style={{ flexShrink: 0 }}
                                    >
                                        <Icon icon="mdi:account-pending" className="h5 mb-0" />
                                    </div>
                                    <h6 className="mb-0 text-white">{taskStats.onHold}</h6>
                                </div>
                                <Link
                                    to="#"
                                    className="btn text-danger hover-text-danger px-0 py-0 d-inline-flex align-items-center gap-2"
                                >
                                    Total On Hold{" "}
                                    {/* <Icon icon="iconamoon:arrow-right-2" className="text-xl" /> */}
                                </Link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Task Data Header */}
            <div className="card basic-data-table">
                <div className="card-header d-flex justify-content-between align-items-center">
                    <h5 className="card-title mb-0">Task Data</h5>
                    <div className="d-flex align-items-center gap-3">
                        {/* Filter Dropdown */}
                        <div className="dropdown">
                            <button
                                className="btn btn-outline-primary-600 btn-sm radius-8 not-active px-16 py-9 dropdown-toggle toggle-icon"
                                type="button"
                                data-bs-toggle="dropdown"
                                aria-expanded="false"
                            >
                                Filter
                            </button>
                            <ul className="dropdown-menu">
                                <li>
                                    <button
                                        className="dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-neutral-200 text-hover-neutral-900"
                                        onClick={() => handleFilterClick('All')}
                                    >
                                        All
                                    </button>
                                </li>
                                <li>
                                    <button
                                        className="dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-neutral-200 text-hover-neutral-900"
                                        onClick={() => handleFilterClick('completed')}
                                    >
                                        Completed
                                    </button>
                                </li>
                                <li>
                                    <button
                                        className="dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-neutral-200 text-hover-neutral-900"
                                        onClick={() => handleFilterClick('in_progress')}
                                    >
                                        In Progress
                                    </button>
                                </li>
                                <li>
                                    <button
                                        className="dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-neutral-200 text-hover-neutral-900"
                                        onClick={() => handleFilterClick('not_started')}
                                    >
                                        Not Started
                                    </button>
                                </li>
                                <li>
                                    <button
                                        className="dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-neutral-200 text-hover-neutral-900"
                                        onClick={() => handleFilterClick('on_hold')}
                                    >
                                        On Hold
                                    </button>
                                </li>
                            </ul>
                        </div>
                        <form className="navbar-search d-flex">
                            <input
                                type="text"
                                className="bg-base h-40-px w-auto"
                                name="search"
                                placeholder="Search"
                            />
                            <Icon icon="ion:search-outline" className="icon" />
                        </form>
                        <Link
                            to="/addtaskpage"
                            className="btn btn-primary text-sm btn-sm radius-8 px-16 py-9 radius-8 d-flex align-items-center gap-2"
                        >
                            <Icon
                                icon="ic:baseline-plus"
                                className="icon text-xl line-height-1"
                            />
                            Add Task
                        </Link>
                    </div>
                </div>
            </div>

            {/* Project Data Cards */}
            <div className="row gy-4 mt-10">
                {loading && <div className="text-center">Loading tasks...</div>}
                {error && <div className="alert alert-danger">{error}</div>}

                {filteredTasks.map((task, index) => (
                    <div key={index} className="col-xxl-4 col-sm-6">
                        <div className="card radius-12 h-100">
                            {/* Card Header */}
                            <div className="card-header py-16 px-24 bg-base d-flex align-items-center gap-1 justify-content-between border border-end-0 border-start-0 border-top-0">
                                <h6 className="text-lg mb-0" style={{
                                    whiteSpace: 'nowrap',
                                    overflow: 'hidden',
                                    textOverflow: 'ellipsis',
                                    maxWidth: '200px'
                                }}
                                    title={task.title} // Shows full title on hover
                                >
                                    {task.title}
                                </h6>
                                <span className={`${getStatusDisplay(task).className} px-20 py-6 rounded-pill fw-medium text-sm`}>
                                    {getStatusDisplay(task).text}
                                </span>
                            </div>

                            {/* Card Body */}
                            <div className="card-body py-16 px-24">
                                <p className="card-text text-neutral-600 mb-0">
                                    {task.project?.projectName} - {task.project?.client?.name}
                                </p>
                                <p className="text-muted small">Due Date: {formatDate(task.endDate)}</p>

                                <div className="d-flex justify-content-between">
                                    <p className="fw-bold mb-1">Progress:</p>
                                    <p className="fw-bold mb-1 text-end">{task.progress || 0}%</p>
                                </div>

                                {/* Progress Bar */}
                                <div className="progress mt-2" style={{ height: '20px' }}>
                                    <div
                                        className="progress-bar text-center"
                                        role="progressbar"
                                        style={{
                                            width: `${task.progress ?? 0}%`,
                                            minWidth: '40px',
                                            backgroundColor: task.progress === 100 ? '#198754' : '#0d6efd'
                                        }}
                                        aria-valuenow={task.progress ?? 0}
                                        aria-valuemin="0"
                                        aria-valuemax="100"
                                    >
                                        {`${task.progress ?? 0}%`}
                                    </div>
                                </div>
                            </div>

                            {/* Card Footer */}
                            <div className="card-footer text-end bg-transparent border border-end-0 border-start-0 border-bottom-0 py-16 px-24">
                                <button
                                    onClick={() => handleOpenClick(task.id)}
                                    className="btn btn-primary"
                                >
                                    Open
                                </button>
                            </div>
                        </div>
                    </div>
                ))}

                {!loading && filteredTasks.length === 0 && (
                    <div className="col-12 text-center">
                        <p className="text-muted">No tasks found</p>
                    </div>
                )}
            </div>
        </div >
    );
};

export default TaskData;
