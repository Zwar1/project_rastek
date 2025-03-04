import { useState, useEffect } from 'react';
import api from '../apiService';

const useTask = (taskId = null) => {
    const [tasks, setTasks] = useState([]);
    const [singleTask, setSingleTask] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchAllTasks = async () => {
        try {
            setLoading(true);
            const response = await api.get('/api/project/getAllTasks');
            console.log('Raw API Response:', response.data);

            // Extract tasks from the nested data property
            const taskData = response.data?.data || [];
            console.log('Extracted task data:', taskData);

            setTasks(taskData);
            setError(null);
        } catch (error) {
            console.error('Error fetching tasks:', error);
            setError('Failed to load tasks.');
            setTasks([]);
        } finally {
            setLoading(false);
        }
    };

    const fetchTaskById = async (id) => {
        try {
            setLoading(true);
            const response = await api.get(`/api/project/getTask/${id}`);
            // Assuming single task response might also be wrapped in data property
            const taskData = response.data?.data || response.data;
            console.log('Single Task Data:', taskData);
            setSingleTask(taskData);
        } catch (error) {
            console.error('Error fetching task:', error);
            setError('Failed to load task details.');
        } finally {
            setLoading(false);
        }
    };

    // const addTask = async (taskData) => {
    //     try {
    //         const response = await api.post(`/api/project/${projectId}/addTask`, {
    //             title: taskData.title,
    //             startDate: taskData.startDate,
    //             endDate: taskData.endDate,
    //             description: taskData.description,
    //             progress: taskData.progress || 0,
    //             taskMember: taskData.taskMember
    //         });
    //         if (response.data.data) {
    //             setTask(prev => [...prev, response.data.data]);
    //             return response.data.data;
    //         }
    //         throw new Error('Invalid response format');
    //     } catch (error) {
    //         throw new Error(error.response?.data?.message || 'Failed to add task');
    //     }
    // }

    useEffect(() => {
        if (taskId) {
            fetchTaskById(taskId);
        } else {
            fetchAllTasks();
        }
    }, [taskId]);

    // Debug logging
    useEffect(() => {
        console.log('Current tasks state:', tasks);
    }, [tasks]);

    return {
        tasks,
        task: singleTask,
        loading,
        error,
        fetchTaskById,
        fetchAllTasks
    };
};

export default useTask;