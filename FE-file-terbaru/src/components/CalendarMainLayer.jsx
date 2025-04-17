import "bootstrap/dist/js/bootstrap.bundle.min.js";
import React, { useEffect, useRef, useState } from "react";
import Calendar from "./child/Calendar";
import { Icon } from "@iconify/react/dist/iconify.js";
import flatpickr from "flatpickr";
import "flatpickr/dist/flatpickr.min.css";
import { event } from "jquery";
import useCompanyCalendar from "../hook/useCompanyCalendar";
import useEmployeeCalendar from "../hook/useEmployeeCalendar";
import * as bootstrap from "bootstrap";

const DatePicker = ({
  id,
  placeholder,
  selected,
  onChange,
  enableTime = true,
}) => {
  const datePickerRef = useRef(null);
  const fpInstance = useRef(null);

  useEffect(() => {
    fpInstance.current = flatpickr(datePickerRef.current, {
      enableTime: enableTime,
      noCalendar: !enableTime ? false : undefined,
      dateFormat: "Y-m-d H:i",
      time_24hr: true,
      minuteIncrement: 1,
      defaultDate: null,
      defaultHour: selected ? new Date(selected).getHours() : new Date().getHours(),
      defaultMinute: selected ? new Date(selected).getMinutes() : new Date().getMinutes(),
      onChange: (selectedDates) => {
        if (selectedDates[0]) {
          //const date = selectedDates[0];
          //const localDate = new Date(selectedDates[0].toISOString().replace('Z', ''));
          onChange(selectedDates[0]);
        }
      }
    });

    return () => fpInstance.current.destroy();
  }, [onChange, enableTime, selected]);

  useEffect(() => {
    if (fpInstance.current) {
      if (selected) {
        fpInstance.current.setDate(selected, false);
      }
      else {
        fpInstance.current.clear();
      }
    }
  }, [selected]);

  return (
    <input
      ref={datePickerRef}
      id={id}
      type="text"
      className="form-control radius-8 bg-base"
      placeholder={placeholder}

    />
  );
};

