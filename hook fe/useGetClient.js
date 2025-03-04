import {useEffect, useState} from "react";
import api from "../apiService";

const useGetClient = () => {
    const [clientList, setClientList] = useState([]);

    useEffect(() => {
        const fetchClients = async () => {
            try {
                const response = await api.get("api/get/client");
                console.log("Client response:", response.data);
                setClientList(response.data);
            } catch (error) {
                console.error(
                    "Error Fetching Employee : ",
                    error.response?.status || error.message
                );
            }
        };
        fetchClients();
    }, []);
    return clientList;
};

export default useGetClient;