import { useState, useEffect } from 'react';
import api from '../apiService';
import { start } from '@popperjs/core';

const useCalendar = () => {
    const [CalendarEvents, setEvents] = useState([])
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchEvents = async () => {
        try {
            setLoading(true);
            const response = await api.get('/api/getCompanyCalendar', {
                headers: {
                    'Cache-Control': 'no-cache',
                    'Pragma': 'no-cache'
                }
            });
            console.log('Raw API Response:', response.data);

            const formattedEvents = response.data.map(event => ({
                id: event.idCalendar,
                idEvent: event.idEvent,
                eventName: event.nameEvent,
                title: event.nameEvent,
                isFree: event.isFree,
                startDate: event.startDate,
                endDate: event.endDate,
                start: event.startDate,
                end: event.endDate,
                description: event.description,
                backgroundColor: event.isFree ? '#dc3545' : '#0d6efd',
                borderColor: event.isFree ? '#dc3545' : '#0d6efd',
                allDay: true
            }));

            setEvents(formattedEvents);
            setError(null);
        } catch (error) {
            console.error('Error fetching events:', error);
            setError('Failed to fetch events');
        } finally {
            setLoading(false);
        }
    };

    const addCompanyCalendar = async (eventData) => {
        try {
            const eventResponse = await api.post('/api/addCompanyEvent', {
                eventName: eventData.eventName,
                isFree: eventData.isFree,
            });

            if (!eventResponse.data) {
                throw new Error('Failed to create event');
            }

            const calendarResponse = await api.post('/api/addCompanyCalendar', {
                idEvent: eventResponse.data.data.id,
                startDate: eventData.startDate,
                endDate: eventData.endDate,
                description: eventData.description
            });

            if (calendarResponse.data) {
                const newEvent = {
                    id: calendarResponse.data.id,
                    eventName: eventResponse.data.eventName,
                    isFree: eventResponse.data.isFree,
                    startDate: calendarResponse.data.startDate,
                    endDate: calendarResponse.data.endDate,
                    description: calendarResponse.data.description
                };
                setEvents(prev => [...prev, newEvent]);
                return newEvent;
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
                if (error.response.data.message.includes('Start date')) {
                    throw new Error("Start date can't be earlier than today");
                }
                else if (error.response.data.message.includes('End date')) {
                    throw new Error("End date must be after than start date");
                }
                throw new Error(error.response.data.message);
            }
            throw new Error('Failed to add event. Please check your input.');
        }
    };

    const editCompanyCalendar = async (calendarId, updatedEventData) => {
        try {
            console.log('Editing calendar:', calendarId, updatedEventData);

            const response = await api.put(`/api/updateEvent/${calendarId}`, {
                calendarReq: {
                    startDate: updatedEventData.calendarReq.startDate,
                    endDate: updatedEventData.calendarReq.endDate,
                    description: updatedEventData.calendarReq.description,
                    idEvent: updatedEventData.calendarReq.idEvent
                },
                eventReq: {
                    eventName: updatedEventData.eventReq.eventName,
                    isFree: updatedEventData.eventReq.isFree
                }
            });

            if (response.data) {
                await fetchEvents();
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

    const deleteCompanyCalendar = async (calendarId) => {
        try {
            const calendar = CalendarEvents.find(event => event.id === calendarId);

            if (!calendar) {
                throw new Error('Calendar event not found');
            }

            const idEvent = calendar.idEvent;

            console.log('Event ID:', idEvent);
            console.log('Deleting calendar:', calendarId, idEvent);
            await api.delete(`/api/deleteCalendar/${calendarId}`);
            await api.delete(`/api/deleteEvent/${idEvent}`);

            await fetchEvents();
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
        fetchEvents();
    }, []);

    return {
        CalendarEvents,
        loading,
        error,
        refreshEvents: fetchEvents,
        addCompanyCalendar,
        editCompanyCalendar,
        deleteCompanyCalendar
    };
};

export default useCalendar;