const CalendarMainLayer = () => {
  const { CalendarEvents, loading, error, refreshEvents,
    addCompanyCalendar, editCompanyCalendar, deleteCompanyCalendar } = useCompanyCalendar();
  const { employeeEvents, loading: employeeLoading, error: employeeError, refreshEmployeeEvents,
    addEmployeeCalendar,
    editEmployeeCalendar,
    deleteEmployeeCalendar } = useEmployeeCalendar();
  const [viewEvent, setViewEvent] = useState(null);
  const [viewEmployeeEvent, setViewEmployeeEvent] = useState(null);
  const [selectedEventId, setSelectedEventId] = useState(null);
  const [selectedEmployeeEventId, setSelectedEmployeeEventId] = useState(null);
  const [editEventData, setEditEventData] = useState(null);
  const [editEmployeeEventData, setEditEmployeeEventData] = useState(null);
  const [view, setView] = useState({});
  const [newEvent, setNewEvent] = useState({
    date: null,
    title: "",
  });

  const [formData, setFormData] = useState({
    title: "",
    startDate: null,
    endDate: null,
    description: "",
    isFree: false,
    isEmployeeEvent: false
  });

  const [leaveFormData, setLeaveFormData] = useState({
    title: "",
    startDate: new Date(),
    endDate: new Date(),
    description: "",
    isFree: true,
    isEmployeeEvent: false
  });



  useEffect(() => {
    const modalElements = document.querySelectorAll('.modal');
    modalElements.forEach(modalElement => {
      if (!bootstrap.Modal.getInstance(modalElement)) {
        new bootstrap.Modal(modalElement);
      }
    });

    const closeAllDropdowns = (event) => {
      if (!event.target.closest('.dropdown')) {
        document.querySelectorAll('.dropdown-menu.show').forEach(menu => {
          menu.classList.remove('show');
        });
      }
    };

    document.addEventListener('click', closeAllDropdowns);

    return () => {
      document.removeEventListener('click', closeAllDropdowns);
    };
  }, []);

  const handleFormChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleLeaveFormChange = (e) => {
    const { name, value, type, checked } = e.target;
    setLeaveFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleOpenLeaveForm = (selectedDate) => {
    const newDate = new Date(selectedDate);
    const endDate = new Date(selectedDate);
    endDate.setHours(23, 59, 0, 0);

    setLeaveFormData((prev) => ({
      ...prev,
      startDate: newDate,
      endDate: endDate,
      isEmployeeEvent: false, // Reset to company event by default
    }));
    const cutiModal = new bootstrap.Modal(document.getElementById('cutiModal'));
    cutiModal.show();
  };

  const handleDate = (date) => {
    setNewEvent({ ...newEvent, date });
  };

  const handleSave = async (e) => {
    e.preventDefault();
    console.log('Clicked');

    if (!formData.startDate || !formData.title) {
      alert('Please fill all required fields');
      return;
    }
    try {
      const formatToLocalDateTime = (date) => {
        const offset = date.getTimezoneOffset();
        const localDate = new Date(date.getTime() - (offset * 60 * 1000));
        return localDate.toISOString().slice(0, 19);
      };

      const scheduleData = {
        eventName: formData.title,
        isFree: formData.isFree,
        isCuti: formData.isEmployeeEvent ? formData.isFree : false, // For employee events, we use isCuti instead of isFree
        startDate: formatToLocalDateTime(formData.startDate),
        endDate: formatToLocalDateTime(formData.endDate),
        description: formData.description || ''
      };

      console.log('Sending event data:', scheduleData);
      console.log('Is employee event?', formData.isEmployeeEvent);

      if (formData.isEmployeeEvent) {
        await addEmployeeCalendar(scheduleData);
        alert('Employee event successfully created!');
      } else {
        await addCompanyCalendar(scheduleData);
        alert('Company event successfully created!');
      }

      // Reset form
      setFormData({
        title: '',
        startDate: null,
        endDate: null,
        description: '',
        isFree: false,
        isEmployeeEvent: false
      });

      document.getElementById('modalDismissButton').click();

      await new Promise(resolve => setTimeout(resolve, 300));
      await refreshEvents();
      await refreshEmployeeEvents();
    } catch (error) {
      console.error('Error object:', error);
      console.error('Error details:', error.response?.data);

      if (error.response?.data?.message) {
        alert(error.response.data.message);
      } else if (error.response?.data?.errors && error.response.data.errors.length > 0) {
        alert(error.response.data.errors[0].defaultMessage || error.response.data.errors[0].message);
      } else {
        alert(error.message || 'An error occurred');
      }
    }
  };

  const handleLeaveSave = async (e) => {
    e.preventDefault();
    console.log("Clicked Save Leave");

    if (!leaveFormData.startDate || !leaveFormData.title) {
      alert("Please fill all required fields");
      return;
    }
    try {
      const formatToLocalDate = (date, isStart) => {
        const d = new Date(date);

        if (isStart) {
          d.setHours(0, 0, 0, 0);
        } else {
          d.setHours(23, 59, 59, 999);
        }

        const offset = date.getTimezoneOffset();
        const localDate = new Date(d.getTime() - (offset * 60 * 1000));

        return localDate.toISOString().slice(0, 19);
      };

      const scheduleData = {
        eventName: leaveFormData.title,
        isFree: leaveFormData.isFree,
        isCuti: leaveFormData.isEmployeeEvent ? leaveFormData.isFree : false,
        startDate: formatToLocalDate(leaveFormData.startDate, true),
        endDate: formatToLocalDate(leaveFormData.endDate, false),
        description: leaveFormData.description || ""
      };

      console.log("Sending event data:", scheduleData);
      console.log('Is employee event?', leaveFormData.isEmployeeEvent);

      let response;
      if (leaveFormData.isEmployeeEvent) {
        response = await addEmployeeCalendar(scheduleData);
        alert("Employee event successfully created!");
      } else {
        response = await addCompanyCalendar(scheduleData);
        alert("Company event successfully created!");
      }

      console.log("API Response:", response);

      const modal = document.getElementById('cutiModal');
      const bsModal = bootstrap.Modal.getInstance(modal);
      if (bsModal) {
        bsModal.hide();
      }

      await new Promise(resolve => setTimeout(resolve, 500));

      await refreshEvents();
      await refreshEmployeeEvents();

      // Reset leave form
      const newToday = new Date(new Date().toDateString());
      setLeaveFormData({
        title: "",
        startDate: newToday,
        endDate: newToday,
        description: "",
        isFree: true,
        isEmployeeEvent: false
      });
    } catch (error) {
      console.error("Error object:", error);
      console.error("Error details:", error.response?.data);
      if (error.response?.data?.message) {
        alert(error.response.data.message);
      } else if (
        error.response?.data?.errors &&
        error.response.data.errors.length > 0
      ) {
        alert(
          error.response.data.errors[0].defaultMessage ||
          error.response.data.errors[0].message
        );
      } else {
        alert(error.message || "An error occurred");
      }
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: true
    });
  };

  const handleViewEvent = (eventId) => {
    let event = CalendarEvents.find((event) => event.id === eventId);

    // If not found in company events, check employee events
    if (!event) {
      event = employeeEvents.find((event) => event.id === eventId);
      if (event) {
        setViewEmployeeEvent(event);
      }
    } else {
      setViewEvent(event);
    }

    if (event) {
      const viewModal = new bootstrap.Modal(document.getElementById('exampleModalView'));
      viewModal.show();
    } else {
      alert("Event not found");
    }
  };

  const handleViewEmployeeEvent = (employeeEventId) => {
    const employeeEvent = employeeEvents.find((event) => event.id === employeeEventId);
    if (employeeEvent) {
      setViewEmployeeEvent(employeeEvent);
      const viewModal = new bootstrap.Modal(document.getElementById('exampleModalView'));
      viewModal.show();
    } else {
      alert("Employee\'s Event not found");
    }
  };

  const handleEditEvent = (eventId) => {
    console.log('Company Event ID received:', eventId);
    const event = CalendarEvents.find((event) => event.id === eventId);
    console.log('Found event:', event);

    if (event) {
      setEditEventData({
        id: event.id,
        idEvent: event.idEvent,
        eventName: event.eventName,
        startDate: new Date(event.startDate),
        endDate: new Date(event.endDate),
        description: event.description || '',
        isFree: event.isFree
      });

      const modalElement = document.getElementById('exampleModalEdit');
      if (modalElement) {
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
      }
    } else {
      alert("Event not found");
    }
  };

  const handleEditEmployeeEvent = (employeeEventId) => {
    console.log('Employee Event ID received:', employeeEventId);
    console.log('All employee events:', employeeEvents);

    // Use both id and idEmployeeCalendar for finding the event
    const employeeEvent = employeeEvents.find(
      (event) => event.id === employeeEventId || event.idEmployeeCalendar === employeeEventId
    );

    console.log('Found employee event:', employeeEvent);

    if (employeeEvent) {
      setEditEventData(null);

      setEditEmployeeEventData({
        id: employeeEvent.idEmployeeCalendar || employeeEvent.id,
        idEmployeeCalendar: employeeEvent.idEmployeeCalendar,
        idEvent: employeeEvent.idEmployeeEvent || employeeEvent.idEvent,
        idEmployeeEvent: employeeEvent.idEmployeeEvent,
        eventName: employeeEvent.eventName,
        startDate: new Date(employeeEvent.startDate),
        endDate: new Date(employeeEvent.endDate),
        description: employeeEvent.description || '',
        isCuti: employeeEvent.isCuti,
        nik: employeeEvent.nik
      });

      const modalElement = document.getElementById('exampleModalEdit');
      if (modalElement) {
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
      }
    } else {
      console.error("Employee Event not found with ID:", employeeEventId);
      alert("Employee Event not found");
    }
  };

  const handleSaveEdit = async (e) => {
    e.preventDefault();

    const currentEditData = editEventData || editEmployeeEventData;
    console.log('Current edit data:', currentEditData);

    const now = new Date();
    now.setHours(0, 0, 0, 0);

    if (!currentEditData?.startDate || !currentEditData?.eventName) {
      alert("Please fill all required fields");
      return;
    }

    if (new Date(currentEditData.startDate) < now) {
      alert("Start date cannot be in the past");
      return;
    }

    if (new Date(currentEditData.endDate) < new Date(currentEditData.startDate)) {
      alert("End date must be after start date");
      return;
    }

    try {
      const formatToLocalDateTime = (date) => {
        const offset = date.getTimezoneOffset();
        const localDate = new Date(date.getTime() - (offset * 60 * 1000));
        return localDate.toISOString().slice(0, 19);
      };

      // Check if we're editing a company event or employee event
      if (editEventData) {
        console.log('Edit Company Event Data:', editEventData);

        const updatedEvent = {
          calendarReq: {
            startDate: formatToLocalDateTime(new Date(editEventData.startDate)),
            endDate: formatToLocalDateTime(new Date(editEventData.endDate)),
            description: editEventData.description || "",
            idEvent: editEventData.idEvent
          },
          eventReq: {
            eventName: editEventData.eventName,
            isFree: editEventData.isFree
          }
        };

        console.log('Sending update request:', updatedEvent);

        await editCompanyCalendar(editEventData.id, updatedEvent);
        alert("Company Event successfully updated!");
      }
      else if (editEmployeeEventData) {
        console.log('Edit Employee Event Data:', editEmployeeEventData);

        const updatedEmployeeEvent = {
          employeeCalendarReq: {
            startDate: formatToLocalDateTime(new Date(editEmployeeEventData.startDate)),
            endDate: formatToLocalDateTime(new Date(editEmployeeEventData.endDate)),
            description: editEmployeeEventData.description || "",
            idEmployeeEvent: editEmployeeEventData.idEmployeeEvent || editEmployeeEventData.idEvent,
            NIK: editEmployeeEventData.nik
          },
          employeeEventReq: {
            eventName: editEmployeeEventData.eventName,
            isCuti: editEmployeeEventData.isCuti,
            NIK: editEmployeeEventData.nik
          }
        };

        console.log('Sending update request:', updatedEmployeeEvent);

        const calendarId = editEmployeeEventData.idEmployeeCalendar || editEmployeeEventData.id;
        console.log('Using calendar ID for update:', calendarId);

        await editEmployeeCalendar(editEmployeeEventData.id, updatedEmployeeEvent);
        alert("Employee Event successfully updated!");
      }

      const modal = document.getElementById('exampleModalEdit');
      if (modal) {
        const bsModal = bootstrap.Modal.getInstance(modal);
        if (bsModal) {
          bsModal.hide();
        }
      }

      setEditEventData(null);
      setEditEmployeeEventData(null);

      await refreshEvents();
      await refreshEmployeeEvents();
    } catch (error) {
      console.error('Error updating event:', error);
      alert(error.message || "An error occurred while updating the event.");
    }
  };

  useEffect(() => {
    const viewModal = document.getElementById('exampleModalView');
    if (viewModal) {
      viewModal.addEventListener('hidden.bs.modal', () => {
        setViewEvent(null);
        setViewEmployeeEvent(null);
      });

      return () => {
        viewModal.removeEventListener('hidden.bs.modal', () => {
          setViewEvent(null);
          setViewEmployeeEvent(null);
        });
      };
    }
  }, []);

  return (
    <>
      <div className="row gy-4">
        <div className="col-xxl-3 col-lg-4">
          <div className="card h-100 p-0">
            <div className="card-body p-24">
              <button
                type="button"
                className="btn btn-primary text-md btn-sm px-12 py-12 w-100 radius-8 d-flex justify-content-center align-items-center gap-2 mb-32"
                data-bs-toggle="modal"
                data-bs-target="#exampleModal"
              >
                Add Schedule (Company/Employee)
              </button>
              {/* Company Events */}
              <h5 className="text-primary-light fw-semibold mb-3 mt-4">Company Events</h5>
              <div className="mt-32" style={{
                maxHeight: "430px",
                maxWidth: "300px",
                overflowY: "auto",
                overflowX: "hidden",
                paddingRight: "16px",
                marginRight: "-16px"
              }}>
                {loading ? (
                  <div>Loading events...</div>
                ) : error ? (
                  <div className="text-danger">{error}</div>
                ) : CalendarEvents.length === 0 ? (
                  <div className="text-muted">No company events</div>
                ) : (
                  [...CalendarEvents].sort((a, b) => {
                    const dateA = new Date(a.startDate);
                    const dateB = new Date(b.startDate);
                    return dateA - dateB;
                  }).map((event) => (
                    <div key={event.id} className="event-item d-flex align-items-center justify-content-between gap-4 pb-16 mb-16 border border-start-0 border-end-0 border-top-0">
                      <div className="flex-grow-1 overflow-hidden">
                        <div className="d-flex align-items-center gap-10">
                          <span className={`w-12-px h-12-px ${event.isFree ? 'bg-success-600' : 'bg-warning-600'} rounded-circle fw-medium`} />
                          <span className="text-secondary-light">
                            {formatDate(event.startDate)} - {formatDate(event.endDate)}
                          </span>
                        </div>
                        <span className="text-primary-light fw-semibold text-md mt-4 d-block text-truncate" title={event.eventName}>
                          {event.eventName}
                        </span>
                        {event.description && (
                          <p className="text-muted mt-2 mb-0 text-truncate" title={event.description}>
                            {event.description}
                          </p>
                        )}
                      </div>
                      <div className="dropdown flex-shrink-0">
                        <button
                          type="button"
                          className="btn btn-light btn-sm p-2 rounded-circle"
                          onClick={(e) => {
                            e.stopPropagation();
                            const dropdownMenu = e.currentTarget.nextElementSibling;
                            if (dropdownMenu.classList.contains('show')) {
                              dropdownMenu.classList.remove('show');
                            } else {
                              document.querySelectorAll('.dropdown-menu.show').forEach(menu => {
                                menu.classList.remove('show');
                              });
                              dropdownMenu.classList.add('show');
                            }
                          }}
                        >
                          <Icon
                            icon="entypo:dots-three-vertical"
                            className="icon text-secondary-light"
                          />
                        </button>
                        <ul className="dropdown-menu p-12 border bg-base shadow position-absolute"
                          style={{ zIndex: 1050, right: "auto", left: "auto", transform: "translateX(-100%)" }}>
                          <li>
                            <button
                              type="button"
                              className="dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-neutral-200 text-hover-neutral-900 d-flex align-items-center gap-10"
                              onClick={(e) => {
                                e.stopPropagation();
                                handleViewEvent(event.id);
                              }}
                            >
                              <Icon icon="hugeicons:view" className="icon text-lg line-height-1" />
                              View
                            </button>
                          </li>
                          <li>
                            <button
                              type="button"
                              className="dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-neutral-200 text-hover-neutral-900 d-flex align-items-center gap-10"
                              onClick={(e) => {
                                e.stopPropagation();
                                handleEditEvent(event.id);
                              }}
                            >
                              <Icon icon="lucide:edit" className="icon text-lg line-height-1" />
                              Edit
                            </button>
                          </li>
                          <li>
                            <button
                              type="button"
                              className="delete-item dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-danger-100 text-hover-danger-600 d-flex align-items-center gap-10"
                              onClick={(e) => {
                                e.stopPropagation();
                                setSelectedEventId(event.id);
                              }}
                              data-bs-toggle="modal"
                              data-bs-target="#exampleModalDelete"
                            >
                              <Icon icon="fluent:delete-24-regular" className="icon text-lg line-height-1" />
                              Delete
                            </button>
                          </li>
                        </ul>
                      </div>
                    </div>
                  ))
                )}
              </div>
              {/* Employee Events */}
              <h5 className="text-primary-light fw-semibold mb-3 mt-4">My Events</h5>
              <div className="mt-32" style={{
                maxHeight: "300px",
                maxWidth: "300px",
                overflowY: "auto",
                overflowX: "hidden",
                paddingRight: "16px",
                marginRight: "-16px"
              }}>
                {employeeLoading ? (
                  <div>Loading your events...</div>
                ) : employeeError ? (
                  <div className="text-danger">{employeeError}</div>
                ) : employeeEvents.length === 0 ? (
                  <div className="text-muted">No personal events</div>
                ) : (
                  [...employeeEvents].sort((a, b) => {
                    const dateA = new Date(a.startDate);
                    const dateB = new Date(b.startDate);
                    return dateA - dateB;
                  }).map((event) => (
                    <div key={event.id} className="event-item d-flex align-items-center justify-content-between gap-4 pb-16 mb-16 border border-start-0 border-end-0 border-top-0">
                      <div className="flex-grow-1 overflow-hidden">
                        <div className="d-flex align-items-center gap-10">
                          <span className={`w-12-px h-12-px ${event.isCuti ? 'bg-success-600' : 'bg-warning-600'} rounded-circle fw-medium`} />
                          <span className="text-secondary-light">
                            {formatDate(event.startDate)} - {formatDate(event.endDate)}
                          </span>
                        </div>
                        <span className="text-primary-light fw-semibold text-md mt-4 d-block text-truncate" title={event.eventName}>
                          {event.eventName}
                        </span>
                        {event.description && (
                          <p className="text-muted mt-2 mb-0 text-truncate" title={event.description}>
                            {event.description}
                          </p>
                        )}
                      </div>
                      <div className="dropdown flex-shrink-0">
                        <button
                          type="button"
                          className="btn btn-light btn-sm p-2 rounded-circle"
                          onClick={(e) => {
                            e.stopPropagation();
                            const dropdownMenu = e.currentTarget.nextElementSibling;
                            if (dropdownMenu.classList.contains('show')) {
                              dropdownMenu.classList.remove('show');
                            } else {
                              document.querySelectorAll('.dropdown-menu.show').forEach(menu => {
                                menu.classList.remove('show');
                              });
                              dropdownMenu.classList.add('show');
                            }
                          }}
                        >
                          <Icon
                            icon="entypo:dots-three-vertical"
                            className="icon text-secondary-light"
                          />
                        </button>
                        <ul className="dropdown-menu p-12 border bg-base shadow position-absolute"
                          style={{ zIndex: 1050, right: "auto", left: "auto", transform: "translateX(-100%)" }}>
                          <li>
                            <button
                              type="button"
                              className="dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-neutral-200 text-hover-neutral-900 d-flex align-items-center gap-10"
                              onClick={(e) => {
                                e.stopPropagation();
                                handleViewEmployeeEvent(event.id);
                              }}
                            >
                              <Icon icon="hugeicons:view" className="icon text-lg line-height-1" />
                              View
                            </button>
                          </li>
                          <li>
                            <button
                              type="button"
                              className="dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-neutral-200 text-hover-neutral-900 d-flex align-items-center gap-10"
                              onClick={(e) => {
                                e.stopPropagation();
                                handleEditEmployeeEvent(event.id);
                              }}
                            >
                              <Icon icon="lucide:edit" className="icon text-lg line-height-1" />
                              Edit
                            </button>
                          </li>
                          <li>
                            <button
                              type="button"
                              className="delete-item dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-danger-100 text-hover-danger-600 d-flex align-items-center gap-10"
                              onClick={(e) => {
                                e.stopPropagation();
                                setSelectedEmployeeEventId(event.id);
                              }}
                              data-bs-toggle="modal"
                              data-bs-target="#exampleModalDeleteEmployee"
                            >
                              <Icon icon="fluent:delete-24-regular" className="icon text-lg line-height-1" />
                              Delete
                            </button>
                          </li>
                        </ul>
                      </div>
                    </div>
                  ))
                )}
              </div>
            </div>
          </div>
        </div>
        <div className="col-xxl-9 col-lg-8">
          <div className="card h-100 p-0">
            <div className="card-body p-24">
              <div id="wrap">
                <div id="calendar" />
                <div style={{ clear: "both" }} />
                <Calendar
                  events={CalendarEvents}
                  employeeEvents={employeeEvents}
                  onDateSelected={(clickedDate) => handleOpenLeaveForm(clickedDate)}
                  loading={loading}
                  employeeLoading={employeeLoading}
                  refreshEvents={refreshEvents}
                  refreshEmployeeEvents={refreshEmployeeEvents}
                  deleteCompanyCalendar={deleteCompanyCalendar}
                  deleteEmployeeCalendar={deleteEmployeeCalendar}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
      {/* Modal Add Event */}
      <div
        className="modal fade"
        id="exampleModal"
        tabIndex={-1}
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
      >
        <div className="modal-dialog modal-lg modal-dialog modal-dialog-centered">
          <div className="modal-content radius-16 bg-base">
            <div className="modal-header py-16 px-24 border border-top-0 border-start-0 border-end-0">
              <h1 className="modal-title fs-5" id="exampleModalLabel">
                Add New Event
              </h1>
              <button
                type="button"
                className="btn-close"
                data-bs-dismiss="modal"
                aria-label="Close"
              />
            </div>
            <div className="modal-body p-24">
              <form action="#">
                <div className="row">
                  <div className="col-12 mb-20">
                    <label className="form-label fw-semibold text-primary-light text-sm mb-8">
                      Event Title :{" "}
                    </label>
                    <input
                      type="text"
                      name="title"
                      value={formData.title}
                      onChange={handleFormChange}
                      className="form-control radius-8"
                      placeholder="Event Title "
                    />
                  </div>
                  <div className="col-md-6 mb-20">
                    <label
                      htmlFor="startDate"
                      className="form-label fw-semibold text-primary-light text-sm mb-8"
                    >
                      Start Date
                    </label>
                    <div className="position-relative">
                      <DatePicker
                        selected={formData.startDate}
                        onChange={(date) => {
                          console.log('Start date selected:', date);
                          setFormData(prev => ({
                            ...prev,
                            startDate: date
                          }));
                        }}
                        minDate={new Date()}
                        enableTime={true}
                        showTimeSelect
                        timeFormat="hh:mm aa"
                        dateFormat="MM/dd/yyyy, hh:mm aa"
                        minuteInterval={1}
                        className="form-control radius-8 bg-base"
                        id="startDate"
                        placeholder="Select Date & Time"
                      />
                      <span className="position-absolute end-0 top-50 translate-middle-y me-12 line-height-1">
                        <Icon
                          icon="solar:calendar-linear"
                          className="icon text-lg"
                        ></Icon>
                      </span>
                    </div>
                  </div>
                  <div className="col-md-6 mb-20">
                    <label
                      htmlFor="endDate"
                      className="form-label fw-semibold text-primary-light text-sm mb-8"
                    >
                      End Date
                    </label>
                    <div className="position-relative">
                      <DatePicker
                        selected={formData.endDate}
                        onChange={(date) => {
                          console.log('End date selected:', date);
                          setFormData(prev => ({
                            ...prev,
                            endDate: date
                          }));
                        }}
                        showTimeSelect
                        timeFormat="hh:mm aa"
                        dateFormat="MM/dd/yyyy, hh:mm aa"
                        minuteInterval={1}
                        className="form-control radius-8 bg-base"
                        id="endDate"
                        placeholder="Select Date & Time"
                      />
                      <span className="position-absolute end-0 top-50 translate-middle-y me-12 line-height-1">
                        <Icon
                          icon="solar:calendar-linear"
                          className="icon text-lg"
                        ></Icon>
                      </span>
                    </div>
                  </div>

                  <div className="form-check d-flex align-items-center px-3 mb-3 gap-4">
                    <div className="form-check d-flex align-items-center gap-2">
                      <input
                        type="radio"
                        name="eventType"
                        value="holiday"
                        checked={formData.isFree}
                        onChange={(e) => setFormData(prev => ({ ...prev, isFree: e.target.value === 'holiday' }))}
                        className="form-check-input"
                      />
                      <label className="form-check-label">
                        {formData.isEmployeeEvent ? 'Leave' : 'Holiday'}
                      </label>
                    </div>
                    <div className="form-check d-flex align-items-center gap-2">
                      <input
                        type="radio"
                        name="eventType"
                        value="work"
                        checked={!formData.isFree}
                        onChange={(e) => setFormData(prev => ({ ...prev, isFree: e.target.value === 'holiday' }))}
                        className="form-check-input"
                      />
                      <label className="form-check-label">Work</label>
                    </div>
                  </div>

                  <div className="form-check d-flex align-items-center px-3 mb-3">
                    <input
                      type="checkbox"
                      name="isEmployeeEvent"
                      id="isEmployeeEvent"
                      checked={formData.isEmployeeEvent}
                      onChange={handleFormChange}
                      className="form-check-input"
                    />
                    <label className="form-check-label ms-2" htmlFor="isEmployeeEvent">
                      Add as Personal Event
                    </label>
                  </div>

                  <div className="col-12 mb-20">
                    <label
                      htmlFor="desc"
                      className="form-label fw-semibold text-primary-light text-sm mb-8"
                    >
                      Description
                    </label>
                    <textarea
                      name="description"
                      value={formData.description}
                      onChange={handleFormChange}
                      className="form-control"
                      id="desc"
                      rows={4}
                      cols={50}
                      placeholder="Write some text"
                    />
                  </div>

                  <div className="d-flex align-items-center justify-content-center gap-3 mt-24">
                    <button
                      type="reset"
                      className="border border-danger-600 bg-hover-danger-200 text-danger-600 text-md px-40 py-11 radius-8"
                      data-bs-dismiss="modal"
                    >
                      Cancel
                    </button>
                    <button type="button" id="modalDismissButton" className="d-none" data-bs-dismiss="modal"></button>
                    <button
                      onClick={handleSave}
                      type="button"
                      className="btn btn-primary border border-primary-600 text-md px-24 py-12 radius-8"
                      data-bs-dismiss="modal"
                    >
                      Save
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      {/* Modal Cuti */}
      <div
        className="modal fade"
        id="cutiModal"
        tabIndex={-1}
        aria-labelledby="cutiModalLabel"
        aria-hidden="true"
      >
        <div className="modal-dialog modal-lg modal-dialog modal-dialog-centered">
          <div className="modal-content radius-16 bg-base">
            <div className="modal-header py-16 px-24 border border-top-0 border-start-0 border-end-0">
              <h1 className="modal-title fs-5" id="exampleModalLabel">
                Add Event
              </h1>
              <button
                type="button"
                className="btn-close"
                data-bs-dismiss="modal"
                aria-label="Close"
              />
            </div>
            <div className="modal-body p-24">
              <form action="#">
                <div className="row">
                  <div className="col-12 mb-20">
                    <label className="form-label fw-semibold text-primary-light text-sm mb-8">
                      Event Title :{" "}
                    </label>
                    <input
                      type="text"
                      name="title"
                      value={leaveFormData.title}
                      onChange={handleLeaveFormChange}
                      className="form-control radius-8"
                      placeholder="Enter Event Title"
                    />
                  </div>
                  <div className="col-md-6 mb-20">
                    <label
                      htmlFor="startDate"
                      className="form-label fw-semibold text-primary-light text-sm mb-8"
                    >
                      Start Date
                    </label>
                    <div className="position-relative">
                      <DatePicker
                        selected={leaveFormData.startDate}
                        onChange={(date) =>
                          setLeaveFormData((prev) => ({
                            ...prev,
                            startDate: date
                          }))
                        }
                        minDate={new Date()}
                        enableTime={false}
                        className="form-control radius-8 bg-base"
                        id="startDate"
                        placeholder="Select Date"
                      />
                      <span className="position-absolute end-0 top-50 translate-middle-y me-12 line-height-1">
                        <Icon
                          icon="solar:calendar-linear"
                          className="icon text-lg"
                        />
                      </span>
                    </div>
                  </div>
                  <div className="col-md-6 mb-20">
                    <label
                      htmlFor="endDate"
                      className="form-label fw-semibold text-primary-light text-sm mb-8"
                    >
                      End Date
                    </label>
                    <div className="position-relative">
                      <DatePicker
                        selected={leaveFormData.endDate}
                        onChange={(date) =>
                          setLeaveFormData((prev) => ({
                            ...prev,
                            endDate: date
                          }))
                        }
                        minDate={new Date()}
                        enableTime={false}
                        className="form-control radius-8 bg-base"
                        id="endDate"
                        placeholder="Select Date"
                      />
                      <span className="position-absolute end-0 top-50 translate-middle-y me-12 line-height-1">
                        <Icon
                          icon="solar:calendar-linear"
                          className="icon text-lg"
                        />
                      </span>
                    </div>
                  </div>

                  {/* even type */}
                  <div className="form-check d-flex align-items-center px-3 mb-3 gap-4">
                    <div className="form-check d-flex align-items-center gap-2">
                      <input
                        type="radio"
                        name="cutiEventType"
                        value="holiday"
                        checked={leaveFormData.isFree}
                        onChange={(e) => setLeaveFormData(prev => ({ ...prev, isFree: e.target.value === 'holiday' }))}
                        className="form-check-input"
                      />
                      <label className="form-check-label">
                        {leaveFormData.isEmployeeEvent ? 'Leave' : 'Holiday'}
                      </label>
                    </div>
                    <div className="form-check d-flex align-items-center gap-2">
                      <input
                        type="radio"
                        name="cutiEventType"
                        value="work"
                        checked={!leaveFormData.isFree}
                        onChange={(e) => setLeaveFormData(prev => ({ ...prev, isFree: e.target.value === 'holiday' }))}
                        className="form-check-input"
                      />
                      <label className="form-check-label">Work</label>
                    </div>
                  </div>

                  <div className="form-check d-flex align-items-center px-3 mb-3">
                    <input
                      type="checkbox"
                      name="isEmployeeEvent"
                      id="cutiIsEmployeeEvent"
                      checked={leaveFormData.isEmployeeEvent}
                      onChange={(e) => setLeaveFormData(prev => ({
                        ...prev,
                        isEmployeeEvent: e.target.checked
                      }))}
                      className="form-check-input"
                    />
                    <label className="form-check-label ms-2" htmlFor="cutiIsEmployeeEvent">
                      Add as Personal Event
                    </label>
                  </div>

                  <div className="col-12 mb-20">
                    <label
                      htmlFor="desc"
                      className="form-label fw-semibold text-primary-light text-sm mb-8"
                    >
                      Description
                    </label>
                    <textarea
                      name="description"
                      value={leaveFormData.description}
                      onChange={handleLeaveFormChange}
                      className="form-control"
                      id="desc"
                      rows={4}
                      cols={50}
                      placeholder="Write some text"
                    />
                  </div>
                  <div className="d-flex align-items-center justify-content-center gap-3 mt-24">
                    <button
                      type="reset"
                      className="border border-danger-600 bg-hover-danger-200 text-danger-600 text-md px-40 py-11 radius-8"
                      data-bs-dismiss="modal"
                    >
                      Cancel
                    </button>
                    <button
                      onClick={handleLeaveSave}
                      type="submit"
                      className="btn btn-primary border border-primary-600 text-md px-24 py-12 radius-8"
                      data-bs-dismiss="modal"
                    >
                      Save
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      {/* Modal View Event */}
      <div
        className="modal fade"
        id="exampleModalView"
        tabIndex={-1}
        aria-labelledby="exampleModalViewLabel"
        aria-hidden="true"
      >
        <div className="modal-dialog modal-lg modal-dialog modal-dialog-centered">
          <div className="modal-content radius-16 bg-base">
            <div className="modal-header py-16 px-24 border border-top-0 border-start-0 border-end-0">
              <h1 className="modal-title fs-5" id="exampleModalViewLabel">
                View Details
              </h1>
              <button
                type="button"
                className="btn-close"
                data-bs-dismiss="modal"
                aria-label="Close"
              />
            </div>
            <div className="modal-body p-24">
              <div className="mb-12">
                <span className="text-secondary-light txt-sm fw-medium">
                  Title
                </span>
                <h6 className="text-primary-light fw-semibold text-md mb-0 mt-4">
                  {viewEvent?.eventName || viewEmployeeEvent?.eventName || 'N/A'}
                </h6>
              </div>
              <div className="mb-12">
                <span className="text-secondary-light txt-sm fw-medium">
                  Start Date
                </span>
                <h6 className="text-primary-light fw-semibold text-md mb-0 mt-4">
                  {(viewEvent?.startDate || viewEmployeeEvent?.startDate) ?
                    formatDate(viewEvent?.startDate || viewEmployeeEvent?.startDate) : "N/A"}
                </h6>
              </div>
              <div className="mb-12">
                <span className="text-secondary-light txt-sm fw-medium">
                  End Date
                </span>
                <h6 className="text-primary-light fw-semibold text-md mb-0 mt-4">
                  {(viewEvent?.endDate || viewEmployeeEvent?.endDate) ?
                    formatDate(viewEvent?.endDate || viewEmployeeEvent?.endDate) : "N/A"}
                </h6>
              </div>
              <div className="mb-12">
                <span className="text-secondary-light txt-sm fw-medium">
                  Description
                </span>
                <h6 className="text-primary-light fw-semibold text-md mb-0 mt-4">
                  {viewEvent?.description || viewEmployeeEvent?.description || 'N/A'}
                </h6>
              </div>
              <div className="mb-12">
                <span className="text-secondary-light txt-sm fw-medium">
                  Label
                </span>
                <h6 className="text-primary-light fw-semibold text-md mb-0 mt-4 d-flex align-items-center gap-2">
                  <span className={`w-8-px h-8-px ${viewEvent?.isFree || viewEmployeeEvent?.isCuti ? 'bg-success-600' : 'bg-warning-600'} rounded-circle`}
                  />
                  {viewEvent?.isFree || viewEmployeeEvent?.isCuti ? "Holiday/Leave" : "Work"}
                </h6>
              </div>
              {/* Employee-specific field */}
              {viewEmployeeEvent && (
                <div className="mb-12">
                  <span className="text-secondary-light txt-sm fw-medium">
                    Employee NIK
                  </span>
                  <h6 className="text-primary-light fw-semibold text-md mb-0 mt-4">
                    {viewEmployeeEvent.nik || 'N/A'}
                  </h6>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
      {/* Modal Edit Event */}
      <div
        className="modal fade"
        id="exampleModalEdit"
        tabIndex={-1}
        aria-labelledby="exampleModalEditLabel"
        aria-hidden="true"
      >
        <div className="modal-dialog modal-lg modal-dialog modal-dialog-centered">
          <div className="modal-content radius-16 bg-base">
            <div className="modal-header py-16 px-24 border border-top-0 border-start-0 border-end-0">
              <h1 className="modal-title fs-5" id="exampleModalEditLabel">
                Edit Event
              </h1>
              <button
                type="button"
                className="btn-close"
                data-bs-dismiss="modal"
                aria-label="Close"
              />
            </div>
            <div className="modal-body p-24">
              <form onSubmit={handleSaveEdit}>
                <div className="row">
                  <div className="col-12 mb-20">
                    <label className="form-label fw-semibold text-primary-light text-sm mb-8">
                      Event Title:{" "}
                    </label>
                    <input
                      type="text"
                      className="form-control radius-8"
                      value={editEventData?.eventName || editEmployeeEventData?.eventName || ''}
                      onChange={(e) => {
                        if (editEventData) {
                          setEditEventData(prev => ({ ...prev, eventName: e.target.value }))
                        } else if (editEmployeeEventData) {
                          setEditEmployeeEventData(prev => ({ ...prev, eventName: e.target.value }))
                        }
                      }}
                      placeholder="Enter Event Title"
                    />
                  </div>
                  <div className="col-md-6 mb-20">
                    <label
                      htmlFor="editstartDate"
                      className="form-label fw-semibold text-primary-light text-sm mb-8"
                    >
                      Start Date
                    </label>
                    <div className="position-relative">
                      <DatePicker
                        selected={editEventData?.startDate || editEmployeeEventData?.startDate}
                        onChange={(date) => {
                          if (editEventData) {
                            setEditEventData(prev => ({ ...prev, startDate: date }))
                          } else if (editEmployeeEventData) {
                            setEditEmployeeEventData(prev => ({ ...prev, startDate: date }))
                          }
                        }}
                        minDate={new Date()}
                        className="form-control radius-8 bg-base"
                        placeholderText="Select Start Date"
                      />
                      <span className="position-absolute end-0 top-50 translate-middle-y me-12 line-height-1">
                        <Icon
                          icon="solar:calendar-linear"
                          className="icon text-lg"
                        />
                      </span>
                    </div>
                  </div>
                  <div className="col-md-6 mb-20">
                    <label
                      htmlFor="editendDate"
                      className="form-label fw-semibold text-primary-light text-sm mb-8"
                    >
                      End Date
                    </label>
                    <div className="position-relative">
                      <DatePicker
                        selected={editEventData?.endDate || editEmployeeEventData?.endDate}
                        onChange={(date) => {
                          if (editEventData) {
                            setEditEventData(prev => ({ ...prev, endDate: date }))
                          } else if (editEmployeeEventData) {
                            setEditEmployeeEventData(prev => ({ ...prev, endDate: date }))
                          }
                        }}
                        minDate={new Date()}
                        className="form-control radius-8 bg-base"
                        placeholderText="Select End Date"
                      />
                      <span className="position-absolute end-0 top-50 translate-middle-y me-12 line-height-1">
                        <Icon
                          icon="solar:calendar-linear"
                          className="icon text-lg"
                        />
                      </span>
                    </div>
                  </div>

                  <div className="form-check d-flex align-items-center px-3 mb-3 gap-4">
                    <div className="form-check d-flex align-items-center gap-2">
                      <input
                        type="radio"
                        name="eventType"
                        value="holiday"
                        checked={editEventData ? editEventData.isFree : editEmployeeEventData?.isCuti}
                        onChange={(e) => {
                          if (editEventData) {
                            setEditEventData(prev => ({ ...prev, isFree: e.target.value === 'holiday' }))
                          } else if (editEmployeeEventData) {
                            setEditEmployeeEventData(prev => ({ ...prev, isCuti: e.target.value === 'holiday' }))
                          }
                        }}
                        className="form-check-input"
                      />
                      <label className="form-check-label">
                        {editEmployeeEventData ? 'Leave' : 'Holiday'}
                      </label>
                    </div>
                    <div className="form-check d-flex align-items-center gap-2">
                      <input
                        type="radio"
                        name="eventType"
                        value="work"
                        checked={editEventData ? !editEventData.isFree : !editEmployeeEventData?.isCuti}
                        onChange={(e) => {
                          if (editEventData) {
                            setEditEventData(prev => ({ ...prev, isFree: e.target.value === 'holiday' }))
                          } else if (editEmployeeEventData) {
                            setEditEmployeeEventData(prev => ({ ...prev, isCuti: e.target.value === 'holiday' }))
                          }
                        }}
                        className="form-check-input"
                      />
                      <label className="form-check-label">Work</label>
                    </div>
                  </div>

                  <div className="col-12 mb-20">
                    <textarea
                      className="form-control"
                      value={editEventData?.description || editEmployeeEventData?.description || ''}
                      onChange={(e) => {
                        if (editEventData) {
                          setEditEventData(prev => ({ ...prev, description: e.target.value }))
                        } else if (editEmployeeEventData) {
                          setEditEmployeeEventData(prev => ({ ...prev, description: e.target.value }))
                        }
                      }}
                      rows={4}
                      placeholder="Write description"
                    />
                  </div>

                  <div className="d-flex justify-content-end gap-2">
                    <button
                      type="button"
                      className="btn btn-secondary"
                      data-bs-dismiss="modal"
                    >
                      Cancel
                    </button>
                    <button type="submit" className="btn btn-primary">
                      Save Changes
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      {/* Modal Delete Event */}
      <div
        className="modal fade"
        id="exampleModalDelete"
        tabIndex={-1}
        aria-hidden="true"
      >
        <div className="modal-dialog modal-sm modal-dialog modal-dialog-centered">
          <div className="modal-content radius-16 bg-base">
            <div className="modal-body p-24 text-center">
              <span className="mb-16 fs-1 line-height-1 text-danger">
                <Icon icon="fluent:delete-24-regular" className="menu-icon" />
              </span>
              <h6 className="text-lg fw-semibold text-primary-light mb-0">
                Are your sure you want to delete this event
              </h6>
              <div className="d-flex align-items-center justify-content-center gap-3 mt-24">
                <button
                  type="reset"
                  className="w-50 border border-danger-600 bg-hover-danger-200 text-danger-600 text-md px-40 py-11 radius-8"
                  data-bs-dismiss="modal"
                >
                  Cancel
                </button>
                <button
                  type="button"
                  className="w-50 btn btn-primary border border-primary-600 text-md px-24 py-12 radius-8"
                  onClick={async () => {
                    if (selectedEventId) {
                      await deleteCompanyCalendar(selectedEventId);
                      alert('Event deleted successfully');
                    }
                    setSelectedEventId(null);
                    document.getElementById("exampleModalDelete").classList.remove("show");
                    document.body.classList.remove("modal-open");
                  }}
                  data-bs-dismiss="modal"
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      {/* Modal Delete Employee Event */}
      <div
        className="modal fade"
        id="exampleModalDeleteEmployee"
        tabIndex={-1}
        aria-hidden="true"
      >
        <div className="modal-dialog modal-sm modal-dialog modal-dialog-centered">
          <div className="modal-content radius-16 bg-base">
            <div className="modal-body p-24 text-center">
              <span className="mb-16 fs-1 line-height-1 text-danger">
                <Icon icon="fluent:delete-24-regular" className="menu-icon" />
              </span>
              <h6 className="text-lg fw-semibold text-primary-light mb-0">
                Are you sure you want to delete this personal event?
              </h6>
              <div className="d-flex align-items-center justify-content-center gap-3 mt-24">
                <button
                  type="reset"
                  className="w-50 border border-danger-600 bg-hover-danger-200 text-danger-600 text-md px-40 py-11 radius-8"
                  data-bs-dismiss="modal"
                >
                  Cancel
                </button>
                <button
                  type="button"
                  className="w-50 btn btn-primary border border-primary-600 text-md px-24 py-12 radius-8"
                  onClick={async () => {
                    try {
                      console.log('Delete button clicked');
                      console.log('Selected employee event ID:', selectedEmployeeEventId);
                      console.log('All employee events:', employeeEvents);
                      if (selectedEmployeeEventId) {
                        console.log('Deleting employee event with ID:', selectedEmployeeEventId);
                        await deleteEmployeeCalendar(selectedEmployeeEventId);

                        await refreshEmployeeEvents();

                        alert('Personal event deleted successfully');

                        const modalElement = document.getElementById("exampleModalDeleteEmployee");
                        const bsModal = bootstrap.Modal.getInstance(modalElement);
                        if (bsModal) {
                          bsModal.hide();
                        }
                      } else {
                        alert('No event selected for deletion');
                      }

                      setSelectedEmployeeEventId(null);
                    } catch (error) {
                      console.error('Error deleting employee event:', error);
                      alert('Failed to delete event: ' + (error.message || 'Unknown error'));
                    }
                  }}
                  data-bs-dismiss="modal"
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default CalendarMainLayer;
