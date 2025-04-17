import { useState, useEffect } from "react";
import api from "../apiService";
import useDetailedEmployee from "./useDetailedEmployee";

const useEmployeeCalendar = () => {
    const [employeeEvents, setEmployeeEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const detailedEmployee = useDetailedEmployee();
    const nik = detailedEmployee?.data?.employee?.nik;

    const fetchEmployeeEvents = async () => {
        if (!nik) {
            console.warn('No NIK available');
            return;
        }

        console.log('Fetching employee events:', nik);
        try {
            setLoading(true);
            const response = await api.get(`/api/getEmployeeCalendar/${nik}`, {
                headers: {
                    'Cache-Control': 'no-cache',
                    'Pragma': 'no-cache'
                }
            });
            console.log('Employee events fetched successfully:', response.data);

            const eventsData = response.data.data || [];
            console.log('Events data:', eventsData);

            const formattedEvents = eventsData.map(event => ({
                idEmployeeCalendar: event.idEmployeeCalendar,
                id: event.idEmployeeCalendar, // Keep the 'id' for compatibility
                idEvent: event.idEmployeeEvent,
                eventName: event.nameEvent,
                title: event.nameEvent,
                isCuti: event.isCuti,
                startDate: event.startDate,
                endDate: event.endDate,
                // start: event.startDate,
                // end: event.endDate,
                description: event.description,
                // backgroundColor: event.isFree ? '#dc3545' : '#0d6efd',
                // borderColor: event.isFree ? '#dc3545' : '#0d6efd',
                // allDay: true,
                nik: event.nik
            }));

            console.log('Formatted events:', formattedEvents);
            setEmployeeEvents(formattedEvents);
            setError(null);
        } catch (error) {
            console.error('Calendar API Error:', {
                status: error.response?.status,
                statusText: error.response?.statusText,
                data: error.response?.data,
                message: error.message
            });

            if (error.response?.status === 403) {
                setError('You do not have permission to view this calendar. Please check your access rights.');
            } else {
                setError(error.response?.data?.message || 'Failed to fetch calendar events');
            }
        } finally {
            setLoading(false);
        }
    };

    const addEmployeeCalendar = async (eventData) => {
        try {
            const eventResponse = await api.post(`/api/addEmployeeEvent/${nik}`, {
                eventName: eventData.eventName,
                isCuti: eventData.isCuti
            });

            if (!eventResponse.data) {
                throw new Error('Failed to create event');
            }

            const calendarResponse = await api.post(`/api/addEmployeeCalendar/${nik}`, {
                idEmployeeEvent: eventResponse.data.data.id,
                startDate: eventData.startDate,
                endDate: eventData.endDate,
                description: eventData.description
            });

            if (calendarResponse.data) {
                const newEvent = {
                    id: calendarResponse.data.id,
                    eventName: eventResponse.data.eventName,
                    isCuti: eventResponse.data.isCuti,
                    startDate: calendarResponse.data.startDate,
                    endDate: calendarResponse.data.endDate,
                    description: calendarResponse.data.description
                };
                setEmployeeEvents(prev => [...prev, newEvent]);
                return newEvent;
            }
            throw new Error('Invalid response format');
        } catch (error) {
            console.error('Error details:', error.response?.data);

            if (error.response?.data?.error) {
                if (error.response.data.error.startDateValid) {
                    throw new Error(error.response.data.error.startDateValid);
                }
                if (error.response.data.error.endDateValid) {
                    throw new Error(error.response.data.error.endDateValid);
                }
            }

            if (error.response?.data?.errors) {
                const validationErrors = error.response.data.errors;
                const dateErrors = validationErrors.filter(error =>
                    error.defaultMessage.includes('must be a date') ||
                    error.field === 'startDate' ||
                    error.field === 'endDate'
                );

                if (dateErrors.length > 0) {
                    throw new Error(dateErrors[0].defaultMessage);
                }
            }

            if (error.response?.data?.message) {
                if (error.response.data.message.includes('Start date')) {
                    throw new Error("Start date can't be earlier than today");
                }
                else if (error.response.data.message.includes('End date')) {
                    throw new Error("End date must be after start date");
                }
                throw new Error(error.response.data.message);
            }

            // Handle generic error
            throw new Error('Failed to add event. Please check your input.');
        }
    };

    const editEmployeeCalendar = async (calendarId, updatedEventData) => {
        try {
            console.log('Editing calendar:', calendarId, updatedEventData);

            const response = await api.put(`/api/updateEmployeeCalendar/${calendarId}`, {
                employeeCalendarReq: {
                    startDate: updatedEventData.employeeCalendarReq.startDate,
                    endDate: updatedEventData.employeeCalendarReq.endDate,
                    description: updatedEventData.employeeCalendarReq.description,
                    idEmployeeEvent: updatedEventData.employeeCalendarReq.idEmployeeEvent
                },
                employeeEventReq: {
                    eventName: updatedEventData.employeeEventReq.eventName,
                    isCuti: updatedEventData.employeeEventReq.isCuti
                }
            });

            if (response.data) {
                await fetchEmployeeEvents();
                return response.data;
            }
            throw new Error('Invalid response format');
        } catch (error) {
            console.error('Error details:', error.response?.data);

            if (error.response?.status === 403) {
                throw new Error('You do not have permission to update this event');
            }

            if (error.response?.data?.errors) {
                const validationErrors = error.response.data.errors;
                const dateErrors = validationErrors.filter(error =>
                    error.defaultMessage.includes('must be a date') ||
                    error.field === 'startDate' ||
                    error.field === 'endDate'
                );

                if (dateErrors.length > 0) {
                    throw new Error(dateErrors[0].defaultMessage);
                }
            }

            if (error.response?.data?.message) {
                throw new Error(error.response.data.message);
            }
            throw new Error('Failed to update event. Please check your input.');
        }
    };

    const deleteEmployeeCalendar = async (calendarId) => {
        try {
            console.log('Deleting employee calendar with ID:', calendarId);
            console.log('Current employee events:', employeeEvents);

            // Use both id and idEmployeeCalendar for finding the event
            const calendar = employeeEvents.find(
                event => event.idEmployeeCalendar === calendarId || event.id === calendarId
            );

            if (!calendar) {
                console.error('Calendar event not found with ID:', calendarId);
                throw new Error('Calendar event not found');
            }

            // Use idEmployeeEvent if available, fallback to idEvent
            const idEvent = calendar.idEmployeeEvent || calendar.idEvent;

            console.log('Found event to delete:', calendar);
            console.log('Event ID:', idEvent);
            console.log('Calendar ID:', calendarId);

            await api.delete(`/api/deleteEmployeeCalendar/${calendarId}`);
            await api.delete(`/api/deleteEmployeeEvent/${idEvent}`);

            await fetchEmployeeEvents();
        } catch (error) {
            console.error('Error details:', error.response?.data);
            if (error.response?.status === 403) {
                throw new Error('You do not have permission to delete this event');
            }
            if (error.response?.data?.message) {
                throw new Error(error.response.data.message);
            }
            throw new Error('Failed to delete event. Please check your input.');
        }
    };

    useEffect(() => {
        if (nik) {
            fetchEmployeeEvents();
        }
    }, [nik]);

    return {
        employeeEvents,
        loading: loading || detailedEmployee.loading,
        error,
        currentNik: nik,
        refreshEmployeeEvents: fetchEmployeeEvents,
        addEmployeeCalendar,
        editEmployeeCalendar,
        deleteEmployeeCalendar
    };
};

export default useEmployeeCalendar;