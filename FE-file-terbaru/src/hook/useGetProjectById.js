import { useState, useEffect } from "react";
import api from "../apiService";

const useGetProjectById = (id) => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        let isMounted = true;
        
        const fetchProject = async () => {
            console.log("Fetching project with ID:", id);
            if (!id) {
                setError('No project ID provided');
                setLoading(false);
                return;
            }

            try {
                setLoading(true);
                const response = await api.get(`/api/get/project/${id}`);
                console.log("Project API Response:", response.data);
                
                if (response.data) {
                    setData(response.data.data || response.data);
                    setError(null);
                } else {
                    throw new Error('No data received');
                }
            } catch (err) {
                console.error("Error fetching project:", err);
                setError(err.message || 'Failed to fetch project data');
                setData(null);
            } finally {
                setLoading(false);
            }
        };

        fetchProject();

        return () => {
            isMounted = false;
        };
    }, [id]);

    return { data, loading, error };
};

export default useGetProjectById;