import { useState, useEffect, useCallback } from "react";
import api from "../apiService";

const useGetClientById = (id) => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [profilePicture, setProfilePicture] = useState(null);

    const getClientProfilePicture = async (clientId) => {
        try {
            const response = await api.get(`/api/client/${clientId}/profile-picture`, {
                responseType: 'blob'
            });
            const imageUrl = URL.createObjectURL(response.data);
            setProfilePicture(imageUrl);
        } catch (error) {
            console.error('Failed to load profile picture:', error);
            setProfilePicture(null);
        }
    };

    const fetchClient = useCallback(async () => {
        console.log("fetchClient called with ID:", id);
        if (!id) {
            console.warn("No client ID provided");
            setError("No client ID provided");
            setLoading(false);
            return;
        }

        try {
            setLoading(true);
            console.log("Making API request for client ID:", id);
            const response = await api.get(`/api/get/client/${id}`);
            console.log("API Response:", response.data);

            if (response.data) {
                setData(response.data.data || response.data);
                setError(null);
            } else {
                throw new Error('No data received');
            }
        } catch (err) {
            console.error("Error fetching client:", err);
            setError(err.message || "Failed to fetch client data");
            setData(null);
        } finally {
            setLoading(false);
        }
    }, [id]);

    const editClient = async (clientData) => {
        try {
            console.log("Updating client with ID:", id);
            console.log("Client Data:", clientData);
            setLoading(true);
            const response = await api.put(`/api/update/client/${id}`, clientData);
            console.log("Client API Response:", response.data);
            if (response.data.data) {
                setData(response.data.data);
                return response.data.data;
            }
            throw new Error('Invalid response format');
        } catch (error) {
            console.error('Error details:', error.response?.data);
            if (error.response?.data.message) {
                console.log("Error Message from API:", error.response.data.message);
                throw new Error(error.response.data.message);
            }
            throw new Error('Failed to update client. Please check your input.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        let isMounted = true;

        const fetchData = async () => {
            if (!id) {
                setError('No client ID provided');
                setLoading(false);
                return;
            }

            try {
                setLoading(true);
                await fetchClient();
                if (isMounted) {
                    await getClientProfilePicture(id);
                }
            } catch (err) {
                if (isMounted) {
                    setError(err.message);
                }
            } finally {
                if (isMounted) {
                    setLoading(false);
                }
            }
        };

        fetchData();

        return () => {
            isMounted = false;
            if (profilePicture) {
                URL.revokeObjectURL(profilePicture);
            }
        };
    }, [id]);

    return {
        data,
        loading,
        error,
        profilePicture,
        refreshClient: fetchClient,
        editClient
    };
};

export default useGetClientById;



