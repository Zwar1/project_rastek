import { useState, useEffect } from 'react';
import api from '../apiService';

const useProject = () => {
  const [sortBy, setSortBy] = useState('endDate');
  const [sortDir, setSortDir] = useState('asc');
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchProjects = async () => {
    try {
      setLoading(true);
      const response = await api.get(`/api/get/project?sortBy=${sortBy}&sortDir=${sortDir}`);
      console.log('API Response:', response.data);

      const projectData = Array.isArray(response.data) ? response.data : [];
      setProjects(projectData);
      setLoading(false);
    } catch (error) {
      setError('Failed to load projects.');
      setLoading(false);
    }
  };

  const addProject = async (projectData) => {
    try {
      const response = await api.post('/api/create/project', {
        projectName: projectData.projectName,
        projectDescription: projectData.projectDescription,
        startDate: projectData.projectStart,
        endDate: projectData.projectEnd,
        summary: projectData.summary,
        estimatedHours: projectData.estimatedHours,
        totalHours: projectData.totalHours,
        priority: projectData.priority,
        status: projectData.status,
        progress: projectData.progress || 0,
        clientId: projectData.clientId,
        member: projectData.member,
        // logo: projectData.logo,
        // logoType: projectData.logoType
      });

      if (response.data.data) {
        setProjects(prev => [...prev, response.data.data]);
        return response.data.data;
      }
      throw new Error('Invalid response format');
    } catch (error) {
      console.error('Error details:', error.response?.data);

      if (error.response?.data.errors) {
        const validationErrors = error.response.data.errors;

        const dateErrors = validationErrors.filter(error =>
          error.defaultMessage.includes('must be a date in the past or in the present') ||
          error.field === 'startDate' ||
          error.field === 'endDate'
        );

        if (dateErrors.length > 0) {
          throw new Error(dateErrors[0].defaultMessage);
        }
      }

      if (error.response?.data.message) {
       if (error.response.data.message.includes('End date')) {
          throw new Error("End date must be after than start date");
        }
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to add event. Please check your input.');
    }
  };

  const deleteProject = async (projectId) => {
    try {
      await api.delete(`/api/delete/project/${projectId}`);
      setProjects(prev => prev.filter(project => project.id !== projectId));
    } catch (error) {
      throw new Error('Failed to delete project');
    }
  };

  useEffect(() => {
    fetchProjects();
  }, [sortBy, sortDir]);
  const updateProject = async (projectId, updateData) => {
    try {
      const response = await api.put(`/api/update/project/${projectId}`, updateData);
      if (response.data.data) {
        setProjects(prev => prev.map(project =>
          project.id === projectId ? { ...project, ...response.data.data } : project
        ));
        return response.data.data;
      }
      throw new Error('Invalid response format');
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to update project');
    }
  };

  return {
    projects,
    loading,
    error,
    sortBy,
    sortDir,
    fetchProjects,
    addProject,
    deleteProject,
    updateProject,
    setSortBy,
    setSortDir
  };
};

export default useProject;