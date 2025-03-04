import React, { useState } from 'react';
import Select from 'react-select';

const clients = [
    { value: 'client1', label: 'Client 1' },
    { value: 'client2', label: 'Client 2' },
    { value: 'client3', label: 'Client 3' },
    { value: 'client4', label: 'Client 4' },
];

const teamMembers = [
    { value: 'member1', label: 'Member 1' },
    { value: 'member2', label: 'Member 2' },
    { value: 'member3', label: 'Member 3' },
    { value: 'member4', label: 'Member 4' },
    { value: 'member5', label: 'Member 5' },
];

const AddUserLayer = () => {
    const [selectedClient, setSelectedClient] = useState(null);
    const [selectedTeamMembers, setSelectedTeamMembers] = useState([]);

    const handleClientChange = (selectedOption) => {
        setSelectedClient(selectedOption);
    };

    const handleTeamMembersChange = (selectedOptions) => {
        setSelectedTeamMembers(selectedOptions);
    };

    const customStyles = {
        control: (provided) => ({
            ...provided,
            borderColor: '#ced4da', // Border color for control
            borderRadius: '0.375rem', // Rounded corners to match the form inputs
            boxShadow: 'none', // Remove the box-shadow
            '&:hover': {
                borderColor: '#80bdff', // Hover border color
            },
        }),
        multiValue: (provided) => ({
            ...provided,
            backgroundColor: '#487FFF', // Background color for selected tags
            color: '#fff', // Text color for tags
            borderRadius: '0.375rem', // Same border radius as form controls
            padding: '0.10rem 0.5rem', // Add padding for tag size
            margin: '0.2rem', // Small space between tags
        }),
        multiValueLabel: (provided) => ({
            ...provided,
            color: '#fff', // Text color inside tags
            fontWeight: '500', // Slightly bold text for tags
        }),
        multiValueRemove: (provided) => ({
            ...provided,
            color: '#fff', // Color for remove (X) button
            '&:hover': {
                backgroundColor: '#458EFF', // Red color for the remove button on hover
                color: '#fff', // White color for remove button text
            },
        }),
        dropdownIndicator: (provided) => ({
            ...provided,
            color: '#007bff', // Indicator color for dropdown
            '&:hover': {
                color: '#0056b3', // Hover color for indicator
            },
        }),
        clearIndicator: (provided) => ({
            ...provided,
            color: '#e74a3b', // Clear indicator color (X button)
            '&:hover': {
                color: '#c82333', // Hover color for clear button
            },
        }),
    };

    return (
        <div className="col-lg-12">
            <div className="card">
                <div className="card-header">
                    <h5 className="card-title mb-0">Add New Task</h5>
                </div>
                <div className="card-body">
                    <form className="row gy-3 needs-validation" noValidate="">
                        <div className="col-md-4">
                            <label className="form-label">Task Name</label>
                            <input
                                type="text"
                                name="#0"
                                className="form-control"
                                placeholder="Input Task Name"
                                required=""
                            />
                            <div className="valid-feedback">Looks good!</div>
                        </div>
                        <div className="col-md-4">
                            <label className="form-label">Start Date</label>
                            <input type="date" name="#0" className="form-control" />
                        </div>
                        <div className="col-md-4">
                            <label className="form-label">End Date</label>
                            <input type="date" name="#0" className="form-control" />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Client</label>
                            <Select
                                value={selectedClient}
                                onChange={handleClientChange}
                                options={clients}
                                placeholder="Select Client"
                                isRequired
                            />
                            <div className="valid-feedback">Looks good!</div>
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Team</label>
                            <Select
                                isMulti
                                options={teamMembers}
                                value={selectedTeamMembers}
                                onChange={handleTeamMembersChange}
                                placeholder="Select Team Members"
                                className="basic-multi-select"
                                classNamePrefix="select"
                                styles={customStyles} // Apply custom styles
                            />
                            <div className="valid-feedback">Looks good!</div>
                        </div>
                        <div className="col-md-12">
                            <label className="form-label">Description</label>
                            <textarea
                                name="#0"
                                className="form-control"
                                rows={4}
                                placeholder="Enter a description.."
                                defaultValue={""}
                            />
                        </div>
                        <div className="d-flex justify-content-end mt-3 mb-2">
                            <button className="btn btn-primary-600" type="submit">
                                Add
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default AddUserLayer;