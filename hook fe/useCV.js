import { useState, useContext, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { setDumpNik, incrementStep, setCurrentStep } from "../ReduxStore/employeeReducer.js";
import API_BASE_URL from "../apiConfig.js";
import api from "../apiService.js";
import { NotificationContext } from "../Notifications/NotificationContext.js";

const useAddCV = (nik) => {
    const dispatch = useDispatch();
    const { addNotification } = useContext(NotificationContext);

    const [cvData, setCvData] = useState({
        projectName: "",
        projectRole: "",
        projectStart: "",
        projectEnd: "",
        projectDescription: ""
    });

    const [cvList, setCvList] = useState([]);

    // Fetch CV list when component mounts or when nik changes
    useEffect(() => {
        console.log("Fetching CV List for NIK:", nik);
        if (nik) {
            fetchCVList();
        }
    }, [nik]);

    useEffect(() => {
        console.log("Updated CV List:", cvList);
    }, [cvList]);

    const fetchCVList = async () => {
        try {
          const response = await api.get(`/api/employee/${nik}`);
          console.log('Fetching CV List for NIK:', nik);
          console.log('API Response:', response);
      
          // Access the nested cv array correctly
          const cvData = response.data?.data?.cv || [];
          console.log('Updated CV List:', cvData);
          
          setCvList(cvData);
        } catch (error) {
          console.error(
            "Error Fetching CV List: ",
            error.response?.status || error.message
          );
          setCvList([]);
        }
      };

    const handleChange = (event) => {
        const { id, value } = event.target;
        setCvData((prevData) => ({
            ...prevData,
            [id]: value,
        }));
    };

    const handleSubmit = async (event) => {
        try {
            const response = await api.post(
                `/api/employee/${nik}/addCV`,
                cvData
            );

            if (response.data?.data?.id) {
                console.log("CV ID berhasil diambil:", response.data.data.id);
            }

            console.log("Data successfully submitted:", response.data);
            addNotification("CV Successfully Saved", "success");

            setCvData({
                projectName: "",
                projectRole: "",
                projectStart: "",
                projectEnd: "",
                projectDescription: ""
            });

            // Fetch updated CV list after adding
            fetchCVList();
        } catch (error) {
            console.error(
                "Error submitting data:",
                error.response?.data || error.message
            );
            addNotification(
                "Error Status! : " + (error.message || "Unknown Error"),
                'error'
            );
        }
    };

    return {
        cvData,
        setCvData,
        handleChange,
        handleSubmit,
        cvList,
        handleDelete: async (id) => {
            try {
                await api.delete(`/api/employee/${nik}/deleteCV/${id}`);
                addNotification("CV Entry Successfully Deleted", "success");
                fetchCVList();
            } catch (error) {
                console.error("Error deleting CV:", error);
                addNotification(
                    "Error deleting CV: " + (error.message || "Unknown Error"),
                    'error'
                );
            }
        }
    };
};

export default useAddCV;