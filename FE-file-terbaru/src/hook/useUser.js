import { useState, useEffect } from 'react';
import api from '../apiService';

const useUser = (clientId) => {
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchUserByClientId = async (clientId) => {
        try {
            console.log("Fetching user data for client ID:", clientId);
            setLoading(true);

            if (!clientId) {
                throw new Error('No client ID provided');
            }

            const clientResponse = await api.get(`/api/get/client/${clientId}`);
            console.log('Client Response:', clientResponse.data);

            const clientData = clientResponse.data.data;
            if (!clientData?.user?.id) {
                throw new Error('No user associated with this client');
            }

            const userId = clientData.user.id;
            console.log('Found user ID:', userId);

            const userResponse = await api.get(`/api/get/user/${userId}`);
            console.log('User API Response:', userResponse.data);

            if (userResponse.data.data) {
                setUserData(userResponse.data.data);
                setError(null);
            } else {
                throw new Error('Invalid user data format');
            }
        } catch (error) {
            console.error('Error fetching user data:', error);
            setError(error.response?.data?.message || error.message || 'Failed to fetch user data');
        } finally {
            setLoading(false);
        }
    };

    const updateUserAccount = async (userId, updateData) => {
        try {
            console.log('Updating user with ID:', userId);
            setLoading(true);
            const response = await api.put(`/api/update/user/${userId}`, updateData);
            console.log('Update Response:', response.data);

            if (response.data.data) {
                setUserData(response.data.data);
                return response.data.data;
            }
            throw new Error('Invalid response format');
        } catch (error) {
            console.error('Error updating user:', error);
            throw new Error(error.response?.data?.message || 'Failed to update user account');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        console.log("useUser hook mounted with clientId:", clientId);
        if (clientId) {
            fetchUserByClientId(clientId);
        } else {
            setLoading(false);
            setError('No client ID provided');
        }
    }, [clientId]);

    return {
        userData,
        loading,
        error,
        updateUserAccount,
        refreshUser: () => fetchUserByClientId(clientId)
    };
};

export default useUser;